/*
 * SunSpotApplication.java
 *
 * Created on 13-Oct-2014 23:18:39;
 */

package org.sunspotworld;

import org.sunspotworld.spotRadios.RadiosFactory;
import org.sunspotworld.spotRadios.ISendingRadio;
import spotMonitors.ILightMonitor;
import spotMonitors.IThermoMonitor;
import spotMonitors.MonitorFactory;
import com.sun.spot.peripheral.radio.RadioFactory;
import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ISwitch;
import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.ITriColorLEDArray;
import com.sun.spot.service.BootloaderListenerService;
import com.sun.spot.util.IEEEAddress;
import com.sun.spot.util.Utils;

import java.io.IOException;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 * The startApp method of this class is called by the VM to start the
 * application.
 * 
 * The manifest specifies this class as MIDlet-1, which means it will
 * be selected for execution.
 */
public class SunSpotApplication extends MIDlet {
    //private ISendingRadio sendingRadio;
    private ILightMonitor lightMonitor;
    private IThermoMonitor thermoMonitor;
    private ISendingRadio lightSendingRadio, thermoSendingRadio;
    private static final int SAMPLE_RATE = 60 * 1000; //60 seconds

    public SunSpotApplication() {
        lightMonitor = MonitorFactory.createLightMonitor(); 
        thermoMonitor = MonitorFactory.createThermoMonitor(); 

        try {
            lightSendingRadio = RadiosFactory.createSendingRadio(lightMonitor.getPort());
            thermoSendingRadio = RadiosFactory.createSendingRadio(thermoMonitor.getPort());
        } catch (IOException io) {
            System.out.println("IO Exception while creating sending radios: " + io);
        }
    }

    protected void startApp() throws MIDletStateChangeException {
        BootloaderListenerService.getInstance().start();   // monitor the USB (if connected) and recognize commands from host
        long ourAddr = RadioFactory.getRadioPolicyManager().getIEEEAddress();
        System.out.println("Our radio address = " + IEEEAddress.toDottedHex(ourAddr));
        
        while(true)
        {
            lightSendingRadio.sendLight(lightMonitor.getLightIntensity());
            thermoSendingRadio.sendHeat(thermoMonitor.getCelsiusTemp());
            Utils.sleep(SAMPLE_RATE);
        }

        //notifyDestroyed();                      // cause the MIDlet to exit
    }

    protected void pauseApp() {
        // This is not currently called by the Squawk VM
    }

    /**
     * Called if the MIDlet is terminated by the system.
     * It is not called if MIDlet.notifyDestroyed() was called.
     *
     * @param unconditional If true the MIDlet must cleanup and release all resources.
     */
    protected void destroyApp(boolean unconditional) throws MIDletStateChangeException {
    }
}
  
