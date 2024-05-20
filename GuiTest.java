import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/";
        String userName = "admin";
        String password = "00000000";
        String dbName = "test";

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement;
        ResultSet resultset;
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

    }

}
