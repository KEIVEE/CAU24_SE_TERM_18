import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class AdminF extends JFrame { //admin frame 클래스
    public AdminF(){
        super("Admin : ISSUE HANDLING SYSTEM");
        setSize(400,300);
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
        /*JPanel inAC = new JPanel();
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

class AC extends JPanel{  //계정 ID, password 입력 Panel
    public AC(){
        setLayout(new GridLayout(2,2));
        JLabel ID = new JLabel("ID");
        JLabel pw = new JLabel("password");
        JTextField IDT  = new JTextField(30);
        JTextField pwT = new JTextField(30);
        add(ID);
        add(IDT);
        add(pw);
        add(pwT);
    }

}


public class AdminFrame {
    AdminF t = new AdminF();

}
