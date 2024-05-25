import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {
    private String content;
    private String userName;
    private String date;
    private String shortDate;
    //디비와 달리 어트리뷰트가 하나 적다: 이슈 아이디와 본인 아이디가 없고 shortDate 가 추가되었다
    //커멘트는 이슈 객체의 어트리뷰트이기 때문에 이미 어떤 이슈 안에 있어서 이슈 아이디가 필요없다.
    //이슈에서처럼 본인 아이디도 자바 파일 안에서는 필요가 없다. 본인 아이디는(커멘트의 아이디) 디비에서 커멘트끼리의 확실한 구분을 위한 것이다
    //커멘트 아이디의 길이를 줄이기 위한 shortDate 도 존재한다. 이슈 아이디와 커멘트 아이디는 겹치는 일이 없게 하기 위해 시간을 활용한다.
    public Comment(String content,String userName){
        this.content = content;
        this.userName = userName;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); //지금 시간 저장.
        this.shortDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")); //지금 시간 저장.
    } //데브가 커멘트를 추가할 때 생성자.





    public Comment(String content, String userName, String date){
        this.content = content;
        this.userName = userName;
        this.date = date;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        this.shortDate = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }//이미 디비에 있는 것을 불러와서 이슈 안에다가 저장할 때 쓰는 생성자.

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public String getDate() {
        return date;
    }

    public String getShortDate() {
        return shortDate;
    }
}
