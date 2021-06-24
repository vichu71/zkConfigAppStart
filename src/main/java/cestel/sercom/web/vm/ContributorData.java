package cestel.sercom.web.vm;

import java.util.ArrayList;
import java.util.List;

public class ContributorData {
	 
    private List<String> titles = new ArrayList<String>();
    private List<Contributor> contributors = new ArrayList<Contributor>();
  
    public ContributorData() {
        titles.add("Code");
        titles.add("Bug");
        titles.add("Docs");
        titles.add("Arts");
 
        contributors.add(new Contributor("Kaleb", "Leonel", titles.get(0).toString(), 321));
        contributors.add(new Contributor("Balu", "Haben", titles.get(0).toString(), 321));
        contributors.add(new Contributor("Trey", "Wyatt", titles.get(0).toString(), 323));
       
         
      
    }
 
    public List<String> getTitles() {
        return titles;
    }
 
    public List<Contributor> getContributors() {
        return contributors;
    }

 
}