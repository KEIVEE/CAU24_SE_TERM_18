import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class AdminF extends JFrame { //admin frame 클래스
    public AdminF(){
        super("Admin : ISSUE HANDLING SYSTEM"); //어드민으로 접속했을 때 뜨는 창이다
        setSize(900,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane pane = createTab();
        add(pane, BorderLayout.CENTER);
        setVisible(true);
    }

    public JTabbedPane createTab(){ //admin tab
        JTabbedPane pane = new JTabbedPane();
        JPanel AC = new JPanel();
        JButton dev = new JButton("계정 추가"); //계정 추가하고 싶을 때 누르는 버튼
        JPanel Pr = new JPanel();
        AC.add(dev);
        JButton MakeProj = new JButton("프로젝트 추가"); //프로젝트 추가하고 싶을 때 누르는 버튼
        Pr.add(MakeProj);
        pane.addTab("계정 추가", AC);
        pane.addTab("프로젝트 추가",Pr);

        // 이슈 검색 패널 추가
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("이슈 검색");

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        pane.addTab("이슈 검색", searchPanel);

        // 프로젝트 검색 패널 추가
        JPanel searchProjectPanel = new JPanel(new BorderLayout());
        JTextField searchProjectField = new JTextField();
        JButton searchProjectButton = new JButton("프로젝트 검색");

        searchProjectPanel.add(searchProjectField, BorderLayout.CENTER);
        searchProjectPanel.add(searchProjectButton, BorderLayout.EAST);

        pane.addTab("프로젝트 검색", searchProjectPanel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchIssues(searchField.getText().trim());
            }
        });

        searchProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchProject(searchProjectField.getText().trim());
            }
        });

        dev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewAccount();
            } //계정 추가 버튼을 누르면
            //새 창을 띄울 것이다
        });
        MakeProj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new NewProject();
            } //프로젝트 추가 버튼을 누르면
            //새 창을 띄울 것이다.
        });
        searchPanel.add(searchButton, BorderLayout.EAST);

        return pane;
    }

    private void searchProject(String projectName) {
        String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
        String userName = "admin";
        String serverPassword = "00000000";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, userName, serverPassword); //연결 시도
            String query = "SELECT * FROM issue WHERE projectName = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, projectName);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No issues found for the project.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder results = new StringBuilder();
                do {
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String status = rs.getString("status");
                    String priority = rs.getString("priority");
                    String date = rs.getString("date");
                    String reporter = rs.getString("reporter");
                    String assignee = rs.getString("assignee");
                    String fixer = rs.getString("fixer");

                    results.append("Title: ").append(title).append("\n")
                            .append("Description: ").append(description).append("\n")
                            .append("Status: ").append(status).append("\n")
                            .append("Priority: ").append(priority).append("\n")
                            .append("Date: ").append(date).append("\n")
                            .append("Reporter: ").append(reporter).append("\n")
                            .append("Assignee: ").append(assignee).append("\n")
                            .append("Fixer: ").append(fixer).append("\n\n");
                } while (rs.next());

                JTextArea textArea = new JTextArea(results.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(this, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }

            pstmt.close();
            connection.close(); //연결 종료
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void searchIssues(String keyword) {
        String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
        String userName = "admin";
        String serverPassword = "00000000";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, userName, serverPassword); //연결 시도
            String query = "SELECT * FROM issue WHERE title LIKE ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No issues found.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
            } else {
                StringBuilder results = new StringBuilder();
                do {
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String status = rs.getString("status");
                    String priority = rs.getString("priority");
                    String date = rs.getString("date");
                    String reporter = rs.getString("reporter");
                    String assignee = rs.getString("assignee");
                    String fixer = rs.getString("fixer");

                    results.append("Title: ").append(title).append("\n")
                            .append("Description: ").append(description).append("\n")
                            .append("Status: ").append(status).append("\n")
                            .append("Priority: ").append(priority).append("\n")
                            .append("Date: ").append(date).append("\n")
                            .append("Reporter: ").append(reporter).append("\n")
                            .append("Assignee: ").append(assignee).append("\n")
                            .append("Fixer: ").append(fixer).append("\n\n");
                } while (rs.next());

                JTextArea textArea = new JTextArea(results.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                JOptionPane.showMessageDialog(this, scrollPane, "Search Results", JOptionPane.INFORMATION_MESSAGE);
            }

            pstmt.close();
            connection.close(); //연결 종료
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void displayIssueDetails(int issueId) {
        String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
        String userName = "admin";
        String serverPassword = "00000000";
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, userName, serverPassword); //연결 시도
            String query = "SELECT * FROM issue WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, issueId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String title = rs.getString("title");
                String description = rs.getString("description");
                String comments = rs.getString("comments");

                JTextArea detailsArea = new JTextArea();
                detailsArea.append("Title: " + title + "\n");
                detailsArea.append("Description: " + description + "\n");
                detailsArea.append("Comments: " + comments + "\n");

                JOptionPane.showMessageDialog(this, new JScrollPane(detailsArea), "Issue Details", JOptionPane.INFORMATION_MESSAGE);
            }

            pstmt.close();
            connection.close(); //연결 종료
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}

