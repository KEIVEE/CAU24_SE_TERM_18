import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TrendPanel extends JPanel {
    ArrayList<Issue> issueList;
    String projectName;

    public TrendPanel(String projectName, ArrayList<Issue> issueList){
        //add(GraphPanel(0,issueList));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.projectName = projectName;
        this.issueList = issueList;

        JPanel issuesDay = new JPanel();

        LocalDate oldestDate = null;
        LocalDate newestDate = null;
         //일별 비교
        // 가장 오래된 날짜와 가장 최신 날짜 초기화
        // 이슈 리스트에서 날짜를 파싱하고 최소 및 최대 날짜 찾기
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Issue issue : issueList) {

            LocalDateTime date1 = LocalDateTime.parse(issue.getDate(), formatter);
            LocalDate date = date1.toLocalDate();
            if (oldestDate == null || date.isBefore(oldestDate)) {
                oldestDate = date;
            }
            if (newestDate == null || date.isAfter(newestDate)) {
                newestDate = date;
            }
        }
            // 날짜 차이 계산
        Period p1 = Period.between(oldestDate,newestDate);
        LocalDate t;

        int daysDifference = p1.getDays();
        LocalDate currentDate = oldestDate;
        while (!currentDate.isAfter(newestDate)) {
            if(returnnum(currentDate) ==1 ){
                issuesDay.add(statisticByDate(currentDate));
            }
            currentDate = currentDate.plusDays(1);

        }
        add(issuesDay);

        JScrollPane statisticByIssueStatus = statisticByIssueStatus();
        JScrollPane statisticByDev = statisticByDev();
        JPanel statusPlusDev = new JPanel();
        statusPlusDev.add(statisticByIssueStatus);
        statusPlusDev.add(statisticByDev);

        add(statusPlusDev);

    }

    ArrayList<Issue> issuesByStatus(Status status) {
        ArrayList<Issue> newIssues = new ArrayList<>();
        for (int i = 0; i < issueList.size(); i++) {
            if (issueList.get(i).getStatus().equals(status)) {
                newIssues.add(issueList.get(i));
            }
        }
        return newIssues;
    }

    JScrollPane statisticByIssueStatus(){

        JPanel statisticByIssueStatus = new JPanel();
        statisticByIssueStatus.setLayout(new BoxLayout(statisticByIssueStatus, BoxLayout.Y_AXIS));
        ArrayList<Issue> newIssues = issuesByStatus(Status.NEW);
        ArrayList<Issue> assignedIssues = issuesByStatus(Status.ASSIGNED);
        ArrayList<Issue> fixedIssues = issuesByStatus(Status.FIXED);
        ArrayList<Issue> resolvedIssues = issuesByStatus(Status.RESOLVED);
        ArrayList<Issue> closedIssues = issuesByStatus(Status.CLOSED);
        ArrayList<Issue> reopenedIssues = issuesByStatus(Status.REOPENED);

        JLabel newIssuesLabel = new JLabel("new issues: " + newIssues.size());
        JLabel assignedIssuesLabel = new JLabel("assigned issues: " + assignedIssues.size());
        JLabel fixedIssuesLabel = new JLabel("fixed issues: " + fixedIssues.size());
        JLabel resolvedIssuesLabel = new JLabel("resolved issues: " + resolvedIssues.size());
        JLabel closedIssuesLabel = new JLabel("closed issues: " + closedIssues.size());
        JLabel reopenedIssuesLabel = new JLabel("reopened issues: " + reopenedIssues.size());

        statisticByIssueStatus.add(newIssuesLabel);
        statisticByIssueStatus.add(assignedIssuesLabel);
        statisticByIssueStatus.add(fixedIssuesLabel);
        statisticByIssueStatus.add(resolvedIssuesLabel);
        statisticByIssueStatus.add(closedIssuesLabel);
        statisticByIssueStatus.add(reopenedIssuesLabel);



        statisticByIssueStatus.setPreferredSize(new Dimension(400, 200));
        statisticByIssueStatus.setMaximumSize(new Dimension(400, 200));
        statisticByIssueStatus.setMinimumSize(new Dimension(400, 200));


        statisticByIssueStatus.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "Issues by Status"));

        JScrollPane statisticByIssueStatusWithScroll = new JScrollPane(statisticByIssueStatus);
        statisticByIssueStatusWithScroll.setVerticalScrollBar(new JScrollBar());


        return statisticByIssueStatusWithScroll;
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

    ArrayList<Issue> issuesByDev(String userName){
        ArrayList<Issue> newIssues = new ArrayList<>();
        for(int i = 0; i <issueList.size(); i++){
            if(issueList.get(i).getFixer() != null && issueList.get(i).getFixer().equals(userName)){
                newIssues.add(issueList.get(i));
            }
        }
        return newIssues;
    }

    JScrollPane statisticByDev(){
        JPanel statisticByDev = new JPanel();
        statisticByDev.setLayout(new BoxLayout(statisticByDev, BoxLayout.Y_AXIS));


        Statement devStatement;
        try {
            String devQuery = "select name from account where category = 'dev'";
            String url = "jdbc:mysql:aws://sedb.cf866m2eqkwj.us-east-1.rds.amazonaws.com/sedb";
            String serverUserName = "admin";
            String serverPassword = "00000000";
            Connection connection;
            connection = DriverManager.getConnection(url, serverUserName, serverPassword);//디비에 연결

            devStatement = connection.createStatement();
            ResultSet rs = devStatement.executeQuery(devQuery);//실행


            while(rs.next()){
                ArrayList<Issue> issuesByDev = issuesByDev(rs.getString("name"));
                JLabel devLabel = new JLabel(rs.getString("name") + ": " + issuesByDev.size());
                statisticByDev.add(devLabel);
            }


            devStatement.close();
            connection.close();


        } catch (SQLException ex) {
            throw new RuntimeException(ex);

    }

        statisticByDev.setPreferredSize(new Dimension(400, 200));
        statisticByDev.setMaximumSize(new Dimension(400, 200));
        statisticByDev.setMinimumSize(new Dimension(400, 200));

        statisticByDev.setBorder(new TitledBorder(new LineBorder(Color.BLACK, 2), "Fixed number by devs"));


        JScrollPane statisticByDevWithScroll = new JScrollPane(statisticByDev);
        statisticByDevWithScroll.setVerticalScrollBar(new JScrollBar());

        return statisticByDevWithScroll;
    }

    ArrayList<Issue> issuesByDate(LocalDate date){
        ArrayList<Issue> newIssues = new ArrayList<>();
        for(int i = 0; i <issueList.size(); i++){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(issueList.get(i).getDate(), formatter);
            if(dateTime.toLocalDate().equals(date)){
                newIssues.add(issueList.get(i));
            }


        }
        return newIssues;
    }

    int returnnum(LocalDate date){
        ArrayList<Issue> issues = issuesByDate(date);
        int amountLabel = issues.size();
        if(amountLabel == 0){
            return -1;
        }
        else return 1;
    }

    JPanel statisticByDate(LocalDate date){
        ArrayList<Issue> issues = issuesByDate(date);
        JPanel statisticByDate = new JPanel(new BorderLayout());

        JLabel dateLabel = new JLabel(String.valueOf(date));
        JLabel amountLabel = new JLabel(String.valueOf(issues.size()));

        statisticByDate.add(dateLabel,BorderLayout.NORTH);

        statisticByDate.add(amountLabel,BorderLayout.CENTER);

        return statisticByDate;
    }
}
