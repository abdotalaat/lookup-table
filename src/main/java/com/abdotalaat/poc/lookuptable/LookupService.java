package com.abdotalaat.poc.lookuptable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
@Scope("application")
public class LookupService {

  private Map<String, Object> cache = new HashMap<String, Object>();
  private ArrayList<JsonObject> lookups = new ArrayList<>();
  Logger logger = LoggerFactory.getLogger(LookupService.class);

  public Object find(String query) throws Exception {
    try {
      if (cache.containsKey(query)) return cache.get(query);
      ArrayList<JsonObject> result = new ArrayList<>();
      JsonParser parser = new JsonParser();
      JsonObject qo = parser.parse(query).getAsJsonObject();
      Set<Map.Entry<String, JsonElement>> entrySet = qo.entrySet();
      for (JsonObject obj : lookups) {
        boolean equal = true;
        for (Map.Entry<String, JsonElement> entry : entrySet) {
          String key = entry.getKey();
          if (key.equals("sort")) continue;
          if (!(obj.get(key) != null && qo.get(key) != null && obj.get(key).equals(qo.get(key)))) {
            equal = false;
            break;
          }
        }
        if (equal) result.add(obj);
      }
      sortResult(result, qo);
      if (result.size() == 0) {
        logger.error("Failed to find result for query {}", query);
        return null;
      }
      if (result.size() == 1) {
        cache.put(query, result.get(0));

        return result.get(0);
      }
      cache.put(query, result);
      return result;
    } finally {

    }
  }

  private void sortResult(ArrayList<JsonObject> result, JsonObject qo) {
    if (qo.has("sort")) {
      final String sort = qo.get("sort").getAsString();
      Collections.sort(
          result,
          new Comparator<JsonObject>() {
            @Override
            public int compare(JsonObject o1, JsonObject o2) {
              try {
                if (o1.get(sort) == null || o1.get(sort).getAsString() != null)
                  return o1.get(sort).getAsString().compareTo(o2.get(sort).getAsString());

              } catch (Exception e) {
                e.printStackTrace();
              }
              if (o1.get(sort) == null || !(o1.get(sort) instanceof Comparable)) return 1;
              if (o2.get(sort) == null || !(o2.get(sort) instanceof Comparable)) return -1;
              return ((Comparable) o1.get(sort)).compareTo(o2.get(sort));
            }
          });
    }
  }

  public ArrayList<JsonObject> getLookups() {
    return lookups;
  }

  public void setLookups(ArrayList<JsonObject> objects) {
    this.lookups = objects;
  }

  public void addLookup(JsonObject lookup) {
    this.lookups.add(lookup);
  }
}
