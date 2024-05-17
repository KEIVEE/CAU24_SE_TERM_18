import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class IssueFrame extends JFrame {
    public IssueFrame(){
        super("ISSUE HANDLING SYSTEM");
        setVisible(true);
        setSize(400,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}


class MyFrame extends JFrame{
    JTextField id;
    JTextField password;
    JButton jok;
    public MyFrame(){
        super("ISSUE HANDLING SYSTEM - LOGIN");
        setVisible(true);
        setSize(400,300);
        JPanel panel = new JPanel(); //id,password panel
        panel.setLayout(new GridLayout(2,2));
        id = new JTextField(30);
        password = new JTextField(30);
        panel.add(new JLabel("ID : "));
        panel.add(id);
        panel.add(new JLabel("PASSWORD : "));
        panel.add(password);

        JPanel panel2 = new JPanel();
        jok = new JButton("Log in");
        jok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new IssueFrame();
                dispose();
                revalidate();
                repaint();
            }
        });
        panel2.add(jok);

        JPanel bigPanel = new JPanel();
        bigPanel.setLayout(new GridLayout(2,1));
        bigPanel.add(panel);
        bigPanel.add(panel2);

        add(bigPanel);

        super.revalidate();
        super.repaint();
        pack();//크기정렬
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

}

public class GuiTest{

    public static void main(String[] args){
        new MyFrame();

    }
}
