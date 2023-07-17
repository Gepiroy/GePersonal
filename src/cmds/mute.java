package cmds;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import GPPI.GPinfo;
import GPUtils.GepUtil;
import GPUtils.TextUtil;
import GPobj.punishInfo;
import GePersonal.Events;
import GePersonal.main;

public class mute implements CommandExecutor{
	String pref="&6Ge&bPersonal";
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player p=(Player) sender;
			if(!p.hasPermission("gepers.helper")){
				TextUtil.mes(p, pref, "Мутить могут только хелперы. &aХотите стать &bхелпером&a? &fПроверьте команду &e/become");
				return true;
			}
		}
		if(args.length<4){
			TextUtil.mes(sender, pref, "&a/mute &"+GepUtil.boolString("a", "6", args.length>0)+"<Ник> &"+GepUtil.boolString("a", "6", args.length>1)+"<Срок> &"+GepUtil.boolString("a", "6", args.length>2)+"[Причины]");
			return true;
		}
		int t=main.timeFormat(args[1]);
		if(t==-1){
			TextUtil.mes(sender, pref, "|Нужно указать целое число минут или использовать &bформат сроков: &e1h &b- 1 час |(60 минут); &e1d &b- 1 день |(1440 минут). &bЗа месяц принято брать &e30 дней |(60d = 2 месяца).");
			return true;
		}
		GPinfo tpi;
		Player target=Bukkit.getPlayer(args[0]);
		if(target!=null){
			tpi=Events.plist.get(args[0]);
		}else if(main.is.PIexists(args[0])){
			tpi=new GPinfo(args[0]);
		}else{
			TextUtil.mes(sender, pref, "Игрока нет в базе данных.");
			return true;
		}
		String reason="";
		for(int i=2;i<args.length;i++){
			if(i>2)reason+=" ";
			reason+=args[i];
		}
		
		punishInfo mute=new punishInfo(new Date().getTime(),sender.getName(),t*60,reason);
		tpi.addMute(mute.by, t, reason);//save conf и так здесь
		if(target!=null){
			target.sendTitle(ChatColor.AQUA+"ПОЗДРАВЛЯЕМ!", ChatColor.BLUE+"Вы получили мут!", 60, 60, 60);
		}
		TextUtil.globMessage("&cМут", "Игрока &e"+args[0]+"замутил &6"+sender.getName()+"&f на &6"+mute.remain()+"&f по причине &e"+reason+"&f.", Sound.BLOCK_FIRE_EXTINGUISH, 2, 0, null, null, 0, 0, 0);
		GPinfo pi=Events.plist.get(sender.getName());
		if(pi!=null){
			pi.addHelPoints(6);
		}
		return true;
	}
}
