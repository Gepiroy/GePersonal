package cmds;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import GPPI.GPinfo;
import GPUtils.TextUtil;
import GPobj.Test;
import GPobj.TestInfo;
import GePersonal.Events;
import GePersonal.main;

public class geptest implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Only for players. &cPOSHEL VON!");
			return true;
		}
		Player p=(Player) sender;
		GPinfo pi=Events.plist.get(p.getName());
		if(args.length==0){
			TextUtil.mes(p, main.pref, "Загрузка доступных тестов...");
			new BukkitRunnable() {
				@Override
				public void run() {
					File folder=new File(main.is.path+"/Tests");
					List<TestInfo> completed=new ArrayList<>();
					List<TestInfo> avaliable=new ArrayList<>();
					List<TestInfo> soon=new ArrayList<>();
					List<TestInfo> unk=new ArrayList<>();
					for(File f:folder.listFiles()){
						if(f.length()==0){
							continue;
						}
						TestInfo ti=new TestInfo(f);
						if(!pi.conf.conf.contains("Tests."+f.getName())){
							if(ti.need!=null&&!pi.conf.conf.contains("Tests."+ti.need)){
								unk.add(ti);
							}
							avaliable.add(ti);
							TextUtil.mes(p, main.pref, "&b'&f"+ti.name+"&b' &f-"+" &a/geptest &e"+ti.codename);
						}else{
							completed.add(ti);
						}
					}
					for(TestInfo ti:unk){
						for(TestInfo tia:avaliable){
							if(ti.need.equals(tia.codename)){
								soon.add(ti);
								break;
							}
						}
					}
					if(completed.size()>0){
						p.sendMessage(TextUtil.string("&aПройденные &fтесты:"));
						for(TestInfo ti:completed){
							p.sendMessage(TextUtil.string("- "+ti.name+" |(&e"+ti.codename+"|)"));
						}
					}
					if(avaliable.size()>0){
						p.sendMessage(TextUtil.string("&bДоступные &fтесты:"));
						for(TestInfo ti:avaliable){
							p.sendMessage(TextUtil.string("- "+ti.name+" |(&e"+ti.codename+"|)"));
						}
						p.sendMessage(TextUtil.string("&a/geptest &e<код> &fдля прохождения."));
					}else{
						p.sendMessage("Доступных тестов нет :(");
						return;
					}
					if(soon.size()>0){
						p.sendMessage(TextUtil.string("&eСкоро откроются &fтесты:"));
						for(TestInfo ti:soon){
							p.sendMessage(TextUtil.string("- "+ti.name+" |(&e"+ti.codename+"|, требуется &c"+ti.need+"|)"));
						}
					}
				}
			}.runTaskAsynchronously(main.instance);
		}else{
			File file=new File(main.is.path+"/Tests/"+args[0]+".yml");
			if(file.exists()){
				if(pi.conf.conf.contains("Tests."+args[0])){
					TextUtil.mes(p, main.pref, "Вы уже прошли этот тест.");
					return true;
				}
				Test test=new Test(args[0]);
				String need=test.conf.getString("Need");
				if(need!=null&&!pi.conf.conf.contains("Tests."+need)){
					TextUtil.mes(p, main.pref, "Для этого теста необходимо пройти "+test.conf.getString("Name"));
					return true;
				}
				pi.test=new Test(args[0]);
				pi.test.talkAbout(p);
			}else{
				TextUtil.mes(p, main.pref, "Данного теста не существует.");
			}
		}
		return true;
	}
}
