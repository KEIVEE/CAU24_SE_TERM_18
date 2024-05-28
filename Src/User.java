public abstract class User { //유저. 추상 클래스
    //유저를 상속받는 클래스 중 어드민은 없음. 어드민이 쓰이지 않기 때문인데, 명목상으로라도 필요하면 추가바람
    private String userName;
    private String category;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

}
