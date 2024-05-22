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
        JButton dev = new JButton("계정 추가");

        AC.add(dev);
        pane.addTab("계정 추가", AC);
        pane.addTab("프로젝트 추가",new JLabel("t1")); //나중에 바꾸기

        dev.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewAccount makeDev = new NewAccount();
            }
        });


        return pane;
    }
}


class NewAccount extends JFrame{ //테스터 계정 만들기 클래스
    public NewAccount(){
        super("Make a new account");
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
                String category = myAC.type[myAC.category.getSelectedIndex()];
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
                    pstmt.setString(4, category);
                    pstmt.executeUpdate();




                } catch (SQLException ex) {
                    if(ex.getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException")) //id가 겹칠 시
                        new NewAccount();//다시.

                }

                repaint();
                revalidate();
                try {
                    assert pstmt != null;
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
      String[] type = {"tester", "dev", "PL"};
      JComboBox<String> category = new JComboBox<>(type);


    public AC(){
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
