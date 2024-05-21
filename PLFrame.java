import javax.swing.*;

class PLF extends JFrame {
    public PLF(){
        super("ISSUE HANDLING");
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createTab());
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();

        pane.addTab("전체 이슈", new JButton("전체 리스트"));
        pane.addTab("새 이슈", new JLabel("새 이슈 리스트. 클릭하면 상세정보, 데브 어사인 하기"));
        pane.addTab("풀린 이슈", new JLabel("resolved인 것만. 클릭하면 closed로"));

        return pane;
    }
}

public class PLFrame {
    PLF plF;
    PLFrame(){
        plF = new PLF();
    }//생성자는 나중에 name을 받아올 것임
}
