import javax.swing.*;

class TesterF extends JFrame {
    public TesterF(){
        super("ISSUE HANDLING");
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createTab());
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();

        pane.addTab("이슈 등록하기", new JButton("이슈 등록 버튼"));
        pane.addTab("내가 올린 이슈", new JLabel("내가 올린 걸 보고 fixed를 resolved로"));

        return pane;
    }
}

public class TesterFrame {
    TesterF testerF;
    TesterFrame(){
        testerF = new TesterF();
    }
}
