import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

//피엘, 데브 설명을 보고 오는 것을 추천
class TesterF extends JFrame { //테스터가 프로젝트를 고르면 실행되는 창이다
    String projectName;
    String userName;
    private IssueList issues;


    public TesterF(String projectName, String userName){
        super("ISSUE HANDLING");
        this.projectName = projectName; //프로젝트 이름 저장
        this.userName = userName; //테스터의 이름 저장
        this.issues = new IssueList(projectName);//이슈 불러오기
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createTab());
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();
        JPanel addIssuePane = new JPanel();
        JPanel myIssuePane = new JPanel();
        JPanel myFixedIssuePane = new JPanel();


        JButton addIssueButton = new JButton("이슈 등록 버튼");//이슈 등록 버튼을 누르면
        addIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddIssueF(projectName, userName);
            }
            //새 창을 띄울 것이다.
        });
        addIssuePane.add(addIssueButton);

        JScrollPane totalPane1 = new JScrollPane(addIssuePane);//모든 이슈들을 모아놓은 것에 스크롤바를 적용시킨 패널.
        //다른 탭에 있는 패널과 데브, 테스터 창의 패널에도 적용시켜야 한다.
        totalPane1.setVerticalScrollBar(new JScrollBar());

        pane.addTab("이슈 등록하기", totalPane1);

        myIssuePane.setLayout(new BoxLayout(myIssuePane, BoxLayout.Y_AXIS));
        myFixedIssuePane.setLayout(new BoxLayout(myFixedIssuePane, BoxLayout.Y_AXIS));

        JPanel firstPanel = new JPanel(new GridLayout(1,5));
        firstPanel.add(new JLabel("Title"));
        firstPanel.add(new JLabel("Status"));
        firstPanel.add(new JLabel("Priority"));
        firstPanel.add(new JLabel("Date"));
        firstPanel.add(new JLabel("Reporter"));
        LineBorder d1 = new LineBorder(Color.BLACK,1);
        firstPanel.setBorder(d1);
        firstPanel.setPreferredSize(new Dimension(800, 50));
        firstPanel.setMaximumSize(new Dimension(800, 50));
        firstPanel.setMinimumSize(new Dimension(800, 50));
        myIssuePane.add(firstPanel);


        int myIssueNum = 0;
        int myFixedIssueNum = 0;
        for(int i = 0; i < issues.getSize(); i++){//이슈 하나하나가
            if(issues.getTheIssue(i).getReporter().equals(userName)){
                JPanel issuePanel = issuePanel(i);//리포터가 본인 이름과 같다면 그걸 추가하고
                myIssuePane.add(issuePanel);

                myIssuePane.add(issuePanel);
                myIssueNum++;
                if(issues.getTheIssue(i).getStatus().equals(Status.FIXED)){
                    myFixedIssuePane.add(issuePanel(i));//그 이슈의 status 가 픽스드라면 픽스드에도 추가한다.
                    myFixedIssuePane.add(issuePanel(i));//그 이슈의 status 가 픽스드라면 픽스드에도 추가한다.
                    myFixedIssueNum ++;
                }
            }
        }
        Font myFont = new Font("Bold",Font.BOLD, 20);
        JLabel emptys = new JLabel("there is no issue");
        JLabel emptyfixed = new JLabel("there is no fixed issue :(");
        emptys.setFont(myFont);
        emptyfixed.setFont(myFont);
        if(myIssueNum ==0){
            myIssuePane.add(emptys);
        }
        if(myFixedIssueNum == 0){
            myFixedIssuePane.add(emptyfixed);
        }

        JButton refresh = new JButton("refresh");
        JButton refresh1  = new JButton("refresh");
        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TesterF t = new TesterF(projectName, userName);
                dispose();

            }
        });
        refresh1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TesterF t = new TesterF(projectName, userName);
                dispose();

            }
        });
        myIssuePane.add(refresh);
        myFixedIssuePane.add(refresh1);

        JScrollPane totalPane2 = new JScrollPane(myIssuePane);//모든 이슈들을 모아놓은 것에 스크롤바를 적용시킨 패널.
        //다른 탭에 있는 패널과 데브, 테스터 창의 패널에도 적용시켜야 한다.
        totalPane2.setVerticalScrollBar(new JScrollBar());

        JScrollPane totalPane3 = new JScrollPane(myFixedIssuePane);//모든 이슈들을 모아놓은 것에 스크롤바를 적용시킨 패널.
        //다른 탭에 있는 패널과 데브, 테스터 창의 패널에도 적용시켜야 한다.
        totalPane3.setVerticalScrollBar(new JScrollBar());
        pane.addTab("내가 올린 이슈", totalPane2);

        pane.addTab("내가 올린 고쳐진 이슈", totalPane3);

        return pane;
    }

    JPanel issuePanel(int index){ //이슈 하나를 나타내는 패널: 피엘과 데브와 유사하지만 살짝 다르다
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

        panel.addMouseListener(new MouseAdapter() { //이 패널을 클릭하면
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame newFrame = new JFrame("Issue Information");//새 창이 나타나고: 상세정보창임
                newFrame.setSize(900, 600);
                newFrame.setVisible(true);
                newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel totalPane = new JPanel(new BorderLayout());

                JPanel titlePane = new JPanel();
                JLabel title1 = new JLabel("title: " + theIssue.getTitle());//제목과
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

                JButton justClose = new JButton("cancel");//닫기 버튼
                justClose.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        newFrame.dispose();
                    }
                });

                JButton seeComments = new JButton("see comments");// 커멘트 보기 버튼
                seeComments.addActionListener(new ActionListener() {//이 버튼이 하는 일은 피엘의 커멘트 보기 버튼과 같다.
                    //데브는 커멘트를 달 자격이 있기 때문에 살짝 다름
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame commentFrame = new JFrame("comments");
                        commentFrame.setSize(900, 600);
                        commentFrame.setVisible(true);
                        commentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        JPanel totalPane = new JPanel(new BorderLayout());

                        JButton close = new JButton("close");
                        close.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                commentFrame.dispose();
                            }
                        });

                        JPanel commentsPane = new JPanel();

                        commentsPane.setLayout(new BoxLayout(commentsPane, BoxLayout.Y_AXIS));

                        for(int i = 0; i < theIssue.getComments().size(); i++){
                            commentsPane.add(new CommentPane(theIssue, i).getTotalPane());
                        }

                        JScrollPane commentScroll = new JScrollPane(commentsPane);//모든 이슈들을 모아놓은 것에 스크롤바를 적용시킨 패널.
                        //다른 탭에 있는 패널과 데브, 테스터 창의 패널에도 적용시켜야 한다.
                        commentScroll.setVerticalScrollBar(new JScrollBar());


                        totalPane.add(commentScroll, BorderLayout.CENTER);
                        totalPane.add(close, BorderLayout.SOUTH);
                        commentFrame.add(totalPane);
                        repaint();
                        revalidate();

                    }
                });



                JButton fixedButton = new JButton("resolve this issue"); //이슈를 리솔브로 만드는 버튼
                fixedButton.addActionListener(new ActionListener() {//이 버튼을 누르면
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PreparedStatement updateStatement;
                        try {
                            String updateQuery = "update issue set status = 'RESOLVED' where date = ?";
                            //이 날짜에 올라온 이슈의 상태를 리솔브로 바꾸라는 쿼리.
                            String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                            String serverUserName = "admin";
                            String serverPassword = "00000000";
                            Connection connection;
                            connection = DriverManager.getConnection(url, serverUserName, serverPassword);//디비에 연결

                            updateStatement = connection.prepareStatement(updateQuery);
                            updateStatement.setString(1, theIssue.getDate());
                            updateStatement.executeUpdate();//실행

                            updateStatement.close();
                            connection.close();


                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                        newFrame.dispose();//실행 후 창 닫기.
                    }
                });

                totalPane.add(titlePane, BorderLayout.NORTH);
                totalPane.add(descriptionPane, BorderLayout.CENTER);
                if(theIssue.getStatus().equals(Status.FIXED)){//지금 이슈 상태가 픽스드라면
                    JPanel fixedPane = new JPanel();
                    fixedPane.add(fixedButton);//리솔브로 만드는 버튼과
                    fixedPane.add(seeComments);//커멘트 보기 버튼과
                    fixedPane.add(justClose);//그냥 닫기 버튼을 화면에 추가할 것이고
                    totalPane.add(fixedPane, BorderLayout.SOUTH);
                }
                else{//픽스드가 아니라면
                    JPanel southPane = new JPanel();
                    southPane.add(seeComments);//커멘트 보기 버튼과
                    southPane.add(justClose);//창닫기 버튼을 화면에 추가할 것이다
                    totalPane.add(southPane, BorderLayout.SOUTH);
                }


                newFrame.add(totalPane);
                repaint();
                revalidate();
            }
        });

        return panel;
    }


}

