package cmds;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import GPPI.Conf;
import GPPI.GPinfo;
import GPUtils.GepUtil;
import GPUtils.TextUtil;
import GePersonal.Events;
import GePersonal.main;

public class become implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("Only for players. &cPOSHEL VON!");
			return true;
		}
		Player p=(Player) sender;
		if(args.length==0){
			TextUtil.mes(p, main.pref, "Стать &9хелпером&f: &a/become &ehelper");
			TextUtil.mes(p, main.pref, "Стать &6билдером&f: &a/become &ebuilder");
		}else{
			if(args[0].equalsIgnoreCase("helper")){
				GPinfo pi=Events.plist.get(p.getName());
				boolean can=true;
				TextUtil.mes(p, main.pref, "Проверка готовности стать хелпером...");
				if(!pi.conf.conf.get("Status").equals("Player")){
					TextUtil.mes(p, main.pref, "Вы уже являетесь частью персонала.");
					return true;
				}
				Conf global=new Conf(main.is.path+"/global.yml");
				if(global.getKeys("Become.helpers").contains(p.getName())){
					TextUtil.mes(p, main.pref, "Вы уже являетесь частью персонала.");
					return true;
				}
				if(pi.conf.conf.contains("Tests.personal")){
					TextUtil.mes(p, main.pref, "Основной &6персональный &fтест &aпройден!");
				}else{
					TextUtil.mes(p, main.pref, "Основной &6персональный &fтест &cне пройден!");
					can=false;
				}
				if(pi.conf.conf.contains("Tests.helper")){
					TextUtil.mes(p, main.pref, "Основной тест &6хелпера &aпройден!");
				}else{
					TextUtil.mes(p, main.pref, "Основной тест &6хелпера &cне пройден!");
					can=false;
				}
				int played=pi.conf.conf.getInt("mins");
				int need=300;
				TextUtil.mes(p, main.pref, "Наиграно &"+GepUtil.boolString("a", "c", played>=need)+played+"&f/"+need+" минут.");
				if(played<need)can=false;
				if(can){
					List<String> becomers=global.getKeys("Become.helpers");
					becomers.add(p.getName());
					global.conf.set("Become.helpers", becomers);
					global.save();
					TextUtil.mes(p, main.pref, "&aПоздравляем! &fВаша заявка отправлена. Когда потребуются новые хелперы, мы выберим &bсамых подходящих &fи свяжемся с ними по данным, указанным в &6персональном&f тесте.");
					TextUtil.mes(p, main.pref, "Если мы посчитаем, что вы не можете быть хелпером или ваша заявка подана неправильно, она будет удалена.");
					TextUtil.mes(p, main.pref, "&cВажно! &fЕсли вас не было на сервере более &e36 часов&f, ваша заявка &6будет удалена&f автоматически. Чтобы проверить её наличие, &b/become helper&f сообщит вам об этом и, при её отсутствии, снова подаст заявку.");
				}
				TextUtil.mes(p, main.pref, "&e/gepers help helper &fдля получения доп. информации |(как повысить шансы, что предстоит делать и как пройти тесты)");
			}
			if(args[0].equalsIgnoreCase("helperLIST")){
				TextUtil.mes(p, main.pref, "Список заявок на хелперство:");
				Conf global=new Conf(main.is.path+"/global.yml");
				for(String st:global.getKeys("Become.helpers")){
					p.sendMessage(TextUtil.string("- &b"+st));
				}
			}
		}
		return true;
	}
}
