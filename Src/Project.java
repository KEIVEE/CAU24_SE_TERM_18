import java.util.ArrayList;

public class Project { //프로젝트는
    private ArrayList<String> names = new ArrayList<>();//프로젝트 이름만 모아놓은 것이다

    public Project(ArrayList<String> name ){
        this.names = name;
    }

    public Project(){

    }

    public int getSize(){
        return names.size();
    } //프로젝트 개수

    public ArrayList<String> getNames() {
        return names;
    }

    public void add(String name){
        this.names.add(name);
    } //프로젝트 추가: 있는 프로젝트를 불러오는 파일에서 사용함.

    public String getName(int i){
        return names.get(i);
    } //~번째 프로젝트를 불러옴
}
