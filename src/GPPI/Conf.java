package GPPI;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import GPUtils.GepUtil;

public class Conf {
	public File file;
	public FileConfiguration conf;
	public Conf(String path){
		file=new File(path);
		conf=YamlConfiguration.loadConfiguration(file);
	}
	public void save(){
		try {
	        conf.save(file);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void setLoc(String st, Location loc){
		GepUtil.saveLocToConf(conf, st, loc);
	}
	public Location getLoc(String st){
		return GepUtil.getLocFromConf(conf, st);
	}
	public List<String> getKeys(String section){
		List<String> ret=new ArrayList<>();
		if(!conf.contains(section))return ret;
		else{
			for(String st:conf.getConfigurationSection(section).getKeys(false)){
				ret.add(st);
			}
			return ret;
		}
	}
	public void add(String where, int am){
		int now=0;
		if(conf.contains(where))now=conf.getInt(where);
		conf.set(where, now+am);
	}
	public List<String> getStringList(String section){
		List<String> ret=conf.getStringList(section);
		if(ret==null)ret=new ArrayList<>();
		return ret;
	}
	public boolean StringEqual(String where, String equal){
		if(!conf.contains(where))return false;
		else if(conf.getString(where)==null)return false;
		else if(conf.getString(where).equals(equal))return true;
		else return false;
	}
	public void setHashMap(String where, HashMap<String, Integer> hm){
		for(String st:hm.keySet()){
			conf.set(where+"."+st, hm.get(st));
		}
	}
	public HashMap<String,Integer> getHashMap(String where){
		HashMap<String,Integer> ret = new HashMap<>();
		for(String st:getKeys(where)){
			ret.put(st, conf.getInt(where+"."+st));
		}
		return ret;
	}
}
