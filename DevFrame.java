import javax.swing.*;

class DevF extends JFrame {
    public DevF(){
        super("ISSUE HANDLING");
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createTab());
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();

        pane.addTab("내 이슈", new JLabel("나한테 어사인 된 이슈들. 클릭하면 상세정보 새 창(커멘트 포함)\n" +
                "그리고 커멘트를 달 수 있는 기능. 이슈를 fixed로 바꾸고, fixer은 본인으로"));
        return pane;
    }
}

public class DevFrame {
    DevF devF;
    DevFrame(){
        devF = new DevF();
    }//생성자는 나중에 name을 받아올 것임
}
