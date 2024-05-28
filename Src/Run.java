import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


class MyFrame extends JFrame{ //로그인 창
    JTextField id; //로그인 아이디
    JPasswordField password; //로그인 비밀번호
    JButton jok; //로그인 버튼
    ArrayList<Issue> issues = new ArrayList<>(); //이슈들 모임.

    Project proj = new Project(); //프로젝트: 프로젝트 객체는 프로젝트 이름들을 모아놓은 것임
    public MyFrame(){
        super("ISSUE HANDLING SYSTEM - LOGIN"); //로그인 창 제목
        setVisible(true);
        setSize(400,350);

        JPanel panelname = new JPanel();
        JLabel lb = new JLabel(" ISSUE HANDLING SYSTEM ");

        Font myFont = new Font("italic",Font.ITALIC,20);
        lb.setFont(myFont);


        panelname.add(lb,BorderLayout.CENTER);


        JPanel panel = new JPanel(); //아이디랑 비밀번호가 포함된 패널
        panel.setLayout(new GridLayout(2,2));//2*2표로 만들어볼 것
        id = new JTextField(30);
        password = new JPasswordField(30);

        panel.add(new JLabel("ID : "));
        panel.add(id);
        panel.add(new JLabel("PASSWORD : "));
        panel.add(password);//아이디랑 비밀번호 추가함

        JLabel loginfail = new JLabel("");
        JPanel logpanel = new JPanel();
        logpanel.add(loginfail);

        JPanel panel2 = new JPanel();
        jok = new JButton("Log in");//버튼 이름 지정
        jok.addActionListener(new ActionListener() { //로그인 버튼을 누르면
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean login_status = false; //로그인 상태를 false 로 시작하게 함
                String IDcheck; //아이디 텍스트필드에 입력된 내용 저장할 곳
                String pwcheck; //패스워드 텍스트필드에 입력된 내용 저장할 곳

                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                String userName = "admin";
                String serverPassword = "00000000";

                Connection connection;
                try {
                    connection = DriverManager.getConnection(url, userName, serverPassword); //연결 시도


                    IDcheck = id.getText();
                    pwcheck = new String(password.getPassword()); //텍스트필드에 있는 값을 찾아와서

                    String loginQuery = "select * from account where id = '" + IDcheck + "'";//쿼리를 작성
                    //어카운트에서 id가 이것인 것을 골라라
                    String issueQuery = "select * from issue";
                    //이슈의 모든 것을 불러와라
                    String projectQuery = "select * from project";
                    //프로젝트의 모든 것을 불러와라


                    Statement projectStmt = connection.createStatement();
                    ResultSet projectRs = projectStmt.executeQuery(projectQuery); //프로젝트 불러오기 쿼리를 실행했을 때
                    while(projectRs.next()){
                        proj.add(projectRs.getString("name")); //프로젝트 객체 proj 에다가, 프로젝트 이름을 더함
                    }



                    Statement issueStmt = connection.createStatement();
                    ResultSet issueRs = issueStmt.executeQuery(issueQuery); //이슈 불러오기 쿼리를 실행했을 때
                    while(issueRs.next()){
                        String name = issueRs.getString("projectName");
                        String title = issueRs.getString("title");
                        String description = issueRs.getString("description");
                        Status status = Status.valueOf(issueRs.getString("status"));
                        Priority priority = Priority.valueOf(issueRs.getString("priority"));
                        String date = issueRs.getString("date");
                        String reporter = issueRs.getString("reporter");
                        String assignee = issueRs.getString("assignee");
                        String fixer = issueRs.getString("fixer");

                        ArrayList<Comment> comments = new ArrayList<>();

                        //데이터베이스에 있는 이슈의 정보들을 불러와서,
                        issues.add(new Issue(name, title, description, status, priority, date, reporter, assignee, fixer, comments));
                        //새 이슈 객체를 생성해서 이슈리스트 객체에 추가할 것. 프로젝트를 구분하지 않고 넣는다.
                        //지금은 어디 프로젝트에 들어갈 지 정하지 않았기 때문이다
                    }

                    Statement stmt = connection.createStatement();
                    ResultSet loginRs = stmt.executeQuery(loginQuery); //로그인 쿼리를 실행하고 나서
                    while(loginRs.next()) {
                        if (loginRs.getString("password").equals(pwcheck)) { //패스워드가 데이터베이스와 일치하는지 확인하고
                            if (loginRs.getString("category").equals("admin")) { //얘가 어드민이면 어드민 창을 띄운다
                                new AdminFrame(proj, issues);
                            } else if (loginRs.getString("category").equals("tester")) { //테스터면
                                Tester testerUser = new Tester(loginRs.getString("name"));//테스터 객체를 생성,
                                new ProjectSelection(proj, issues, testerUser);//프로젝트 고르는 창으로 이동한다.
                            } else if (loginRs.getString("category").equals("PL")) { //PL 이면
                                PL PLUser = new PL(loginRs.getString("name")); //PL 객체를 생성
                                new ProjectSelection(proj, issues, PLUser); //프로젝트 고르는 창으로 이동한다.
                            } else if (loginRs.getString("category").equals("dev")) { //데브면
                                Dev devUser = new Dev(loginRs.getString("name")); //데브 객체를 생성
                                new ProjectSelection(proj, issues, devUser);//프로젝트 고르는 창으로 이동한다
                            }
                            login_status = true;//그리고 로그인 상태를 트루로 만들어줌

                        }
                    }




                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if(!login_status){//로그인 실패 시: 아이디가 틀렸거나 비번이 틀렸거나. 아직은 구분하지 않았다. 경고메세지같은거 띄우고 싶긴 함
                   id.setText("");
                   password.setText("");
                   loginfail.setText("잘못된 ID 혹은 비밀번호입니다.");
                   proj = new Project();
                   issues = new ArrayList<>();
                }

                if(login_status == true) {
                    dispose(); //로그인 실패하던 성공하던 그 창은 닫음
                }
                revalidate();
                repaint();

                try {
                    connection.close(); //연결 종료
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        panel2.add(jok);//로그인버튼 추가

        JPanel bigPanel = new JPanel(); //패널들을 모아놓은 패널
        bigPanel.setLayout(new GridLayout(4,1));
        bigPanel.add(panelname);
        bigPanel.add(panel);// 위쪽에 아이디 패스워드
        bigPanel.add(logpanel);
        bigPanel.add(panel2);//아래쪽에 로그인 버튼


        add(bigPanel);//전체 창에 최종 패널을 추가한다

        super.revalidate();
        super.repaint();
        pack();//크기정렬
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

}

public class Run {

    public static void main(String[] args) {
        new MyFrame();


    }
}

