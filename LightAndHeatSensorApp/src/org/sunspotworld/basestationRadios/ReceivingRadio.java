/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sunspotworld.basestationRadios;

import com.sun.spot.io.j2me.radiogram.*;

import java.io.*;
import javax.microedition.io.*;
/**
 *
 * @author adamcornforth
 */
public class ReceivingRadio implements IReceivingRadio
{   
    //Connection and datagram variables
    private RadiogramConnection radioConn;
    private Datagram datagram;
    private String spotAddress = System.getProperty("IEEE_ADDRESS");
    private SunspotPort _port;
    public ReceivingRadio(SunspotPort port) throws IOException
    {
        _port = port;
        radioConn = (RadiogramConnection) Connector.open("radiogram://:" + port.getPort());
        datagram = radioConn.newDatagram(radioConn.getMaximumLength());   
        System.out.println("Receiving Radio created for " + spotAddress + " on port " + port.getPort());
    }

    public String getReceivedAddress() throws IOException
    {   
        return datagram.getAddress();  
    }

    public String receiveSwitch() throws IOException
    {
        datagram.reset();
        radioConn.receive(datagram); 
        return datagram.readUTF(); 
    }

    public double receiveLight() throws IOException
    {
        datagram.reset();
        radioConn.receive(datagram); 
        return datagram.readDouble(); 
    }

    public double receiveHeat() throws IOException
    {
        datagram.reset();   
        radioConn.receive(datagram); 
        return datagram.readDouble(); 
    }
    
    public double receiveAccel() throws IOException
    {
        datagram.reset();
        radioConn.receive(datagram); 
        return datagram.readDouble(); 
    }
    
    public int receiveWater() throws IOException
    {
        datagram.reset();
        radioConn.receive(datagram);
        return datagram.readInt();
              
    }

    public long receiveMotion() throws IOException
    {
        datagram.reset();
        radioConn.receive(datagram);
        return datagram.readLong();
    }

    public String receiveDiscoverMe() throws IOException
    {
        datagram.reset();
        radioConn.receive(datagram);
        return datagram.getAddress();
    }

    public String receiveZonePacket()
    {
        try {
            datagram.reset();
            radioConn.receive(datagram);
            return datagram.readUTF();
        } catch(Exception e) {
            System.err.println("Error receiving zone packets" + e);
        }
            return null;
    }

    public int receiveBatteryLevel() {
        try {
            datagram.reset();
            radioConn.receive(datagram);
            return datagram.readInt();
        } catch(Exception e) {
            System.err.println("Error receiving zone packets" + e);
        }
            return 0;
    }

    public double receiveBarometer() throws IOException {
        try { 
            datagram.reset();   
            radioConn.receive(datagram); 
            return datagram.readDouble();
        } catch (Exception e) {
            System.err.println("Error getting barometer from radio");
            e.printStackTrace();
        }
        return 0;
    }
    public int receiveImpactFlag() {
        try{
           System.out.println("Received a flag"); 
           datagram.reset();
           radioConn.receive(datagram);
           return datagram.readInt();
        } catch (Exception e) {
            System.err.println("Error reading impact from radio");
            e.printStackTrace();
        }
        return 0;
    }
    
}
