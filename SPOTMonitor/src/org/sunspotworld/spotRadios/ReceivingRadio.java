/**
 * Encapsulates reception of datagrams.
 * Dominic Lindsay + Adam Cornfourth
 */
package org.sunspotworld.spotRadios;

import com.sun.spot.io.j2me.radiogram.*;
import com.sun.spot.io.j2me.radiogram.Radiogram;

import java.io.*;
import javax.microedition.io.*;
import org.sunspotworld.data.TowerSpot;

public class ReceivingRadio implements IReceivingRadio
{
    private static final int HOST_PORT = 96;
    //sample period in milliseconds
    private RadiogramConnection radioConn = null;
    private Datagram  datagram = null;
    
    private String spotAddress = System.getProperty("IEEE_ADDRESS");
    private String lastPingAddress = null;

    public ReceivingRadio(SunspotPort port)
    {
        try {
            radioConn = (RadiogramConnection) Connector.open("radiogram://:" + port.getPort());
            datagram = radioConn.newDatagram(radioConn.getMaximumLength());   
            System.out.println("Receiving Radio created for " + spotAddress + " on port " + port.getPort());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getReceivedAddress()
    {   
        return datagram.getAddress();  
    }

    /**
     * Receives thes discovey response and returns an array of integers, 
     * containing the port numbers and sensor thresholds
     */
    public int[] receiveDiscoverResponse()
    {
        final int minLenght = 3;
        int[] portsThresholds;
        try {
            String spot_address = "";
            while(!spot_address.equals(spotAddress)) {
                radioConn.receive(datagram);
                spot_address = datagram.readUTF();
                // System.out.println("Sun SPOT address " + spotAddress + " has received a discover response with the recipient of " + spot_address);
            }
            
            
                int datagramLength = datagram.readInt();
                if(datagramLength < minLenght){
                    portsThresholds = new int[3];
                    for(int i = 0; i < 2; i++){
                        portsThresholds[i] = -1;
                    }
                    return portsThresholds;
                }
                portsThresholds = new int[datagramLength];
            
                for (int i = 0;i < datagramLength; i += 3) {
                    portsThresholds[i] = datagram.readInt();
                    portsThresholds[i+1] = datagram.readInt();
                    portsThresholds[i+2] = datagram.readInt();
                }
                
                    
            
            return portsThresholds;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    /**
     * gets the address of the last received ping packet
     * @return address String
     */
    public String getLastPingAddress()
    {
        return lastPingAddress;
    }

    /**
     * receives a ping packet from roaming spot and 
     * returns TowerSpot Object
     * @return 
     */
    public TowerSpot receivePing()
    {
        try
        {
            datagram.reset();
            radioConn.receive(datagram);
            return new TowerSpot(datagram.getAddress(),
                    ((Radiogram)datagram).getRssi());
        } catch (IOException e) {
            System.err.println("Error while waiting on packet");
        }
        return null;
    }   
}
