package com.example.todoapp.api;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class HolidayApiController {
    private final HolidayClient holidayClient;

    public HolidayApiController(HolidayClient holidayClient) {
        this.holidayClient = holidayClient;
    }

    @GetMapping("/api/holidays")
    public ResponseEntity<Map<String, String>> getHolidays(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to) {
        HolidayClient.HolidayResult result = holidayClient.getHolidays();
        Map<String, String> holidays = result.holidays().entrySet().stream()
                .filter(entry -> isInRange(entry.getKey(), from, to))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        ResponseEntity.BodyBuilder response = ResponseEntity.ok();
        if (result.unavailable()) {
            response.header("X-Holidays-Unavailable", "true");
        }
        return response.body(holidays);
    }

    private boolean isInRange(String date, LocalDate from, LocalDate to) {
        LocalDate holidayDate = LocalDate.parse(date);
        return (from == null || !holidayDate.isBefore(from))
                && (to == null || !holidayDate.isAfter(to));
    }
}
