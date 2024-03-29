/*
 * Thread for receiving acceleration data.
 * Dominic Lindsay
 */

package org.sunspotworld.threads;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import org.sunspotworld.basestationRadios.IReceivingRadio;
import org.sunspotworld.basestationMonitors.IMotionMonitor;
import org.sunspotworld.basestationMonitors.MonitorFactory;
import org.sunspotworld.basestationRadios.RadiosFactory;
import org.sunspotworld.basestationRadios.SunspotPort;

import org.sunspotworld.database.QueryManager;
public class TReceivingMotionThreshold implements Runnable
{

    private IMotionMonitor motionMonitor;
    private QueryManager queryManager;

    // Init receiving radio
    IReceivingRadio motionReceivingRadio;
    private final ConcurrentHashMap<String, String> _addressMap;

    // creates an instance of SunSpotHostApplication class and initialises
    // instance variables
    public TReceivingMotionThreshold(ConcurrentHashMap addressMap)
    {
        try
        {
            motionMonitor = MonitorFactory.createMotionMonitor();
            motionReceivingRadio = RadiosFactory.createReceivingRadio(motionMonitor.getPort());
            queryManager = new QueryManager();
        }
        catch(Exception e)
        {
           System.out.println("Unable initiate polling");
        }
        _addressMap = addressMap;
    }

    public void startPolling() throws Exception
    {
    }

    public void run()
    {
        // main switch reading/polling loop
        while (true)
        {
            try
            {
                // Read accel
                long  motionValue = motionReceivingRadio.receiveMotion();
                String address = motionReceivingRadio.getReceivedAddress();
                if(!_addressMap.contains(address)) continue; 
                // Print out accel
                System.out.println("Message from " + motionReceivingRadio.getReceivedAddress() + " \t\t " + "Motion: \t\t " + motionValue);

                try
                {
                    queryManager.createMotionRecord(1, motionReceivingRadio.getReceivedAddress(), System.currentTimeMillis(), SunspotPort.WATER_PORT);
                } catch (NullPointerException npe) {
                    System.out.println("MotionService: queryManager - NullPointerException");
                }
            }
            catch (IOException io)
            {
                System.err.println("Caught " + io +  " while polling SPOT");
                io.printStackTrace();
            }
        }
    }
}