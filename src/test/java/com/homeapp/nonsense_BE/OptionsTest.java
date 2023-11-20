package com.homeapp.nonsense_BE;

import com.homeapp.nonsense_BE.models.bike.Frame;
import com.homeapp.nonsense_BE.models.bike.FullBike;
import com.homeapp.nonsense_BE.models.bike.Options;
import com.homeapp.nonsense_BE.services.OptionsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static com.homeapp.nonsense_BE.models.bike.Enums.BrakeType.*;
import static com.homeapp.nonsense_BE.models.bike.Enums.FrameStyle.*;
import static com.homeapp.nonsense_BE.models.bike.Enums.HandleBarType.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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
        Options options = optionsService.updateOptions(bike, o);
        assertFalse(options.isShowFrontGears());
        assertFalse(options.isShowRearGears());
        assertTrue(options.isShowBarStyles());
        assertTrue(options.isShowBrakeStyles());
        assertTrue(options.getBrakeStyles().contains(RIM));
        assertEquals(options.getBrakeStyles().size(), 1);
        assertTrue(options.getBarStyles().contains(BULLHORNS));
        assertTrue(options.getBarStyles().contains(FLAT));
        assertTrue(options.getBarStyles().contains(DROPS));
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
        Options options = optionsService.updateOptions(bike, o);
        assertTrue(options.isShowFrontGears());
        assertTrue(options.isShowRearGears());
        assertTrue(options.isShowBarStyles());
        assertTrue(options.getBrakeStyles().contains(RIM));
        assertTrue(options.getBrakeStyles().contains(MECHANICAL_DISC));
        assertTrue(options.getBrakeStyles().contains(HYDRAULIC_DISC));
        assertEquals(options.getBrakeStyles().size(), 3);
        assertTrue(options.getBarStyles().contains(DROPS));
        assertFalse(options.getBarStyles().contains(FLARE));
        assertFalse(options.getBarStyles().contains(FLAT));
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
        Options options = optionsService.updateOptions(bike, o);
        assertTrue(options.isShowFrontGears());
        assertTrue(options.isShowRearGears());
        assertTrue(options.isShowBarStyles());
        assertTrue(options.getBrakeStyles().contains(RIM));
        assertTrue(options.getBrakeStyles().contains(MECHANICAL_DISC));
        assertTrue(options.getBrakeStyles().contains(HYDRAULIC_DISC));
        assertEquals(options.getBrakeStyles().size(), 3);
        assertTrue(options.getBarStyles().contains(DROPS));
        assertTrue(options.getBarStyles().contains(FLARE));
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
        Options options = optionsService.updateOptions(bike, o);
        assertTrue(options.isShowFrontGears());
        assertTrue(options.isShowRearGears());
        assertTrue(options.isShowBarStyles());
        assertTrue(options.getBrakeStyles().contains(RIM));
        assertTrue(options.getBrakeStyles().contains(MECHANICAL_DISC));
        assertTrue(options.getBrakeStyles().contains(HYDRAULIC_DISC));
        assertEquals(options.getBrakeStyles().size(), 3);
        assertTrue(options.getBarStyles().contains(DROPS));
        assertTrue(options.getBarStyles().contains(FLARE));
        assertTrue(options.getBarStyles().contains(FLAT));
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