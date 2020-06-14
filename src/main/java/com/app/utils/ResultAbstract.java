package com.app.utils;

import java.util.HashMap;
import java.util.Map;

public class ResultAbstract {

     protected Map<String, ValidationDetails> validationResult = new HashMap<>();

     public Map<String, ValidationDetails> getValidationResult() {
          return validationResult;
     }

}
