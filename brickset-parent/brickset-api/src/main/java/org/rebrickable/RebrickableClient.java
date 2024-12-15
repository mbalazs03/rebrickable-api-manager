package org.rebrickable;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "rebrickableClient", url = "https://rebrickable.com/api/v3")
public interface RebrickableClient {

    @GetMapping("/lego/sets/{set_num}/")
    RebrickableResponse getSetByNumber(
            @PathVariable("set_num") int setNum,
            @RequestParam("key") String apiKey
    );

    @GetMapping("/lego/sets/")
    RebrickableResponse searchSets(
            @RequestParam("search") String search,
            @RequestParam("page") Integer page,
            @RequestParam("page_size") Integer pageSize,
            @RequestParam("ordering") String ordering,
            @RequestParam("key") String apiKey
    );

    @GetMapping("/lego/sets/")
    RebrickableResponse getAllSets(
            @RequestParam("page") Integer page,
            @RequestParam("page_size") Integer pageSize,
            @RequestParam("ordering") String ordering,
            @RequestParam("key") String apiKey
    );
}