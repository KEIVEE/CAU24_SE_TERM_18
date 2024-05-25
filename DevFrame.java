import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.time.LocalDateTime;

class DevF extends JFrame {
    String projectName;
    String userName;
    private IssueList issues;

    public DevF(String projectName, String userName){
        super("ISSUE HANDLING");
        this.projectName = projectName;
        this.userName = userName;
        this.issues = new IssueList(projectName);
        this.setSize(900, 600);
        this.setVisible(true);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createTab());
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();
        JPanel assignedIssuePane = new JPanel();

        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        assignedIssuePane.setLayout(gb);

        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.VERTICAL;

        for(int i = 0; i < issues.getSize(); i++){
            if(issues.getTheIssue(i).getAssignee()!=null && issues.getTheIssue(i).getAssignee().equals(userName)){
                JPanel issuePanel = issuePanel(i);
                assignedIssuePane.add(issuePanel, constraints);
            }
        }
        pane.addTab("내 이슈", assignedIssuePane);
        return pane;
    }
    JPanel issuePanel(int index){
        Issue theIssue = issues.browseAll().get(index);
        JPanel panel = new JPanel(new GridLayout(1, 5));
        panel.add(new JLabel(theIssue.getTitle()));
        panel.add(new JLabel(theIssue.getStatus().toString()));
        panel.add(new JLabel(theIssue.getPriority().toString()));
        panel.add(new JLabel(theIssue.getDate()));
        panel.add(new JLabel(theIssue.getReporter()));

        LineBorder b1 = new LineBorder(Color.BLACK, 2);
        panel.setBorder(b1);
        panel.setPreferredSize(new Dimension(800, 100));
        panel.setMaximumSize(new Dimension(800, 100));
        panel.setMinimumSize(new Dimension(800, 100));

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                String serverUserName = "admin";
                String serverPassword = "00000000";
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(url, serverUserName, serverPassword);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                JFrame newFrame = new JFrame("Issue Information");
                newFrame.setSize(900, 600);
                newFrame.setVisible(true);
                newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel totalPane = new JPanel(new BorderLayout());

                JPanel titlePane = new JPanel();
                JLabel title1 = new JLabel("title: " + theIssue.getTitle());
                titlePane.add(title1);

                JPanel descriptionPane = new JPanel();
                JLabel description1 = new JLabel("Description: \r\n" + theIssue.getDescription());
                descriptionPane.add(description1);

                JButton justClose = new JButton("cancel");
                justClose.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newFrame.dispose();
                    }
                });

                JButton commentButton = new JButton("leave comment");
                commentButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame leaveCommentFrame = new JFrame("leave comment");
                        leaveCommentFrame.setSize(900, 600);
                        leaveCommentFrame.setVisible(true);
                        leaveCommentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                        JPanel totalPane = new JPanel(new BorderLayout());
                        JTextField content = new JTextField(500);
                        totalPane.add(content, BorderLayout.CENTER);
                        JButton leaveCommentButton = new JButton("post");
                        JButton closeButton = new JButton("cancel");

                        closeButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                leaveCommentFrame.dispose();
                            }
                        });

                        leaveCommentButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                PreparedStatement insertStatement = null;
                                Statement getIssueIdStatement = null;
                                try {
                                    String insertQuery = "insert into comment values (?, ?, ?, ?, ?)";
                                    String getIssueIdQuery = "select id from issue where date = '" + theIssue.getDate() + "'";
                                    String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                                    String serverUserName = "admin";
                                    String serverPassword = "00000000";
                                    Connection connection = null;
                                    connection = DriverManager.getConnection(url, serverUserName, serverPassword);

                                    getIssueIdStatement = connection.createStatement();
                                    ResultSet rs = getIssueIdStatement.executeQuery(getIssueIdQuery);

                                    String issueId = null;

                                    while(rs.next()){
                                        issueId = rs.getString("id");
                                    }
                                    Comment newComment = new Comment(content.getText(), userName);

                                    insertStatement = connection.prepareStatement(insertQuery);
                                    insertStatement.setString(1, issueId + newComment.getShortDate());
                                    insertStatement.setString(2, issueId);
                                    insertStatement.setString(3, newComment.getContent());
                                    insertStatement.setString(4, newComment.getUserName());
                                    insertStatement.setString(5, newComment.getDate());
                                    insertStatement.executeUpdate();

                                    insertStatement.close();
                                    connection.close();

                                    leaveCommentFrame.dispose();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                        });
                        JPanel postPane = new JPanel();
                        postPane.add(leaveCommentButton);
                        postPane.add(closeButton);
                        totalPane.add(postPane, BorderLayout.SOUTH);
                        leaveCommentFrame.add(totalPane);
                        repaint();
                        revalidate();


                    }
                });

                totalPane.add(titlePane, BorderLayout.NORTH);
                totalPane.add(descriptionPane, BorderLayout.CENTER);
                JPanel fixedPane = new JPanel();
                fixedPane.add(commentButton);
                fixedPane.add(justClose);
                totalPane.add(fixedPane, BorderLayout.SOUTH);


                newFrame.add(totalPane);
                repaint();
                revalidate();
            }
        });

        return panel;
    }

}

public class DevFrame {
    DevF devF;
    DevFrame(String projectName, String userName) {
        devF = new DevF(projectName, userName);
    }//생성자는 나중에 name을 받아올 것임
}
