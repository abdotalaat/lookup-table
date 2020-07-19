package com.abdotalaat.poc.lookuptable;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/lookup")
public class LookupController {

  private Logger logger = LoggerFactory.getLogger(LookupController.class);

  @Autowired private LookupService lookupService;

  @GetMapping("{query}")
  public ResponseEntity<String> findLookup(@PathVariable("query") String query) {

    try {
      Object result = lookupService.find(query);
      return ResponseEntity.ok(result.toString());
    } catch (Exception e) {
      logger.error("Failed to find result for query {}", query);
    }
    return ResponseEntity.ok("");
  }
}
