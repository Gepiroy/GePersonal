package GePersonal;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.Files;

import GPPI.Conf;
import GPPI.GPinfo;
import GPUtils.ChatManager;
import GPUtils.GepUtil;
import GPUtils.NFBmanager;
import GPUtils.TextUtil;
import GPobj.BanInfo;

public class main extends JavaPlugin{
	public static main instance;
	public static String pref="&6Ge&bPersonal";
	public static InfoStorage is;
	public static NFBmanager NFB=new NFBmanager();
	public static ChatManager cm=new ChatManager();
	HashMap<String,String> lang=new HashMap<>();
	HashMap<String,List<String>> lmes=new HashMap<>();
	public static Conf offers;
	public static Conf offersLog;
	public static Conf monipl;
	public void onEnable(){
		instance=this;
		is=new InfoStorage();
		offers=new Conf(is.path+"/offers.yml");
		offersLog=new Conf(main.is.path+"/offersLog.yml");
		monipl=new Conf(is.path+"/monitoring/players.yml");
		File lfile=new File(getDataFolder()+"/lang.yml");
		FileConfiguration lconf=YamlConfiguration.loadConfiguration(lfile);
		if(!lconf.contains("Words")){
			lconf.set("Words.MainPref", "&6Ge&bPersonal");
			lconf.set("Words.FullPref", "&6Gepiroy's &bPersonal");
			GepUtil.saveCfg(lconf, lfile);
		}
		if(!lconf.contains("Mes")){
			{
				List<String> mes=new ArrayList<>();
				mes.add("&7---===~&a(&cКОНСТИТУЦИЯ &6(&9полная&6)&a)&7~===---");
				mes.add("&b1. &fНет правил выше конституции.");
				mes.add("&b2. &9Gepiroy &fв праве изменить конституцию, &eно не&f пойти против уже существующей.");
				mes.add("&b3. &fВысший приоритет проекта - уникальность и демократичность.");
				mes.add("&b4. &fМы &cпротив враждебности &fи &cчёрной рекламы&f. &7(Не унижайте другие проекты и не распространяйте наш проект путём спама. То, что вы играете у нас - самое лучшее, что вы можете сделать для нашего продвижения!)");
				mes.add("&b5. &fКаждый волен свободно выражаться, &6НО&f исключительно в безобидной форме. &7(Подробнее в &a/rules constitution&7)");
				mes.add("&b6. &fИгроки, которые провоцируют недовольство среди других игроков, должны попадать в &9народный суд&f.");
				mes.add("&b7. &f&cЧитерство&f - самое &cтяжкое&f преступление. Игрок, обвинённый в читерстве, не может попасть в персонал. Снять обвинения в читерстве можно лишь в случае");
				
				mes.add("&7---===~&a(&cКОНСТИТУЦИЯ &6(&9полная&6)&a)&7~===---");
				lconf.set("Mes.constFull", mes);
			}{
				List<String> mes=new ArrayList<>();
				mes.add("&7---===~&a(&cКОНСТИТУЦИЯ&a)&7~===---");
				mes.add("&7Здесь представлены лишь основы, касающиеся игрового процесса, свода правил для игроков.");
				mes.add("&7Полную конституцию смотрите в &a/rules constitution &efull");
				mes.add("");
				mes.add("&b1.&f Важнейший приоритет проекта - уникальность и демократичность.");
				mes.add("   Т. е. ");
				mes.add("&b2.&f Изучите &bконституцию&f проекта. Правила - &eне так важны&f, но так же рекоммендуются к изучению для предотвращения разногласий.");
				mes.add("   &e/rules &6constitution &f- конституция.   &e/rules &6rules &f - свод правил по конституции.");
				mes.add("&7---===~&a(&bКак стать хелпером?&a)&7~===---");
				lconf.set("Mes.const", mes);
			}{
				List<String> mes=new ArrayList<>();
				mes.add("&7---===~&b(&6become&b)&7~===---");
				mes.add("&7|&fИнформация о становлении &9хелпером&f: &a/become &ehelper");
				mes.add("&7|&fИнформация о становлении &6билдером&f: &a/become &ebuilder");
				mes.add("&7---===~&b(&6become&b)&7~===---");
				lconf.set("Mes.become", mes);
			}{
				List<String> mes=new ArrayList<>();
				mes.add("&7---===~&a(&bКак стать хелпером?&a)&7~===---");
				mes.add("&7|&b1.&f Узнайте о том, как устроено хелперство у нас. &6Оно &cочень&6 отличается&f от других проектов!");
				mes.add("&7|   &a/become helper &eabout");
				mes.add("&7|&b2.&f Изучите &bконституцию&f проекта. Правила - &eне так важны&f, но так же рекоммендуются к изучению для предотвращения разногласий.");
				mes.add("&7|   &e/rules &6constitution &f- конституция.   &e/rules &6rules &f - свод правил по конституции.");
				mes.add("&7---===~&a(&bКак стать хелпером?&a)&7~===---");
				lconf.set("Mes.becomeHelper", mes);
			}
			GepUtil.saveCfg(lconf, lfile);
		}
		for(String st:lconf.getConfigurationSection("Words").getKeys(false)){
			lang.put(st, TextUtil.string(lconf.getString("Words."+st)));
		}
		Bukkit.getPluginManager().registerEvents(new Events(), this);
		Bukkit.getPluginCommand("ban").setExecutor(new cmds.ban());
		Bukkit.getPluginCommand("gepers").setExecutor(new cmds.gepers());
		Bukkit.getPluginCommand("become").setExecutor(new cmds.become());
		Bukkit.getPluginCommand("geptest").setExecutor(new cmds.geptest());
		Bukkit.getPluginCommand("report").setExecutor(new cmds.report());
		Bukkit.getPluginCommand("bug").setExecutor(new cmds.report());
		Bukkit.getPluginCommand("offer").setExecutor(new cmds.report());
		//Подгрузка классов для предотвращения NoClassDefFoundError
		new GPinfo();
		new TextUtil();
		new GepUtil();
		new BanInfo(0,null,0,null);
		
		for(Player p:Bukkit.getOnlinePlayers()){
			Events.doJoin(p);
		}
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			int secRate=0;
			@Override
			public void run() {
				secRate++;
				if(secRate%100==0){//Обновление раз в 10 секунд
					if(is.isMain){
						long day=new Date().getTime()/86400000;
						if(monipl.conf.contains("dayUp")){
							long monitDay=monipl.conf.getLong("dayUp");
							if(day>monitDay){
								TextUtil.sdebug("&6Saving monitoring for monipl...");
								new BukkitRunnable() {
									@Override
									public void run() {
										long started=new Date().getTime();
										File to=new File(is.path+"/monitoring/logs/players/players"+day+".yml");
										if(to.exists()){
											TextUtil.sdebug("&cFile TO exists!!! Cancelled.");
											return;
										}
										try {
											to.createNewFile();
										} catch (IOException e1) {
											e1.printStackTrace();
										}
										TextUtil.sdebug("&eSetting lost people's values...");
										for(File f:new File(is.path+"/players").listFiles()){
											FileConfiguration conf=YamlConfiguration.loadConfiguration(f);
											long lastOut=conf.getLong("LastLogout");
											int daysAgo=(int) ((new Date().getTime()-lastOut)/86400000);
											if(daysAgo>=3){
												if(daysAgo==3)monipl.add("offPlus", 1);
												if(daysAgo>=15){
													if(daysAgo==15)monipl.add("missPlus", 1);
													if(daysAgo>=30){
														if(daysAgo==30)monipl.add("dropPlus", 1);
														monipl.add("dropTotal", 1);
													}else{
														monipl.add("missTotal", 1);
													}
												}else{
													monipl.add("offTotal", 1);
												}
											}
										}
										monipl.save();
										TextUtil.sdebug("&eCopying...");
										try {
											Files.copy(monipl.file, to);
										} catch (IOException e) {
											e.printStackTrace();
										}
										if(monipl.file.delete()){
										    try {
												monipl.file.createNewFile();
												monipl.conf=YamlConfiguration.loadConfiguration(monipl.file);
											} catch (IOException e) {
												e.printStackTrace();
												TextUtil.sdebug("&cFILE NOT CREATED!");
											}
										}else{
										    TextUtil.sdebug("&cFILE NOT DELETED!");
										}
										TextUtil.sdebug("End. Taked "+(new Date().getTime()-started)+" ms for all.");
									}
								}.runTaskAsynchronously(instance);
							}
						}else{
							monipl.conf.set("dayUp", day);
							monipl.save();
						}
					}
				}
				for(String st:new ArrayList<>(Events.plist.keySet())){
					GPinfo pi=Events.plist.get(st);
					pi.update();
				}
			}
		}, 1, 2);
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			@Override
			public void run() {
				long now=new Date().getTime();
				for(Player p:Bukkit.getOnlinePlayers()){
					GPinfo pi=Events.plist.get(p.getName());
					if(now-pi.NFB.updated>10000&&pi.NFB.handling){
						pi.NFB.check(p);
					}
				}
			}
		}, 1, 1);
	}
	
	public void onDisable(){
		
		for(Player p:Bukkit.getOnlinePlayers()){
			//p.kickPlayer("disablyat");
			Events.doLeave(p);
		}
	}
	
	public static int timeFormat(String st){
		if(st.length()>4)return -1;
		int t=GepUtil.intFromString(st);
		if(t<=0){
			return -1;
		}
		if(st.charAt(st.length()-1)=='h'){
			return t*60;
		}
		else if(st.charAt(st.length()-1)=='d'){
			return t*60*24;
		}
		else{
			return t;
		}
	}
	
	public static String dateByLong(long l){
		Date when=new Date(l);
		DateFormat TIMESTAMP = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return TIMESTAMP.format(when);
	}
	
	public List<String> splitInfo(String st, String filter){
		List<String> ret=new ArrayList<>();
		for(int i=0;i<st.length();i++){
			char c;
			String word="";
			for(;i<st.length();i++){
				c=st.charAt(i);
				if(c=='(')break;
				word+=c;
			}
			if(!word.equals(filter)){
				for(;i<st.length()-1;){
					i++;
					c=st.charAt(i);
					if(c==')'){
						break;
					}
				}
				continue;
			}
			String sub="";
			for(;i<st.length()-1;){
				i++;
				c=st.charAt(i);
				if(c==')'){
					break;
				}
				sub+=c;
			}
			ret.add(sub);
		}
		return ret;
	}
	public String findInfo(String st, String filter){
		for(int i=0;i<st.length();i++){
			char c;
			String word="";
			for(;i<st.length();i++){
				c=st.charAt(i);
				if(c=='(')break;
				word+=c;
			}
			if(!word.equals(filter)){
				for(;i<st.length()-1;){
					i++;
					c=st.charAt(i);
					if(c==')'){
						break;
					}
				}
				continue;
			}
			String sub="";
			for(;i<st.length()-1;){
				i++;
				c=st.charAt(i);
				if(c==')'){
					break;
				}
				sub+=c;
			}
			return sub;
		}
		return null;
	}
	public boolean transContains(String codename){
		Conf conf=new Conf(is.path+"/lang.yml");
		return conf.conf.contains(codename);
	}
	public String trans(String codename){
		Conf conf=new Conf(is.path+"/lang.yml");
		if(conf.conf.contains(codename))return conf.conf.getString(codename);
		conf.conf.set(codename, codename);
		conf.save();
		return codename;
	}
	public List<String> transAr(String codename){
		Conf conf=new Conf(is.path+"/lang.yml");
		if(conf.conf.contains(codename))return conf.conf.getStringList(codename);
		//TextUtil.debug("conf.conf.contains(codename)? "+conf.conf.contains(codename));
		List<String> ret=new ArrayList<>();
		ret.add("unknown");
		ret.add("translate");
		ret.add("for codename");
		ret.add(codename);
		conf.conf.set(codename, ret);
		conf.save();
		return ret;
	}
}
