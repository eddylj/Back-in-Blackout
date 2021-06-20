package unsw.blackout;

import java.time.LocalTime;
import java.util.Collections;

public class SpaceXSatellite extends Satellite {

    public SpaceXSatellite(String id, String type, double height, double position) {
        super(id, type, height, position);
    }

    public void updateVelocity() {
        super.setVelocity(3330/(double)60);
    }
    
    @Override
    public void connect(Device device, LocalTime currTime) {
        Connection connection = new Connection(device, this);
        connection.connect(currTime);
        connection.setDelay(0);
        super.getConnections().add(connection);
        Collections.sort(super.getConnections());
        return;

    }  

    @Override
    public boolean canConnectType(Device device) {
        if (device.getType().equals("HandheldDevice")) {
            return true;
        }
        return false;
    }
}
