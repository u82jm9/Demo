package com.homeapp.nonsense_BE.services;

import com.homeapp.nonsense_BE.models.bike.FullBike;
import com.homeapp.nonsense_BE.models.bike.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import static com.homeapp.nonsense_BE.models.bike.Enums.BrakeType.*;
import static com.homeapp.nonsense_BE.models.bike.Enums.FrameStyle.*;
import static com.homeapp.nonsense_BE.models.bike.Enums.GroupsetBrand.SHIMANO;
import static com.homeapp.nonsense_BE.models.bike.Enums.HandleBarType.*;

@Service
public class OptionsService {

    private static final Logger LOGGER = LogManager.getLogger(OptionsService.class);

    private static OptionsService instance;
    private Options options;

    private OptionsService() {
        this.options = new Options();
    }

    public static OptionsService getInstance() {
        if (instance == null) {
            instance = new OptionsService();
        }
        return instance;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public Options startNewBike() {
        LOGGER.info("Getting Options for a new Bike!");
        Options o = getOptions();
        o.getGroupsetBrand().add(SHIMANO);
        o.setShowGroupSetBrand(true);
        o.getFrameSizes().add(48L);
        o.getFrameSizes().add(50L);
        o.getFrameSizes().add(52L);
        o.getFrameSizes().add(54L);
        o.getFrameSizes().add(56L);
        o.setShowFrameSizes(true);
        o.getFrameStyles().add(SINGLE_SPEED);
        o.getFrameStyles().add(GRAVEL);
        o.getFrameStyles().add(TOUR);
        o.getFrameStyles().add(ROAD);
        o.setShowFrameStyles(true);
        return o;
    }

    public Options updateOptions(FullBike bike, Options o) {
        setOptions(o);
        if (!o.isShowFrameStyles()) {
            getGearOptions(bike);
            getBarOptions(bike);
            getBrakeOptions(bike);
        }
        return o;
    }

    public void getGearOptions(FullBike bike) {
        Options o = getOptions();
        switch (bike.getFrame().getFrameStyle()) {
            case ROAD -> {
                o.getNumberOfRearGears().add(10L);
                o.getNumberOfRearGears().add(11L);
                o.getNumberOfRearGears().add(12L);
                o.getNumberOfFrontGears().add(2L);
            }
            case TOUR -> {
                o.getNumberOfRearGears().add(11L);
                o.getNumberOfRearGears().add(10L);
                o.getNumberOfRearGears().add(9L);
                o.getNumberOfRearGears().add(8L);
                o.getNumberOfFrontGears().add(2L);
                o.getNumberOfFrontGears().add(3L);
            }
            case GRAVEL -> {
                o.getNumberOfRearGears().add(9L);
                o.getNumberOfRearGears().add(10L);
                o.getNumberOfRearGears().add(11L);
                o.getNumberOfFrontGears().add(2L);
                o.getNumberOfFrontGears().add(1L);
            }
            default -> {

            }
        }
        if (!bike.getFrame().getFrameStyle().equals(SINGLE_SPEED)) {
            o.setShowFrontGears(true);
            o.setShowRearGears(true);
        }
    }

    public void getBarOptions(FullBike b) {
        Options o = getOptions();
        o.setShowBarStyles(true);
        o.getBarStyles().add(DROPS);
        switch (b.getFrame().getFrameStyle()) {
            case SINGLE_SPEED -> {
                o.getBarStyles().add(BULLHORNS);
                o.getBarStyles().add(FLAT);
            }
            case TOUR -> {
                o.getBarStyles().add(FLARE);
                o.getBarStyles().add(FLAT);
            }
            case GRAVEL -> {
                o.getBarStyles().add(FLARE);
            }
            default -> {
            }
        }
    }

    public void getBrakeOptions(FullBike b) {
        Options o = getOptions();
        o.setShowBrakeStyles(true);
        o.getBrakeStyles().add(RIM);
        if (!b.getFrame().getFrameStyle().equals(SINGLE_SPEED)) {
            o.getBrakeStyles().add(MECHANICAL_DISC);
            o.getBrakeStyles().add(HYDRAULIC_DISC);
        }
    }
}