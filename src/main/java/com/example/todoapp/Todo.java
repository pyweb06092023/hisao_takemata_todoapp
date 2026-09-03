package com.example.todoapp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class Todo {
    private Long id;
    @NotBlank(message = "やることを入力してください")
    @Length(max = 255, message = "やることは255文字以内で入力してください")
    private String title;
    @Length(max = 255, message = "メモは255文字以内で入力してください")
    private String detail;
    @NotBlank(message = "ジャンルを選んでください")
    private String category;
    @NotNull(message = "優先度を選んでください")
    private Integer priority;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private Boolean completed;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
