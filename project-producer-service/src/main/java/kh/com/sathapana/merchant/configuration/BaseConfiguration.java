package kh.com.sathapana.merchant.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class BaseConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
