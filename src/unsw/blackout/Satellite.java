package unsw.blackout;

import java.time.LocalTime;
import java.util.ArrayList;
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

    /**
     * Checks if a satellite can connect to the type of the device inputted.
     * 
     * @param device
     * @return true if the satellite can connect to the device type, false otherwise.
     */
    public boolean canConnectType(Device device) {
        if (this instanceof SovietSatellite) {
            if (device instanceof LaptopDevice || device instanceof DesktopDevice) {
                return true;
            }
        }

        if (this instanceof BlueOriginSatellite || this instanceof NasaSatellite) {
            return true;
        }

        if (this instanceof SpaceXSatellite) {
            if (device instanceof HandheldDevice) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a satellite can connect to a device.
     * 
     * @param device
     * @return true if the satellite can connect to the device, false otherwise.
     */

    public boolean canConnect(Device device) {
        if (MathsHelper.satelliteIsVisibleFromDevice(this.position
            , this.height, device.getPosition()) && canConnectType(device)
            && !device.getConnected()) {
            for (Connection connection: connections) {
                if (device.getId().equals(connection.getDeviceId()) && connection.isActive()) {
                    return false;
                }
            }
            if (this instanceof SpaceXSatellite || this instanceof SovietSatellite) {
                return true;
            }
            else if (this instanceof BlueOriginSatellite) {
                int laptopNum = 0;
                int desktopNum = 0;
                for (Connection connection: connections) {
                    if (connection.isActive()) {
                        if (connection.getDeviceId().equals("LaptopDevice")) {
                            laptopNum++;
                        }
                        else if (connection.getDeviceId().equals("DesktopDevice")) {
                            desktopNum++;
                        }
                    }
                }
                if (activeConnections < 10 && laptopNum < 5 && desktopNum < 2) {
                    return true;
                }
            }
            else if (this instanceof NasaSatellite) {
                int outsideRegion = 0;
                for (Connection connection: connections) {
                    if (connection.getDevice().getPosition() < 30 || connection.getDevice().getPosition() > 40) {
                    outsideRegion++;
                    }
                }
                if (activeConnections < 6) {
                    return true;
                }
                else if (outsideRegion > 0) {
                    if (device.getPosition() >= 30 && device.getPosition() <= 40) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Connects a satellite to a device.
     * 
     * @param device
     * @param currentTime
     */
    public void connect(Device device, LocalTime currentTime) {
        int delay = 0;

        if (this instanceof SpaceXSatellite) {
            delay = 0;
        }
        if (this instanceof BlueOriginSatellite) {
            delay = device.getDelay();
        }
        if (this instanceof NasaSatellite) {
            delay = 10;
            if (activeConnections >= 6) {
                for (Connection connection: connections) {
                    if (connection.getDevicePosition() < 30 || connection.getDevicePosition() > 40) {
                        if (connection.isActive()) {
                            connection.disconnect(currentTime);
                            break;
                        }
                    }
                }
            }
        }
        if (this instanceof SovietSatellite) {
            delay = device.getDelay() * 2;
            if (activeConnections >= 9) {
                for (Connection connection: connections) {
                    if (connection.isActive()) {
                        connection.disconnect(currentTime);
                        break;
                    }
                }
            }
        }

        Connection connection = new Connection(device, this);
        connection.connect(currentTime);
        connection.setDelay(delay);
        connections.add(connection);
        Collections.sort(connections);
        return;
    }

    /**
     * Updates the position of the satellite per minute.
     *  */ 
    public void updatePosition() {
        if (this instanceof SovietSatellite) {
            if (position < 140 || position > 190) {
                if (position > 190 && position < 345) {
                    velocity = -6000/60;
                }
                else if (position < 140 || position > 345) {
                    velocity = 6000/60;
                }
            }
        }
        double newPosition = position + (velocity / height);
        if (newPosition >= 360) {
            newPosition -= 360;
        }
        position = newPosition;
        return;
    }

    /**
     * If possible, connects an unconnected device to a satellite.
     * 
     * @param device
     * @param currentTime
     */
    public void updateNewConnection(Device device, LocalTime currentTime) {
        if (canConnect(device) && device.isValidTime(currentTime)) {
            connect(device, currentTime);
        }
        return;
    }

    // Sorts satellites by id.
    @Override
    public int compareTo(Satellite satellite) {
        if (getId() == null || satellite.getId() == null) {
            return 0;
        }
        return getId().compareTo(satellite.getId());
    }
}
