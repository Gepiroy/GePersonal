package GPUtils;

import java.util.HashMap;
import java.util.regex.Pattern;

public class ChatManager {
	char[] dots={'.',',','!','?'};
	public HashMap<String,String> replaces=new HashMap<>();
	
	public ChatManager(){
		replaces.put("хуй", "фиг");
		replaces.put("хуя", "фига");
	}
	
	public String repl(String in){
		String ret=in;
		//Автозамена всех слов
		//Поиск капса (>50%)
		//Починка кривых точек и заглавных букаф
		//
		//
		for(String key:replaces.keySet()){
			TextUtil.sdebug("detecting "+key);
			ret.replaceAll("(?i)"+Pattern.quote(key), Pattern.quote(replaces.get(key)));
		}
		TextUtil.sdebug("replaced");
		int low=0,up=0;
		for(int i=0;i<in.length();i++){
			char c=in.charAt(i);
			if(Character.isLetter(c)){
				if(Character.isLowerCase(c))low++;
				else up++;
			}
		}
		if(up>low)in.toLowerCase();
		TextUtil.sdebug("capsfixed");
		String pret="";
		boolean big=false;
		for(int i=0;i<in.length();i++){
			char c=in.charAt(i);
			if(c==' '&&in.length()<i+1&&charCont(dots,in.charAt(i+1))){
				pret+=". ";
				i++;
				big=true;
			}else if(big&&!charCont(dots, c)&&in.length()<i+1&&!charCont(dots,in.charAt(i+1))){
				pret+=(""+c).toUpperCase();
				big=false;
			}else pret+=c;
			if(c=='.'||c=='?'||c=='!'&&!big)big=true;
		}
		TextUtil.sdebug("doted and upped.");
		ret=pret;
		return ret;
	}
	boolean charCont(char[] array, char c){
		for(char d:array){
			if(c==d)return true;
		}
		return false;
	}
}
