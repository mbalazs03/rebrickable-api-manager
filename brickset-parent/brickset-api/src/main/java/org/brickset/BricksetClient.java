package org.brickset;

import feign.Param;
import feign.RequestLine;

public interface BricksetClient {

    @RequestLine("GET /api/v3.asmx/getSets?apiKey={apiKey}&userHash={userHash}&params={params}")
    BricksetResponse getSets(@Param("apiKey") String apiKey,
                   @Param("userHash") String userHash,
                   @Param("params") String params);
}
