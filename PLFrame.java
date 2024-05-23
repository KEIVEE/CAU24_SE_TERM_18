import javax.swing.*;

class PLF extends JFrame {
    private Browse browse;
    public PLF(Browse browse){
        super("ISSUE HANDLING");
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createTab());

        this.browse = browse;
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();

        JButton listButton = new JButton("전체 리스트");
        listButton.addActionListener(e -> showBrowsePanel(pane));

        pane.addTab("전체 이슈", listButton);
        pane.addTab("새 이슈", new JLabel("새 이슈 리스트. 클릭하면 상세정보, 데브 어사인 하기"));
        pane.addTab("풀린 이슈", new JLabel("resolved인 것만. 클릭하면 closed로"));

        return pane;
    }

    private void showBrowsePanel(JTabbedPane pane) {
        BrowsePanel browsePanel = new BrowsePanel(browse);
        pane.setComponentAt(0, browsePanel); // 첫번째 탭을 BrowsePanel로 교체
    }
}

public class PLFrame {
    PLF plF;
    PLFrame(Browse browse){
        plF = new PLF(browse);
    }//생성자는 나중에 name을 받아올 것임
}
