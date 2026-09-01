package com.example.todoapp.api;

import java.util.Map;
import java.time.Duration;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Component
public class HolidayClient {
    private static final String HOLIDAYS_URL = "https://holidays-jp.github.io/api/v1/date.json";

    private final RestClient restClient;

    public HolidayClient() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(5));
        this.restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .build();
    }

    public HolidayResult getHolidays() {
        try {
            Map<String, String> holidays = restClient.get()
                    .uri(HOLIDAYS_URL)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, String>>() {
                    });
            return new HolidayResult(holidays == null ? Map.of() : holidays, false);
        } catch (RestClientException e) {
            return new HolidayResult(Map.of(), true);
        }
    }

    public record HolidayResult(Map<String, String> holidays, boolean unavailable) {
    }
}
