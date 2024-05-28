import java.sql.*;
import java.util.ArrayList;

public class IssueList { //이슈리스트는 어떤 프로젝트의 이슈들을 모아놓은 것이다.
    //전에 브라우즈라는 클래스가 있었는데, 브라우즈는 이름이 직관적이지 않아서 이슈리스트 안에서 브라우즈 메소드를 추가함.

    private ArrayList<Issue> issues = new ArrayList<>();

    public IssueList(String ProjectName) {//생성자는 프로젝트 이름을 받는다
        String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
        String userName = "admin";
        String serverPassword = "00000000";

        Connection connection;//연결
        try {
            connection = DriverManager.getConnection(url, userName, serverPassword);
            String issueQuery = "select * from issue where projectName = '" + ProjectName + "'";//이슈 쿼리
            //프로젝트 이름이 이것인 이슈의 모든 것을 골라라

            Statement issueStmt;
            ResultSet issueRs;


            issueStmt = connection.createStatement();
            issueRs = issueStmt.executeQuery(issueQuery); //이슈 고르기 쿼리를 실행했을 때
            while (issueRs.next()) {
                String title = issueRs.getString("title");
                String id = issueRs.getString("id");
                String description = issueRs.getString("description");
                Status status = Status.valueOf(issueRs.getString("status"));
                Priority priority = Priority.valueOf(issueRs.getString("priority"));
                String date = issueRs.getString("date");
                String reporter = issueRs.getString("reporter");
                String assignee = issueRs.getString("assignee");
                String fixer = issueRs.getString("fixer");
                //디비에 있는 정보들을 받아온다

                String commentQuery = "select * from comment where issue_id = '" + id + "'";//이슈 각각의 커멘트를 확인할 쿼리
                //이슈 id가 이것인 커멘트의 모든 것을 골라와라
                ArrayList<Comment> comments = new ArrayList<>();

                Statement commentStmt;
                ResultSet commentRs;
                commentStmt = connection.createStatement();
                commentRs = commentStmt.executeQuery(commentQuery); // 커멘트 쿼리를 실행했을 때
                while (commentRs.next()) {

                    String content = commentRs.getString("content");
                    String uName = commentRs.getString("userName");
                    String Cdate = commentRs.getString("createdDate");
                    //디비에서 데이터를 가져온 결과를 바탕으로
                    comments.add(new Comment(content, uName, Cdate));
                    //날짜에 맞는 커멘트를 추가함

                }
                commentStmt.close();
                commentRs.close();


                issues.add(new Issue(ProjectName, title, description, status, priority, date, reporter, assignee, fixer, comments));
                //이슈 하나의 모든 정보를 가지고 왔으니 이슈 모음집에 넣는다.
            }
            issueRs.close();
            issueStmt.close();

            connection.close();//연결 종료.

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public ArrayList<Issue> browseAll() {
        return issues; //모든 이슈를 불러오기
    }

    public int getSize() {
        return issues.size();//이슈 개수 불러오기
    }

    public Issue getTheIssue(int index) {
        return issues.get(index); //볓 번째 이슈 불러오기.
    }

}