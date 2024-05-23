import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {
    private String content;
    private String userName;
    private String date;

    public Comment(String content,String userName, String date){
        this.content = content;
        this.userName = userName;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //지금 시간 저장.
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }
}
