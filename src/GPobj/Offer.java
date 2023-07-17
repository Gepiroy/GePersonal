package GPobj;

import java.util.ArrayList;
import java.util.List;

import GePersonal.main;

public class Offer {
	public int type=0;
	public String id;
	public String lore="";
	public String by;
	public String to=null;
	public List<Ans> answers=new ArrayList<>();
	
	public Offer(String Id){
		id=Id;
		if(id.charAt(0)=='R')type=1;
		if(id.charAt(0)=='B')type=2;
		if(id.charAt(0)=='O')type=3;
		lore=main.offersLog.conf.getString("list."+id+".lore");
		by=main.offersLog.conf.getString("list."+id+".by");
		if(type==1)to=main.offersLog.conf.getString("list."+id+".to");
		for(String st:main.offersLog.getKeys("list."+id+".ans")){
			Ans ans=new Ans(
					st,
					main.offersLog.conf.getString("list."+id+".ans."+st+".num"),
					main.offersLog.conf.getString("list."+id+".ans."+st+".comment"));
			answers.add(ans);
		}
	}
}
