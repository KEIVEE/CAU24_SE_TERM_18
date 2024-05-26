import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

class DevF extends JFrame { //데브가 프로젝트 선택까지 마치면 뜨는 창이다
    String projectName; //지금 접속한 프로젝트 이름
    String userName; //지금 접속한 데브의 계정 이름
    private IssueList issues; //지금 접속한 프로젝트의 이슈들

    public DevF(String projectName, String userName){
        super("ISSUE HANDLING");
        this.projectName = projectName;
        this.userName = userName;
        this.issues = new IssueList(projectName);//프로젝트 이름에 따라서 지금 있는 이슈들을 저장함
        this.setSize(900, 600);
        this.setVisible(true);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createTab());
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();
        JPanel assignedIssuePane = new JPanel();

        GridBagLayout gb = new GridBagLayout(); //레이아웃: 그리드백 레이아웃 사용
        GridBagConstraints constraints = new GridBagConstraints();

        assignedIssuePane.setLayout(gb);

        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.VERTICAL;
        //패널들을 아래 방향으로 추가되게 한다.


        for(int i = 0; i < issues.getSize(); i++){ // 이 프로젝트의 이슈 중에서
            if(issues.getTheIssue(i).getAssignee()!=null && issues.getTheIssue(i).getAssignee().equals(userName) && issues.getTheIssue(i).getStatus().equals(Status.ASSIGNED)){
                //어사이니가 널이 아니고 본인이면: 앞의 조건이 없으면 오류가 난다. 이슈 생성 시에는 어사이니가 없어서 null 이기 때문임

                JPanel issuePanel = issuePanel(i); //그 이슈에 대한 패널을 만들고
                assignedIssuePane.add(issuePanel, constraints); //화면에 추가함.
            }
        }
        JButton refresh = new JButton("refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DevF t = new DevF(projectName, userName);
                dispose();

            }
        });
        assignedIssuePane.add(refresh, constraints);
        JScrollPane totalPane1 = new JScrollPane(assignedIssuePane);//모든 이슈들을 모아놓은 것에 스크롤바를 적용시킨 패널.
        //다른 탭에 있는 패널과 데브, 테스터 창의 패널에도 적용시켜야 한다.
        totalPane1.setVerticalScrollBar(new JScrollBar());
        pane.addTab("내 이슈", totalPane1);
        return pane;
    }
    JPanel issuePanel(int index){ //이슈 하나에 대한 간단한 정보를 가지고 있는 패널이다.
        Issue theIssue = issues.browseAll().get(index); //몇 번째 이슈인지 받아옴
        JPanel panel = new JPanel(new GridLayout(1, 5));
        panel.add(new JLabel(theIssue.getTitle()));
        panel.add(new JLabel(theIssue.getStatus().toString()));
        panel.add(new JLabel(theIssue.getPriority().toString()));
        panel.add(new JLabel(theIssue.getDate()));
        panel.add(new JLabel(theIssue.getReporter()));

        LineBorder b1 = new LineBorder(Color.BLACK, 2);//이슈끼리 구분하기 위해 보더가 필요하다
        panel.setBorder(b1);
        panel.setPreferredSize(new Dimension(800, 100));
        panel.setMaximumSize(new Dimension(800, 100));
        panel.setMinimumSize(new Dimension(800, 100));

        panel.addMouseListener(new MouseAdapter() { //이 이슈 패널을 클릭하면, 상세 정보를 띄울 것이다.
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame newFrame = new JFrame("Issue Information");
                newFrame.setSize(900, 600);
                newFrame.setVisible(true);
                newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel totalPane = new JPanel(new BorderLayout());

                JPanel titlePane = new JPanel();
                JLabel title1 = new JLabel("title: " + theIssue.getTitle()); //제목 부분
                titlePane.add(title1);

                JPanel descriptionPane = new JPanel(new BorderLayout());
                LineBorder border = new LineBorder(Color.GRAY, 1);
                JLabel description1 = new JLabel("Description:");//설명 부분
                JLabel description2 = new JLabel( theIssue.getDescription());
                description1.setBorder(border);
                description2.setBorder(border);

                //뉴라인으로 넘어가지 않는 문제가 있다. 검색 후 해결해야 함
                descriptionPane.add(description1, BorderLayout.NORTH);
                descriptionPane.add(description2, BorderLayout.CENTER);

                JButton fixed = new JButton("fixed");
                JButton justClose = new JButton("cancel"); //아무것도 하지 않고 창 닫는 버튼.
                String fixerquery = "update issue set fixer = ?, status = 'FIXED' where id = ?";
                fixed.addActionListener(new ActionListener() { //고치면 fixer에 dev가 들어가야되고, 상태를 fixed로 바꿈
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        PreparedStatement updateStatement;
                        try {

                            String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                            String serverUserName = "admin";
                            String serverPassword = "00000000";
                            Connection connection;
                            connection = DriverManager.getConnection(url, serverUserName, serverPassword);

                            updateStatement = connection.prepareStatement(fixerquery);
                            updateStatement.setString(1, userName);
                            updateStatement.setString(2, projectName + theIssue.getShortDate());
                            updateStatement.executeUpdate();

                            updateStatement.close();
                            connection.close();


                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                        newFrame.dispose();
                    }
                });

                justClose.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newFrame.dispose();
                    }
                });

                JButton seeComments = new JButton("see comments"); //이슈에 달린 커멘트를 보게 할 버튼.
                seeComments.addActionListener(new ActionListener() {//이 버튼을 누르면,
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame commentFrame = new JFrame("comments");//또 새 창을 띄운다.
                        commentFrame.setSize(900, 600);
                        commentFrame.setVisible(true);
                        commentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        JPanel totalPane = new JPanel(new BorderLayout());

                        JButton close = new JButton("close"); //새 창에서 close 버튼을 누르면
                        close.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                commentFrame.dispose();//그 창만 꺼진다.
                            }
                        });

                        JPanel commentsPane = new JPanel(); //커멘트들을 보이게 할 패널
                        GridBagLayout gb = new GridBagLayout();
                        GridBagConstraints constraints = new GridBagConstraints();

                        commentsPane.setLayout(gb);

                        constraints.gridx = 0;
                        constraints.gridy = GridBagConstraints.RELATIVE;
                        constraints.fill = GridBagConstraints.VERTICAL;
                        //이슈들 보이게 하는 패널과 똑같이 그리드백 레이아웃을 적용한다

                        for(int i = 0; i < theIssue.getComments().size(); i++){
                            commentsPane.add(commentPane(theIssue, i), constraints);
                        }//이슈에 달린 커멘트의 개수만큼 추가하기.
                        totalPane.add(commentsPane, BorderLayout.CENTER);
                        totalPane.add(close, BorderLayout.SOUTH);
                        commentFrame.add(totalPane);
                        repaint();
                        revalidate();

                    }
                });

                JButton commentButton = new JButton("leave comment"); //데브는 커멘트를 적을 수 있다.
                commentButton.addActionListener(new ActionListener() { //이 커멘트 적기 버튼을 누르면,
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame leaveCommentFrame = new JFrame("leave comment"); //새 창이 생김
                        leaveCommentFrame.setSize(900, 600);
                        leaveCommentFrame.setVisible(true);
                        leaveCommentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                        JPanel totalPane = new JPanel(new BorderLayout());
                        JTextField content = new JTextField(500);
                        totalPane.add(content, BorderLayout.CENTER);
                        JButton leaveCommentButton = new JButton("post");
                        JButton closeButton = new JButton("cancel");

                        closeButton.addActionListener(new ActionListener() {//캔슬 버튼을 누르면
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                leaveCommentFrame.dispose();//이 창은 그냥 닫히게 도고
                            }
                        });

                        leaveCommentButton.addActionListener(new ActionListener() { // 등록 버튼을 누르면
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                PreparedStatement insertStatement;
                                Statement getIssueIdStatement;
                                try {
                                    String insertQuery = "insert into comment values (?, ?, ?, ?, ?)";
                                    //새 커멘트를 작성할 쿼리.
                                    String getIssueIdQuery = "select id from issue where date = '" + theIssue.getDate() + "'";
                                    //새 커멘트를 추가할 쿼리에는 해당 이슈의 아이디가 필요하다.
                                    //시간은 초 단위로 같지 않는 한 겹치지 않으니 시간을 기준으로 검색.
                                    //뜻: 이 시간에 올라온 이슈의 아이디를 골라라
                                    String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                                    String serverUserName = "admin";
                                    String serverPassword = "00000000";
                                    Connection connection;
                                    connection = DriverManager.getConnection(url, serverUserName, serverPassword);//연결

                                    getIssueIdStatement = connection.createStatement();
                                    ResultSet rs = getIssueIdStatement.executeQuery(getIssueIdQuery); //이슈 아이디 검색을 했으면

                                    String issueId = null;

                                    while(rs.next()){ //
                                        issueId = rs.getString("id"); //그 아이디 이름을 저장
                                    }
                                    Comment newComment = new Comment(content.getText(), userName);//커멘트 객체를 생성하는데
                                    //적은 내용과 본인 이름으로 커멘트를 생성한다. 그러면

                                    insertStatement = connection.prepareStatement(insertQuery);
                                    insertStatement.setString(1, issueId + newComment.getShortDate());
                                    //커멘트의 아이디는 이슈 아이디에다가 지금 시간의 숏데이트를 추가한 것
                                    insertStatement.setString(2, issueId); //이슈 아이디에는 저장한 이슈 아이디
                                    insertStatement.setString(3, newComment.getContent()); //적은 내용
                                    insertStatement.setString(4, newComment.getUserName()); //본인 이름
                                    insertStatement.setString(5, newComment.getDate()); // 현재 시간
                                    insertStatement.executeUpdate();
                                    //이 저장되게 된다

                                    insertStatement.close();
                                    connection.close();

                                    leaveCommentFrame.dispose(); //커멘트 추가 후에는 창을 닫음.
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
                fixedPane.add(seeComments);
                fixedPane.add(fixed);
                fixedPane.add(justClose);
                totalPane.add(fixedPane, BorderLayout.SOUTH);


                newFrame.add(totalPane);
                repaint();
                revalidate();
            }
        });

        return panel;
    }

    JPanel commentPane(Issue theIssue, int index){ //커멘트 하나에 대한 정보를 보여주는 패널
        JPanel totalPane = new JPanel(new BorderLayout());
        LineBorder b1 = new LineBorder(Color.BLACK, 2);
        totalPane.setBorder(b1); //커멘트끼리 구분을 위해 보더가 필요하다
        totalPane.setPreferredSize(new Dimension(800, 100));
        totalPane.setMaximumSize(new Dimension(800, 100));
        totalPane.setMinimumSize(new Dimension(800, 100));

        LineBorder b2 = new LineBorder(Color.GRAY, 1);

        JLabel user = new JLabel(theIssue.getComments().get(index).getUserName());
        //커멘트를 올린 유저
        user.setBorder(b2);

        JLabel content = new JLabel(theIssue.getComments().get(index).getContent());
        //커멘트 내용
        content.setBorder(b2);

        JLabel date = new JLabel(theIssue.getComments().get(index).getDate());
        //커멘트를 올린 날짜를 뜨게 할 것이다
        date.setBorder(b2);

        totalPane.add(user, BorderLayout.WEST);
        totalPane.add(content, BorderLayout.CENTER);
        totalPane.add(date, BorderLayout.SOUTH);

        return totalPane;
    }

}

public class DevFrame {
    DevF devF;
    DevFrame(String projectName, String userName) {
        devF = new DevF(projectName, userName);
    }
}
