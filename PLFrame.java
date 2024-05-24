import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        GridBagLayout gb = new GridBagLayout();
        JPanel issuesPanel = new JPanel();
        GridBagConstraints constraints = new GridBagConstraints();
        issuesPanel.setLayout(gb);
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.VERTICAL;


        for(int i = 0; i < issues.getSize(); i++){
            JPanel issuePanel = issuePanel(i);
            issuesPanel.add(issuePanel, constraints);
        }
        JScrollPane totalPane = new JScrollPane(issuesPanel);
        totalPane.setVerticalScrollBar(new JScrollBar());


        pane.addTab("전체 이슈", totalPane);
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
        panel.setPreferredSize(new Dimension(800, 100));
        panel.setMaximumSize(new Dimension(800, 100));
        panel.setMinimumSize(new Dimension(800, 100));

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFrame newFrame = new JFrame("Issue Information");
                newFrame.setSize(900, 600);
                newFrame.setVisible(true);
                newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                JPanel totalPane = new JPanel(new BorderLayout());

                JPanel titlePane = new JPanel();
                JLabel title1 = new JLabel("title: " + theIssue.getTitle());
                titlePane.add(title1);

                JPanel descriptionPane = new JPanel();
                JLabel description1 = new JLabel("Description: \r\n" + theIssue.getDescription());
                descriptionPane.add(description1);

                JButton assignButton = new JButton("Assign"); //데브 어사인하기.
                JComboBox<String> devs = new JComboBox<>();

                totalPane.add(titlePane, BorderLayout.NORTH);
                totalPane.add(descriptionPane, BorderLayout.CENTER);
                newFrame.add(totalPane);
                repaint();
                revalidate();
            }
        });

        return panel;
    }

}

public class PLFrame {
    PLF plF;
    PLFrame(String projectName, String userName){
        plF = new PLF(projectName,userName);
    }//생성자는 나중에 name을 받아올 것임
}