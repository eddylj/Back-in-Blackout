package unsw.blackout;

import java.time.LocalTime;
import java.util.ArrayList;

public class Device implements Comparable<Device> {
    private String id;
    private String type;
    private double position;
    private int delay;
    private ArrayList<ActivationTime> activationTimes;
    private Boolean connected;
    private int connectionNo;
    private ArrayList<Connection> connections;

    public Device(String id, String type, double position) {
        this.id = id;
        this.type = type;
        this.position = position;
        this.connected = false;
        activationTimes = new ArrayList<ActivationTime>();
        connectionNo = 0;
        connections = new ArrayList<Connection>();
    }
    
    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public double getPosition() {
        return this.position;
    }

    public int getDelay() {
        return this.delay;
    }

    public Boolean getConnected() {
        return this.connected;
    }

    public ArrayList<ActivationTime> getActivationTimes() {
        return this.activationTimes;
    }

    public int getConnectionNo() {
        return this.connectionNo;
    }

    public ArrayList<Connection> getConnections() {
        return this.connections;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void addConnectionNo() {
        connectionNo++;
    }

    public void minusConnectionNo() {
        connectionNo--;
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    /**
     * Checks if the provided time is within the activation time of the device.
     * 
     * @param time
     * @return true if the provided time is within the activation time, false otherwise.
     */
    public Boolean isValidTime(LocalTime time) {
        for (ActivationTime activationTime: this.activationTimes) {
                if (activationTime.isValidTime(time)) {
                    return true;
                }
        }
        return false;
    }

    /**
     * Schedules an activation time for the device.
     * 
     * @param start
     * @param durationInMinutes
     */
    public void schedule(LocalTime start, int durationInMinutes) {
        ActivationTime activationTime = new ActivationTime(start, start.plusMinutes(durationInMinutes));
        activationTimes.add(activationTime);
    }

    // Sorts the device by id.
    @Override
    public int compareTo(Device device) {
        if (getId() == null || device.getId() == null) {
            return 0;
        }
        return getId().compareTo(device.getId());
    }
}
