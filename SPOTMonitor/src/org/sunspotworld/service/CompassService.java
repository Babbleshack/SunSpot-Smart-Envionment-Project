/**
 * A service for encapsulating the barometer
 * @author Dominic Lindsay
 */
package org.sunspotworld.service;

import com.sun.spot.resources.transducers.ITriColorLED;
import com.sun.spot.resources.transducers.LEDColor;
import java.io.IOException;
import operators.GreaterThan;
import operators.IOperator;
import operators.LessThan;
import operators.NullOperator;
import org.sunspotworld.controllers.LEDController;
import org.sunspotworld.homePatterns.TaskObservable;
import org.sunspotworld.homePatterns.TaskObserver;
import org.sunspotworld.spotMonitors.IMonitor;
import org.sunspotworld.spotRadios.ISendingRadio;
import org.sunspotworld.spotRadios.PortOutOfRangeException;
import org.sunspotworld.spotRadios.RadiosFactory;
import org.sunspotworld.spotRadios.SunspotPort;
import org.sunspotworld.data.SensorData;


public class CompassService implements IService, TaskObserver {
    private final IMonitor _monitor;
    private final int _serviceId; 
    private ISendingRadio _sRadio;
    private final ITriColorLED _feedbackLED;
    private final LEDColor _serviceColour;
    private IOperator _direction;
    public CompassService(final IMonitor monitor, final int serviceId) {
        _monitor = monitor;
	_monitor.addMonitorObserver(this);
        _serviceId = serviceId;
        try {
            _sRadio = RadiosFactory.createSendingRadio(
                    new SunspotPort(serviceId));
        } catch (PortOutOfRangeException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        _serviceColour = LEDColor.MAGENTA;
        _feedbackLED = LEDController.getLED(LEDController.STATUS_LED);
    }

    public void startService() {
        if(_monitor.getStatus())
            return;
        _monitor.startMonitor();
        LEDController.turnLEDOn(
                LEDController.getLED(LEDController.COMPASS_LED),
                _serviceColour
        );
    }

    public void stopService() {
        if(!_monitor.getStatus())
            return;
        _monitor.stopMonitor();
        LEDController.turnLEDOff(
                LEDController.getLED(LEDController.COMPASS_LED)
        );
    }

    public boolean isScheduled() {
        return _monitor.getStatus();
    }

    public int getServiceId() {
        return _serviceId;
    }

    public IMonitor getMonitor() {
        return _monitor;
    }

    public void setData(int data) {
        _monitor.setVariable(data);
    }

    public void update(TaskObservable o, Object arg) {
        LEDController.flashLED(_feedbackLED, _serviceColour);
        _sRadio.sendBarometerReadion(
                ((SensorData)arg).getDataAsDouble()
        );
    }

    public void update(TaskObservable o) {
        LEDController.flashLED(_feedbackLED, _serviceColour);
        _sRadio.sendBarometerReadion(
                ((IMonitor)o).getSensorReading().getDataAsDouble()
        
        );
    }
    public void setDirection(int dir) {
        if(dir == IOperator.ABOVE)
            _monitor.setDirection(new GreaterThan());
        else if(dir == IOperator.BELOW)
            _monitor.setDirection(new LessThan());
        else 
            _monitor.setDirection(new NullOperator());
    }
    public IOperator getDirecton() {
        return _direction;
    }
    
    
}
