package GPobj;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class TestInfo {
	public String name;
	public String need;
	public String codename;
	public TestInfo(){}
	public TestInfo(File file){
		FileConfiguration conf=YamlConfiguration.loadConfiguration(file);
		name=conf.getString("Name");
		need=conf.getString("Need");
		if(need!=null&&need.equals("null"))need=null;
		codename=file.getName().substring(0, file.getName().length()-4);
	}
}
