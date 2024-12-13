package org.rebrickable;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("Authorization: key {apiKey}")
public interface RebrickableClient {

    @RequestLine("GET /lego/sets/{set_num}/")
    RebrickableResponse getSetByNumber(
            @Param("set_num") String setNum,
            @Param("apiKey") String apiKey
    );

    @RequestLine("GET /lego/sets/?search={search}&page_size=1000")
    RebrickableResponse searchSets(
            @Param("search") String search,
            @Param("apiKey") String apiKey
    );

    @RequestLine("GET /lego/sets/?page_size=1000")
    RebrickableResponse getAllSets(@Param("apiKey") String apiKey);
}