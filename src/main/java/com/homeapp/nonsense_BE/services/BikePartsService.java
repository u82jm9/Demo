package com.homeapp.nonsense_BE.services;

import com.homeapp.nonsense_BE.models.bike.BikeParts;
import com.homeapp.nonsense_BE.models.bike.FullBike;
import com.homeapp.nonsense_BE.models.bike.Part;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import static com.homeapp.nonsense_BE.models.bike.Enums.BrakeType.RIM;
import static com.homeapp.nonsense_BE.models.bike.Enums.FrameStyle.SINGLE_SPEED;
import static com.homeapp.nonsense_BE.models.bike.Enums.GroupsetBrand.SHIMANO;

@Service
@Scope("singleton")
public class BikePartsService {

    private static final Logger LOGGER = LogManager.getLogger(BikePartsService.class);
    private static final String chainReactionURL = "https://www.chainreactioncycles.com/p/";
    private static final String wiggleURL = "https://www.wiggle.com/p/";
    private static final String haloURL = "https://www.halowheels.com/";
    private static final String dolanURL = "https://www.dolan-bikes.com/";
    private static final String genesisURL = "https://www.genesisbikes.co.uk/";
    private static FullBike bike;
    private BikeParts bikeParts;

    @Autowired
    FullBikeService fullBikeService;

    @Autowired
    private ShimanoGroupsetService shimanoGroupsetService;

    public BikeParts getBikePartsForBike() {
        bikeParts = new BikeParts();
        bike = fullBikeService.getBike();
        CompletableFuture<Void> handleBarFuture = CompletableFuture.runAsync(this::getHandlebarParts);
        CompletableFuture<Void> frameFuture = CompletableFuture.runAsync(this::getFrameParts);
        CompletableFuture<Void> gearFuture = CompletableFuture.runAsync(this::getGearSet);
        CompletableFuture<Void> wheelFuture = CompletableFuture.runAsync(this::getWheels);
        CompletableFuture.allOf(handleBarFuture, frameFuture, gearFuture, wheelFuture).join();
        calculateTotalPrice();
        return bikeParts;
    }

    private void getWheels() {
        String link = "";
        try {
            bike = fullBikeService.getBike();
            LOGGER.info("Method for getting Bike Wheels from Web");
            if (!bike.getFrame().getFrameStyle().equals(SINGLE_SPEED)) {
                if (!bike.getBrakeType().equals(RIM)) {
                    if (bike.getWheelPreference().equals("Cheap")) {
                        link = wiggleURL + "prime-baroudeur-disc-alloy-wheelset";
                    } else {
                        link = wiggleURL + "prime-baroudeur-alloy-wheelset";
                    }
                } else {
                    if (bike.getWheelPreference().equals("Cheap")) {
                        link = wiggleURL + "prime-primavera-56-carbon-disc-wheelset";
                    } else {
                        link = wiggleURL + "prime-primavera-50-carbon-rim-brake-wheelset";
                    }
                }
                shimanoGroupsetService.setBikePartsFromLink(link, "Wheel Set");
            } else {
                if (bike.getWheelPreference().equals("Cheap")) {
                    link = haloURL + "shop/wheels/aerorage-track-700c-wheels/";
                } else {
                    link = haloURL + "shop/wheels/carbaura-crit-700c-wheelset/";
                }
                String wheelPrice;
                String wheelName;
                Document doc = Jsoup.connect(link).get();
                Element e = doc.select("div.productDetails").get(0);
                wheelName = e.select("h1").first().text();
                if (e.select("div.priceSummary").select("ins").first() != null) {
                    wheelPrice = e.select("div.priceSummary").select("ins").select("span").first().text().replace("£", "").split(" ")[0];
                } else {
                    wheelPrice = e.select("div.priceSummary").select("span").first().text().replace("£", "").split(" ")[0];
                }
                e = e.select("div.priceSummary").select("ins").first();
                System.out.println(e);
                if (!wheelPrice.contains(".")) {
                    wheelPrice = wheelPrice + ".00";
                }
                LOGGER.info("Found Product: " + wheelName);
                LOGGER.info("For Price: " + wheelPrice);
                LOGGER.info("Link: " + link);
                bikeParts.getListOfParts().add(new Part("Wheel Set", wheelName, wheelPrice, link));
            }
        } catch (IOException e) {
            handleException("Get Wheels", e);
        }
    }

