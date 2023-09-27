package com.homeapp.one.demo.services;

import com.homeapp.one.demo.models.bike.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class OptionsService {

    private static Logger LOGGER = LogManager.getLogger(OptionsService.class);

    public Options startNewBike() {
        Options options = new Options();
        return options;
    }
}