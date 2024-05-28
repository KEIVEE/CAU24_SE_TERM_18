import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;


public class PLFrame {
    PLF plF;
    PLFrame(String projectName, String userName){
        plF = new PLF(projectName,userName);
    }


    class PLF extends JFrame { //피엘이 프로젝트를 고르고 나면 뜨는 창이다
        private IssueList issues;
        public PLF(String projectName, String userName){
            super("ISSUE HANDLING");
            this.issues = new IssueList(projectName);
            this.setSize(900, 600);
            this.setVisible(true);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            add(createTab(projectName, userName));
        }

        public JTabbedPane createTab(String projectName, String userName){
            JTabbedPane pane = new JTabbedPane();


            JPanel issuesPanel = new JPanel(); //전체 이슈를 담을 패널
            JPanel newIssuesPanel = new JPanel(); //새 이슈를 담을 패널
            JPanel resolvedIssuesPanel = new JPanel(); //풀린 이슈를 담을 패널



            issuesPanel.setLayout(new BoxLayout(issuesPanel, BoxLayout.Y_AXIS));
            newIssuesPanel.setLayout(new BoxLayout(newIssuesPanel, BoxLayout.Y_AXIS));
            resolvedIssuesPanel.setLayout(new BoxLayout(resolvedIssuesPanel, BoxLayout.Y_AXIS));

            ArrayList<Issue> allIssues = new ArrayList<>();
            ArrayList<Issue> newIssues = new ArrayList<>();
            for(int i = 0; i < issues.getSize(); i++){
                if(issues.getTheIssue(i).getReporter().equals(userName)){
                    allIssues.add(issues.getTheIssue(i));
                    if(issues.getTheIssue(i).getStatus().equals(Status.FIXED)){
                        newIssues.add(issues.getTheIssue(i));//그 이슈의 status 가 픽스드라면 픽스드에도 추가한다.
                    }
                }
            }
            FirstPanel myIssuesFirstPanel = new FirstPanel(allIssues);
            FirstPanel myFixedIssuesFirstPanel = new FirstPanel(newIssues);
            issuesPanel.add(myIssuesFirstPanel);
            newIssuesPanel.add(myFixedIssuesFirstPanel);


            for(int i = 0; i < issues.getSize(); i++){
                //이슈들을 돌아보면서,
                JPanel issuePanel = issuePanel(i,projectName, userName);
                issuesPanel.add(issuePanel);
                // 모든 이슈를 넣을 패널에는 그냥 다 넣고
                if(issues.getTheIssue(i).getStatus().equals(Status.NEW)){
                    //새 이슈들을 넣을 패널에는 status 가 NEW 인 것을 넣고
                    newIssuesPanel.add(issuePanel(i, projectName, userName));
                }
                else if(issues.getTheIssue(i).getStatus().equals(Status.RESOLVED)){
                    //풀린 이슈들을 넣을 패널에는 status 가 RESOLVED 인 것을 넣을 것이다
                    resolvedIssuesPanel.add(issuePanel(i,projectName, userName));
                }
            }
            JButton refresh = new JButton("refresh");
            JButton refresh1 = new JButton("refresh");
            refresh.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PLF t = new PLF(projectName, userName);
                    dispose();

                }
            });
            refresh1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    PLF t = new PLF(projectName, userName);
                    dispose();

                }
            });

            JScrollPane totalPane1 = new JScrollPane(issuesPanel);//모든 이슈들을 모아놓은 것에 스크롤바를 적용시킨 패널.
            //다른 탭에 있는 패널과 데브, 테스터 창의 패널에도 적용시켜야 한다.
            totalPane1.setVerticalScrollBar(new JScrollBar());

            JScrollPane totalPane2 = new JScrollPane(newIssuesPanel);//모든 이슈들을 모아놓은 것에 스크롤바를 적용시킨 패널.
            //다른 탭에 있는 패널과 데브, 테스터 창의 패널에도 적용시켜야 한다.
            totalPane2.setVerticalScrollBar(new JScrollBar());

            JScrollPane totalPane3 = new JScrollPane(resolvedIssuesPanel);//모든 이슈들을 모아놓은 것에 스크롤바를 적용시킨 패널.
            //다른 탭에 있는 패널과 데브, 테스터 창의 패널에도 적용시켜야 한다.
            totalPane3.setVerticalScrollBar(new JScrollBar());


            pane.addTab("전체 이슈", totalPane1);
            pane.addTab("새 이슈", totalPane2);
            pane.addTab("풀린 이슈",totalPane3);
            pane.addTab("트렌드", new TrendPanel(projectName, issues.browseAll()));

            return pane;
        }

        JPanel issuePanel(int index,String projectName, String userName){ //이슈 하나에 대한 패널
            Issue theIssue = issues.browseAll().get(index);
            JPanel panel = new JPanel(new GridLayout(1, 5));
            panel.add(new JLabel(theIssue.getTitle()));
            panel.add(new JLabel(theIssue.getStatus().toString()));
            panel.add(new JLabel(theIssue.getPriority().toString()));
            panel.add(new JLabel(theIssue.getDate()));
            panel.add(new JLabel(theIssue.getReporter()));

            LineBorder b1 = new LineBorder(Color.BLACK, 2); //이슈끼리 구별하기 위해 필요한 보더
            panel.setBorder(b1);
            panel.setPreferredSize(new Dimension(800, 100));
            panel.setMaximumSize(new Dimension(800, 100));
            panel.setMinimumSize(new Dimension(800, 100));

            panel.addMouseListener(new MouseAdapter() {//이 패널을 클릭하면
                @Override
                public void mouseClicked(MouseEvent e) {
                    JFrame newFrame = new JFrame("Issue Information");//새 창이 뜨고
                    newFrame.setSize(900, 600);
                    newFrame.setVisible(true);
                    newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    JPanel totalPane = new JPanel(new BorderLayout());

                    JPanel titlePane = new JPanel();
                    JLabel title1 = new JLabel("title: " + theIssue.getTitle()); //제목을 출력
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

                    JButton justClose = new JButton("cancel");//취소를 누르면
                    justClose.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            newFrame.dispose();//꺼지도록.
                        }
                    });


                    JButton seeComments = new JButton("see comments"); //커멘트들을 볼 때 쓸 버튼이다
                    seeComments.addActionListener(new ActionListener() {//누르면
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JFrame commentFrame = new JFrame("comments");//새 창이 뜨고
                            commentFrame.setSize(900, 600);
                            commentFrame.setVisible(true);
                            commentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            JPanel totalPane = new JPanel(new BorderLayout());

                            //코멘트 메시지 추가 관련 코드
                            JPanel addClose = new JPanel(new GridLayout(1,2));
                            JButton addcomment = new JButton("add comment");
                            JButton close = new JButton("close");
                            addClose.add(addcomment);
                            addClose.add(close);

                            addcomment.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    JFrame msgComment = new JFrame("message");
                                    msgComment.setSize(900, 600);
                                    msgComment.setVisible(true);

                                    JPanel msgBigPanel = new JPanel(new BorderLayout());

                                    JPanel msg = new JPanel(new BorderLayout());
                                    JLabel msg1 = new JLabel("message(up to 500 characters)");
                                    JTextField msg2 = new JTextField(500);
                                    msg.add(msg1,BorderLayout.NORTH);
                                    msg.add(msg2,BorderLayout.CENTER);

                                    JPanel okcan = new JPanel(new GridLayout(1,2));
                                    JButton ok = new JButton("LEAVE MESSAGE");
                                    JButton cancel = new JButton("CANCEL");

                                    ok.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {

                                            String issueComment = msg2.getText();

                                            String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                                            String serverUserName = "admin";
                                            String serverPassword = "00000000";

                                            String commentquery =  "insert into comment values (?, ?, ?, ?, ?)";

                                            Connection connection;
                                            try{
                                                connection = DriverManager.getConnection(url, serverUserName, serverPassword);//디비에 연결


                                                PreparedStatement pstmtcomment;


                                                pstmtcomment = connection.prepareStatement(commentquery);
                                                //id,issue_id, content, userName,createdDate


                                                Comment newComment = new Comment(issueComment, userName);


                                                pstmtcomment.setString(1,projectName + theIssue.getShortDate() + newComment.getShortDate());
                                                pstmtcomment.setString(2,projectName + theIssue.getShortDate());
                                                pstmtcomment.setString(3, newComment.getContent());
                                                pstmtcomment.setString(4,userName);
                                                pstmtcomment.setString(5, newComment.getDate());


                                                pstmtcomment.executeUpdate();

                                                pstmtcomment.close();
                                                connection.close();

                                            } catch (SQLException ex) {
                                                throw new RuntimeException(ex);
                                            }

                                            repaint();
                                            revalidate();

                                            msgComment.dispose();//등록했으면 창 닫기
                                        }
                                    });
                                    cancel.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            msgComment.dispose();
                                        }
                                    });
                                    okcan.add(ok);
                                    okcan.add(cancel);
                                    msgBigPanel.add(msg,BorderLayout.CENTER);
                                    msgBigPanel.add(okcan,BorderLayout.SOUTH);
                                    msgComment.add(msgBigPanel);

                                    msgComment.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                }
                            });

                            close.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    commentFrame.dispose();//거기서 취소 버튼이 누르면 창 종료
                                }
                            });

                            JPanel commentsPane = new JPanel();

                            commentsPane.setLayout(new BoxLayout(commentsPane, BoxLayout.Y_AXIS));

                            for(int i = 0; i < theIssue.getComments().size(); i++){
                                commentsPane.add(new CommentPane(theIssue, i).getTotalPane());
                                //커멘트 개수만큼 커멘트 페인을 추가한다.
                            }
                            JScrollPane commentsScroll = new JScrollPane(commentsPane);//모든 이슈들을 모아놓은 것에 스크롤바를 적용시킨 패널.
                            //다른 탭에 있는 패널과 데브, 테스터 창의 패널에도 적용시켜야 한다.
                            commentsScroll.setVerticalScrollBar(new JScrollBar());


                            totalPane.add(commentsScroll, BorderLayout.CENTER);
                            totalPane.add(addClose, BorderLayout.SOUTH);
                            commentFrame.add(totalPane);
                            repaint();
                            revalidate();

                        }
                    });

                    if (theIssue.getStatus().equals(Status.NEW)) {//지금 이슈 상태가 뉴라면, 피엘은 데브를 어사인해야 한다.
                        JPanel assignPane = new JPanel();
                        JComboBox<String> devs = new JComboBox<>();//데브를 선택할 콤보박스
                        JButton assignButton = new JButton("Assign"); //데브 어사인하기.

                        String updateQuery = "update issue set assignee = ?, status = 'ASSIGNED' where date = ?";
                        //어사이니를 누구로 하고, 스테이터스를 어사인드로 바꾸라는 쿼리


                        assignButton.addActionListener(new ActionListener() {//어사인 버튼을 누르면
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                PreparedStatement updateStatement;
                                try {

                                    String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                                    String serverUserName = "admin";
                                    String serverPassword = "00000000";
                                    Connection connection;
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
                        //데브 선택하는 콤보박스에서 내용을 채우기 위한 쿼리.


                        Statement devStatement;

                        try {
                            String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                            String serverUserName = "admin";
                            String serverPassword = "00000000";
                            Connection connection;
                            connection = DriverManager.getConnection(url, serverUserName, serverPassword);//연결

                            devStatement = connection.createStatement();
                            ResultSet rs = devStatement.executeQuery(devQuery);
                            while (rs.next()) {
                                devs.addItem(rs.getString("name"));//데브의 개수만큼 그 이름을 콤보박스에 추가
                            }
                            repaint();
                            revalidate();
                            devStatement.close();
                            connection.close();

                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        assignPane.add(seeComments);
                        assignPane.add(devs);
                        assignPane.add(assignButton);
                        assignPane.add(justClose);

                        totalPane.add(assignPane, BorderLayout.SOUTH);

                    }
                    else if(theIssue.getStatus().equals(Status.RESOLVED)){//그 이슈 상태가 리솔브드라면,
                        JPanel closedPane = new JPanel();
                        String closeQuery = "update issue set status = 'CLOSED' where date = ?";
                        //해당 이슈의 상태를 클로스드로 바꾸는 쿼리.

                        JButton makeClose = new JButton("close issue");
                        makeClose.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                PreparedStatement updateStatement;
                                try {
                                    String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                                    String serverUserName = "admin";
                                    String serverPassword = "00000000";
                                    Connection connection;
                                    connection = DriverManager.getConnection(url, serverUserName, serverPassword);

                                    updateStatement = connection.prepareStatement(closeQuery);
                                    updateStatement.setString(1, theIssue.getDate());
                                    updateStatement.executeUpdate();
                                    //쿼리 실행

                                    updateStatement.close();
                                    connection.close();
                                } catch (SQLException ex) {
                                    throw new RuntimeException(ex);
                                }


                                newFrame.dispose();//실행하면 이슈 상세정보창을 닫는다.
                            }

                        });

                        closedPane.add(seeComments);
                        closedPane.add(makeClose);
                        closedPane.add(justClose);
                        totalPane.add(closedPane, BorderLayout.SOUTH);
                    }
                    else{
                        JPanel southPane = new JPanel();
                        southPane.add(seeComments);
                        southPane.add(justClose);
                        totalPane.add(southPane, BorderLayout.SOUTH);
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
}