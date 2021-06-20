package unsw.blackout;

import java.time.LocalTime;
import java.util.Collections;

public class BlueOriginSatellite extends Satellite {

    public BlueOriginSatellite(String id, String type, double height, double position) {
        super(id, type, height, position);
    }

    public void updateVelocity() {
        super.setVelocity(8500/(double)60);
    }

    @Override
    public void connect(Device device, LocalTime currTime) {
        Connection connection = new Connection(device, this);
        connection.connect(currTime);
        connection.setDelay(device.getDelay());
        super.getConnections().add(connection);
        Collections.sort(getConnections());
        return;

    }

    @Override
    public boolean canConnect(Device device) {
        if (MathsHelper.satelliteIsVisibleFromDevice(super.getPosition()
            , super.getHeight(), device.getPosition()) && canConnectType(device)
            && !device.getConnected()) {
            int laptopNum = 0;
            int desktopNum = 0;
            for (Connection connection: super.getConnections()) {
                if (connection.isActive()) {
                    if (connection.getDeviceId().equals("LaptopDevice")) {
                        laptopNum++;
                    }
                    else if (connection.getDeviceId().equals("DesktopDevice")) {
                        desktopNum++;
                    }
                }
            }
            if (super.getActiveConnections() < 10 && laptopNum < 5 && desktopNum < 2) {
                return true;
            }
        }
        return false;
    }
}
