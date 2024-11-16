package org.brickset;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        BricksetApiRequests bricksetApiRequests = new BricksetApiRequests();

        Map<String, String> customParams = new HashMap<>();
        customParams.put("year", "2011");

        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~response4~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        String response4 = bricksetApiRequests.getSet(customParams);
        System.out.println(response4);
        String outputFilePath = "sets_output.json";
        bricksetApiRequests.extarctKeyInfoToFile(response4, outputFilePath);

    }

}








