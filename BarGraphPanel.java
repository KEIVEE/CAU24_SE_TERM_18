import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
        int graphWidth = getWidth() - 100;
        int graphHeight = getHeight() - 100;
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



        // 막대 그래프 그리기
        for (LocalDate date : dailyCounts.keySet()) {
            int x = startX + (int) (Duration.between(oldestDate3, date.atStartOfDay()).toDays() * ((double) graphWidth / Duration.between(oldestDate3, newestDate3).toDays()));
            int y = startY + graphHeight - (int) ((double) dailyCounts.get(date) / maxCount * graphHeight);
            int barHeight = (int) ((double) dailyCounts.get(date) / maxCount * graphHeight);
            int barWidth = (int) (((double) graphWidth / Duration.between(oldestDate3, newestDate3).toDays()) - 1);

            g2d.setColor(Color.BLUE);
            g2d.fillRect(x, y, barWidth, barHeight);
        }

        // 그래프 축 그리기
        g2d.setColor(Color.BLACK);
        g2d.drawLine(startX, startY + graphHeight, startX + graphWidth, startY + graphHeight); // x 축
        g2d.drawLine(startX, startY + graphHeight, startX, startY); // y 축

        // x 축 눈금 그리기
        currentDate = oldestDate2;
        while (!currentDate.isAfter(newestDate2)) {
            int x = startX + (int) (Duration.between(oldestDate3, currentDate.atStartOfDay()).toDays() * ((double) graphWidth / Duration.between(oldestDate3, newestDate3).toDays()));
            g2d.drawLine(x, startY + graphHeight, x, startY + graphHeight + 5);
            g2d.drawString(currentDate.format(DateTimeFormatter.ofPattern("MM/dd")), x - 15, startY + graphHeight + 20);
            currentDate = currentDate.plusDays(1);
        }

        // y 축 눈금 그리기
        for (int i = 0; i <= 10; i++) {
            int y = startY + graphHeight - (int) ((double) i / 10 * graphHeight);
            g2d.drawLine(startX, y, startX - 5, y);
            g2d.drawString(String.valueOf(i * maxCount / 10), startX - 40, y);
        }

        // 축 레이블 그리기
        g2d.drawString("Date", startX + graphWidth / 2 - 20, startY + graphHeight + 40);
        g2d.drawString("Issue Count", startX - 60, startY + graphHeight / 2);


    }
}