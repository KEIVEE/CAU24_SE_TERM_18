import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class SelectionF extends JFrame{ //테스터, 피엘, 데브가 거치는 프로젝트 고르기 창이다
    JComboBox<String> project = new JComboBox<>(); //접속할 프로젝트를 고를 때 쓸 콤보박스
    ArrayList<Issue> issues; //모든 프로젝트의 이슈를 모아놓을 issues

    SelectionF(Project proj, ArrayList<Issue> issues, User category){
        super("Select Project"); //창 이름
        this.setSize(600, 200);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.issues = issues;

        for(int i = 0; i < proj.getSize(); i++){
            project.addItem(proj.getName(i));//프로젝트 개수만큼 콤보박스에 아이템을 추가한다
        }
        JPanel buttonPanel = new JPanel(); //버튼을 넣을 패널
        JButton select = new JButton("CONNECT");
        select.addActionListener(new ActionListener() { //커넥트 버튼을 누르면,
            @Override
            public void actionPerformed(ActionEvent e) {
                if(category.getCategory().equals("tester")){//지금 들어온 유저가 테스터라면
                    new TesterFrame(proj.getName(project.getSelectedIndex()), category.getUserName());//새 테스터 창을
                    //파라미터는 지금 고른 프로젝트의 이름이고 본인 계정 이름임
                }
                else if(category.getCategory().equals("PL")){//들어온 유저가 PL 이면
                    new PLFrame(proj.getName(project.getSelectedIndex()), category.getUserName());//새 PL 창을
                }
                else if(category.getCategory().equals("dev")){//들어온 유저가 데브면
                    new DevFrame(proj.getName(project.getSelectedIndex()), category.getUserName());//새 데브 창을 생성한다.
                }

                dispose(); //다음 창을 띄우고 나면 본인 창은 사라진다
            }
        });



        buttonPanel.add(select);
        JPanel combo = selectionP();
        JPanel bigPanel = new JPanel();
        bigPanel.add(combo);
        bigPanel.add(buttonPanel);
        add(bigPanel); //창에 띄우기

        repaint();
        revalidate();
    }

    private JPanel selectionP(){
        JPanel pane = new JPanel();
        pane.add(project); //프로젝트 콤보박스를 넣은 패널
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
