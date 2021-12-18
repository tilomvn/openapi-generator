package kh.com.sathapana.store.service.configuration;

import kh.com.sathapana.merchant.api.MerchantServiceApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;

@Configuration
public class BaseConfiguration {

    @Value("${merchant.host}")
    private String merchantHost;

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MerchantServiceApi merchantClient() {
        var client = new MerchantServiceApi();
        client.setApiClient(new kh.com.sathapana.merchant.invoker.ApiClient().setBasePath(merchantHost));
        return client;
    }
}
