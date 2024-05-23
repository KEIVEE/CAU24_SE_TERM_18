import java.util.ArrayList;

public class Project {
    private ArrayList<String> names = new ArrayList<>();

    public Project(ArrayList<String> name ){
        this.names = name;
    }

    public Project(){

    }

    public int getSize(){
        return names.size();
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public void add(String name){
        this.names.add(name);
    }

    public String getName(int i){
        return names.get(i);
    }
}
