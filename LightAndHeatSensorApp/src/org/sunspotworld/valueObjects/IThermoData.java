/*
 * encapsulate Thermonitor data extends ISpotData
 * Dominic Lindsay
 */
package org.sunspotworld.valueObjects;

import java.sql.Timestamp;
/**
 *
 * @author Babblebase
 */
public interface IThermoData extends ISunSpotData
{
    String getSpotAddress();
    void setSpotAddress(String spotAddress);
    double getCelciusData();
    void setCelciusData(double data);
    Timestamp getTime();
    void setTime(Timestamp time);
    int getZoneId();
    void setZoneId(int zoneId);
}
