import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class SelectionF extends JFrame{
    JComboBox<String> project = new JComboBox<>();
    ArrayList<Issue> issues = new ArrayList<>();

    SelectionF(Project proj, ArrayList<Issue> issues, User category){
        super("Select Project");
        this.setSize(600, 200);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.issues = issues;

        for(int i = 0; i < proj.getSize(); i++){
            project.addItem(proj.getName(i));
        }
        JPanel buttonPanel = new JPanel();
        JButton select = new JButton("CONNECT");
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(category.getCategory().equals("tester")){
                    new TesterFrame(proj.getName(project.getSelectedIndex()));
                }
                else if(category.getCategory().equals("PL")){
                    new PLFrame(new Browse(issues));
                }
                else if(category.getCategory().equals("dev")){
                    new DevFrame();
                }

                dispose();
            }
        });



        buttonPanel.add(select);
        JPanel combo = selectionP();
        JPanel bigPanel = new JPanel();
        bigPanel.add(combo);
        bigPanel.add(buttonPanel);
        add(bigPanel);

        repaint();
        revalidate();
    }

    private JPanel selectionP(){
        JPanel pane = new JPanel();
        pane.add(project);
        repaint();
        revalidate();
        return pane;
    }

}
public class ProjectSelection {
    public ProjectSelection(Project proj, ArrayList<Issue> issues, User category){
        new SelectionF(proj, issues, category);
    }
}
