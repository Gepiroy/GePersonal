package cmds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import GPPI.GPinfo;
import GPUtils.TextUtil;
import GePersonal.Events;
import GePersonal.main;

public class report implements CommandExecutor{
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player))return true;
		Player p=(Player) sender;
		GPinfo pi=Events.plist.get(p.getName());
		if(pi.fastTimers.containsKey("reported")){
			TextUtil.mes(p, main.pref, "&bВоу, полегче! &fСледующий запрос вы сможете подать через &e"+(pi.fastTimers.get("reported")/10)+" секунд.");
			return true;
		}
		String st=null;
		String lore = null;
		if(label.equalsIgnoreCase("report")){
			if(args.length==0){
				TextUtil.mes(p, main.pref, "/&areport &e<Ник> <Описание нарушения>");
			}else if(args.length==1){
				TextUtil.mes(p, main.pref, "/&areport "+args[0]+" &e<Описание нарушения>");
			}else{
				//Player target=Bukkit.getPlayer(args[0]);
				if(!main.is.PIexists(args[0])){
					TextUtil.mes(sender, main.pref, "Игрока нет в базе данных.");
					return true;
				}
				int id=main.offers.conf.getInt("Id.Report");
				st="list.R"+id;
				List<String> list=main.offers.conf.getStringList("list");
				if(list==null)list=new ArrayList<>();
				list.add(st);
				main.offers.conf.set("list", list);
				lore=args[1];
				for(int i=2;i<args.length;i++){
					lore+=" "+args[i];
				}
				main.offersLog.conf.set(st+".to", args[0]);
				TextUtil.mes(p, main.pref, "&bРепорт отправлен&f. Вскоре администрация его рассмотрит и проверит этого игрока.");
			}
		}else if(label.equalsIgnoreCase("bug")){
			if(args.length==0){
				TextUtil.mes(p, main.pref, "/&abug &e<Описание бага>");
			}else{
				int id=main.offers.conf.getInt("Id.Bug");
				st="list.B"+id;
				lore=args[0];
				for(int i=1;i<args.length;i++){
					lore+=" "+args[i];
				}
				main.offers.conf.set("Id.Bug", id+1);
				TextUtil.mes(p, main.pref, "&bИнформация о баге отправлена&f. Вскоре администрация его проверит. Просим не багоюзить: если это влияет на баланс игры или раздражает игроков, вы будете наказаны!");
			}
		}else if(label.equalsIgnoreCase("offer")){
			if(args.length==0){
				TextUtil.mes(p, main.pref, "/&aoffer &e<Обращение>");
			}else{
				int id=main.offers.conf.getInt("Id.Offer");
				st="list.O"+id;
				lore=args[0];
				for(int i=1;i<args.length;i++){
					lore+=" "+args[i];
				}
				main.offers.conf.set("Id.Offer", id+1);
				TextUtil.mes(p, main.pref, "&bОбращение отправлено&f. Вскоре мы рассмотрим его.");
			}
		}
		if(st!=null){
			main.offersLog.conf.set(st+".when", new Date().getTime());
			main.offersLog.conf.set(st+".by", p.getName());
			main.offersLog.conf.set(st+".to", args[0]);
			if(lore==null)lore="Игрок решил не оставлять объяснений.";
			main.offersLog.conf.set(st+".lore", lore);
			main.offersLog.save();
			main.offers.save();
			pi.fastTimers.put("reported", 1500);
		}
		return true;
	}

}
