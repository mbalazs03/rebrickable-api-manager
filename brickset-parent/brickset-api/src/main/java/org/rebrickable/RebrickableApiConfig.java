package org.rebrickable;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.rebrickable")
public class RebrickableApiConfig {
}
