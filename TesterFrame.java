import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



class TesterF extends JFrame {
    String projectName;
    public TesterF(String projectName){
        super("ISSUE HANDLING");
        this.projectName = projectName;
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(createTab());
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();
        JPanel addIssuePane = new JPanel();
        JButton addIssueButton = new JButton("이슈 등록 버튼");
        addIssueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddIssueF(projectName);
            }
        });
        addIssuePane.add(addIssueButton);
        pane.addTab("이슈 등록하기", addIssuePane);
        pane.addTab("내가 올린 이슈", new JLabel("내가 올린 걸 보고 fixed를 resolved로"));

        return pane;
    }
}

class AddIssueF extends JFrame{
    JComboBox<Priority> selectPriority = new JComboBox<>();

    AddIssueF(String projectName){
        super("REPORT ISSUE");
        this.setSize(900, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pane = reportIssuePanel(projectName);
        add(pane);
        repaint();
        revalidate();
    }

    JPanel reportIssuePanel(String projectName){
        JPanel bigPanel = new JPanel(new BorderLayout());

        JPanel title = new JPanel(new GridLayout(1, 2));
        JLabel title1 = new JLabel("Title: ");
        JTextField title2 = new JTextField(45);
        title.add(title1);
        title.add(title2);
        bigPanel.add(title, BorderLayout.NORTH);

        JPanel description = new JPanel(new BorderLayout());
        JLabel description1 = new JLabel("Description");
        JTextField description2 = new JTextField(500);
        description.add(description1, BorderLayout.NORTH);
        description.add(description2, BorderLayout.CENTER);
        bigPanel.add(description, BorderLayout.CENTER);

        JPanel priority = new JPanel(new GridLayout(1, 4));
        JPanel priority1 = new JPanel(); //combobox button
        JPanel priority2 = new JPanel(); // vacant panel
        JButton priority3 = new JButton("REPORT"); //ok button
        priority3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton priority4 = new JButton("CANCEL"); //cancel button
        priority4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        selectPriority.addItem(Priority.MAJOR);
        selectPriority.addItem(Priority.BLOCKER);
        selectPriority.addItem(Priority.CRITICAL);
        selectPriority.addItem(Priority.MINOR);
        selectPriority.addItem(Priority.TRIVIAL);
        priority1.add(selectPriority);
        priority.add(priority1);
        priority.add(priority2);
        priority.add(priority3);
        priority.add(priority4);



        bigPanel.add(new JLabel("  "), BorderLayout.EAST);
        bigPanel.add(new JLabel("  "), BorderLayout.WEST);
        bigPanel.add(priority, BorderLayout.SOUTH);


        return bigPanel;
    }
}

public class TesterFrame {
    TesterF testerF;
    TesterFrame(String projectName){
        testerF = new TesterF(projectName);
    }
}