    private void getGearSet() {
        bike = fullBikeService.getBike();
        bike.setGroupsetBrand(SHIMANO);
        shimanoGroupsetService.getShimanoGroupset(bikeParts);
    }

    private void getHandlebarParts() {
        String link = "";
        try {
            bike = fullBikeService.getBike();
            LOGGER.info("Method for Getting Handlebar Parts from web");
            switch (bike.getHandleBarType()) {
                case DROPS -> link = chainReactionURL + "prime-primavera-x-light-pro-carbon-handlebar";
                case FLAT -> link = chainReactionURL + "nukeproof-horizon-v2-alloy-riser-handlebar-35mm";
                case BULLHORNS -> link = chainReactionURL + "cinelli-bullhorn-road-handlebar";
                case FLARE -> link = chainReactionURL + "ritchey-comp-venturemax-handlebar";
            }
            shimanoGroupsetService.setBikePartsFromLink(link, "Bar");
        } catch (Exception e) {
            handleException("Get HandleBar Parts", e);
        }
    }

    private void getFrameParts() {
        String link = "";
        try {
            bike = fullBikeService.getBike();
            LOGGER.info("Jsoup Method for Getting Frame Parts");
            String frameName;
            String framePrice;
            Document doc;
            switch (bike.getFrame().getFrameStyle()) {
                case ROAD -> {
                    if (bike.getFrame().isDiscBrakeCompatible()) {
                        link = dolanURL + "dolan-rdx-aluminium-disc--frameset/";
                    } else {
                        link = dolanURL + "dolan-preffisio-aluminium-road--frameset/";
                    }
                }
                case TOUR -> {
                    if (bike.getFrame().isDiscBrakeCompatible()) {
                        link = genesisURL + "genesis-fugio-frameset-vargn22330/";
                    } else {
                        link = genesisURL + "genesis-equilibrium-725-frameset-vargn21810";
                    }
                }
                case GRAVEL -> {
                    link = dolanURL + "dolan-gxa2020-aluminium-gravel-frameset/";
                }
                case SINGLE_SPEED -> {
                    link = dolanURL + "dolan-pre-cursa-aluminium-frameset/";
                }
            }
            doc = Jsoup.connect(link).get();
            if (link.contains("dolan-bikes")) {
                frameName = doc.select("div.productBuy > div.productPanel").first().select("h1").first().text();
                framePrice = doc.select("div.productBuy > div.productPanel").first().select("div.price").select("span.price").first().text();
            } else if (link.contains("genesisbikes")) {
                frameName = doc.select("h1.page-title span").first().text();
                framePrice = doc.select("div.price-box.price-final_price span.price").first().text();
            } else {
                frameName = "";
                framePrice = "";
            }
            framePrice = framePrice.replaceAll("[^\\d.]", "");
            framePrice = framePrice.split("\\.")[0] + "." + framePrice.split("\\.")[1].substring(0, 2);
            if (!framePrice.contains(".")) {
                framePrice = framePrice + ".00";
            }
            bikeParts.getListOfParts().add(new Part("Frame", frameName, framePrice, link));
            LOGGER.info("Found Frame: {}", frameName);
            LOGGER.info("For price: {}", framePrice);
            LOGGER.info("Frame link: {}", link);
        } catch (IOException e) {
            handleException("Get Frame Parts", e);
        }
    }

    private void calculateTotalPrice() {
        BigDecimal total = new BigDecimal(0);
        for (Part p : bikeParts.getListOfParts()) {
            BigDecimal bd = new BigDecimal(p.getPrice());
            total = total.add(bd);
        }
        bikeParts.setTotalBikePrice(total);
    }

    private void handleException(String message, Exception e) {
        LOGGER.error("An IOException occurred from: {}!\n{}", message, e.getMessage());
    }
}