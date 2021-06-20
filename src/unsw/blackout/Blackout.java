package unsw.blackout;

import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class Blackout {
    private ArrayList<Device> devices;
    private ArrayList<Satellite> satellites;
    private LocalTime currentTime;

    public Blackout() {
        devices = new ArrayList<>();
        satellites = new ArrayList<>();
        currentTime = LocalTime.of(0, 0);
    }

    public void createDevice(String id, String type, double position) {
        if (type.equals("HandheldDevice")) {
            HandheldDevice device = new HandheldDevice(id, type, position);
            device.updateDelay();
            devices.add(device);
            Collections.sort(devices);
        }
        else if (type.equals("LaptopDevice")) {
            LaptopDevice device = new LaptopDevice(id, type, position);
            device.updateDelay();
            devices.add(device);
            Collections.sort(devices);
        }
        else if (type.equals("DesktopDevice")) {
            DesktopDevice device = new DesktopDevice(id, type, position);
            device.updateDelay();
            devices.add(device);
            Collections.sort(devices);
        }
    }

    public void createSatellite(String id, String type, double height, double position) {
        if (type.equals("SpaceXSatellite")) {
            SpaceXSatellite satellite = new SpaceXSatellite(id, type, height, position);
            satellite.updateVelocity();
            satellites.add(satellite);
            Collections.sort(satellites);
        }
        else if (type.equals("BlueOriginSatellite")) {
            BlueOriginSatellite satellite = new BlueOriginSatellite(id, type, height, position);
            satellite.updateVelocity();
            satellites.add(satellite);
            Collections.sort(satellites);
        }
        else if (type.equals("NasaSatellite")) {
            NasaSatellite satellite = new NasaSatellite(id, type, height, position);
            satellite.updateVelocity();
            satellites.add(satellite);
            Collections.sort(satellites);
        }
        else if (type.equals("SovietSatellite")) {
            SovietSatellite satellite = new SovietSatellite(id, type, height, position);
            satellite.updateVelocity();
            satellites.add(satellite);
            Collections.sort(satellites);
        }
    }

    public void scheduleDeviceActivation(String deviceId, LocalTime start, int durationInMinutes) {
        for (Device device: devices) {
            if (device.getId().equals(deviceId)) {
                device.schedule(start, durationInMinutes);
            }
        }
    }

    public void removeSatellite(String id) {
        for (Satellite satellite: satellites) {
            if (id.equals(satellite.getId())) {
                satellites.remove(satellite);
            }
        }
    }

    public void removeDevice(String id) {
        for (Device device: devices) {
            if (id.equals(device.getId())) {
                devices.remove(device);
            }
        }
    }

    public void moveDevice(String id, double newPos) {
        for (Device device: devices) {
            if (id.equals(device.getId())) {
                device.setPosition(newPos);
            }
        }
    }

    public JSONObject showWorldState() {
        JSONObject result = new JSONObject();
        JSONArray devices = new JSONArray();
        JSONArray satellites = new JSONArray();

        for (Satellite satellite: this.satellites) {
            JSONObject JSONSatellite = new JSONObject();
            JSONSatellite.put("id", satellite.getId());
            JSONSatellite.put("position", satellite.getPosition());
            JSONSatellite.put("velocity", satellite.getVelocity());

            ArrayList<String> possConnections = new ArrayList<String>();
            for (Device device: this.devices) {
                if (MathsHelper.satelliteIsVisibleFromDevice(satellite.getPosition()
                    , satellite.getHeight(), device.getPosition()) && 
                    satellite.canConnectType(device)) {
                    possConnections.add(device.getId());
                    }
            }
            if (possConnections != null) {
                Collections.sort(possConnections);
            }
            JSONSatellite.put("possibleConnections", possConnections);

            JSONSatellite.put("type", satellite.getType());

            JSONArray JSONConnections = new JSONArray();
            if (satellite.getConnections() != null) {
                for (Connection connection: satellite.getConnections()) {
                    JSONObject JSONConnection = new JSONObject();
                    JSONConnection.put("deviceId", connection.getDeviceId());
                    JSONConnection.put("endTime", connection.getEndTime());
                    JSONConnection.put("minutesActive", connection.getMinutesActive());
                    JSONConnection.put("satelliteId", connection.getSatelliteId());
                    JSONConnection.put("startTime", connection.getStartTime());

                    JSONConnections.put(JSONConnection);
                }
            }
            JSONSatellite.put("connections", JSONConnections);

            JSONSatellite.put("height", satellite.getHeight());

            satellites.put(JSONSatellite);
        }

        for (Device device: this.devices) {
            JSONObject JSONDevice = new JSONObject();
            JSONDevice.put("id", device.getId());

            Boolean isConnected = device.getConnected();
            JSONDevice.put("isConnected", (isConnected));
            JSONDevice.put("position", device.getPosition());

            JSONArray JSONActivationPeriods = new JSONArray();
            if (device.getActivationTimes() != null) {
                for (ActivationTime activationTime: device.getActivationTimes()) {
                    JSONObject JSONActivationTime = new JSONObject();
                    JSONActivationTime.put("startTime", activationTime.getStartTime());
                    JSONActivationTime.put("endTime", activationTime.getEndTime());
                    JSONActivationPeriods.put(JSONActivationTime);
                }
            }
            JSONDevice.put("activationPeriods", JSONActivationPeriods);
            JSONDevice.put("type", device.getType());

            devices.put(JSONDevice);
        }

        result.put("devices", devices);
        result.put("satellites", satellites);

        result.put("currentTime", this.currentTime);

        return result;
    }

    public void simulate(int tickDurationInMinutes) {
    
        for (int i = 0; i < tickDurationInMinutes; i++) {
            for (Satellite satellite: this.satellites) {
                satellite.SovietSatelliteVelocity();
                satellite.updatePosition();
            }

            for (Satellite satellite: this.satellites) {
                for (Connection connection: satellite.getConnections()) {
                    if (connection.isActive() && connection.getMinutesActive() > 0) {
                        if (!connection.isValidTime(currentTime) ||
                            !MathsHelper.satelliteIsVisibleFromDevice(satellite.getPosition()
                            , satellite.getHeight(), connection.getDevice().getPosition())) {
                            connection.disconnect(currentTime);
                        }
                    }
                    connection.updateMinutesActive(currentTime);
                }
            }

            for (Device device: this.devices) {
                List<Double> satellitePositions = new ArrayList<>();
                for (Satellite satellite: satellites) {
                    if (satellite.canConnect(device) && device.isValidTime(currentTime)) {
                        satellitePositions.add(satellite.getPosition());
                    }
                }
                if (satellitePositions.size() >= 1) {
                    double temp = 361;
                    for (double position: satellitePositions) {
                        if (position < temp) {
                            temp = position;
                        }
                    }
                    for (Satellite satellite: satellites) {
                        if (satellite.getPosition() == temp) {
                            satellite.connect(device, currentTime);
                        }
                    }
                }
            }
            
            // TODO: if device can connect to multiple satellites, use satellite with smallest angle
            this.currentTime = this.currentTime.plusMinutes(1);
        }
    }
}
