import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TrendPanel extends JPanel {
    public TrendPanel(String projectName, ArrayList<Issue> issueList){
        add(GraphPanel(0,issueList));


    }
    JPanel GraphPanel(int time, ArrayList<Issue> issueList){

        long daysDifference = 0;

        JPanel graph = new JPanel();
        LocalDateTime oldestDate = null;
        LocalDateTime newestDate = null;
        if(time == 0) { //일별 비교
            // 가장 오래된 날짜와 가장 최신 날짜 초기화


            // 이슈 리스트에서 날짜를 파싱하고 최소 및 최대 날짜 찾기
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (Issue issue : issueList) {
                LocalDateTime date = LocalDateTime.parse(issue.getDate(), formatter);
                if (oldestDate == null || date.isBefore(oldestDate)) {
                    oldestDate = date;
                }
                if (newestDate == null || date.isAfter(newestDate)) {
                    newestDate = date;
                }
            }

            // 날짜 차이 계산
            daysDifference = java.time.Duration.between(oldestDate, newestDate).toDays();
            BarGraphPanel barGraphPanel = new BarGraphPanel(issueList, oldestDate, newestDate);
            graph.add(barGraphPanel);


        }
        else if(time == 1){ //월별 비교

        }



        return graph;
    }


}


