package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import java.time.LocalTime;

import test.test_helpers.DummyConnection;
import test.test_helpers.ResponseHelper;
import test.test_helpers.TestHelper;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task3Tests {
    @Test
    public void testAWSExample() {
        String initialWorldState = new ResponseHelper(LocalTime.of(0, 0))
            .expectSatellite("BlueOriginSatellite", "A", 10000, 110, 141.66, 
                new String[] { "AWS" })
            .expectSatellite("BlueOriginSatellite", "B", 10000, 20, 141.66, 
            new String[] { "AWS" })
            .expectSatellite("BlueOriginSatellite", "C", 10000, 350, 141.66, 
                new String[] { "AWS" })
            .expectDevice("AWSCloudServer", "AWS", 40.50, false,
                new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(23, 20)}})
            .toString();
        
        String afterADay = new ResponseHelper(LocalTime.of(0, 0))
            .expectSatellite("BlueOriginSatellite", "A", 10000, 130.4, 141.66, 
            new String[] { },
            new DummyConnection[] {
                new DummyConnection("AWS", LocalTime.of(0, 0), LocalTime.of(13, 58), 832),
            })
            .expectSatellite("BlueOriginSatellite", "B", 10000, 40.4, 141.66, 
            new String[] { "AWS" },
            new DummyConnection[] {
                new DummyConnection("AWS", LocalTime.of(0, 0), LocalTime.of(23, 21), 1395),
            })
            .expectSatellite("BlueOriginSatellite", "C", 10000, 10.4, 141.66, 
                new String[] { "AWS" },
                new DummyConnection[] {
                    new DummyConnection("AWS", LocalTime.of(13, 58), LocalTime.of(23, 21), 557),
                })
            .expectDevice("AWSCloudServer", "AWS", 40.50, false,
                new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(23, 20)}})
            .toString();

        
        TestHelper plan = new TestHelper().createDevice("AWSCloudServer", "AWS", 40.5)
            .scheduleDeviceActivation("AWS", LocalTime.of(0, 0), 1400)
            .createSatellite("BlueOriginSatellite", "A", 10000, 110)
            .createSatellite("BlueOriginSatellite", "B", 10000, 20)
            .createSatellite("BlueOriginSatellite", "C", 10000, 350)
            .showWorldState(initialWorldState)
            .simulate(1440)
            .showWorldState(afterADay);
        plan.executeTestPlan();
    }

    @Test
    public void testMobileXPhone() {
        String initialWorldState = new ResponseHelper(LocalTime.of(0, 0))
            .expectSatellite("BlueOriginSatellite", "A", 10000, 20, 141.66, 
                new String[] { "MobileX" })
            .expectSatellite("SpaceXSatellite", "B", 10000, 50, 55.5, 
            new String[] { "MobileX" })
            .expectDevice("MobileXPhone", "MobileX", 30, false,
                new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(0, 1)}})
            .toString();

        String afterTwoMinutes = new ResponseHelper(LocalTime.of(0, 3))
            .expectSatellite("BlueOriginSatellite", "A", 10000, 20.04, 141.66, 
            new String[] { "MobileX" })
            .expectSatellite("SpaceXSatellite", "B", 10000, 50.02, 55.5, 
            new String[] { "MobileX" },
            new DummyConnection[] {
                new DummyConnection("MobileX", LocalTime.of(0, 0), LocalTime.of(0, 2), 1),
            })
            .expectDevice("MobileXPhone", "MobileX", 30, false,
                new LocalTime[][] { { LocalTime.of(0, 0), LocalTime.of(0, 1)}})
            .toString();

        TestHelper plan = new TestHelper().createDevice("MobileXPhone", "MobileX", 30)
            .scheduleDeviceActivation("MobileX", LocalTime.of(0, 0), 1)
            .createSatellite("BlueOriginSatellite", "A", 10000, 20)
            .createSatellite("SpaceXSatellite", "B", 10000, 50)
            .showWorldState(initialWorldState)
            .simulate(3)
            .showWorldState(afterTwoMinutes);
        plan.executeTestPlan();
    }
}
