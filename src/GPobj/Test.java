package GPobj;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import GPUtils.GepUtil;
import GPUtils.TextUtil;
import GePersonal.main;

public class Test {
	public String codeName;
	public List<String> ans=new ArrayList<>();
	public String ansType="num";//num-число,nums-числа,text-текст.
	public FileConfiguration conf;
	public Test(String f){
		File file=new File(main.is.path+"/Tests/"+f+".yml");
		if(!file.exists()){
			TextUtil.debug("&4FILE NOT EXIST!!!!");
		}
		conf=YamlConfiguration.loadConfiguration(file);
		codeName=file.getName();
		TextUtil.debug("test loaded");
	}
	public String getQ(int me){
		int num=0;
		for(String qst:conf.getConfigurationSection("Q").getKeys(false)){
			if(num==me){
				return "Q."+qst;
			}
			num++;
		}
		TextUtil.debug("&cReturning &4NULL! &7(getQ)");
		return null;
	}
	public void update(){
		String q=getQ(ans.size());
		ansType=conf.getString(q+".AnsType");
	}
	public boolean talkAbout(Player p){
		String q=getQ(ans.size());
		if(q!=null){
			p.sendMessage(TextUtil.string("&6---===&7(&f"+conf.getString("Name")+"&7)&6===---"));
			TextUtil.mes(p, "&a?№"+(ans.size()+1), conf.getString(q+".Name"));
			if(ansType.equals("num")){
				TextUtil.mes(p, "&9i", "Для ответа &bвыберите &aодин &fиз &bвариантов&f:");
			}else if(ansType.equals("nums")){
				TextUtil.mes(p, "&bi", "Для ответа &bвыберите &aодин или несколько &fиз &bвариантов&f:");
			}else if(ansType.equals("text")){
				TextUtil.mes(p, "&bi", "Для ответа &bвведите в чат &aсообщение&f.");
			}else if(ansType.equals("word")){
				TextUtil.mes(p, "&bi", "Для ответа &bвведите в чат &aодно слово&f.");
			}
			if(conf.contains(q+".Ans"))for(String st:conf.getConfigurationSection(q+".Ans").getKeys(false)){
				TextUtil.mes(p, "&b"+st, conf.getString(q+".Ans."+st));
			}
			p.sendMessage(TextUtil.string("&6---===&7(&f"+conf.getString("Name")+"&7)&6===---"));
			return true;
		}else{
			return false;
		}
	}
	int getMaxNum(){
		String q=getQ(ans.size());
		if(!conf.contains(q+".Ans"))return 0;
		return conf.getConfigurationSection(q+".Ans").getKeys(false).size();
	}
	public String ans(String ans){
		if(ansType.equals("num")){
			int max=getMaxNum();
			if(ans.length()>1)return "long";
			if(!GepUtil.isInt(ans))return "int";
			int my=Integer.parseInt(ans);
			if(my<=0||my>max)return "range";
		}else if(ansType.equals("nums")){
			int max=getMaxNum();
			if((ans.length()+1)/2>max)return "long";
			if(!GepUtil.isInt(ans))return "int";
			for(int i=0;i<ans.length();i++){//1234
				char c=ans.charAt(i);
				int my=Integer.parseInt(c+"");
				if(my<=0||my>max)return "range";
			}
		}
		//Нужно чтобы первую часть теста прошёл - молодец, конец.
		//Вторую - к первой прибавилось.
		//Вторую часть вызывать отдельно надо, но через тот же вызов, что и 1.
		return null;
	}
}
