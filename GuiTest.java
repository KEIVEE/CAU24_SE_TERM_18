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
                String query = "select password, category from account where id = '" + IDcheck + "'";
                try {
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while(rs.next()){
                        if(rs.getString("password").equals(pwcheck)){
                            if(rs.getString("category").equals("admin")){
                                new AdminFrame();
                            }
                            else if(rs.getString("category").equals("tester")){
                                new TesterFrame();
                            }
                            else if(rs.getString("category").equals("PL")){
                                String issueQuery = "select * from issue";
                                ArrayList<Issue> issues = new ArrayList<>();
                                Statement issueStmt = connection.createStatement();
                                ResultSet issueRs = issueStmt.executeQuery(issueQuery);

                                while(issueRs.next()){
                                    String title = issueRs.getString("title");
                                    Status status = Status.valueOf(issueRs.getString("status"));
                                    Priority priority = Priority.valueOf(issueRs.getString("priority"));
                                    String date = issueRs.getString("date");
                                    String reporter = issueRs.getString("reporter");
                                    String assignee = issueRs.getString("assignee");
                                    String fixer = issueRs.getString("fixer");

                                    ArrayList<Comment> comments = new ArrayList<>();

                                    issues.add(new Issue(title, status, priority, reporter, assignee, fixer, comments));
                                }
                                new PLFrame(new Browse(issues));
                            }
                            else if(rs.getString("category").equals("dev")){
                                new DevFrame();
                            }
                            login_status = true;
                        }

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
