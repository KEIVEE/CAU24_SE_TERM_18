import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class AdminF extends JFrame {
    public AdminF() {
        super("Admin : ISSUE HANDLING SYSTEM");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane pane = createTab();
        add(pane, BorderLayout.CENTER);
        setVisible(true);
    }

    public JTabbedPane createTab() {
        JTabbedPane pane = new JTabbedPane();
        JPanel AC = new JPanel();
        JButton dev = new JButton("계정 추가");
        JPanel Pr = new JPanel();
        AC.add(dev);
        JButton MakeProj = new JButton("프로젝트 추가");
        Pr.add(MakeProj);
        pane.addTab("계정 추가", AC);
        pane.addTab("프로젝트 추가", Pr);

        // 이슈 검색 패널 추가
        JPanel searchPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("이슈 검색");

        inputPanel.add(searchField, BorderLayout.CENTER);
        inputPanel.add(searchButton, BorderLayout.EAST);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS)); // 검색 결과를 세로로 나열

        searchPanel.add(inputPanel, BorderLayout.NORTH);
        searchPanel.add(resultPanel, BorderLayout.CENTER);

        pane.addTab("이슈 검색", searchPanel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText().trim();
                if (!keyword.isEmpty()) {
                    List<String> searchResults = searchIssues(keyword);
                    resultPanel.removeAll();
                    if (searchResults.isEmpty()) {
                        resultPanel.add(new JLabel("검색 결과가 없습니다."));
                    } else {
                        for (String result : searchResults) {
                            JLabel issueLink = createHyperlink(result);
                            issueLink.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    showIssueDetails(result);
                                }
                            });
                            resultPanel.add(issueLink);
                        }
                    }
                    resultPanel.revalidate();
                    resultPanel.repaint();
                }
            }
        });

        // 프로젝트 검색 패널 추가
        JPanel projectSearchPanel = new JPanel(new BorderLayout());
        JPanel projectInputPanel = new JPanel(new BorderLayout());
        JTextField projectSearchField = new JTextField(20);
        JButton projectSearchButton = new JButton("프로젝트 검색");

        projectInputPanel.add(projectSearchField, BorderLayout.CENTER);
        projectInputPanel.add(projectSearchButton, BorderLayout.EAST);

        JPanel projectResultPanel = new JPanel();
        projectResultPanel.setLayout(new BoxLayout(projectResultPanel, BoxLayout.Y_AXIS)); // 검색 결과를 세로로 나열

        projectSearchPanel.add(projectInputPanel, BorderLayout.NORTH);
        projectSearchPanel.add(projectResultPanel, BorderLayout.CENTER);

        pane.addTab("프로젝트 검색", projectSearchPanel);

        projectSearchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = projectSearchField.getText().trim();
                if (!keyword.isEmpty()) {
                    List<String> searchResults = searchProjects(keyword);
                    projectResultPanel.removeAll();
                    if (searchResults.isEmpty()) {
                        projectResultPanel.add(new JLabel("검색 결과가 없습니다."));
                    } else {
                        for (String result : searchResults) {
                            JLabel projectLink = createHyperlink(result);
                            projectLink.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    showProjectIssues(result);
                                }
                            });
                            projectResultPanel.add(projectLink);
                        }
                    }
                    projectResultPanel.revalidate();
                    projectResultPanel.repaint();
                }
            }
        });

        dev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewAccount();
            }
        });
        MakeProj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewProject();
            }
        });

        return pane;
    }

    private List<String> searchIssues(String keyword) {
        List<String> results = new ArrayList<>();
        String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
        String userName = "admin";
        String serverPassword = "00000000";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, userName, serverPassword);
            String query = "SELECT title FROM issue WHERE title LIKE ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(rs.getString("title"));
            }

            pstmt.close();
            connection.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return results;
    }

    private List<String> searchProjects(String keyword) {
        List<String> results = new ArrayList<>();
        String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
        String userName = "admin";
        String serverPassword = "00000000";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, userName, serverPassword);
            String query = "SELECT name FROM project WHERE name LIKE ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                results.add(rs.getString("name"));
            }

            pstmt.close();
            connection.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return results;
    }

    private void showIssueDetails(String issueTitle) {
        String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
        String userName = "admin";
        String serverPassword = "00000000";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, userName, serverPassword);

            // 이슈 정보 가져오기
            String query = "SELECT * FROM issue WHERE title = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, issueTitle);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder issueDetails = new StringBuilder();
            String issueId = null;

            if (rs.next()) {
                issueId = rs.getString("id"); // 이슈 ID 가져오기
                String title = rs.getString("title");
                String description = rs.getString("description");
                String status = rs.getString("status");
                String priority = rs.getString("priority");
                String date = rs.getString("date");
                String reporter = rs.getString("reporter");
                String assignee = rs.getString("assignee");
                String fixer = rs.getString("fixer");

                issueDetails.append("Title: ").append(title).append("\n")
                        .append("Description: ").append(description).append("\n")
                        .append("Status: ").append(status).append("\n")
                        .append("Priority: ").append(priority).append("\n")
                        .append("Date: ").append(date).append("\n")
                        .append("Reporter: ").append(reporter).append("\n")
                        .append("Assignee: ").append(assignee).append("\n")
                        .append("Fixer: ").append(fixer).append("\n\n");
            }

            // 코멘트 가져오기
            // 코멘트 가져오기
            String commentQuery = "SELECT * FROM comment WHERE issue_id = ?";
            PreparedStatement commentPstmt = connection.prepareStatement(commentQuery);
            commentPstmt.setString(1, issueId);
            ResultSet commentRs = commentPstmt.executeQuery();

            issueDetails.append("Comments:\n");
            while (commentRs.next()) {
                String commentContent = commentRs.getString("content");
                String commentUser = commentRs.getString("userName");
                String commentDate = commentRs.getString("createdDate");

                issueDetails.append(commentContent).append("\n").append("Comment by ").append(commentUser).append(" on ").append(commentDate).append("\n")
                        .append("\n");
            }

            commentPstmt.close();

            JTextArea textArea = new JTextArea(issueDetails.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "Issue Details", JOptionPane.INFORMATION_MESSAGE);

            pstmt.close();
            connection.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void showProjectIssues(String projectName) {
        String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
        String userName = "admin";
        String serverPassword = "00000000";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, userName, serverPassword);
            String query = "SELECT title FROM issue WHERE projectName = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, projectName);
            ResultSet rs = pstmt.executeQuery();

            JPanel issuePanel = new JPanel();
            issuePanel.setLayout(new BoxLayout(issuePanel, BoxLayout.Y_AXIS));

            while (rs.next()) {
                String issueTitle = rs.getString("title");
                JLabel issueLink = createHyperlink(issueTitle);
                issueLink.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        showIssueDetails(issueTitle);
                    }
                });
                issuePanel.add(issueLink);
            }

            JScrollPane scrollPane = new JScrollPane(issuePanel);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            JOptionPane.showMessageDialog(this, scrollPane, "Project Issues", JOptionPane.INFORMATION_MESSAGE);

            pstmt.close();
            connection.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private JLabel createHyperlink(String text) {
        JLabel label = new JLabel("<html><a href=''>" + text + "</a></html>");
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return label;
    }

    public static void main(String[] args) {
        new AdminFrame();
    }
}

