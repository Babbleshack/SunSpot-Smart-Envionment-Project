/*
 * Thread for receiving acceleration data.
 * Dominic Lindsay
 */

package org.sunspotworld;

import java.io.IOException;
import org.sunspotworld.basestationMonitors.IReceivingRadio;
import org.sunspotworld.basestationMonitors.IMotionMonitor;
import org.sunspotworld.basestationMonitors.MonitorFactory;
import org.sunspotworld.basestationRadios.RadiosFactory;

import org.sunspotworld.DB.DatabaseConnectionFactory;
import org.sunspotworld.DB.MySQLConnectionManager;
import org.sunspotworld.DB.QueryManager;
public class TReceivingMotion implements Runnable
{

    private IMotionMonitor motionMonitor;
    private QueryManager queryManager;

    // Init receiving radio
    IReceivingRadio motionReceivingRadio;

    // creates an instance of SunSpotHostApplication class and initialises
    // instance variables
    public TReceivingMotion()
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
                long  motionValue   = motionReceivingRadio.receiveAccel();

                // Print out accel
                System.out.println("Message from " + motionReceivingRadio.getReceivedAddress() + " - " + "acceleration: " + accelValue);

                try
                {
                    queryManager.createMotionRecord(accelValue, motionReceivingRadio.getReceivedAddress(), System.currentTimeMillis());
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