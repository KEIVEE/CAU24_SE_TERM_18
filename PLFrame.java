import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;

class PLF extends JFrame {
    private IssueList issues;
    public PLF(String projectName, String userName){
        super("ISSUE HANDLING");
        this.issues = new IssueList(projectName);
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createTab());
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();
        GridBagLayout gb = new GridBagLayout();

        JPanel issuesPanel = new JPanel();
        JPanel newIssuesPanel = new JPanel();
        JPanel resolvedIssuesPanel = new JPanel();

        GridBagConstraints constraints = new GridBagConstraints();

        issuesPanel.setLayout(gb);
        newIssuesPanel.setLayout(gb);
        resolvedIssuesPanel.setLayout(gb);

        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.VERTICAL;


        for(int i = 0; i < issues.getSize(); i++){
            JPanel issuePanel = issuePanel(i);
            issuesPanel.add(issuePanel, constraints);
            if(issues.getTheIssue(i).getStatus().equals(Status.NEW)){
                newIssuesPanel.add(issuePanel(i), constraints);
            }
            else if(issues.getTheIssue(i).getStatus().equals(Status.RESOLVED)){
                resolvedIssuesPanel.add(issuePanel(i), constraints);
            }
        }


        JScrollPane totalPane = new JScrollPane(issuesPanel);
        totalPane.setVerticalScrollBar(new JScrollBar());


        pane.addTab("전체 이슈", totalPane);
        pane.addTab("새 이슈", newIssuesPanel);
        pane.addTab("풀린 이슈",resolvedIssuesPanel);

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

                if (theIssue.getStatus().equals(Status.NEW)) {
                    JPanel assignPane = new JPanel();
                    JComboBox<String> devs = new JComboBox<>();
                    JButton assignButton = new JButton("Assign"); //데브 어사인하기.

                    String updateQuery = "update issue set assignee = ?, status = 'ASSIGNED' where date = ?";


                    assignButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            PreparedStatement updateStatement = null;
                            try {

                                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                                String serverUserName = "admin";
                                String serverPassword = "00000000";
                                Connection connection = null;
                                connection = DriverManager.getConnection(url, serverUserName, serverPassword);

                                updateStatement = connection.prepareStatement(updateQuery);
                                updateStatement.setString(1, devs.getItemAt(devs.getSelectedIndex()));
                                updateStatement.setString(2, theIssue.getDate());
                                updateStatement.executeUpdate();

                                updateStatement.close();
                                connection.close();


                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }

                            newFrame.dispose();
                        }
                    });


                    String devQuery = "select name from account where category = 'dev'";


                    Statement devStatement = null;

                    try {
                        devStatement = connection.createStatement();
                        ResultSet rs = devStatement.executeQuery(devQuery);
                        while (rs.next()) {
                            devs.addItem(rs.getString("name"));
                        }


                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    repaint();
                    revalidate();
                    try {
                        devStatement.close();
                        connection.close();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    assignPane.add(devs);
                    assignPane.add(assignButton);
                    assignPane.add(justClose);



                    totalPane.add(assignPane, BorderLayout.SOUTH);



                }
                else if(theIssue.getStatus().equals(Status.RESOLVED)){
                    JPanel closedPane = new JPanel();
                    String closeQuery = "update issue set status = 'CLOSED' where date = ?";

                    JButton makeClose = new JButton("close issue");
                    makeClose.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            PreparedStatement updateStatement = null;
                            try {

                                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                                String serverUserName = "admin";
                                String serverPassword = "00000000";
                                Connection connection = null;
                                connection = DriverManager.getConnection(url, serverUserName, serverPassword);

                                updateStatement = connection.prepareStatement(closeQuery);
                                updateStatement.setString(1, theIssue.getDate());
                                updateStatement.executeUpdate();

                                updateStatement.close();
                                connection.close();


                            } catch (SQLException ex) {
                                throw new RuntimeException(ex);
                            }


                            newFrame.dispose();
                        }

                    });

                    closedPane.add(makeClose);
                    closedPane.add(justClose);
                    totalPane.add(closedPane, BorderLayout.SOUTH);
                }
                else{
                    totalPane.add(justClose, BorderLayout.SOUTH);
                }
                totalPane.add(titlePane, BorderLayout.NORTH);
                totalPane.add(descriptionPane, BorderLayout.CENTER);

                newFrame.add(totalPane);
                repaint();
                revalidate();
            }
        });

        return panel;
    }

}

public class PLFrame {
    PLF plF;
    PLFrame(String projectName, String userName){
        plF = new PLF(projectName,userName);
    }//생성자는 나중에 name을 받아올 것임
}