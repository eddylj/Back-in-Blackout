package unsw.blackout;

import java.time.LocalTime;
import java.util.Collections;

public class NasaSatellite extends Satellite {

    public NasaSatellite(String id, String type, double height, double position) {
        super(id, type, height, position);
    }

    public void updateVelocity() {
        super.setVelocity(5100/(double)60);
    }
    
    @Override
    public void connect(Device device, LocalTime currTime) {
        Connection connection = new Connection(device, this);

        if (super.getActiveConnections() >= 6) {
            for (int i = 0; i < super.getConnections().size(); i++) {
                if (super.getConnections().get(i).getDevicePosition() < 30 ||
                super.getConnections().get(i).getDevicePosition() > 40) {
                    if (super.getConnections().get(i).isActive()) {
                        super.getConnections().get(i).disconnect(currTime);
                        break;
                    }
                }
            }
        }
        connection.connect(currTime);
        connection.setDelay(10);
        super.getConnections().add(connection);
        Collections.sort(super.getConnections());
        return;
    }

    @Override
    public boolean canConnect(Device device) {
        if (MathsHelper.satelliteIsVisibleFromDevice(super.getPosition()
            , super.getHeight(), device.getPosition()) && canConnectType(device)
            && !device.getConnected()) {
            int outsideRegion = 0;
            for (Connection connection: super.getConnections()) {
                if (connection.getDevice().getPosition() < 30 || connection.getDevice().getPosition() > 40) {
                    outsideRegion++;
                }
            }
            if (super.getActiveConnections() < 6) {
                return true;
            }
            else if (outsideRegion > 0) {
                if (device.getPosition() >= 30 && device.getPosition() <= 40) {
                    return true;
                }
            }
        }
        return false;
    }
}
