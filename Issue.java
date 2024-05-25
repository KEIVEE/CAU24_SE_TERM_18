import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Issue { //이슈는
    private String projectName;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String date;
    private String shortDate; //sql 에 id에 쓸 것.
    private String reporter;
    private String assignee;
    private String fixer;
    private ArrayList<Comment> comments;
    /*
    디비에서와 달리 새로 생기는 어트리뷰트가 두개가 있는데:

    이슈의 id를 줄이기 위한 shortDate. date 는 yyyy-MM-dd HH:mm:ss 형식으로 저장되지만(디비의 DATETIME 형식을 따르기 위해서)
    shortDate 는 yyyyMMddHHmmss 형식으로 저장된다.

    그리고 그 이슈의 커멘트들을 저장할 comments 라는 어레이리스트.
     */

    public Issue(String projectName,String title, String description, Status status, Priority priority, String reporter){
        this.projectName = projectName;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //지금 시간 저장.
        this.shortDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); //지금 시간 저장.
        this.reporter = reporter;
        this.assignee = null;
        this.fixer = null;
        this.comments = new ArrayList<>();
    }
    //테스터가 새로 이슈를 만들 때 쓰는 생성자. 날짜는 지금 시간을 저장하고, 어사이니 픽서 커멘트는 비어있다.

    public Issue(String projectName,String title, String description, Status status, Priority priority, String date, String reporter, String assignee, String fixer, ArrayList<Comment> comments){
        this.projectName = projectName;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.date = date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        this.shortDate = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        this.reporter = reporter;
        this.assignee = assignee;
        this.fixer = fixer;
        this.comments = comments;
    }
    //디비에 이미 있는 이슈를 불러와서 이슈 어레이리스트(GuiTest 에 있는)에 넣을 때 쓰는 생성자.

    public String getTitle(){
        return title;
    }

    public String getFixer() {
        return fixer;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }

    public String getReporter() {
        return reporter;
    }

    public String getDate() {
        return date;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public String getShortDate() {
        return shortDate;
    }
}