class AddIssueF extends JFrame{ //이슈 추가하기를 눌렀을 때 나오는 새 창.
    JComboBox<Priority> selectPriority = new JComboBox<>();
    //우선순위를 고르기 위해 필요한 콤보박스

    AddIssueF(String projectName, String userName){
        super("REPORT ISSUE");
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel pane = reportIssuePanel(projectName, userName); //이슈 등록에 대한 패널
        add(pane);
        repaint();
        revalidate();
    }

    JPanel reportIssuePanel(String projectName, String userName){
        JPanel bigPanel = new JPanel(new BorderLayout());

        JPanel title = new JPanel(new GridLayout(1, 2));
        JLabel title1 = new JLabel("Title: "); //이슈 제목
        JTextField title2 = new JTextField(45);//최대 45자: 디비에 따르면 varchar(45)기 때문
        title.add(title1);
        title.add(title2);
        bigPanel.add(title, BorderLayout.NORTH);


        JPanel middlePanel = new JPanel(new GridLayout(2,1));  //description과 comment를 담을 중간 패널
        JPanel description = new JPanel(new BorderLayout());
        JLabel description1 = new JLabel("Description(up to 500 characters)");//이슈 내용
        JTextField description2 = new JTextField(500);//최대 500자
        description.add(description1, BorderLayout.NORTH);
        description.add(description2, BorderLayout.CENTER);
        middlePanel.add(description);

        JPanel comment = new JPanel(new BorderLayout());
        JLabel comment1 = new JLabel("Comment(up to 500 characters)");
        JTextField comment2 = new JTextField(500);
        comment.add(comment1,BorderLayout.NORTH);
        comment.add(comment2,BorderLayout.CENTER);
        middlePanel.add(comment);

        bigPanel.add(middlePanel,BorderLayout.CENTER); //중간 패널을 bigpanel의 center부분에 위치


        JPanel priority = new JPanel(new GridLayout(1, 4));
        JPanel priority1 = new JPanel(); //콤보박스가 있는 곳
        JPanel priority2 = new JPanel(); //빈칸: 보기 좋게 하기 위함
        JButton priority3 = new JButton("REPORT"); //등록 버튼
        priority3.addActionListener(new ActionListener() {//등록 버튼을 누르면
            @Override
            public void actionPerformed(ActionEvent e) {

                String issueTitle = title2.getText();//적은 제목과
                String issueDescription = description2.getText();//적은 내용과
                String issueComment = comment2.getText();
                Priority issuePriority = selectPriority.getItemAt(selectPriority.getSelectedIndex());//고른 우선순위를 가지고

                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                String serverUserName = "admin";
                String serverPassword = "00000000";

                String query = "insert into issue values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                String commentquery =  "insert into comment values (?, ?, ?, ?, ?)";


                //새 이슈를 등록한다는 쿼리

                Connection connection;
                try {
                    connection = DriverManager.getConnection(url, serverUserName, serverPassword);//디비에 연결

                    PreparedStatement pstmt;
                    PreparedStatement pstmtcomment;

                    pstmt = connection.prepareStatement(query);
                    pstmtcomment = connection.prepareStatement(commentquery);
                    //id,issue_id, content, userName,createdDate

                    Issue newIssue = new Issue(projectName, issueTitle, issueDescription, Status.NEW, issuePriority, userName);
                    Comment newComment = new Comment(issueComment, userName);

                    //새 이슈를 만들고
                    pstmt.setString(1, projectName);
                    pstmt.setString(2, projectName + newIssue.getShortDate()); //issue의 id
                    pstmt.setString(3, issueTitle);
                    pstmt.setString(4, issueDescription);
                    pstmt.setString(5, Status.NEW.toString());
                    pstmt.setString(6, issuePriority.toString());
                    pstmt.setString(7, newIssue.getDate());
                    pstmt.setString(8, userName);
                    pstmt.setString(9, null);
                    pstmt.setString(10, null);
                    //디비에 이슈를 추가한다. 어사이니와 픽서는 처음에는 null 로 둘 것이다

                    pstmtcomment.setString(1,projectName + newIssue.getShortDate() + newComment.getShortDate());
                    pstmtcomment.setString(2,projectName + newIssue.getShortDate());
                    pstmtcomment.setString(3, newComment.getContent());
                    pstmtcomment.setString(4,userName);
                    pstmtcomment.setString(5, newComment.getDate());

                    pstmt.executeUpdate();
                    pstmtcomment.executeUpdate();

                    pstmt.close();
                    pstmtcomment.close();
                    connection.close();


                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                repaint();
                revalidate();

                dispose();//등록했으면 창 닫기

            }
        });

        JButton priority4 = new JButton("CANCEL"); //취소 버튼을 누르면
        priority4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();//그냥 창을 닫는다
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



        bigPanel.add(new JLabel("  "), BorderLayout.EAST);//여백
        bigPanel.add(new JLabel("  "), BorderLayout.WEST);//여백
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

