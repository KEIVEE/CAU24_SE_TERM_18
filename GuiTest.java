import javax.swing.*;
import javax.xml.transform.Result;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

class IssueFrame extends JFrame {
    public IssueFrame(){
        super("ISSUE HANDLING SYSTEM");
        setVisible(true);
        setSize(400,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}


class MyFrame extends JFrame{
    JTextField id;
    JTextField password;
    JButton jok;
    ArrayList<Issue> issues = new ArrayList<>();

    Project proj = new Project();
    public MyFrame(){
        super("ISSUE HANDLING SYSTEM - LOGIN");
        setVisible(true);
        setSize(400,300);
        JPanel panel = new JPanel(); //id,password panel
        panel.setLayout(new GridLayout(2,2));
        id = new JTextField(30);
        password = new JTextField(30);
        panel.add(new JLabel("ID : "));
        panel.add(id);
        panel.add(new JLabel("PASSWORD : "));
        panel.add(password);

        JPanel panel2 = new JPanel();
        jok = new JButton("Log in");
        jok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean login_status = false;
                String IDcheck;
                String pwcheck;

                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                String userName = "admin";
                String serverPassword = "00000000";

                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(url, userName, serverPassword);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                IDcheck = id.getText();
                pwcheck = password.getText();

                String loginQuery = "select * from account where id = '" + IDcheck + "'";
                String issueQuery = "select * from issue";
                String projectQuery = "select * from project";

                try {

                    Statement projectStmt = connection.createStatement();
                    ResultSet projectRs = projectStmt.executeQuery(projectQuery);
                    while(projectRs.next()){
                        proj.add(projectRs.getString("name"));
                    }



                    Statement issueStmt = connection.createStatement();
                    ResultSet issueRs = issueStmt.executeQuery(issueQuery);
                    while(issueRs.next()){
                        String name = issueRs.getString("projectName");
                        String title = issueRs.getString("title");
                        Status status = Status.valueOf(issueRs.getString("status"));
                        Priority priority = Priority.valueOf(issueRs.getString("priority"));
                        String date = issueRs.getString("date");
                        String reporter = issueRs.getString("reporter");
                        String assignee = issueRs.getString("assignee");
                        String fixer = issueRs.getString("fixer");

                        ArrayList<Comment> comments = new ArrayList<>();

                        issues.add(new Issue(name, title, status, priority, date, reporter, assignee, fixer, comments));
                    }

                    Statement stmt = connection.createStatement();
                    ResultSet loginRs = stmt.executeQuery(loginQuery);
                    while(loginRs.next()) {
                        if (loginRs.getString("password").equals(pwcheck)) {
                            if (loginRs.getString("category").equals("admin")) {
                                new AdminFrame();
                            } else if (loginRs.getString("category").equals("tester")) {
                                Tester testerUser = new Tester(loginRs.getString("name"));
                                new ProjectSelection(proj, issues, testerUser);
                            } else if (loginRs.getString("category").equals("PL")) {
                                PL PLUser = new PL(loginRs.getString("name"));
                                new ProjectSelection(proj, issues, PLUser);
                            } else if (loginRs.getString("category").equals("dev")) {
                                Dev devUser = new Dev(loginRs.getString("name"));
                                new ProjectSelection(proj, issues, devUser);
                            }
                            login_status = true;

                        }//비번 틀린 곳을 구현해야 함
                    }




                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if(!login_status){//로그인 실패 시
                    new MyFrame();//다시 뜨게 함
                }


                dispose();
                revalidate();
                repaint();

                try {
                    connection.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        panel2.add(jok);

        JPanel bigPanel = new JPanel();
        bigPanel.setLayout(new GridLayout(2,1));
        bigPanel.add(panel);
        bigPanel.add(panel2);


        add(bigPanel);

        super.revalidate();
        super.repaint();
        pack();//크기정렬
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

}

public class GuiTest{

    public static void main(String[] args) {
        new MyFrame();


    }
}
