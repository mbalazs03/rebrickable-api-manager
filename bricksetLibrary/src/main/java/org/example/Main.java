package org.example;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        BricksetApiRequests bricksetApiRequests = new BricksetApiRequests();

//        String response1 = bricksetApiRequests.getSet(1979);
//        System.out.println(response1);
//
//        // Example 2: Query by year and theme
//        String response2 = bricksetApiRequests.getSet(2011, "NINJAGO");
//        System.out.println(response2);
//
//        // Example 3: Query by year, theme, and name
//        System.out.println("response3~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        String response3 = bricksetApiRequests.getSet(2011, "NINJAGO", "TURBO SHREDDER");
//        System.out.println(response3);

        // Example 4: Query by custom attributes (dynamic query)
        Map<String, String> customParams = new HashMap<>();
        customParams.put("query", "Kai");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~response4~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        String response4 = bricksetApiRequests.getSet(customParams);
        String keyInfo = bricksetApiRequests.extractKeyInfoToFile(response4);
        System.out.println(keyInfo);


    }
}