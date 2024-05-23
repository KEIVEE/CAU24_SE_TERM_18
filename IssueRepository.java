package com.IssueTracking.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IssueRepository {
    private String url = "jdbc:mysql://localhost:3306/yourdatabase";
    private String username = "yourusername";
    private String password = "yourpassword";

    public IssueRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    public void addCommentToIssue(int issueId, String comment) {
        String query = "INSERT INTO issue_comments (issue_id, comment) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, issueId);
            statement.setString(2, comment);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 기타 데이터베이스 연동 메서드들
}
