package com.example.todoapp;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TodoService {
    private final TodoMapper todoMapper;

    public TodoService(TodoMapper todoMapper) {
        this.todoMapper = todoMapper;
    }

    public List<Todo> search(String keyword, String category, String order) {
        return search(keyword, category, order, null, null);
    }

    public List<Todo> searchForList(String keyword, String category, String order, boolean includeCompleted,
            int limit, int offset) {
        return todoMapper.searchForList(keyword, category, order, includeCompleted, false, limit, offset);
    }

    public List<Todo> searchForTrash(String keyword, String category, String order, int limit, int offset) {
        return todoMapper.searchForList(keyword, category, order, true, true, limit, offset);
    }

    public int countForList(String keyword, String category, boolean includeCompleted) {
        return todoMapper.countForList(keyword, category, includeCompleted, false);
    }

    public int countForTrash(String keyword, String category) {
        return todoMapper.countForList(keyword, category, true, true);
    }

    public List<Todo> search(String keyword, String category, String order, LocalDate from, LocalDate to) {
        return todoMapper.search(keyword, category, order, from, to);
    }

    public Todo findById(Long id) {
        return todoMapper.findById(id);
    }

    public void create(Todo todo) {
        todoMapper.insert(todo);
        log.info("Todo created. id={}", todo.getId());
    }

    public void update(Todo todo) {
        Todo existing = todoMapper.findById(todo.getId());
        boolean wasCompleted = Boolean.TRUE.equals(existing.getCompleted());
        boolean isCompleted = Boolean.TRUE.equals(todo.getCompleted());
        if (!wasCompleted && isCompleted) {
            todo.setCompletedAt(LocalDateTime.now());
        } else if (wasCompleted && !isCompleted) {
            todo.setCompletedAt(null);
        } else {
            todo.setCompletedAt(existing.getCompletedAt());
        }
        todoMapper.update(todo);
        log.info("Todo updated. id={}", todo.getId());
    }

    public void delete(Long id) {
        todoMapper.deleteById(id);
        log.info("Todo deleted. id={}", id);
    }

    public void restore(Long id) {
        todoMapper.restoreById(id);
    }

    public void setPinned(Long id, boolean pinned) {
        todoMapper.togglePinned(id, pinned);
    }
}
