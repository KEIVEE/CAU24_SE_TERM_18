import java.util.ArrayList;

public class Browse {
    private ArrayList<Issue> issues;
    public Browse(ArrayList<Issue> issues){
        this.issues = issues;
    }

    public ArrayList<Issue> browseAll(){
        return issues;
    }
}