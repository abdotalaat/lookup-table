package com.abdotalaat.poc.lookuptable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@SpringBootApplication
public class LookupTableApplication {

  public static void main(String[] args) {
    SpringApplication.run(LookupTableApplication.class, args);
  }

  @Component
  class DataLoader implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private JsonParser parser = new JsonParser();

    @Autowired private LookupService lookupService;

    @Override
    public void run(String... strings) throws Exception {
      logger.info("Seeding data ...");

      addLookup(
          "{\"category\":\"tax\",\"country\":\"UAE\",\"type\":\"drinks\",\"language\":\"EN\",\"label\":\"ABC\",\"rate\":\"5%\"}");
      addLookup(
          "{\"category\":\"tax\",\"country\":\"UAE\",\"type\":\"drinks\",\"language\":\"AR\",\"label\":\"XYZ\",\"rate\":\"5%\"}");
      addLookup(
          "{\"category\":\"tax\",\"country\":\"SG\",\"type\":\"drinks\",\"language\":\"SG\",\"label\":\"dfff\",\"rate\":\"10%\"}");
      addLookup(
          "{\"category\":\"tax\",\"country\":\"SG\",\"type\":\"drinks\",\"language\":\"SG_EN\",\"label\":\"fff\",\"rate\":\"10%\"}");

      addLookup("{\"category\":\"TV\",\"country\":\"UAE\",\"type\":\"sony\"}");
      addLookup("{\"category\":\"TV\",\"country\":\"UAE\",\"type\":\"Samsung\"}");

      logger.info("Seeding data done");
    }

    private void addLookup(String lookup) {
      JsonObject lookupObj = parser.parse(lookup).getAsJsonObject();
      lookupService.addLookup(lookupObj);
    }
  }
}
