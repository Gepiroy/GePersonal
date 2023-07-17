package GPobj;

import java.util.Date;

import org.bukkit.configuration.file.FileConfiguration;

import GPUtils.GepUtil;
import GPUtils.TextUtil;

public class punishInfo {
	public long when;
	public String by,reason;
	public int time;
	public int secRemain;
	public punishInfo(FileConfiguration conf, String st){
		when=conf.getLong(st+".when");
		by=conf.getString(st+".by");
		time=conf.getInt(st+".time");
		reason=conf.getString(st+".reason");
		secRemain=(int) (when/1000+time-(new Date().getTime()/1000));
	}
	public punishInfo(long When, String By, int Time, String Reason){
		when=When;
		by=By;
		time=Time;
		reason=Reason;
		secRemain=(int) (when/1000+time-(new Date().getTime()/1000));
	}
	public String remain(){
		//Сейчас, дата, срок
		//дата+срок=конец
		//Ост. время = конец-сейчас
		int sec=(int)(Math.abs((when/1000+time*60)-new Date().getTime()/1000));
		GepUtil.debug("h="+sec/60/60, null, "info");
		if(sec/86400>0)return TextUtil.days(sec/86400)+" "+TextUtil.hours(sec%86400/3600)+" "+TextUtil.minutes(sec%3600/60);
		return TextUtil.hours(sec%86400/3600)+" "+TextUtil.minutes(sec%3600/60)+" "+TextUtil.secundes(sec%60);
	}
}
