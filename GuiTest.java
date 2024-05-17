import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Myframe extends JFrame{
    public Myframe(String title){
        this.setTitle(title);
        this.setSize(800, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTabbedPane createTab(){
        JTabbedPane pane = new JTabbedPane();

        pane.addTab("New", new JLabel("Tab menu 1"));
        pane.addTab("Browse", new JLabel("Tab menu 2"));
        pane.addTab("", new JLabel("Tab menu 3"));

        return pane;
    }
}

public class GuiTest {

    public static void main(String[] args){
        Myframe login = new Myframe("Log in");

        JButton loginButton = new JButton("Log In");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                Myframe frame = new Myframe("Issue Handling");
                login.dispose();

                JTabbedPane pane = frame.createTab();
                frame.add(pane);

            }
        });
        login.add(loginButton);

        login.revalidate();
        login.repaint();
        //JTabbedPane pane = frame.createTab();
        //frame.add(pane);

        //JButton
    }
}
