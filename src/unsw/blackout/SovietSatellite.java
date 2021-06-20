package unsw.blackout;

import java.time.LocalTime;
import java.util.Collections;

public class SovietSatellite extends Satellite {

    public SovietSatellite(String id, String type, double height, double position) {
        super(id, type, height, position);
    }

    public void updateVelocity() {
        super.setVelocity(6000/(double)60);
    }

    @Override
    public void connect(Device device, LocalTime currTime) {
        Connection connection = new Connection(device, this);

        if (super.getActiveConnections() >= 9) {
            for (int i = 0; i < super.getConnections().size(); i++) {
                if (super.getConnections().get(i).isActive()) {
                    super.getConnections().get(i).disconnect(currTime);
                    break;
                }
            }
        }
        connection.connect(currTime);
        connection.setDelay(device.getDelay()*2);
        super.getConnections().add(connection);
        Collections.sort(super.getConnections());
        return;

    }
    
    @Override
    public boolean canConnectType(Device device) {
        if (device.getType().equals("HandheldDevice") 
            || device.getType().equals("DesktopDevice")) {
            return true;
        }
        return false;
    }
}
