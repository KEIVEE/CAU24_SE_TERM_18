import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class CommentPane extends JPanel {
    private JPanel totalPane;

    public CommentPane(Issue theIssue, int index){ //커멘트 하나에 대한 정보를 보여주는 패널
        totalPane = new JPanel(new BorderLayout());
        LineBorder b1 = new LineBorder(Color.BLACK, 2);
        totalPane.setBorder(b1); //커멘트끼리 구분을 위해 보더가 필요하다
        totalPane.setPreferredSize(new Dimension(800, 100));
        totalPane.setMaximumSize(new Dimension(800, 100));
        totalPane.setMinimumSize(new Dimension(800, 100));

        LineBorder b2 = new LineBorder(Color.GRAY, 1);

        JLabel user = new JLabel(theIssue.getComments().get(index).getUserName());
        //커멘트를 올린 유저
        user.setBorder(b2);

        JLabel content = new JLabel(theIssue.getComments().get(index).getContent());
        //커멘트 내용
        content.setBorder(b2);

        JLabel date = new JLabel(theIssue.getComments().get(index).getDate());
        //커멘트를 올린 날짜를 뜨게 할 것이다
        date.setBorder(b2);

        totalPane.add(user, BorderLayout.WEST);
        totalPane.add(content, BorderLayout.CENTER);
        totalPane.add(date, BorderLayout.SOUTH);

    }

    public JPanel getTotalPane() {
        return totalPane;
    }
}
