import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class AdminF extends JFrame { //admin frame 클래스
    public AdminF(){
        super("Admin : ISSUE HANDLING SYSTEM");
        setSize(900,600);
        JTabbedPane pane = createTab();
        add(pane, BorderLayout.CENTER);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTabbedPane createTab(){ //admin tab
        JTabbedPane pane = new JTabbedPane();
        JPanel AC = new JPanel();
        JButton dev = new JButton("developer");
        JButton PL = new JButton("PL");
        JButton tester = new JButton("tester");
        AC.add(dev);
        AC.add(PL);
        AC.add(tester);
        pane.addTab("계정 추가", AC);
        pane.addTab("프로젝트 추가",new JLabel("t1")); //나중에 바꾸기

        dev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                devAC makeDev = new devAC();
            }
        });
        PL.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PLAC makePL = new PLAC();
            }
        });
        tester.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TesterAC maketester = new TesterAC();
            }
        });

        return pane;
    }
}

class devAC extends JFrame{ //developer 계정 만들기 클래스
    public devAC(){
        super("making developer account");
        setVisible(true);
        setSize(300,100);
        setLayout(new GridLayout(2,1));
        AC myAC = new AC();
        /*
        JPanel inAC = new JPanel();   //id, 비번 입력 panel 인데 만들어놓은 AC 클래스로 대체
        inAC.setLayout(new GridLayout(2,2));
        JButton ID = new JButton("ID");
        JButton pw = new JButton("password");
        JTextField IDT  = new JTextField(30);
        JTextField pwT = new JTextField(30);
        inAC.add(ID);
        inAC.add(IDT);
        inAC.add(pw);
        inAC.add(pwT);
        */
        JPanel OK = new JPanel(); //확인, 취소 버튼
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

class PLAC extends JFrame{  //PL 계정 만들기 클래스
    public PLAC(){
        super("making PL account");
        setVisible(true);
        setSize(300,100);
        setLayout(new GridLayout(2,1));
        AC myAC = new AC();
        /*
        JPanel inAC = new JPanel();
        inAC.setLayout(new GridLayout(2,2));
        JButton ID = new JButton("ID");
        JButton pw = new JButton("password");
        JTextField IDT  = new JTextField(30);
        JTextField pwT = new JTextField(30);
        inAC.add(ID);
        inAC.add(IDT);
        inAC.add(pw);
        inAC.add(pwT);
        */
        JPanel OK = new JPanel(); //확인, 취소 버튼
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

class TesterAC extends JFrame{ //테스터 계정 만들기 클래스
    public TesterAC(){
        super("making tester account");
        setVisible(true);
        setSize(300,100);
        setLayout(new GridLayout(2,1));
        AC myAC = new AC();
        JPanel OK = new JPanel(); //확인, 취소 버튼
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
                String password = myAC.pwT.getText();
                String name = myAC.nameT.getText();

                String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
                String userName = "admin";
                String serverPassword = "00000000";

                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(url, userName, serverPassword);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                PreparedStatement pstmt = null;
                String query = "insert into account values (?, ?, ?, ?)";
                try {
                    pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, name);
                    pstmt.setString(2, id);
                    pstmt.setString(3, password);
                    pstmt.setString(4, "tester");
                    pstmt.executeUpdate();




                } catch (SQLException ex) {
                    if(ex.getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) //id가 겹칠 시
                        new TesterAC();//다시.

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

        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });


    }
}

class AC extends JPanel{  //계정 ID, password 입력 Panel
      JLabel ID;
      JLabel pw;
      JLabel name;
      JTextField IDT;
      JTextField pwT;
      JTextField nameT;
    public AC(){
        setLayout(new GridLayout(3,2));
        ID = new JLabel("ID");
        pw = new JLabel("password");
        name = new JLabel("name");
        IDT  = new JTextField(30);
        pwT = new JTextField(30);
        nameT = new JTextField(30);
        add(ID);
        add(IDT);
        add(pw);
        add(pwT);
        add(name);
        add(nameT);
    }

}


public class AdminFrame {
    AdminF t = new AdminF();

}
