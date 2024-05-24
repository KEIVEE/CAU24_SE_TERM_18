import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Issue {
    private String projectName;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String date;
    private String reporter;
    private String assignee;
    private String fixer;
    private ArrayList<Comment> comments;

    public Issue(String projectName,String title, String description, Status status, Priority priority, String reporter){
        this.projectName = projectName;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //지금 시간 저장.
        this.reporter = reporter;
        this.assignee = null;
        this.fixer = null;
        this.comments = new ArrayList<>();
    }

    public Issue(String projectName,String title, String description, Status status, Priority priority, String date, String reporter, String assignee, String fixer, ArrayList<Comment> comments){
        this.projectName = projectName;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.date = date;
        this.reporter = reporter;
        this.assignee = assignee;
        this.fixer = fixer;
        this.comments = comments;
    }

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
}
