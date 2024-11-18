package org.brickset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        BricksetApiService apiService = new BricksetApiService();

        Map<String, String> setParams = new HashMap<>();
        setParams.put("year", "2011");

        try {
            List<Set> sets = apiService.getSets(setParams);

            System.out.println("Sets from 2011:");
            if (sets.isEmpty()) {
                System.out.println("No sets found.");
            } else {
                for (Set set : sets) {
                    System.out.println(set);
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while fetching sets: " + e.getMessage());
            e.printStackTrace();
        }
    }
}