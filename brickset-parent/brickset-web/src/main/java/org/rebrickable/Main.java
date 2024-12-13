package org.rebrickable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        RebrickableApiService apiService = new RebrickableApiService();

        Map<String, String> searchParams = new HashMap<>();
        searchParams.put("search", "Millennium Falcon");

        List<Set> sets = apiService.getSets(searchParams);
        System.out.println("Fetched Sets:");
        for (Set set : sets) {
            System.out.println(set);
        }
    }
}
