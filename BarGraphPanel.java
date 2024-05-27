import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class BarGraphPanel extends JPanel {
    private ArrayList<Issue> issueList;
    private LocalDate oldestDate2;
    private LocalDateTime oldestDate3;
    private LocalDate newestDate2;
    private LocalDateTime newestDate3;

    public BarGraphPanel(ArrayList<Issue> issueList, LocalDateTime oldestDate, LocalDateTime newestDate) {
        this.issueList = issueList;
        this.oldestDate2 = oldestDate.toLocalDate();
        this.newestDate2 = newestDate.toLocalDate();
        this.oldestDate3 = oldestDate;
        this.newestDate3 = newestDate;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 그래픽 컨텍스트 얻기
        Graphics2D g2d = (Graphics2D) g;

        // 그래프 영역 설정
        int graphWidth = 800;
        int graphHeight = 250;
        int startX = 50;
        int startY = 50;
        int panelX = 400;
        int panelY = 180;
        // 날짜별 이슈 개수 계산
        HashMap<LocalDate, Integer> dailyCounts = new HashMap<>();
        LocalDate currentDate = oldestDate2;
        while (!currentDate.isAfter(newestDate2)) {
            dailyCounts.put(currentDate, 0);
            currentDate = currentDate.plusDays(1);
        }
        for (Issue issue : issueList) {
            LocalDate date = LocalDateTime.parse(issue.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toLocalDate();
            dailyCounts.put(date, dailyCounts.get(date) + 1);

        }

        // 최대 이슈 개수 찾기
        int maxCount = dailyCounts.values().stream().max(Integer::compareTo).orElse(0);




    }
}