import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

class PLF extends JFrame { //피엘이 프로젝트를 고르고 나면 뜨는 창이다
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

        JPanel issuesPanel = new JPanel(); //전체 이슈를 담을 패널
        JPanel newIssuesPanel = new JPanel(); //새 이슈를 담을 패널
        JPanel resolvedIssuesPanel = new JPanel(); //풀린 이슈를 담을 패널

        GridBagConstraints constraints = new GridBagConstraints();

        issuesPanel.setLayout(gb);
        newIssuesPanel.setLayout(gb);
        resolvedIssuesPanel.setLayout(gb);

        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.VERTICAL;
        //이슈들을 나열할 때 아래로 추가되게 하려고 만든 그리드백 레이아웃의 조건이다.


        for(int i = 0; i < issues.getSize(); i++){
            //이슈들을 돌아보면서,
            JPanel issuePanel = issuePanel(i);
            issuesPanel.add(issuePanel, constraints);
            // 모든 이슈를 넣을 패널에는 그냥 다 넣고
            if(issues.getTheIssue(i).getStatus().equals(Status.NEW)){
                //새 이슈들을 넣을 패널에는 status 가 NEW 인 것을 넣고
                newIssuesPanel.add(issuePanel(i), constraints);
            }
            else if(issues.getTheIssue(i).getStatus().equals(Status.RESOLVED)){
                //풀린 이슈들을 넣을 패널에는 status 가 RESOLVED 인 것을 넣을 것이다
                resolvedIssuesPanel.add(issuePanel(i), constraints);
            }
        }


        JScrollPane totalPane = new JScrollPane(issuesPanel);//모든 이슈들을 모아놓은 것에 스크롤바를 적용시킨 패널.
        //다른 탭에 있는 패널과 데브, 테스터 창의 패널에도 적용시켜야 한다.
        totalPane.setVerticalScrollBar(new JScrollBar());


        pane.addTab("전체 이슈", totalPane);
        pane.addTab("새 이슈", newIssuesPanel);
        pane.addTab("풀린 이슈",resolvedIssuesPanel);

        return pane;
    }

    JPanel issuePanel(int index){ //이슈 하나에 대한 패널
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
                    newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    JPanel totalPane = new JPanel(new BorderLayout());

                    JPanel titlePane = new JPanel();
                    JLabel title1 = new JLabel("title: " + theIssue.getTitle()); //제목을 출력
                    titlePane.add(title1);

                    JPanel descriptionPane = new JPanel();
                    JLabel description1 = new JLabel("Description: \r\n" + theIssue.getDescription());//내용을 출력
                    //뉴라인으로 안 넘어가는 이슈가 있다. 검색해서 해결할 것이다
                    descriptionPane.add(description1);

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
                        commentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        JPanel totalPane = new JPanel(new BorderLayout());

                        JButton close = new JButton("close");
                        close.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                commentFrame.dispose();//거기서 취소 버튼이 누르면 창 종료
                            }
                        });

                        JPanel commentsPane = new JPanel();
                        GridBagLayout gb = new GridBagLayout();
                        GridBagConstraints constraints = new GridBagConstraints();

                        commentsPane.setLayout(gb);

                        constraints.gridx = 0;
                        constraints.gridy = GridBagConstraints.RELATIVE;
                        constraints.fill = GridBagConstraints.VERTICAL;

                        for(int i = 0; i < theIssue.getComments().size(); i++){
                            commentsPane.add(commentPane(theIssue, i), constraints);
                            //커멘트 개수만큼 커멘트 페인을 추가한다.
                        }
                        totalPane.add(commentsPane, BorderLayout.CENTER);
                        totalPane.add(close, BorderLayout.SOUTH);
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

    JPanel commentPane(Issue theIssue, int index){ //코멘트 하나에 대한 패널: 테스터나 데브에 있는 것과 같다.
        JPanel totalPane = new JPanel(new BorderLayout());
        LineBorder b1 = new LineBorder(Color.BLACK, 2);
        totalPane.setBorder(b1);
        totalPane.setPreferredSize(new Dimension(800, 100));
        totalPane.setMaximumSize(new Dimension(800, 100));
        totalPane.setMinimumSize(new Dimension(800, 100));

        LineBorder b2 = new LineBorder(Color.GRAY, 1);

        JLabel user = new JLabel(theIssue.getComments().get(index).getUserName());
        user.setBorder(b2);

        JLabel content = new JLabel(theIssue.getComments().get(index).getContent());
        content.setBorder(b2);

        JLabel date = new JLabel(theIssue.getComments().get(index).getDate());
        date.setBorder(b2);

        totalPane.add(user, BorderLayout.WEST);
        totalPane.add(content, BorderLayout.CENTER);
        totalPane.add(date, BorderLayout.SOUTH);

        return totalPane;
    }

}

public class PLFrame {
    PLF plF;
    PLFrame(String projectName, String userName){
        plF = new PLF(projectName,userName);
    }
}