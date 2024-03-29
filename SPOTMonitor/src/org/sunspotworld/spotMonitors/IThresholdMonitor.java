package org.sunspotworld.spotMonitors;

import org.sunspotworld.data.SensorData;
public interface IThresholdMonitor {
    double getThreshold();
    void setThreshold(double threshold);
    boolean getHasBeenMet();
    void setHasBeenMet(boolean hasBeenMet);
    SensorData getSensorReading();
    SensorData getThresholdAsSensorReading();
}
