import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class SelectionF extends JFrame{
    JComboBox<String> project = new JComboBox<>();

    SelectionF(Project proj, User category){
        super("Select Project");
        this.setSize(600, 200);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for(int i = 0; i < proj.getSize(); i++){
            project.addItem(proj.getName(i));
        }
        JPanel buttonPanel = new JPanel();
        JButton select = new JButton("CONNECT");
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(category.getCategory().equals("tester")){
                    new TesterFrame();
                }
                else if(category.getCategory().equals("PL")){
                    new PLFrame();
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
    public ProjectSelection(Project proj, User category){
        new SelectionF(proj, category);
    }
}
