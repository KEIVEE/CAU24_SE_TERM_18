import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

class PLF extends JFrame {
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
        JPanel issuesPanel = new JPanel(new GridLayout(0, 1));
        for(int i = 0; i < issues.getSize(); i++){
            JPanel issuePanel = issuePanel(i);
            issuesPanel.add(issuePanel);
        }


        pane.addTab("전체 이슈", issuesPanel);
        pane.addTab("새 이슈", new JLabel("새 이슈 리스트. 클릭하면 상세정보, 데브 어사인 하기"));
        pane.addTab("풀린 이슈", new JLabel("resolved인 것만. 클릭하면 closed로"));

        return pane;
    }

    JPanel issuePanel(int index){
        Issue theIssue = issues.browseAll().get(index);
        JPanel panel = new JPanel(new GridLayout(1, 5));
        panel.add(new JLabel(theIssue.getTitle()));
        panel.add(new JLabel(theIssue.getStatus().toString()));
        panel.add(new JLabel(theIssue.getPriority().toString()));
        panel.add(new JLabel(theIssue.getDate()));
        panel.add(new JLabel(theIssue.getReporter()));

        LineBorder b1 = new LineBorder(Color.BLACK, 2);
        panel.setBorder(b1);
        return panel;
    }

}

public class PLFrame {
    PLF plF;
    PLFrame(String projectName, String userName){
        plF = new PLF(projectName,userName);
    }//생성자는 나중에 name을 받아올 것임
}
