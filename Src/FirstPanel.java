import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class FirstPanel extends JPanel {

    FirstPanel(ArrayList<Issue> issues){
        //사이즈가 0이면 there is no issue라는 japnel만 뜨게 하고
        //사이즈가 1이상이면 그 퍼스트패널을 뜨게 하고.

        if(issues.isEmpty()){
            Font myFont = new Font("Bold",Font.BOLD, 20);
            JLabel emptys = new JLabel("there is no issue");
            emptys.setFont(myFont);
            add(emptys);
        }
        else{
            super.setLayout(new GridLayout(1,5));
            add(new JLabel("Title"));
            add(new JLabel("Status"));
            add(new JLabel("Priority"));
            add(new JLabel("Date"));
            add(new JLabel("Reporter"));
            LineBorder d1 = new LineBorder(Color.BLACK,1);
            setBorder(d1);
            setPreferredSize(new Dimension(800, 50));
            setMaximumSize(new Dimension(800, 50));
            setMinimumSize(new Dimension(800, 50));
        }
    }
}
