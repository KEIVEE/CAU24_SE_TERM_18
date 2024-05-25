import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {
    private String content;
    private String userName;
    private String date;
    private String shortDate;

    public Comment(String content,String userName){
        this.content = content;
        this.userName = userName;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //지금 시간 저장.
        this.shortDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); //지금 시간 저장.
    }

    public String getShortDate() {
        return shortDate;
    }

    public Comment(String content, String userName, String date){
        this.content = content;
        this.userName = userName;
        this.date = date; //지금 시간 저장.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        this.shortDate = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
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
