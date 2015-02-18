/**
 * receives bearing readings from spots.
 * @author Dominic Lindsay
 */
package org.sunspotworld.threads;

import java.io.IOException;
import org.sunspotworld.basestationRadios.IReceivingRadio;
import org.sunspotworld.basestationRadios.PortOutOfRangeException;
import org.sunspotworld.basestationRadios.RadiosFactory;
import org.sunspotworld.basestationRadios.SunspotPort;
import org.sunspotworld.database.QueryManager;


public class TReceivingCompassThreshold implements Runnable {
    private final QueryManager _qm;
    private IReceivingRadio _rRadio;
    private final int _port;
    public TReceivingCompassThreshold() {
        _port = SunspotPort.COMPASS_THRESH;
        try {
            _rRadio = RadiosFactory.createReceivingRadio(
                    new SunspotPort(SunspotPort.COMPASS_THRESH)
            );
            //need radio
        } catch (PortOutOfRangeException ex) {
            System.err.println("error with radio in Barometer thread");
        }
        _qm = new QueryManager();
    }

    public void run() {
        double angle;
        while(true) {
            try {
                angle = _rRadio.receiveBarometer();
                _qm.createBarometerRecord(angle, _rRadio.getReceivedAddress(),
                        System.currentTimeMillis(), _port);
            } catch (IOException ex) {
                System.err.println("Error storing barometer reading");
            }
        }
    }
}
