package com.homeapp.nonsense_BE;

import com.homeapp.nonsense_BE.models.bike.CombinedData;
import com.homeapp.nonsense_BE.models.bike.Frame;
import com.homeapp.nonsense_BE.models.bike.FullBike;
import com.homeapp.nonsense_BE.models.bike.Options;
import com.homeapp.nonsense_BE.services.OptionsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.homeapp.nonsense_BE.models.bike.Enums.BrakeType.*;
import static com.homeapp.nonsense_BE.models.bike.Enums.FrameStyle.*;
import static com.homeapp.nonsense_BE.models.bike.Enums.HandleBarType.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class OptionsTest {

    @Autowired
    private OptionsService optionsService;

    @Test
    public void test_That_Start_New_Bike_Returns_Correctly() {
        Options options = optionsService.startNewBike();
        assertEquals(options.getFrameStyles().size(), 4);
        assertTrue(options.isShowFrameSizes());
        assertTrue(options.isShowGroupSetBrand());
        assertEquals(options.getGroupsetBrand().size(), 1);
    }

    @Test
    public void test_That_Single_Speed_Gets_No_Gear_Options() {
        Options o = optionsService.startNewBike();
        o.setShowFrameStyles(false);
        Frame f = new Frame();
        f.setFrameStyle(SINGLE_SPEED);
        FullBike bike = new FullBike();
        bike.setFrame(f);
        CombinedData cd = new CombinedData();
        cd.setBike(bike);
        cd.setOptions(o);
        Options options = optionsService.updateOptions(cd);
        assertFalse(options.isShowFrontGears());
        assertFalse(options.isShowRearGears());
        assertTrue(options.isShowBarStyles());
        assertTrue(options.isShowBrakeStyles());
        assertTrue(options.getBrakeStyles().contains(RIM.getName()));
        assertEquals(options.getBrakeStyles().size(), 1);
        assertTrue(options.getBarStyles().contains(BULLHORNS.getName()));
        assertTrue(options.getBarStyles().contains(FLAT.getName()));
        assertTrue(options.getBarStyles().contains(DROPS.getName()));
        assertEquals(options.getBarStyles().size(), 3);
    }

    @Test
    public void test_That_Road_Gets_Right_Options() {
        Options o = optionsService.startNewBike();
        o.setShowFrameStyles(false);
        Frame f = new Frame();
        f.setFrameStyle(ROAD);
        FullBike bike = new FullBike();
        bike.setFrame(f);
        CombinedData cd = new CombinedData();
        cd.setBike(bike);
        cd.setOptions(o);
        Options options = optionsService.updateOptions(cd);
        assertTrue(options.isShowFrontGears());
        assertTrue(options.isShowRearGears());
        assertTrue(options.isShowBarStyles());
        assertTrue(options.getBrakeStyles().contains(RIM.getName()));
        assertTrue(options.getBrakeStyles().contains(MECHANICAL_DISC.getName()));
        assertTrue(options.getBrakeStyles().contains(HYDRAULIC_DISC.getName()));
        assertEquals(options.getBrakeStyles().size(), 3);
        assertTrue(options.getBarStyles().contains(DROPS.getName()));
        assertFalse(options.getBarStyles().contains(FLARE.getName()));
        assertFalse(options.getBarStyles().contains(FLAT.getName()));
        assertEquals(options.getBarStyles().size(), 1);
        assertTrue(options.getNumberOfRearGears().contains(10L));
        assertTrue(options.getNumberOfRearGears().contains(11L));
        assertTrue(options.getNumberOfRearGears().contains(12L));
        assertEquals(options.getNumberOfRearGears().size(), 3);
        assertTrue(options.getNumberOfFrontGears().contains(2L));
        assertEquals(options.getNumberOfFrontGears().size(), 1);
    }

    @Test
    public void test_That_Gravel_Gets_Right_Options() {
        Options o = optionsService.startNewBike();
        o.setShowFrameStyles(false);
        Frame f = new Frame();
        f.setFrameStyle(GRAVEL);
        FullBike bike = new FullBike();
        bike.setFrame(f);
        CombinedData cd = new CombinedData();
        cd.setBike(bike);
        cd.setOptions(o);
        Options options = optionsService.updateOptions(cd);
        assertTrue(options.isShowFrontGears());
        assertTrue(options.isShowRearGears());
        assertTrue(options.isShowBarStyles());
        assertTrue(options.getBrakeStyles().contains(RIM.getName()));
        assertTrue(options.getBrakeStyles().contains(MECHANICAL_DISC.getName()));
        assertTrue(options.getBrakeStyles().contains(HYDRAULIC_DISC.getName()));
        assertEquals(options.getBrakeStyles().size(), 3);
        assertTrue(options.getBarStyles().contains(DROPS.getName()));
        assertTrue(options.getBarStyles().contains(FLARE.getName()));
        assertEquals(options.getBarStyles().size(), 2);
        assertTrue(options.getNumberOfRearGears().contains(9L));
        assertTrue(options.getNumberOfRearGears().contains(10L));
        assertTrue(options.getNumberOfRearGears().contains(11L));
        assertEquals(options.getNumberOfRearGears().size(), 3);
        assertTrue(options.getNumberOfFrontGears().contains(2L));
        assertTrue(options.getNumberOfFrontGears().contains(1L));
        assertEquals(options.getNumberOfFrontGears().size(), 2);
    }

    @Test
    public void test_That_Tour_Gets_Right_Options() {
        Options o = optionsService.startNewBike();
        o.setShowFrameStyles(false);
        Frame f = new Frame();
        f.setFrameStyle(TOUR);
        FullBike bike = new FullBike();
        bike.setFrame(f);
        CombinedData cd = new CombinedData();
        cd.setBike(bike);
        cd.setOptions(o);
        Options options = optionsService.updateOptions(cd);
        assertTrue(options.isShowFrontGears());
        assertTrue(options.isShowRearGears());
        assertTrue(options.isShowBarStyles());
        assertTrue(options.getBrakeStyles().contains(RIM.getName()));
        assertTrue(options.getBrakeStyles().contains(MECHANICAL_DISC.getName()));
        assertTrue(options.getBrakeStyles().contains(HYDRAULIC_DISC.getName()));
        assertEquals(options.getBrakeStyles().size(), 3);
        assertTrue(options.getBarStyles().contains(DROPS.getName()));
        assertTrue(options.getBarStyles().contains(FLARE.getName()));
        assertTrue(options.getBarStyles().contains(FLAT.getName()));
        assertEquals(options.getBarStyles().size(), 3);
        assertTrue(options.getNumberOfRearGears().contains(8L));
        assertTrue(options.getNumberOfRearGears().contains(9L));
        assertTrue(options.getNumberOfRearGears().contains(10L));
        assertTrue(options.getNumberOfRearGears().contains(11L));
        assertEquals(options.getNumberOfRearGears().size(), 4);
        assertTrue(options.getNumberOfFrontGears().contains(3L));
        assertTrue(options.getNumberOfFrontGears().contains(2L));
        assertEquals(options.getNumberOfFrontGears().size(), 2);
    }
}