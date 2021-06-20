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



    public Satellite(String id, String type, double height, double position) {
        this.id = id;
        this.type = type;
        this.height = height;
        this.position = position;
        connections = new ArrayList<>();
    }

    public void intialiseType() {
        if (type.equals("SpaceXSatellite")) {
            velocity = 3330/(double)60;
        }
        else if (type.equals("BlueOriginSatellite")) {
            velocity = 8500/(double)60;
        }
        else if (type.equals("NasaSatellite")) {
            velocity = 5100/(double)60;
        }
        else if (type.equals("SovietSatellite")) {
            velocity = 6000/(double)60;
        }
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

    public void setPosition(double position) {
        this.position = position;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public boolean canConnectType(Device device) {
        if (this.type.equals("SpaceXSatellite")) {
            if (device.getType().equals("HandheldDevice")) {
                return true;
            }
        }
        else if (this.type.equals("BlueOriginSatellite")) {
            if (device.getType().equals("HandheldDevice") || device.getType().equals("LaptopDevice")
                || device.getType().equals("DesktopDevice")) {
                    return true;
            }
        }
        else if (this.type.equals("NasaSatellite")) {
            if (device.getType().equals("HandheldDevice") || device.getType().equals("LaptopDevice")
                || device.getType().equals("DesktopDevice")) {
                    return true;
            }
        }
        else if (this.type.equals("SovietSatellite")) {
             if (device.getType().equals("LaptopDevice")
                || device.getType().equals("DesktopDevice")) {
                return true;
            }
        }
        return false;
    }

    public boolean canConnect(Device device) {
        if (MathsHelper.satelliteIsVisibleFromDevice(this.position
            , this.height, device.getPosition()) && canConnectType(device)
            && !device.getConnected()) {
            if (this.type.equals("SpaceXSatellite")) {
                return true;
            }
            else if (this.type.equals("BlueOriginSatellite")) {
                int connNum = 0;
                int laptopNum = 0;
                int desktopNum = 0;
                for (Connection connection: connections) {
                    if (connection.getEndTime() == null) {
                        connNum++;
                        if (connection.getDeviceId().equals("LaptopDevice")) {
                            laptopNum++;
                        }
                        else if (connection.getDeviceId().equals("DesktopDevice")) {
                            desktopNum++;
                        }
                    }
                }
                if (connNum < 10 && laptopNum < 5 && desktopNum < 2) {
                    return true;
                }
            }
            else if (this.type.equals("NasaSatellite")) {
                int conNum = 0;
                int outsideRegion = 0;
                for (Connection connection: connections) {
                    if (connection.getEndTime() == null) {
                        conNum++;
                    }
                    if (connection.getDevice().getPosition() < 30 || connection.getDevice().getPosition() > 40) {
                        outsideRegion++;
                    }
                }
                if (conNum < 6) {
                    return true;
                }
                else if (outsideRegion > 0) {
                    if (device.getPosition() >= 30 && device.getPosition() <= 40) {
                        return true;
                    }
                }
            }
            else if (this.type.equals("SovietSatellilte")) {
                return true;
            }
            return false;
        }
        return false;
    }

    public void connect(Device device, LocalTime currTime) {
        Connection connection = new Connection(device, this);

        int delay = 0;
        delay = device.getDelay();

        if (this.type.equals("SpaceXSatellite")) {
            delay = 0;
        }
        else if (this.type.equals("NasaSatellite")) {
            delay = 10;
            int conNum = 0;
            for (Connection con: connections) {
                if (con.getEndTime() == null) {
                    conNum++;
                }
            }
            if (conNum >= 6) {
                for (int i = 0; i < connections.size(); i++) {
                    if (connections.get(i).getDevice().getPosition() < 30 ||
                        connections.get(i).getDevice().getPosition() > 40) {
                        if (connections.get(i).getEndTime() == null) {
                            connections.get(i).setEndTime(currTime);
                            connections.get(i).getDevice().setConnected(false);
                            break;
                        }
                    }
                }
            }
        }
        else if (this.type.equals("SovietSatellite")) {
            delay *= 2;
            int conNum = 0;
            for (Connection con: connections) {
                if (con.getEndTime() == null) {
                    conNum++;
                }
            }
            if (conNum >= 9) {
                for (int i = 0; i < connections.size(); i++) {
                    if (connections.get(i).getEndTime() == null) {
                        connections.get(i).setEndTime(currTime);
                        connections.get(i).getDevice().setConnected(false);
                        break;
                    }
                }
            }
        }
        connection.setStartTime(currTime);
        connection.setDelay(delay);
        device.setConnected(true);
        connections.add(connection);
        Collections.sort(connections);
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
