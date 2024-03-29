package org.sunspotworld.basestationRadios;

import org.sunspotworld.basestationRadios.PortOutOfRangeException;

/**
 * Integer wrapper class for storing a port number for the SunSpots. 
 * Forces a value between 31 and 255 as the values 0-31 are reserved for system servicea
 * and 255 is the maximum port value for the SunSpots. 
 */
public class SunspotPort 
{
    public static final int MAX_VALUE           = 255;
    public static final int MIN_VALUE           = 31;
    
    // PORTS FOR SENSORS
    public static final int DISCOVERY_PORT      = 90;
    public static final int SWITCH_PORT         = 100;
    public static final int THERMO_THRESH         = 110;
    public static final int THERMO_SAMPLE         = 115;
    public static final int LIGHT_THRESH          = 120;
    public static final int LIGHT_SAMPLE          = 125;
    public static final int ACCEL_THRESH          = 130;
    public static final int ACCEL_SAMPLE          = 135;
    public static final int MOTION_THRESH         = 140;
    public static final int PING_PORT           = 150; 
    public static final int TOWER_RECIEVER_PORT = 160;
    public static final int BASE_TOWER_PORT     = 150; 
    public static final int WATER_PORT          = 180;
    public static final int BATTERY_PORT        = 190;
    public static final int COMPASS_THRESH      = 200;
    public static final int COMPASS_SAMPLE      = 205;
    public static final int IMPACT_PORT         = 210;


    private final int value;

    public SunspotPort(int value) throws PortOutOfRangeException
    {
        if(value < MAX_VALUE && value > MIN_VALUE) {
            this.value = value;
        } else {
            throw new PortOutOfRangeException("Supplied port number must be in the range 31-255");
        }
    }

    public int getPort() 
    {
        return this.value;
    }
}
