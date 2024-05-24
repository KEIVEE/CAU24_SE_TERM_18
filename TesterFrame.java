import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


class TesterF extends JFrame {
    String projectName;
    String userName;
    public TesterF(String projectName, String userName){
        super("ISSUE HANDLING");
        this.projectName = projectName;
        this.userName = userName;
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createTab());
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();
        JPanel addIssuePane = new JPanel();
        JButton addIssueButton = new JButton("이슈 등록 버튼");
        addIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddIssueF(projectName, userName);
            }
        });
        addIssuePane.add(addIssueButton);
        pane.addTab("이슈 등록하기", addIssuePane);
        pane.addTab("내가 올린 이슈", new JLabel("내가 올린 걸 보고 fixed를 resolved로"));

        return pane;
    }
}

class AddIssueF extends JFrame{
    JComboBox<Priority> selectPriority = new JComboBox<>();

    AddIssueF(String projectName, String userName){
        super("REPORT ISSUE");
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pane = reportIssuePanel(projectName, userName);
        add(pane);
        repaint();
        revalidate();
    }

    JPanel reportIssuePanel(String projectName, String userName){
        JPanel bigPanel = new JPanel(new BorderLayout());

        JPanel title = new JPanel(new GridLayout(1, 2));
        JLabel title1 = new JLabel("Title: ");
        JTextField title2 = new JTextField(45);
        title.add(title1);
        title.add(title2);
        bigPanel.add(title, BorderLayout.NORTH);

        JPanel description = new JPanel(new BorderLayout());
        JLabel description1 = new JLabel("Description(up to 500 characters)");
        JTextField description2 = new JTextField(500);
        description.add(description1, BorderLayout.NORTH);
        description.add(description2, BorderLayout.CENTER);
        bigPanel.add(description, BorderLayout.CENTER);

        JPanel priority = new JPanel(new GridLayout(1, 4));
        JPanel priority1 = new JPanel(); //combobox button
        JPanel priority2 = new JPanel(); // vacant panel
        JButton priority3 = new JButton("REPORT"); //ok button
        priority3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String issueTitle = title2.getText();
                String issueDescription = description2.getText();
                Priority issuePriority = selectPriority.getItemAt(selectPriority.getSelectedIndex());



                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                String serverUserName = "admin";
                String serverPassword = "00000000";

                String query = "insert into issue values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(url, serverUserName, serverPassword);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                PreparedStatement pstmt = null;

                try{
                    pstmt = connection.prepareStatement(query);

                    Issue newIssue = new Issue(projectName, issueTitle, issueDescription, Status.NEW, issuePriority, userName);
                    pstmt.setString(1, projectName);
                    pstmt.setString(2, projectName + newIssue.getDate());
                    pstmt.setString(3, issueTitle);
                    pstmt.setString(4, issueDescription);
                    pstmt.setString(5, Status.NEW.toString());
                    pstmt.setString(6, issuePriority.toString());
                    pstmt.setString(7, newIssue.getDate());
                    pstmt.setString(8, userName);
                    pstmt.setString(9, null);
                    pstmt.setString(10, null);

                    pstmt.executeUpdate();

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                repaint();
                revalidate();
                try {
                    pstmt.close();
                    connection.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();

            }
        });

        JButton priority4 = new JButton("CANCEL"); //cancel button
        priority4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        selectPriority.addItem(Priority.MAJOR);
        selectPriority.addItem(Priority.BLOCKER);
        selectPriority.addItem(Priority.CRITICAL);
        selectPriority.addItem(Priority.MINOR);
        selectPriority.addItem(Priority.TRIVIAL);
        priority1.add(selectPriority);
        priority.add(priority1);
        priority.add(priority2);
        priority.add(priority3);
        priority.add(priority4);



        bigPanel.add(new JLabel("  "), BorderLayout.EAST);
        bigPanel.add(new JLabel("  "), BorderLayout.WEST);
        bigPanel.add(priority, BorderLayout.SOUTH);

        repaint();
        revalidate();
        return bigPanel;
    }
}

public class TesterFrame {
    TesterF testerF;
    TesterFrame(String projectName, String userName){
        testerF = new TesterF(projectName, userName);
    }
}

