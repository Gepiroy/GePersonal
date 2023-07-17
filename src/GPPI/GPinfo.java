package GPPI;

import java.util.Date;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import GPUtils.GepUtil;
import GPUtils.TextUtil;
import GPobj.BanInfo;
import GPobj.ChinInfo;
import GPobj.NoFastBreak;
import GPobj.Test;
import GPobj.punishInfo;
import GePersonal.main;

public class GPinfo {
	public String nick;
	public Conf conf;
	public Test test=null;
	public HashMap<String,Integer> fastTimers=new HashMap<>();
	public String ofArmor=null;
	public NoFastBreak NFB=new NoFastBreak();
	
	public GPinfo(){}
	
	public GPinfo(String plname){
		nick=plname;
		conf=new Conf(main.is.path+"/players/"+plname+".yml");
		if(!conf.file.exists()){
			long now=new Date().getTime();
			conf.conf.set("Reged", now);
			conf.conf.set("LastLogin", now);
			conf.conf.set("LastLogout", now);
			conf.conf.set("Status", "Player");
			conf.save();
		}
	}
	
	long minUpdated=0;
	int rate=0;
	public void update(){//1 сек.
		rate++;
		long now=new Date().getTime();
		if(minUpdated==0){
			minUpdated=now;
			TextUtil.debug(nick+" minup dropped");
		}
		if(now-minUpdated>=60*1000){
			int mins=conf.conf.getInt("mins")+1;
			conf.conf.set("mins", mins);
			int nowHour=(int) (now/1000/3600%24);
			int hm=conf.conf.getInt("HourlyActivity.H"+nowHour)+1;
			conf.conf.set("HourlyActivity.H"+nowHour, hm);
			if(haveChin("Helper.basic")){
				int helpmins=conf.conf.getInt("helpmins")+1;
				conf.conf.set("helpmins", helpmins);
			}
			minUpdated=now;
			conf.save();
			if(mins==10){
				main.monipl.add("New10", 1);
				main.monipl.save();
			}
			TextUtil.debug(nick+" updated minute");
		}
		for(String st:fastTimers.keySet()){
			GepUtil.HashMapReplacer(fastTimers, st, -1, true, false);
		}
	}
	public void addBan(String by, int time, String reason){
		//time in seconds.
		String st="Bans."+conf.getKeys("Bans").size();
		conf.conf.set(st+".by", by);
		conf.conf.set(st+".when", new Date().getTime());
		conf.conf.set(st+".time", time);
		conf.conf.set(st+".reason", reason);
		conf.save();
	}
	
	public void addMute(String by, int time, String reason){
		//time in seconds.
		String st="Mutes."+conf.getKeys("Mutes").size();
		conf.conf.set(st+".by", by);
		conf.conf.set(st+".when", new Date().getTime());
		conf.conf.set(st+".time", time);
		conf.conf.set(st+".reason", reason);
		conf.save();
	}
	
	public BanInfo checkBanned(){
		long now=new Date().getTime();
		if(conf.conf.contains("Bans")){
			for(String st:conf.conf.getConfigurationSection("Bans").getKeys(false)){
				long out=conf.conf.getLong("Bans."+st+".when")+conf.conf.getInt("Bans."+st+".time")*1000;
				if(now<out)return new BanInfo(conf.conf, "Bans."+st);
			}
		}
		return null;
	}
	public punishInfo checkMuted(){
		long now=new Date().getTime();
		if(conf.conf.contains("Mutes")){
			for(String st:conf.conf.getConfigurationSection("Mutes").getKeys(false)){
				long out=conf.conf.getLong("Mutes."+st+".when")+conf.conf.getInt("Mutes."+st+".time")*1000;
				if(now<out)return new punishInfo(conf.conf, "Mutes."+st);
			}
		}
		return null;
	}
	public int addHelPoints(int add){
		int have=conf.conf.getInt("Points.help")+add;
		conf.conf.set("Points.help", have);
		conf.save();
		return have;
	}
	public int getHelPoints(){
		return conf.conf.getInt("Points.help");
	}
	public boolean haveChin(String id){
		return conf.conf.contains("Chins."+id);
	}
	public void addChin(String id){
		conf.conf.set("Chins."+id+".when", new Date().getTime());
	}
	public void remChin(String id){
		conf.conf.set("Chins."+id, null);
	}
	public void chinGUI(Player p){
		Inventory inv = Bukkit.createInventory(null, 27, ChatColor.BLUE+"„ины");
		for(String id:main.is.helpChins.keySet()){
			ChinInfo chin=main.is.helpChins.get(id);
			inv.setItem(chin.slot, chin.createItem(this));
		}
	}
}
