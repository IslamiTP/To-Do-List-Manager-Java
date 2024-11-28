package com.example.todolistmanagerjava;

import java.time.LocalDate;

public class Task {
    private int id;
    private String title;
    private String status;
    private String description;
    private LocalDate dueDate;
    private String priority;
    private String recurrence; // None, Daily, Weekly, Monthly

    public Task(int id, String title, String status, String description, LocalDate dueDate, String priority, String recurrence) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.recurrence = recurrence;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public String getRecurrence() {
        return recurrence;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setRecurrence(String recurrence) {
        this.recurrence = recurrence;
    }

    public LocalDate getNextDueDate() {
        if (recurrence == null) return null;

        switch (recurrence) {
            case "Daily":
                return dueDate.plusDays(1);
            case "Weekly":
                return dueDate.plusWeeks(1);
            case "Monthly":
                return dueDate.plusMonths(1);
            default:
                return null;
        }
    }
}
