/*
 * Handles quearies to Database
 * Dominic Lindsay
 * Adam Cornforth
 */
package org.sunspotworld.database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.ArrayList;
import org.sunspotworld.valueObjects.LightData;
import org.sunspotworld.valueObjects.ThermoData;
import org.sunspotworld.valueObjects.AccelData;

public class QueryManager implements IQueryManager
{
    // private IDatabaseConnectionManager connection;
    private static final String DB_NAME = "testing";
    private PreparedStatement stm = null;

    public QueryManager()
    {
        // connection = DatabaseConnectionFactory.createMySQLConnection();
    }

    /**
     *
     * @param spot_address
     * @return
     */
    public synchronized int isSpotExists(String spot_address) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String isSpotExists = "SELECT * FROM Spot WHERE spot_address = ?";

        PreparedStatement record = null;
        ResultSet result = null;
        try {
            /**
             * Execute select query
             */
            record =
                connection.getConnection().prepareStatement(isSpotExists);
            record.setString(1, spot_address);

            /**
             * Access ResultSet for spot_address
             */
            result = record.executeQuery();

            /**
             * Return result
             */
            if(result.next())
                return 1;
            else
                return 0;

        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "isSpotExists: " + e);
                return 0;
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Insert spot record into db
     * @param spot_address
     * @param time long
     */
    public synchronized void createSpotRecord(String spot_address, long time) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertSpotRecord = "INSERT INTO Spot"
                + "(spot_address, created_at, updated_at)"
                + ("VALUES (?,?,?)");
        PreparedStatement insert = null;
        try {
            insert =
                connection.getConnection().prepareStatement(insertSpotRecord);
            insert.setString(1, spot_address);
            insert.setTimestamp(2, new Timestamp(time));
            insert.setTimestamp(3, new Timestamp(time));
            insert.executeUpdate();
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "insertSpotRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    public synchronized double getLatestReadingFromJobId(int job_id) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String reading_table = this.getReadingTableFromJobId(job_id); 
        String reading_field = this.getReadingFieldFromJobId(job_id);

        if(reading_table == null || reading_field == null) return -1;

        String getReading = "SELECT * " 
                + " FROM " + reading_table
                + " WHERE job_id = ? "
                + " ORDER BY id DESC " 
                + " LIMIT 1";

        PreparedStatement record = null;
        ResultSet result = null;

        try {
            /**
             * Execute select query
             */
            record =
                connection.getConnection().prepareStatement(getReading);
            record.setInt(1, job_id);

            /**
             * Access ResultSet for zone_id
             */
            result = record.executeQuery();
            if(result.next()) {
                /**
                 * Return result
                 */
                return result.getDouble(reading_table + "." + reading_field);
            } else {
                return -1;
            }

        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "getReading: " + e);
                return -1;
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    public synchronized String getReadingTableFromJobId(int job_id) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String getReadingTableFromJobId = "SELECT * " 
                + " FROM Job, Sensor"
                + " WHERE Sensor.id = Job.sensor_id "
                + " AND Job.id = ? "
                + " LIMIT 1";
        PreparedStatement record = null;
        ResultSet result = null;
        try {
            /**
             * Execute select query
             */
            record =
                connection.getConnection().prepareStatement(getReadingTableFromJobId);
            record.setInt(1, job_id);

            /**
             * Access ResultSet for zone_id
             */
            result = record.executeQuery();
            if(result.next()) {
                /**
                 * Return result
                 */
                return result.getString("Sensor.table");
            } else {
                return null;
            }

        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "getReadingTableFromJobId: " + e);
                return null;
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    public synchronized String getReadingFieldFromJobId(int job_id) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String getReadingTableFromJobId = "SELECT * " 
                + " FROM Job, Sensor"
                + " WHERE Sensor.id = Job.sensor_id "
                + " AND Job.id = ? "
                + " LIMIT 1";
        PreparedStatement record = null;
        ResultSet result = null;
        try {
            /**
             * Execute select query
             */
            record =
                connection.getConnection().prepareStatement(getReadingTableFromJobId);
            record.setInt(1, job_id);

            /**
             * Access ResultSet for zone_id
             */
            result = record.executeQuery();
            if(result.next()) {
                /**
                 * Return result
                 */
                return result.getString("Sensor.field");
            } else {
                return null;
            }

        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "getReadingTableFromJobId: " + e);
                return null;
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }


    /**
     * Gets spot_id of a SPOT given a spot_address
     * @param  spot_address spot_address of SPOT to get ID of 
     * @return spot_id
     */
    public synchronized int getSpotIdFromSpotAddress(String spot_address) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String getSpotId = "SELECT Spot.id " 
                + " FROM Spot"
                + " WHERE Spot.spot_address = ? ";

