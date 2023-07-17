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
			TextUtil.mes(p, main.pref, "����� &9��������&f: &a/become &ehelper");
			TextUtil.mes(p, main.pref, "����� &6��������&f: &a/become &ebuilder");
		}else{
			if(args[0].equalsIgnoreCase("helper")){
				GPinfo pi=Events.plist.get(p.getName());
				boolean can=true;
				TextUtil.mes(p, main.pref, "�������� ���������� ����� ��������...");
				if(!pi.conf.conf.get("Status").equals("Player")){
					TextUtil.mes(p, main.pref, "�� ��� ��������� ������ ���������.");
					return true;
				}
				Conf global=new Conf(main.is.path+"/global.yml");
				if(global.getKeys("Become.helpers").contains(p.getName())){
					TextUtil.mes(p, main.pref, "�� ��� ��������� ������ ���������.");
					return true;
				}
				if(pi.conf.conf.contains("Tests.personal")){
					TextUtil.mes(p, main.pref, "�������� &6������������ &f���� &a�������!");
				}else{
					TextUtil.mes(p, main.pref, "�������� &6������������ &f���� &c�� �������!");
					can=false;
				}
				if(pi.conf.conf.contains("Tests.helper")){
					TextUtil.mes(p, main.pref, "�������� ���� &6������� &a�������!");
				}else{
					TextUtil.mes(p, main.pref, "�������� ���� &6������� &c�� �������!");
					can=false;
				}
				int played=pi.conf.conf.getInt("mins");
				int need=300;
				TextUtil.mes(p, main.pref, "�������� &"+GepUtil.boolString("a", "c", played>=need)+played+"&f/"+need+" �����.");
				if(played<need)can=false;
				if(can){
					List<String> becomers=global.getKeys("Become.helpers");
					becomers.add(p.getName());
					global.conf.set("Become.helpers", becomers);
					global.save();
					TextUtil.mes(p, main.pref, "&a�����������! &f���� ������ ����������. ����� ����������� ����� �������, �� ������� &b����� ���������� &f� �������� � ���� �� ������, ��������� � &6������������&f �����.");
					TextUtil.mes(p, main.pref, "���� �� ���������, ��� �� �� ������ ���� �������� ��� ���� ������ ������ �����������, ��� ����� �������.");
					TextUtil.mes(p, main.pref, "&c�����! &f���� ��� �� ���� �� ������� ����� &e36 �����&f, ���� ������ &6����� �������&f �������������. ����� ��������� � �������, &b/become helper&f ������� ��� �� ���� �, ��� � ����������, ����� ������ ������.");
				}
				TextUtil.mes(p, main.pref, "&e/gepers help helper &f��� ��������� ���. ���������� |(��� �������� �����, ��� ��������� ������ � ��� ������ �����)");
			}
			if(args[0].equalsIgnoreCase("helperLIST")){
				TextUtil.mes(p, main.pref, "������ ������ �� ����������:");
				Conf global=new Conf(main.is.path+"/global.yml");
				for(String st:global.getKeys("Become.helpers")){
					p.sendMessage(TextUtil.string("- &b"+st));
				}
			}
		}
		return true;
	}
}
