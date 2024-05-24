import java.sql.*;
import java.util.ArrayList;

public class IssueList {
    private ArrayList<Issue> issues = new ArrayList<>();

    public IssueList(String ProjectName){
        String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
        String userName = "admin";
        String serverPassword = "00000000";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, userName, serverPassword);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        String issueQuery = "select * from issue where projectName = '" + ProjectName + "'";

        Statement issueStmt= null;
        ResultSet issueRs = null;
        Statement commentStmt = null;
        ResultSet commentRs = null;
        try {
            issueStmt = connection.createStatement();
            issueRs = issueStmt.executeQuery(issueQuery);
            while(issueRs.next()){
                String title = issueRs.getString("title");
                String id = issueRs.getString("id");
                String description = issueRs.getString("description");
                Status status = Status.valueOf(issueRs.getString("status"));
                Priority priority = Priority.valueOf(issueRs.getString("priority"));
                String date = issueRs.getString("date");
                String reporter = issueRs.getString("reporter");
                String assignee = issueRs.getString("assignee");
                String fixer = issueRs.getString("fixer");

                String commentQuery = "select content from comment where issue_id = '"+ id + "'";
                ArrayList<Comment> comments = new ArrayList<>();


                try{
                    commentStmt = connection.createStatement();
                    commentRs = commentStmt.executeQuery(commentQuery);
                    while(commentRs.next()){

                        String content = commentRs.getString("content");
                        String uName = commentRs.getString("userName");
                        String Cdate = commentRs.getString("createdDate");

                        comments.add(new Comment(content,uName,Cdate));

                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                issues.add(new Issue(ProjectName, title, description, status, priority, date, reporter, assignee, fixer, comments));
            }

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } finally{
            try{
                issueRs.close();
                issueStmt.close();
                commentStmt.close();
                commentRs.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }

    public ArrayList<Issue> browseAll() {
        return issues;
    }

    public int getSize(){
        return issues.size();
    }

    public Issue getTheIssue(int index){
        return issues.get(index);
    }

}