        PreparedStatement record = null;
        ResultSet result = null;
        try {
            /**
             * Execute select query
             */
            record =
                connection.getConnection().prepareStatement(getSpotId);
            record.setString(1, spot_address);

            /**
             * Access ResultSet for zone_id
             */
            result = record.executeQuery();
            if(result.next()) {
                /**
                 * Return result
                 */
                return result.getInt("Spot.id");
            } else {
                return -1;
            }

        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "getSpotId: " + e);
                return -1;
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Gets spot_address of SPOT from a search on what object it is tracking
     * 
     * Search is done using LIKE %object_title%, so object_title = "center" would return the 
     * first spot_address for objects with "center" in their name
     * 
     * @param  object_title object_title to search for related SPOT on  
     * @return spot_address
     */
    public synchronized String getSpotAddressFromObjectTitle(String object_title) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String getSpotAddress = "SELECT Spot.spot_address " 
                + " FROM Spot, Object"
                + " WHERE Spot.id = Object.spot_id "
                + " AND Object.title LIKE %?%";
        PreparedStatement record = null;
        ResultSet result = null;
        try {
            /**
             * Execute select query
             */
             record =
                connection.getConnection().prepareStatement(getSpotAddress);
            record.setString(1, object_title);

            /**
             * Access ResultSet for zone_id
             */
            result = record.executeQuery();
            if(result.next()) {
                /**
                 * Return result
                 */
                return result.getString("Spot.spot_address");
            } else {
                return null;
            }

        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "getSpotAddress: " + e);
                return null;
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Returns a spot's most recent zone id, given a spot address
     * @param  spot_address spot address to get the most recent zone for
     * @return int zone id
     */
    public synchronized int getZoneIdFromSpotAddress(String spot_address) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String getZoneId = "SELECT ZoneSpot.zone_id " 
                + " FROM Spot, ZoneSpot"
                + " WHERE Spot.spot_address = ? "
                + " AND ZoneSpot.spot_id = Spot.id "
                + " ORDER BY ZoneSpot.id DESC " 
                + " LIMIT 1";
        PreparedStatement record = null;
        ResultSet result = null;
        try {
            /**
             * Execute select query
             */
            record =
                connection.getConnection().prepareStatement(getZoneId);
            record.setString(1, spot_address);

            /**
             * Access ResultSet for zone_id
             */
            result = record.executeQuery();
            if(result.next()) {
                /**
                 * Return result
                 */
                return result.getInt("ZoneSpot.zone_id");
            } else {
                return 1;
            }

        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "getZoneId: " + e);
                return -1;
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Returns a tower's adjacent zone_id, given a spot address and a current zone_id
     * @param spot_address address of tower zone
     * @parem zone_id current zone_id of SPOT
     * @return int
     */
    public synchronized int getOtherTowerZone(String spot_address, int zone_id) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String getZoneIdTowerZone = "SELECT zone_object.zone_id "
         + "FROM Spot, Object, zone_object "
         + "WHERE Spot.spot_address = ? "
         + "AND Object.spot_id = Spot.id "
         + "AND zone_object.object_id = Object.id "
         + "AND zone_object.zone_id != ? "
         + "ORDER BY zone_object.id DESC "
         + "LIMIT 1";
         PreparedStatement record = null;
         ResultSet result = null;
        try {
            /**
             * Execute select query
             */
            record =
                connection.getConnection().prepareStatement(getZoneIdTowerZone);

            record.setString(1, spot_address);
            record.setInt(2, zone_id);

            /**
             * Access ResultSet for zone_id
             */
            result = record.executeQuery();
            if(result.next()) {
                /**
                 * Return result
                 */
                int other_zone_id = result.getInt("zone_object.zone_id");
                System.out.println("Zone id: " + other_zone_id);
                return other_zone_id;
            } else {
                System.out.println("No results for get other tower zone id ");
                return -1;
            }

        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "getZoneIdTowerZone: " + e);
                return -1;
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    private int getLastDigit(int number)
    {
        return number % 10;
    }
    /**
     * Returns a job_id, given a spot address and field column
     */
    public synchronized int getJobIdFromSpotAddressReadingFieldPortNumber(String spot_address, String column_name, int port_number) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String getJobId = null;

