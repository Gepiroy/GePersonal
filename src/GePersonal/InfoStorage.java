package GePersonal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import GPPI.Conf;
import GPUtils.GepUtil;
import GPobj.ChinInfo;

public class InfoStorage {
	
	//Менеджер для всей информации о игроке через файлы и межсерверную связь.
	public String path=main.instance.getDataFolder()+"/GePersInfo";
	public boolean isMain=false;
	
	public HashMap<String,ChinInfo> helpChins=new HashMap<>();
	
	public InfoStorage(){
		File file=new File(main.instance.getDataFolder()+"/isconf.yml");
		FileConfiguration conf=YamlConfiguration.loadConfiguration(file);
		if(!file.exists()){
			conf.set("Path", path);
			conf.set("isMain", false);
			GepUtil.saveCfg(conf, file);
		}
		path=conf.getString("Path");
		if(conf.contains("isMain"))isMain=conf.getBoolean("isMain");
		Conf chc = new Conf(path+"/chins");
		if(chc.conf.contains("Help")){
			for(String st:chc.getKeys("Help")){
				helpChins.put(st, new ChinInfo(st,chc.conf.getString("Help."+st+".name"), chc.getStringList("Help."+st+".lore"), chc.conf.getInt("Help."+st+".price"), chc.getHashMap("Help."+st+".needs"), chc.conf.getInt("Help."+st+".slot"), Material.valueOf(chc.conf.getString("Help."+st+".mat"))));
			}
		}else{
			chc.conf.set("Help.helper.name", "Хелпер");
			List<String> lore=new ArrayList<>();
			lore.add("Добавляет базовые способности хелпера");
			lore.add("и возможность расти, получая новые чины.");
			chc.conf.set("Help.helper.lore", lore);
			chc.conf.set("Help.helper.price", -1);
			HashMap<String,Integer> needs=new HashMap<>();
			needs.put("example", 1337);
			chc.conf.set("Help.helper.needs", needs);
			chc.conf.set("Help.helper.slot", 13);
			chc.conf.set("Help.helper.mat", "IRON_DOOR");
			chc.save();
		}
	}
	public boolean PIexists(String plname){
		File file=new File(path+"/players/"+plname+".yml");
		return file.exists();
	}
}
