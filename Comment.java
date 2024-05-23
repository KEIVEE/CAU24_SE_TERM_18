package com.IssueTracking.model;

import java.util.Date;

public class Comment {

    private String content;
    private String userName;
    private Date createdDate;
    private Issue issue;

    public Comment() {
    }

    public Comment(String content, Issue issue, Date createdDate, String userName) {
        this.content = content;
        this.issue = issue;
        this.createdDate = createdDate;
        this.userName = userName;
    }


}
