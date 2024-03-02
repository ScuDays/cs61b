package gitlet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BranchPointer extends Pointer{
    private ArrayList<String> CommitList;
    public BranchPointer(String CurrentLocation, String Pointer_Name) {
        super(CurrentLocation, Pointer_Name);
        CommitList = new ArrayList<>();
    }
    public void add(String CommitSha1Name){
        this.CommitList.add(CommitSha1Name);
        this.setCurrentLocation(CommitSha1Name);
    }

}
