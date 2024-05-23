package com.IssueTracking.model;

import java.util.Date;
import java.util.List;

public class Issue {

    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private Date reportedDate;
    private String reporter;
    private String assignee;
    private String fixer;
    private List<Comment> comments;


    public Issue(String title, String description, String reporter) {
        this.title = title;
        this.description = description;
        this.status = Status.NEW;
        this.priority = Priority.MAJOR;
        this.reportedDate;   //생성 시의 시간
        this.reporter = reporter;
        this.fixer = null;
        this.assignee = null;
        this.comments = null;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getFixer() {
        return fixer;
    }

    public void setFixer(String fixer) {
        this.fixer = fixer;
    }

    public List<Comment> getComment() {
        return comments;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
