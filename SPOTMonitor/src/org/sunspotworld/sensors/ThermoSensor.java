/**
 * Wrapper for thermostat class
 * @author Dominic Lindsay
 */
package org.sunspotworld.sensors;

import com.sun.spot.resources.Resources;
import com.sun.spot.resources.transducers.ITemperatureInput;
import java.io.IOException;
import org.sunspotworld.data.SensorData;


public class ThermoSensor implements ISensor {
    private final ITemperatureInput thermostat;
    public ThermoSensor()
    {
        thermostat = 
                (ITemperatureInput) Resources.lookup(ITemperatureInput.class);
        
    }
    public double getFahrenheit()
    {
        try {
            return this.thermostat.getFahrenheit();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
    public double getCelcius()
    {
        try {
            return this.thermostat.getCelsius();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public SensorData getData() {
        return new SensorData(this.getCelcius());
    }
    
}