class NewProject extends JFrame { //프로젝트 추가하는 버튼을 누르면 뜨는 창
    public NewProject() {
        super("Make a new project");
        setVisible(true);
        setSize(300, 100);
        setLayout(new GridLayout(2, 1));

        Pr myPr = new Pr(); //프로젝트 추가할때 정보 적는 패널을 만든다
        JPanel make = new JPanel();
        JButton yes = new JButton("OK");
        JButton no = new JButton("CANCEL");
        make.add(yes);
        make.add(no);

        add(myPr);//창에 정보 적는 패널 추가
        add(make);//창에 확인 / 취소 버튼 추가
        pack();

        yes.addActionListener(new ActionListener() { //확인 버튼을 누르면
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = myPr.Pname.getText();

                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                String userName = "admin";
                String serverPassword = "00000000";

                Connection connection;
                Statement stmt = null;

                try {
                    connection = DriverManager.getConnection(url, userName, serverPassword); //연결 시도
                    String query = "insert into project " + "values('" + name + "')";// 프로젝트 추가 쿼리
                    //프로젝트 테이블에, 이런 이름을 가진 투플을 추가해라
                    stmt = connection.createStatement();
                    stmt.executeUpdate(query);

                    repaint();
                    revalidate();

                    stmt.close();
                    connection.close(); //연결 종료
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose(); //추가 완료하면 창을 닫는다
            }
        });
        no.addActionListener(new ActionListener() { //취소 버튼을 누르면
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); //그냥 창을 닫는다.
            }
        });
    }
}

class Pr extends JPanel { //프로젝트 추가할때 정보를 적을 패널
    JLabel name;
    JTextField Pname;

    public Pr() {
        JPanel PrN = new JPanel(new GridLayout(1, 2)); //프로젝트 이름 적는 필드

        name = new JLabel("name");
        Pname = new JTextField(30);//최대 30자까지.
        PrN.add(name);
        PrN.add(Pname);
        this.add(PrN);
        repaint();
        revalidate();
    }
}

class NewAccount extends JFrame { //계정 만들기 버튼을 누르면 뜨는 창
    public NewAccount() {
        super("Make a new account");
        setVisible(true);
        setSize(300, 100);
        setLayout(new GridLayout(2, 1));
        AC myAC = new AC(); //계정 정보 적는 패널
        JPanel OK = new JPanel(); //확인, 취소 버튼
        JButton yes = new JButton("OK");
        JButton no = new JButton("CANCEL");
        OK.add(yes);
        OK.add(no);

        add(myAC);
        add(OK);
        pack();

        yes.addActionListener(new ActionListener() { //정보를 적고 확인 버튼을 누르면
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = myAC.IDT.getText();
                String password = myAC.pwT.getText();
                String name = myAC.nameT.getText();
                String category = myAC.type[myAC.category.getSelectedIndex()];
                //적은 정보들을 가져오고,
                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                String userName = "admin";
                String serverPassword = "00000000";

                Connection connection;
                try {
                    connection = DriverManager.getConnection(url, userName, serverPassword); //연결

                    PreparedStatement pstmt;
                    String query = "insert into account values (?, ?, ?, ?)";//계정에 투플을 추가하는 쿼리

                    pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, name);
                    pstmt.setString(2, id);
                    pstmt.setString(3, password);
                    pstmt.setString(4, category);
                    pstmt.executeUpdate();

                    pstmt.close();
                    connection.close();
                } catch (SQLException ex) {
                    if(ex.getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) {
                        //만약에 익셉션 중에서, id가 겹쳐서 생기는 익셉션이다: id는 프라이머리 키라 서로 달라야 한다.
                        new NewAccount();//계정 만들기 창을 새로 뜨게 한다.
                    }
                }

                repaint();
                revalidate();
                dispose(); //계정 생성 시 창을 닫음
            }
        });

        no.addActionListener(new ActionListener() { //취소 버튼을 누르면
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();//그냥 창을 닫는다
            }
        });
    }
}

class AC extends JPanel {  //새 계정의 아이디, 비밀번호, 종류를 적을 패널이다
    JLabel ID;
    JLabel pw;
    JLabel name;
    JTextField IDT;
    JTextField pwT;
    JTextField nameT; //아이디, 비밀번호, 이름을 적을 칸과
    String[] type = {"tester", "dev", "PL"};//계정 종류를 선택하는 칸이 있다
    JComboBox<String> category = new JComboBox<>(type);

    public AC() { //AC 객체를 생성하는 부분
        JPanel setct = new JPanel();
        JPanel enter = new JPanel(new GridLayout(3, 2));
        ID = new JLabel("ID");
        pw = new JLabel("password");
        name = new JLabel("name");
        IDT  = new JTextField(30);
        pwT = new JTextField(30);
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
