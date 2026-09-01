package com.example.todoapp;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CalendarController {

    @GetMapping("/calendar")
    public String calendar(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            Model model) {
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.of(
                year == null ? today.getYear() : year,
                month == null ? today.getMonthValue() : month);

        LocalDate firstDay = currentMonth.atDay(1);
        LocalDate lastDay = currentMonth.atEndOfMonth();
        List<List<LocalDate>> weeks = makeWeeks(firstDay, lastDay);

        YearMonth previousMonth = currentMonth.minusMonths(1);
        YearMonth nextMonth = currentMonth.plusMonths(1);
        model.addAttribute("year", currentMonth.getYear());
        model.addAttribute("month", currentMonth.getMonthValue());
        model.addAttribute("from", firstDay);
        model.addAttribute("to", lastDay);
        model.addAttribute("weeks", weeks);
        model.addAttribute("previousYear", previousMonth.getYear());
        model.addAttribute("previousMonth", previousMonth.getMonthValue());
        model.addAttribute("nextYear", nextMonth.getYear());
        model.addAttribute("nextMonth", nextMonth.getMonthValue());
        return "calendar";
    }

    private List<List<LocalDate>> makeWeeks(LocalDate firstDay, LocalDate lastDay) {
        List<List<LocalDate>> weeks = new ArrayList<>();
        List<LocalDate> week = new ArrayList<>();
        int firstDayIndex = firstDay.getDayOfWeek().getValue() % 7;
        for (int i = 0; i < firstDayIndex; i++) {
            week.add(null);
        }
        for (LocalDate date = firstDay; !date.isAfter(lastDay); date = date.plusDays(1)) {
            week.add(date);
            if (week.size() == 7) {
                weeks.add(week);
                week = new ArrayList<>();
            }
        }
        if (!week.isEmpty()) {
            while (week.size() < 7) {
                week.add(null);
            }
            weeks.add(week);
        }
        return weeks;
    }
}
