package cmds;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import GPPI.GPinfo;
import GPUtils.GepUtil;
import GPUtils.TextUtil;
import GPobj.BanInfo;
import GePersonal.Events;
import GePersonal.main;

public class ban implements CommandExecutor{
	String pref="&6Ge&bPersonal";
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player){
			Player p=(Player) sender;
			if(!p.hasPermission("gepers.helper")){
				TextUtil.mes(p, pref, "�� �� ������ ������. &a������ ����� &b��������&a? &f��������� ������� &e/become");
				return true;
			}
		}
		if(args.length<4){
			TextUtil.mes(sender, pref, "&a/ban &"+GepUtil.boolString("a", "6", args.length>0)+"<���> &"+GepUtil.boolString("a", "6", args.length>1)+"<����> &"+GepUtil.boolString("a", "6", args.length>2)+"[�������]");
			return true;
		}
		int t=main.timeFormat(args[1]);
		if(t==-1){
			TextUtil.mes(sender, pref, "|����� ������� ����� ����� ����� ��� ������������ &b������ ������: &e1h &b- 1 ��� |(60 �����); &e1d &b- 1 ���� |(1440 �����). &b�� ����� ������� ����� &e30 ���� |(60d = 2 ������).");
			return true;
		}
		GPinfo tpi;
		Player target=Bukkit.getPlayer(args[0]);
		if(target!=null){
			tpi=Events.plist.get(args[0]);
		}else if(main.is.PIexists(args[0])){
			tpi=new GPinfo(args[0]);
		}else{
			TextUtil.mes(sender, pref, "������ ��� � ���� ������.");
			return true;
		}
		String reason="";
		for(int i=2;i<args.length;i++){
			if(i>2)reason+=" ";
			reason+=args[i];
		}
		BanInfo ban=new BanInfo(new Date().getTime(),sender.getName(),t*60,reason);
		tpi.addBan(ban.by, t, reason);//save conf � ��� �����
		if(target!=null){
			ban.kick(target);
		}
		TextUtil.globMessage("&c���", "������ &e"+args[0]+"������� &6"+sender.getName()+"&f �� &6"+ban.remain()+"&f �� ������� &e"+reason+"&f.", Sound.BLOCK_ANVIL_PLACE, 2, 0, null, null, 0, 0, 0);
		GPinfo pi=Events.plist.get(sender.getName());
		if(pi!=null){
			pi.addHelPoints(10);
		}
		return true;
	}
	
}
