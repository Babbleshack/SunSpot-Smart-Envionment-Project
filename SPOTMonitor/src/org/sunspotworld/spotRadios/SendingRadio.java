/**
 * Encapsulates radio packet transmision; implements ISendingRadio.
 * Dominic Lindsay + Adam Cornfourth
 */
package org.sunspotworld.spotRadios;

import java.io.IOException;

import com.sun.spot.io.j2me.radiogram.*;

import javax.microedition.io.*;

/**
 *
 * @author adamcornforth + Dominic Lindsay
 */
public class SendingRadio implements ISendingRadio
{
    private RadiogramConnection radioConn = null;
    private Datagram  datagram = null;
    //Radiogram rdg;
    
    private String spotAddress = System.getProperty("IEEE_ADDRESS");

    public SendingRadio(SunspotPort port) 
    {
        try {
            radioConn = (RadiogramConnection) Connector.open("radiogram://broadcast:" + port.getPort());
            System.out.println("Sending Radio created for " + spotAddress + " on port " + port.getPort()); 
            datagram = radioConn.newDatagram(radioConn.getMaximumLength());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
       // rdg = (Radiogram)radioConn.newDatagram(rcvConn.getMaximumLength()); 
    }

    public void sendLight(double value)
    {
        try {
            datagram.reset();
            datagram.writeDouble(value);
            radioConn.send(datagram);
            System.out.println("Light value " + value + " sent...");
        } catch (Exception e) {
            System.err.println("IOException occured while sending light: " + e);
        }
    }

    public void sendHeat(double value)
    {
        try {
            datagram.reset();
            datagram.writeDouble(value);
            radioConn.send(datagram);
            System.out.println("Heat value " + value + " sent...");
        } catch (IOException e) {
            System.err.println("IOException occured while sending thermo: " + e);
        }
    }

    public void sendAccel(double value)
    {
        try {
            datagram.reset();
            datagram.writeDouble(value);
            radioConn.send(datagram);
            System.out.println("Accel value " + value + " sent...");
        } catch (IOException e) {
            System.err.println("IOException occured while sending accel: " + e);
        }
    }

    public void sendWater(int value)
    {
        try {
            datagram.reset();
            datagram.writeInt(value);
            radioConn.send(datagram);
            System.out.println("Water percentage " + value + " sent...");
        } catch (IOException e) {
            System.err.println("IOException occured while sending accel: " + e);
        }
    }

    public void sendMotionTime(long value)
    {
        try {
            datagram.reset();
            datagram.writeLong(value);
            radioConn.send(datagram);
            System.out.println("Motion time value " + value + " sent...");
        } catch (IOException e) {
            System.err.println("IOException occured while sending Motion Time: " + e);
        
        }
    }

    public void sendSwitch(String value) 
    {
        try {
            datagram.reset();
            datagram.writeUTF(value);
            radioConn.send(datagram);
            System.out.println("Switch ID value " + value + " sent...");
        } catch (IOException e) {
            System.err.println("IOException occured while sending switch ID value: " + e);
        
        }
    }

    /**
     * Sends empty packet to be used as a 'pinging' service
     */
    public void ping()
    {
        try {
            datagram.reset();
            // int hops = radioConn.getMaxBroadcastHops();
            // radioConn.setMaxBroadcastHops(1);
            radioConn.send(datagram);
            // radioConn.setMaxBroadcastHops(hops);
        } catch (IOException e) {
            System.err.println("IOException occured while sending PING" + e);
        }
    }

    /**
     * sends SPOT address to basestation
     */
    public void sendSPOTAddress(String address)
    {
        try
        {
            datagram.reset();
            datagram.writeUTF(address);
            // int hops = radioConn.getMaxBroadcastHops();
            // radioConn.setMaxBroadcastHops(1);
            radioConn.send(datagram);
            // radioConn.setMaxBroadcastHops(hops);
            System.out.println("SPOT address sent to basestation..."); 
        } catch (IOException e) {
            System.err.println("Error sending SPOT address: " + e);
        }

    }

    /**
     * sends Tower address of Tower that pinged the SPOT to tower
     */
    public void sendTowerAddress(String address)
    {
        try
        {
            datagram.reset();
            datagram.writeUTF(address);
            // int hops = radioConn.getMaxBroadcastHops();
            // radioConn.setMaxBroadcastHops(1);
            radioConn.send(datagram);
            // radioConn.setMaxBroadcastHops(hops);
            System.out.println("Tower address sent to tower..."); 
        } catch (IOException e) {
            System.err.println("Error sending Tower address: " + e);
        }

    }
    public void discoverMe() {
        try {
            datagram.reset();
            datagram.writeUTF(spotAddress);
            radioConn.send(datagram);
            System.out.println("DiscoverME request sent..."); 
        } catch (IOException e) {
            System.err.println("IOException occured while sending discovery request: " + e);
        } 
    }
    public void sendBatteryPower(int powerLevel)
    {
        try {
            datagram.reset();
            datagram.writeInt(powerLevel);
            radioConn.send(datagram);
            System.out.println("Sending Battery Information"); 
        } catch (IOException e) {
            System.err.println("IOException occured while sending Battery Data: " + e);
        }  
    }

    public void sendBarometerReadion(double angle) {
        try {
            datagram.reset();
            datagram.writeDouble(angle);
            radioConn.send(datagram);
            System.out.println("Barometer value " + angle + " sent...");
        } catch (IOException e) {
            System.err.println("IOException occured while sending Barometer: " + e);
        }
    }
    public void sendImpactFlag(int val) {
        try {
            datagram.reset();
            datagram.writeInt(val);
            radioConn.send(datagram);
            System.out.println("Impact flag " + val + " sent...");
        } catch (IOException e) {
            System.err.println("IOException occured while sending Barometer: " + e);
        }
    }

}