        // If our port number ends in 5, we are looking for a sample rate monitor. 
        // As the web front-end does not store sensors with ports ending in 5 (to prevent duplication),
        // we need to deduct 5 from the port number and then look for jobs for this sensor 
        // that do not have a threshold set but do have a sample rate set
        if(this.getLastDigit(port_number) == 5) {
            port_number -= 5; 
            getJobId = "SELECT Job.id "
             + "FROM Job, Spot, Object, Sensor "
             + "WHERE Spot.spot_address = ? "
             + "AND Object.spot_id = Spot.id "
             + "AND Job.object_id = Object.id " 
             + "AND Job.threshold IS NULL " 
             + "AND Job.sample_rate IS NOT NULL "
             + "AND Sensor.field = ? "
             + "AND Sensor.port_number = ? " 
             + "AND Sensor.id = Job.sensor_id";


        // If our port number ends in 0, we are looking for a threshold monitor, so look for jobs 
        // where sample rate is null
        // Don't check if threshold is not null because some monitors don't require thresholds
        } else {
            // Impact sensor uses sampel rate and threshold... 
            if(port_number == 210) {
                getJobId = "SELECT Job.id "
                 + "FROM Job, Spot, Object, Sensor "
                 + "WHERE Spot.spot_address = ? "
                 + "AND Object.spot_id = Spot.id "
                 + "AND Job.object_id = Object.id "
                 + "AND Sensor.field = ? "
                 + "AND Sensor.port_number = ? " 
                 + "AND Sensor.id = Job.sensor_id";
            } else {
                 getJobId = "SELECT Job.id "
                 + "FROM Job, Spot, Object, Sensor "
                 + "WHERE Spot.spot_address = ? "
                 + "AND Object.spot_id = Spot.id "
                 + "AND Job.object_id = Object.id "
                 + "AND Job.sample_rate IS NULL " 
                 + "AND Sensor.field = ? "
                 + "AND Sensor.port_number = ? " 
                 + "AND Sensor.id = Job.sensor_id";
            }
        }
        PreparedStatement record = null;
        ResultSet result = null;
        try {
            /**
             * Execute select query
             */
            record =
                connection.getConnection().prepareStatement(getJobId);

               //  System.out.println("Looking for job_id for: " + spot_address + " and column: " + column_name);

            record.setString(1, spot_address);
            record.setString(2, column_name);
            record.setInt(3, port_number);

            /**
             * Access ResultSet for job_id
             */
            result = record.executeQuery();
            if(result.next()) {
                /**
                 * Return result
                 */
                int job_id = result.getInt("Job.id");
                // System.out.println("Job id: " + job_id);
                return job_id;
            } else {
                System.out.println("No results to get job_id ");
                System.out.println(record);
                return -1;
            }

        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "getJobId: " + e);
                return -1;
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Returns all sensor ports and thresholds for a given a spot address
     */
    public synchronized ArrayList getSensorPortsJobThresholdsFromSpotAddress(String spot_address) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String query = "SELECT `Sensor`.`port_number`, `Job`.`threshold`, `Job`.`sample_rate`, `direction` "
         + "FROM Job "
         + "LEFT JOIN Sensor "
         + "ON Sensor.id = Job.sensor_id "
         + "LEFT JOIN Object "
         + "ON Object.id = Job.object_id "
         + "LEFT JOIN Spot "
         + "ON Spot.id = Object.spot_id "
         + "WHERE Spot.spot_address = ? "
         + "AND Job.tracking = 1 ";
         PreparedStatement record = null;
         ResultSet result = null;
        try {
            /**
             * Execute select query
             */
            record =
                connection.getConnection().prepareStatement(query);

            record.setString(1, spot_address);

            /**
             * Access ResultSet for job_id
             */
            result = record.executeQuery();

            /**
             * Return result
             */
            ArrayList output_array = new ArrayList();

            while (result.next()) {
                boolean sample_rate_null = false;
                boolean threshold_null = false; 
                //get port number
                int port_number = result.getInt("Sensor.port_number");
                //get sample rate or threshold value
                int sample_rate = result.getInt("Job.sample_rate");
                if(result.wasNull()) sample_rate_null = true;
                int threshold = result.getInt("Job.threshold");
                if(result.wasNull()) threshold_null = true;

                if(threshold_null == true && sample_rate_null == true) { // Both null, sensor probably doesn't need sample rate
                    output_array.add((Object)Integer.valueOf(port_number));
                    output_array.add((Object)Integer.valueOf(0));
                    output_array.add(Integer.valueOf(-1)); //no direction needed
                } else if(threshold_null == true && sample_rate_null == false) { // Threshold is null, sample rate isn't, set up sample rate
                    port_number += 5; 
                    output_array.add((Object)Integer.valueOf(port_number));
                    output_array.add((Object)Integer.valueOf(sample_rate));
                    output_array.add(Integer.valueOf(Integer.valueOf(-1))); //no direction needed
                } else if(threshold_null == false && sample_rate_null == true) { // Sample rate is null, threshold is't null, set up threshold
                    output_array.add((Object)Integer.valueOf(port_number));
                    output_array.add((Object)Integer.valueOf(threshold));
                    if(result.getString("direction") != null && result.getString("direction").compareTo("BELOW") == 0 ) //string are the same
                        output_array.add(Integer.valueOf(Integer.valueOf(0))); // new below threshold
                    else
                        output_array.add(Integer.valueOf(Integer.valueOf(1))); //new above threshold
                } else { // Both Null, we must be working with Impact Sensor
                    output_array.add((Object)Integer.valueOf(port_number));
                    output_array.add((Object)Integer.valueOf(sample_rate));
                    output_array.add((Object)Integer.valueOf(threshold));
                }
                // System.out.println("Port: " + port_number + " - Sample Rate: " +
                //         sample_rate + " - Threshold: " + threshold +
                //         " - Direction: " + result.getString("direction")
                // );

            }

            if(output_array.size() == 0) 
                System.out.println("No results to get Sensor port number ");
                
            return output_array;

        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "getSensorPortsJobThresholdsFromSpotAddress: " + e);
                return new ArrayList();
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Returns all sensor thresholds for a given a spot address
     */
    public synchronized ArrayList getSensorThresholdsFromSpotAddress(String spot_address) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String query = "SELECT Job.threshold "
         + "FROM Job "
         + "LEFT JOIN Sensor "
         + "ON Sensor.id = Job.sensor_id "
         + "LEFT JOIN Object "
         + "ON Object.id = Job.object_id "
         + "LEFT JOIN Spot "
         + "ON Spot.id = Object.spot_id "
         + "WHERE Spot.spot_address = ? "
         + "AND Job.tracking = 1 ";
        PreparedStatement record = null;
        ResultSet result = null;
        try {
            /**
             * Execute select query
             */
            record =
                connection.getConnection().prepareStatement(query);

            record.setString(1, spot_address);

            /**
             * Access ResultSet for job_id
             */
            result = record.executeQuery();

            /**
             * Return result
             */
            ArrayList output_array = new ArrayList();

            while (result.next()) {
                output_array.add((Object)Integer.valueOf(
                        result.getInt("Job.id")));
                // System.out.println("Job id: " + result.getInt("Job.id"));
            }

            if(output_array.size() == 0) 
                System.out.println("No results to get job_id ");
                
            return output_array;
                
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing "
                + "getJobId: " + e);
                return new ArrayList();
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Insert Switch record to db
     * @param switch_id String
     * @param spot_address string
     * @param time long
     */
    public synchronized void createSwitchRecord(String switch_id, String spot_address, long time) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertSwitchRecord = "INSERT INTO Switch"
                + "(switch_id, spot_address, zone_id, created_at)"
                + ("VALUES (?,?,?,?)");
        PreparedStatement insert = null;
        try {
            insert =
                connection.getConnection().prepareStatement(insertSwitchRecord);
            insert.setString(1, switch_id);
            insert.setString(2, spot_address);
            insert.setInt(3, this.getZoneIdFromSpotAddress(spot_address));
            insert.setTimestamp(4, new Timestamp(time));
            insert.executeUpdate();
            this.updateSpotUpdatedAtTime(spot_address);
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createSwitchRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Insert Zone record to db
     * @param zone_id int
     * @param spot_address int
     * @param time long
     */
    public synchronized void createZoneRecord(int zone_id, String spot_address, String tower_address, long time, int port_number) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertZoneRecord = "INSERT INTO ZoneSpot"
                + "(zone_id, spot_id, job_id, created_at)"
                + ("VALUES (?,?,?,?)");
        PreparedStatement insert = null;
        try {
            int job_id = this.getJobIdFromSpotAddressReadingFieldPortNumber(tower_address, "zone_id", port_number);
            if(job_id > 0) {
                this.createZoneRecordForRoaming(zone_id, spot_address, time, port_number);
                insert =
                    connection.getConnection().prepareStatement(insertZoneRecord);
                insert.setInt(1, zone_id);
                insert.setInt(2, this.getSpotIdFromSpotAddress(spot_address));
                insert.setInt(3, job_id);
                insert.setTimestamp(4, new Timestamp(time));
                insert.executeUpdate();
                this.updateSpotUpdatedAtTime(spot_address);
            } else {
                System.out.println("No job_id for this zone change reading!");
            }
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createZoneRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    public synchronized void createZoneRecordForRoaming(int zone_id, String spot_address, long time, int port_number) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertZoneRecordForRoaming = "INSERT INTO ZoneSpot"
                + "(zone_id, spot_id, job_id, created_at)"
                + ("VALUES (?,?,?,?)");
        PreparedStatement insert = null;
        try {
            int job_id = this.getJobIdFromSpotAddressReadingFieldPortNumber(spot_address, "zone_id", port_number);
            if(job_id > 0) {
                insert =
                    connection.getConnection().prepareStatement(insertZoneRecordForRoaming);
                insert.setInt(1, zone_id);
                insert.setInt(2, this.getSpotIdFromSpotAddress(spot_address));
                insert.setInt(3, job_id);
                insert.setTimestamp(4, new Timestamp(time));
                insert.executeUpdate();
            } else {
                System.out.println("No job_id for this zone change reading! (roaming)");
            }
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createZoneRecordForRoaming: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }


    /**
     * Insert motion Data record to db
     * @param motion int
     * @param zone_id int
     * @param time long
     */
    public synchronized void createMotionRecord(int motion, String spot_address, long time, int port_number) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertMotionRecord = "INSERT INTO Motion"
                + "(motion, spot_address, zone_id, job_id, created_at)"
                + ("VALUES (?,?,?,?,?)");
        PreparedStatement insert = null;
        try {
            int job_id = this.getJobIdFromSpotAddressReadingFieldPortNumber(spot_address, "motion", port_number);
            if(job_id > 0) {
                insert =
                    connection.getConnection().prepareStatement(insertMotionRecord);
                insert.setInt(1, motion);
                insert.setString(2, spot_address);
                insert.setInt(3, this.getZoneIdFromSpotAddress(spot_address));
                insert.setInt(4, job_id);
                insert.setTimestamp(5, new Timestamp(time));
                insert.executeUpdate();
                this.updateSpotUpdatedAtTime(spot_address);
            } else {
                System.out.println("No job_id for this motion reading!");
            }
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createMotionRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Insert light Data record to db
     * @param light double
     * @param zone_id int
     * @param time long
     */
    public synchronized void createLightRecord(double light, String spot_address, long time, int port_number) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertLightRecord = "INSERT INTO Light"
                + "(light_intensity, spot_address, zone_id, job_id, created_at)"
                + ("VALUES (?,?,?,?,?)");
        PreparedStatement insert = null;
        try {
            int job_id = this.getJobIdFromSpotAddressReadingFieldPortNumber(spot_address, "light_intensity", port_number);
            if(job_id > 0) {
                 insert =
                    connection.getConnection().prepareStatement(insertLightRecord);
                insert.setDouble(1, light);
                insert.setString(2, spot_address);
                insert.setInt(3, this.getZoneIdFromSpotAddress(spot_address));
                insert.setInt(4, job_id);
                insert.setTimestamp(5, new Timestamp(time));
                insert.executeUpdate();
                this.updateSpotUpdatedAtTime(spot_address);
            } else {
                System.out.println("No job_id for this light reading!");
            }
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createLightRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * insert Thermonitor record to db
     * @param celsiusData double
     * @param fahrenheitData double
     * @param zone_id int
     * @param time long
     */
    public synchronized void createThermoRecord(double celsiusData, String spot_address, long time, int port_number) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertThermoRecord = "INSERT INTO Heat"
                + "(heat_temperature, spot_address, zone_id, job_id, created_at)"
                + ("VALUES (?,?,?,?,?)");
        PreparedStatement insert = null;
        try {
            int job_id = this.getJobIdFromSpotAddressReadingFieldPortNumber(spot_address, "heat_temperature", port_number);
            if(job_id > 0) {
                insert = connection.getConnection().prepareStatement(insertThermoRecord);
                insert.setDouble(1, celsiusData);
                insert.setString(2, spot_address);
                insert.setInt(3, this.getZoneIdFromSpotAddress(spot_address));
                insert.setInt(4, job_id);
                insert.setTimestamp(5, new Timestamp(time));
                insert.executeUpdate();
                this.updateSpotUpdatedAtTime(spot_address);
            } else {
                System.out.println("No job_id for this heat reading!");
            }
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createThermoRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }
    /**
     * Insert Accel Data record to db
     * @param accel double
     * @param zone_id int
     * @param time long
     */
    public synchronized void createAccelRecord(double accelData, String spot_address, long time, int port_number) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertAccelRecord = "INSERT INTO Acceleration"
                + "(acceleration, spot_address, zone_id, job_id, created_at)"
                + ("VALUES (?,?,?,?,?)");
        PreparedStatement insert = null;
        try {
            int job_id = this.getJobIdFromSpotAddressReadingFieldPortNumber(spot_address, "acceleration", port_number);
            if(job_id > 0) {
                insert = connection.getConnection().prepareStatement(insertAccelRecord);
                insert.setDouble(1, accelData);
                insert.setString(2, spot_address);
                insert.setInt(3, this.getZoneIdFromSpotAddress(spot_address));
                insert.setInt(4, job_id);
                insert.setTimestamp(5, new Timestamp(time));
                insert.executeUpdate();
                this.updateSpotUpdatedAtTime(spot_address);
            } else {
                System.out.println("No job_id for this acceleration reading!");
            }
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createAccelRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Insert water Data record to db
     * @param water_percent percent that the cup has been drank to 
     * @param zone_id int
     * @param time long
     */
    public synchronized void createWaterRecord(int water_percent, String spot_address, long time, int port_number) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertWaterRecord = "INSERT INTO Water"
                + "(water_percent, spot_address, zone_id, job_id, created_at)"
                + ("VALUES (?,?,?,?,?)");
        PreparedStatement insert = null;
        try {
            int job_id = this.getJobIdFromSpotAddressReadingFieldPortNumber(spot_address, "water_percent", port_number);
            if(job_id > 0) {
                insert = connection.getConnection().prepareStatement(insertWaterRecord);
                insert.setInt(1, water_percent);
                insert.setString(2, spot_address);
                insert.setInt(3, this.getZoneIdFromSpotAddress(spot_address));
                insert.setInt(4, job_id);
                insert.setTimestamp(5, new Timestamp(time));
                insert.executeUpdate();
                this.updateSpotUpdatedAtTime(spot_address);
            } else {
                System.out.println("No job_id for this water reading!");
            }
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createWaterRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * insert Spot Record into DB
     * @param spot_address String
     */
    public synchronized void createSpotRecord(String spot_address) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertSpotRecord = "INSERT INTO Spot"
                + "(spot_id, basestation_id, created_at)"
                + "VALUES (?,?,?)";
        PreparedStatement insert = null;
        try {
            insert = connection.getConnection().prepareStatement(insertSpotRecord);
            insert.setString(1, spot_address);
            insert.setInt(2, 1); 
            insert.setDate(3, new Date(System.currentTimeMillis()));
            insert.executeUpdate();
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createLightRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * create  and insert a new zone record to DB
     * @param title String
     */
    public synchronized void createZoneRecord(String title) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertZoneRecord = "INSERT INTO Zone"
                + "(title, created_at)"
                + "VALUES (?,?)";
                PreparedStatement insert = null;
        try {
           insert =
                connection.getConnection().prepareStatement(insertZoneRecord);
            insert.setString(1, title);
            insert.setDate(2, new Date(System.currentTimeMillis()));
            insert.executeUpdate();
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createLightRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Relates a spot address, to a zone_id
     * @param spot_address string
     * @param spot_id int
     */
    public synchronized void createSpotZoneRecord(String spot_address, int spot_id) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertSpotZoneRecord = "INSERT INTO ZoneSpot"
                + "(spot_address, spot_id, created_at)"
                + "VALUES (?,?,?)";
                PreparedStatement insert = null;
        try {
           insert =
                connection.getConnection().prepareStatement(insertSpotZoneRecord);
            insert.setString(1, spot_address);
            insert.setInt(2, spot_id);
            insert.setDate(3, new Date(System.currentTimeMillis()));
            insert.executeUpdate();
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createLightRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    /**
     * Returns past 7 days of light data
     */
    public synchronized ArrayList getPastWeekLight() {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        //ArrayList for collection data
        ArrayList lightDatums = new ArrayList();
        //find timestamps
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Timestamp lastWeekTimestamp = findDateRange(currentTimestamp, -1);
        //queary db for data
        String getLastWeek = "SELECT * FROM Light WHERE "
                + "created_at >= ? "//is greater than last week
                + "AND "
                + "created_at <= ? "; //is less than today
        PreparedStatement getData = null;
        ResultSet rs = null;
        try {
            getData =
            connection.getConnection().prepareStatement(getLastWeek);
            getData.setTimestamp(1, lastWeekTimestamp);
            getData.setTimestamp(2, currentTimestamp);
            rs = (ResultSet)getData.executeQuery();
            //ArrayList for collection data
            while(rs.next())
            {
                lightDatums.add((Object)new LightData(
                    rs.getString("spot_address"),
                    rs.getDouble("light_intensity"),
                    rs.getTimestamp("created_at"),
                    rs.getInt("zone_id")
                    ));
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception getting Past Week Light" + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                rs.close();
                getData.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
            return lightDatums;
        }
    }

    /**
     * Returns past 7 days of thermo data
     */
    public synchronized ArrayList getPastWeekThermo() {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        //ArrayList for collection data
        ArrayList thermoDatums = new ArrayList();
        //find timestamps
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
         Timestamp lastWeekTimestamp = findDateRange(currentTimestamp, -1);
        //queary db for data
        String getLastWeek = "SELECT * FROM Heat WHERE "
            + "created_at >= ? "//is greater than last week
            + "AND "
            + "created_at <= ? "; //is less than today
        PreparedStatement getData = null;
        ResultSet rs = null;
        try {
            getData =
                connection.getConnection().prepareStatement(getLastWeek);
            getData.setTimestamp(1, lastWeekTimestamp);
            getData.setTimestamp(2, currentTimestamp);
            rs = (ResultSet)getData.executeQuery();
        //ArrayList for collection data
        while(rs.next())
        {
        thermoDatums.add((Object)new ThermoData(
            rs.getString("spot_address"),
            rs.getDouble("heat_temperature"),
            rs.getTimestamp("created_at"),
            rs.getInt("zone_id")
            ));
        }
        } catch (SQLException e) {
            System.err.println("SQL Exception getting Past Week Heat" + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                rs.close();
                getData.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
            return thermoDatums;
        }
    }

    /**
     * Returns past 7 days of accelleration and thermo data
     */
    public synchronized ArrayList getPastAccelThermo() {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        //ArrayList for collection data
        ArrayList accelDatums = new ArrayList();
        //find timestamps
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        Timestamp lastWeekTimestamp = findDateRange(currentTimestamp, -1);
        //queary db for data
        String getLastWeek = "SELECT * FROM Acceleration WHERE "
            + "created_at >= ? "//is greater than last week
            + "AND "
            + "created_at <= ? "; //is less than today
        PreparedStatement getData = null;
        ResultSet rs = null;
        try {
            getData =
                connection.getConnection().prepareStatement(getLastWeek);
            getData.setTimestamp(1, lastWeekTimestamp);
            getData.setTimestamp(2, currentTimestamp);
            rs = (ResultSet)getData.executeQuery();
        //ArrayList for collection data
        while(rs.next())
        {
        accelDatums.add((Object)new AccelData(
            rs.getString("spot_address"),
            rs.getDouble("acceleration"),
            rs.getTimestamp("created_at"),
            rs.getInt("zone_id")
            ));
        }
        } catch (SQLException e) {
            System.err.println("SQL Exception getting Past Week Heat" + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                rs.close();
                getData.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
            return accelDatums;
        }
    }

    public synchronized void updateBatteryPower(String spotAddress, int powerLevelPercentage)
    {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String updatePower = "UPDATE Spot SET battery_percent = ? "
                + "where spot_address = ?";
            PreparedStatement putBattery = null;
        try{
            putBattery =
                    connection.getConnection().prepareStatement(updatePower);
            putBattery.setInt(1, powerLevelPercentage);
            putBattery.setString(2, spotAddress);
            putBattery.executeUpdate();
            this.updateSpotUpdatedAtTime(spotAddress);
        }catch (Exception e) {
            System.out.println(e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                putBattery.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

    public synchronized void updateSpotUpdatedAtTime(String spotAddress)
    {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String setSpotUpdatedAt = "UPDATE Spot SET updated_at = ? "
                + "where spot_address = ?";
                PreparedStatement update = null;
        try{
            update =
                    connection.getConnection().prepareStatement(setSpotUpdatedAt);
            update.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            update.setString(2, spotAddress); 
            update.executeUpdate();
        }catch (Exception e) {
            System.out.println(e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                update.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }
    private Timestamp findDateRange(Timestamp from, int noOfWeeks){
        Calendar past = Calendar.getInstance();
        past.setTimeInMillis(from.getTime());
        past.add(Calendar.WEEK_OF_YEAR, noOfWeeks);
        return new Timestamp(past.getTimeInMillis());
    }

    public synchronized void createBarometerRecord(double bearing, String spot_address, long time, int port_number) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        String insertBearingRecord = "INSERT INTO Bearing"
                + "(bearing, spot_address, zone_id, job_id, created_at)"
                + ("VALUES (?,?,?,?,?)");
                PreparedStatement insert = null;
        try {
            int job_id = this.getJobIdFromSpotAddressReadingFieldPortNumber(spot_address, "bearing", port_number);
           if(job_id > 0) {
               insert =
                    connection.getConnection().prepareStatement(insertBearingRecord);
                insert.setDouble(1, bearing);
                insert.setString(2, spot_address);
                insert.setInt(3, this.getZoneIdFromSpotAddress(spot_address));
                insert.setInt(4, job_id);
                insert.setTimestamp(5, new Timestamp(time));
                insert.executeUpdate();
                this.updateSpotUpdatedAtTime(spot_address);
            } else {
                System.out.println("No job_id for this barometer reading!");
            }
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "createThermoRecord: " + e);
        } finally {
            if(connection.getStatus()) { 
                try { 
                if(insert != null) insert.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

	/*
	* Check if basestation exists in database
	* returns boolean
	*/
	public synchronized boolean checkIfBaseStationExists(String bsAddress) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
		String checkBaseStation = "SELECT * FROM Basestation WHERE basestation_address = ?";
        PreparedStatement record = null;
        ResultSet result = null;
		try {
		  record =
			connection.getConnection().prepareStatement(checkBaseStation);
		    record.setString(1, bsAddress);
		   result = record.executeQuery();
		    if(result.next())
			return true;
		    else
			return false;
		} catch (SQLException e) {
			System.err.println("FAILED TO CHECK BASESTATION EXISTS");
			e.printStackTrace();
			return false;
		} finally {
            if(connection.getStatus()) { 
                try { 
                if(result != null) result.close();
                if(record != null) record.close();
                try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
	}
	/*
	 * add basestation record
	 */
	public synchronized void addBasestation(String bsAddress){
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
    	String createBS = "INSERT INTO Basestation"
    		+ "(basestation_address, created_at)"
    		+ "VALUES (?,?)";
    	System.out.println("Adding Basestation: " + bsAddress);
        PreparedStatement insert = null;
    	try {
    	   insert =
    		connection.getConnection().prepareStatement(createBS);
    	    insert.setString(1, bsAddress);
    	    insert.setDate(2, new Date(System.currentTimeMillis()));
    	    insert.executeUpdate();
    	} catch (SQLException e) {
    		System.err.println("FAILED TO CREATE BASESTAION RECORD\n");
    		e.printStackTrace();
    	} finally {
            if(connection.getStatus()) { 
                try {
                    if(insert != null) insert.close();
                    try { if (connection.getConnection() != null && !connection.getConnection().isClosed()) connection.getConnection().close(); } catch (Exception e) {};
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
	}
	/*
	 * checks if spot belongs to BS
	 */
	public synchronized boolean doesSpotBelongToBS(String bsAddress, String spotAddress){
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
		String getSpots = "SELECT Spot.spot_address, Spot.basestation_id, "
			+ "Basestation.id FROM Spot "
			+ "INNER JOIN Basestation "
			+ "ON Spot.basestation_id = Basestation.id "
			+ "WHERE Spot.spot_Address = ? "
			+ "AND Basestation.basestation_address = ?";
		// System.out.println("BSAddress: " + bsAddress + " Spot address: " + spotAddress);
        PreparedStatement doCheck = null;
        ResultSet res = null;
		try {
		   doCheck =
			connection.getConnection().prepareStatement(getSpots);
		    doCheck.setString(1, spotAddress);
		    doCheck.setString(2, bsAddress);
		    res = doCheck.executeQuery();
		    if(!res.next())
			    return false; //there was no match
		    // System.out.println("Basestation owns: " + spotAddress);
		    return true; //jobs a good'un
		} catch (SQLException e) {
			System.err.println("FAILED TO CHECK IF BS EXISTS:\n");
			e.printStackTrace();
		} finally {
            if(connection.getStatus()) { 
                try { 
                    res.close();
                    doCheck.close();
                    connection.disconnect();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
	}
	/*
	 * get list of spots belonging to this basestation 'bsAddress'
	 */
	public synchronized ArrayList<String> getSpots(String bsAddress){
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
		String getSpotsQuery = "SELECT Spot.spot_address FROM Spot " 
		+ "INNER JOIN Basestation " 
		+ "ON Spot.basestation_id = Basestation.id "
		+ "WHERE Basestation.basestation_address = ?";
		ArrayList<String> spots = null;
        PreparedStatement getSpots = null;
        ResultSet res = null;
		try {
		   spots = new ArrayList();
		  getSpots =
			connection.getConnection().prepareStatement(getSpotsQuery);
		    getSpots.setString(1, bsAddress);
		  res = getSpots.executeQuery();
		    while(res.next()){
			spots.add(res.getString("Spot.spot_address"));	
		    }
		    return spots;
		} catch (SQLException e) {
			System.err.println("FAILED TO CREATE BASESTAION RECORD\n");
			e.printStackTrace();
		} finally {
            if(connection.getStatus()) { 
                try { 
                    res.close();
                    getSpots.close();
                    connection.disconnect();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            return spots;
        }
		
	}
    public void createImpactRecord(int flag, String spotAddress, long time, int portNumber) {
        IDatabaseConnectionManager connection;
        connection = DatabaseConnectionFactory.createMySQLConnection();
        System.out.println("No Implementation: \t Got flag: \t" + flag);

        String insertMotionRecord = "INSERT INTO Impact"
                + "(impact, spot_address, zone_id, job_id, created_at)"
                + ("VALUES (?,?,?,?,?)");
        PreparedStatement insert = null;
        try {
            int job_id = this.getJobIdFromSpotAddressReadingFieldPortNumber(spotAddress, "impact", portNumber);
            if(job_id > 0) {
                insert =
                    connection.getConnection().prepareStatement(insertMotionRecord);
                insert.setInt(1, flag);
                insert.setString(2, spotAddress);
                insert.setInt(3, this.getZoneIdFromSpotAddress(spotAddress));
                insert.setInt(4, job_id);
                insert.setTimestamp(5, new Timestamp(time));
                insert.executeUpdate();
                this.updateSpotUpdatedAtTime(spotAddress);
            } else {
                System.out.println("No job_id for this impact flag!");
            }
        } catch (SQLException e) {
                System.err.println("SQL Exception while preparing/Executing"
                + "Impact Record" + e);
                e.printStackTrace();
        } finally {
            if(connection.getStatus()) { 
                try { 
                insert.close();
                connection.disconnect(); 
            } catch(Exception e) {
                e.printStackTrace();
            }
            }
        }
    }
}
