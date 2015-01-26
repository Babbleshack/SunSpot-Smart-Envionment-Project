package org.sunspotworld.service;

import org.sunspotworld.spotMonitors.IMonitor;

/**
 * Service interface.
 * @author babbleshack
 */
public interface IService {
    // Identifiers for services
    public static final int THERMO_THRESH   = 110;
    public static final int THERMO_SAMPLE   = 115;
    public static final int LIGHT_THRESH    = 120;
    public static final int LIGHT_SAMPLE    = 125;
    public static final int ACCEL_THRESH    = 130;
    public static final int ACCEL_SAMPLE    = 135;
    public static final int MOTION_SAMPLE   = 140;
    void startService();
    void stopService();
    boolean isScheduled();
    int getServiceId();
    IMonitor getMonitor();
}
