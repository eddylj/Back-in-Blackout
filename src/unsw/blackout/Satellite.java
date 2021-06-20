package unsw.blackout;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Satellite implements Comparable<Satellite> {
    private String id;
    private String type;
    private double height;
    private double position;
    private double velocity;
    private ArrayList<Connection> connections;
    private int activeConnections;

    public Satellite(String id, String type, double height, double position) {
        this.id = id;
        this.type = type;
        this.height = height;
        this.position = position;
        connections = new ArrayList<>();
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public double getHeight() {
        return this.height;
    }

    public double getPosition() {
        return this.position;
    }

    public double getVelocity() {
        return this.velocity;
    }

    public ArrayList<Connection> getConnections() {
        return this.connections;
    }

    public int getActiveConnections() {
        return this.activeConnections;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setActiveConnections(int activeConnections) {
        this.activeConnections = activeConnections;
    }

    public boolean canConnectType(Device device) {
        if (device.getType().equals("HandheldDevice") || device.getType().equals("LaptopDevice")
            || device.getType().equals("DesktopDevice")) {
                return true;
        }
        return false;
    }

    public boolean canConnect(Device device) {
        if (MathsHelper.satelliteIsVisibleFromDevice(this.position
            , this.height, device.getPosition()) && canConnectType(device)
            && !device.getConnected()) {
            return true;
            }
        return false;
    }

    public void connect(Device device, LocalTime time) {
        return;
    }

    public void SovietSatelliteVelocity() {
        if (this.getType().equals("SovietSatellite")) {
            if (position < 140 || position > 190) {
                if (position > 190 && position < 345) {
                    velocity = -6000/60;
                }
                else if (position < 140 || position > 345) {
                    velocity = 6000/60;
                }
            }
        }
        return;
    }

    public void updatePosition() {
        double newPosition = position + (velocity / height);
        if (newPosition >= 360) {
            newPosition -= 360;
        }
        position = newPosition;
        return;
    }

    @Override
    public int compareTo(Satellite satellite) {
        if (getId() == null || satellite.getId() == null) {
            return 0;
        }
        return getId().compareTo(satellite.getId());
    }
}
