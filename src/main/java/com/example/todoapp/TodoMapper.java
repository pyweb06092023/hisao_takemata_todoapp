package com.example.todoapp;

import java.util.List;
import java.time.LocalDate;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TodoMapper {
    List<Todo> search(@Param("keyword") String keyword, @Param("category") String category,
            @Param("order") String order, @Param("from") LocalDate from, @Param("to") LocalDate to);

    List<Todo> searchForList(@Param("keyword") String keyword, @Param("category") String category,
            @Param("order") String order, @Param("includeCompleted") boolean includeCompleted, @Param("trash") boolean trash,
            @Param("limit") int limit, @Param("offset") int offset);

    int countForList(@Param("keyword") String keyword, @Param("category") String category,
            @Param("includeCompleted") boolean includeCompleted, @Param("trash") boolean trash);

    Todo findById(Long id);

    void deleteById(Long id);

    void restoreById(Long id);

    void insert(Todo todo);

    void update(Todo todo);
}