class NewProject extends JFrame {
    public NewProject() {
        super("Make a new project");
        setVisible(true);
        setSize(300, 100);
        setLayout(new GridLayout(2, 1));

        Pr myPr = new Pr();
        JPanel make = new JPanel();
        JButton yes = new JButton("OK");
        JButton no = new JButton("CANCEL");
        make.add(yes);
        make.add(no);

        add(myPr);
        add(make);
        pack();

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = myPr.Pname.getText();

                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                String userName = "admin";
                String serverPassword = "00000000";

                Connection connection;
                Statement stmt = null;

                try {
                    connection = DriverManager.getConnection(url, userName, serverPassword);
                    String query = "insert into project " + "values('" + name + "')";
                    stmt = connection.createStatement();
                    stmt.executeUpdate(query);

                    repaint();
                    revalidate();

                    stmt.close();
                    connection.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}

class Pr extends JPanel {
    JLabel name;
    JTextField Pname;

    public Pr() {
        JPanel PrN = new JPanel(new GridLayout(1, 2));

        name = new JLabel("name");
        Pname = new JTextField(30);
        PrN.add(name);
        PrN.add(Pname);
        this.add(PrN);
        repaint();
        revalidate();
    }
}

class NewAccount extends JFrame {
    public NewAccount() {
        super("Make a new account");
        setVisible(true);
        setSize(300, 100);
        setLayout(new GridLayout(2, 1));
        AC myAC = new AC();
        JPanel OK = new JPanel();
        JButton yes = new JButton("OK");
        JButton no = new JButton("CANCEL");
        OK.add(yes);
        OK.add(no);

        add(myAC);
        add(OK);
        pack();

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = myAC.IDT.getText();
                String password = new String(myAC.pwT.getPassword());
                String name = myAC.nameT.getText();
                String category = myAC.type[myAC.category.getSelectedIndex()];

                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                String userName = "admin";
                String serverPassword = "00000000";

                Connection connection;
                try {
                    connection = DriverManager.getConnection(url, userName, serverPassword);

                    PreparedStatement pstmt;
                    String query = "insert into account values (?, ?, ?, ?)";

                    pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, name);
                    pstmt.setString(2, id);
                    pstmt.setString(3, password);
                    pstmt.setString(4, category);
                    pstmt.executeUpdate();

                    pstmt.close();
                    connection.close();
                } catch (SQLException ex) {
                    if (ex.getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                        new NewAccount();
                    }
                }

                repaint();
                revalidate();
                dispose();
            }
        });

        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }
}

class AC extends JPanel {
    JLabel ID;
    JLabel pw;
    JLabel name;
    JTextField IDT;
    JPasswordField pwT;
    JTextField nameT;
    String[] type = {"tester", "dev", "PL"};
    JComboBox<String> category = new JComboBox<>(type);

    public AC() {
        JPanel setct = new JPanel();
        JPanel enter = new JPanel(new GridLayout(3, 2));
        ID = new JLabel("ID");
        pw = new JLabel("password");
        name = new JLabel("name");
        IDT = new JTextField(30);
        pwT = new JPasswordField(30);
        nameT = new JTextField(30);

        enter.add(ID);
        enter.add(IDT);
        enter.add(pw);
        enter.add(pwT);
        enter.add(name);
        enter.add(nameT);
        setct.add(category);
        this.add(setct);
        this.add(enter);

        repaint();
        revalidate();
    }
}

public class AdminFrame {
    AdminF t = new AdminF();
}
