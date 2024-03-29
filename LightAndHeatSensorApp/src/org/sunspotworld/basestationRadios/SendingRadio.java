/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld.basestationRadios;

import com.sun.spot.resources.Resources;
import java.io.IOException;

import java.util.ArrayList;
import com.sun.spot.util.Utils;
import com.sun.spot.io.j2me.radiogram.*;

import javax.microedition.io.*;

/**
 *
 * @author adamcornforth + Dominic Lindsay
 */
public class SendingRadio implements ISendingRadio
{
	private static final int HOST_PORT = 96;
    //sample period in milliseconds
    private RadiogramConnection radioConn = null;
    private Datagram  datagram = null;
    
    private String spotAddress = System.getProperty("IEEE_ADDRESS");

    public SendingRadio(SunspotPort port) throws IOException
    {
        radioConn = (RadiogramConnection) Connector.open("radiogram://broadcast:" + port.getPort());
		System.out.println("Sending Radio created for " + spotAddress + " on port " + port.getPort()); 
        datagram = radioConn.newDatagram(radioConn.getMaximumLength()); 
    }

    public void sendDiscoverReponse(String spot_address, ArrayList portsThresholds) 
    {
        try {
            datagram.reset();
            // write spot_address first
            datagram.writeUTF(spot_address);
            datagram.writeInt(portsThresholds.size());
            
            // now write ports + thresh
            for (int i = 0; i < portsThresholds.size(); i += 3) {
                switch(((Integer)portsThresholds.get(i)).intValue()) {
                    case 110:
                        System.out.println("SPOT has job of sensing Heat \t\t (threshold " + ((Integer)portsThresholds.get(i+1)).intValue() + ")");
                        break;
                    case 115:
                        System.out.println("SPOT has job of sending Heat \t\t (sample rate " + ((Integer)portsThresholds.get(i+1)).intValue() + "s)");
                        break;
                    case 120:
                        System.out.println("SPOT has job of sensing Light \t\t (threshold " + ((Integer)portsThresholds.get(i+1)).intValue() + ")");
                        break;
                    case 125:
                        System.out.println("SPOT has job of sending Light \t\t (sample rate " + ((Integer)portsThresholds.get(i+1)).intValue() + "s)");
                        break;
                    case 130:
                        System.out.println("SPOT has job of sensing Acceleration \t\t (threshold " + ((Integer)portsThresholds.get(i+1)).intValue() + ")");
                        break;
                    case 135:
                        System.out.println("SPOT has job of sending Acceleration \t\t (sample rate " + ((Integer)portsThresholds.get(i+1)).intValue() + "s)");
                        break;
                    case 140:
                        System.out.println("SPOT has job of sensing Motion \t\t (threshold " + ((Integer)portsThresholds.get(i+1)).intValue() + ")");
                        break;
                    case 145:
                        System.out.println("SPOT has job of sending Motion \t\t (sample rate " + ((Integer)portsThresholds.get(i+1)).intValue() + "s)");
                        break;
                    case 150:
                        System.out.println("SPOT has job of being a Cell Tower");
                        break;
                    case 160:
                        System.out.println("SPOT has job of being a Roaming Spot");
                        break;
                    case 180:
                        System.out.println("SPOT has job of being a Smart Cup");
                        break;
                }
                datagram.writeInt(
                        ((Integer)portsThresholds.get(i)).intValue());
                datagram.writeInt(
                        ((Integer)portsThresholds.get(i+1)).intValue());
                datagram.writeInt(
                        ((Integer)portsThresholds.get(i+2)).intValue());
                
            }
            
            radioConn.send(datagram);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("IOException occured while sending ports: " + e);
        }
    }
}
