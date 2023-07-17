package cmds;

import java.util.Date;
import java.util.List;

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
import GPobj.Ans;
import GPobj.BanInfo;
import GPobj.Offer;
import GePersonal.Events;
import GePersonal.main;

public class gepers implements CommandExecutor{
	String pref="&6Ge&bPersonal";
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p=null;
		if(sender instanceof Player)p=(Player) sender;
		if(args.length==0){
			String border=TextUtil.string("-~=&f<&6Gepiroy's &bPERSONAL&f>|=~-");
			sender.sendMessage(border);
			sender.sendMessage(TextUtil.string("&a������: &f"+main.instance.getDescription().getVersion()));
			sender.sendMessage(TextUtil.string("&a��� ������ ������ &e/gepers list"));
			sender.sendMessage(border);
		}else{
			if(args[0].equalsIgnoreCase("list")){
				for(String st:main.instance.transAr("Gepers.list")){
					sender.sendMessage(TextUtil.string(st));
				}
			}
			if(args[0].equalsIgnoreCase("help")){
				if(args.length<2){
					for(String st:main.instance.transAr("Gepers.help.list")){
						sender.sendMessage(TextUtil.string(st));
					}
				}else{
					if(main.instance.transContains("Gepers.help."+args[1].toLowerCase())){
						for(String st:main.instance.transAr("Gepers.help."+args[1].toLowerCase())){
							sender.sendMessage(TextUtil.string(st));
						}
					}else{
						TextUtil.mes(sender, pref, "|�� ������� ���������� �� ������� &6"+args[1].toLowerCase());
					}
				}
			}else if(args[0].equalsIgnoreCase("info")){
				if(args.length<2){
					sender.sendMessage(TextUtil.string("&a/gepers info &e<���>"));
					return true;
				}
				if(!main.is.PIexists(args[1])){
					TextUtil.mes(sender, pref, "|������ ��� � ���� ������.");
					return true;
				}
				GPinfo pi=new GPinfo(args[1]);
				String border=TextUtil.string("-~=&f<&6"+args[1]+"&e's &cPERSONAL &bINFO&f>|=~-");
				sender.sendMessage(border);
				sender.sendMessage(TextUtil.string("&a���������������: &f"+main.dateByLong(pi.conf.conf.getLong("Reged"))));
				sender.sendMessage(TextUtil.string("&a� ��������� ��� �������: |"+TextUtil.times((int) ((new Date().getTime()-pi.conf.conf.getLong("LastLogin"))/1000))+" �����"));
				sender.sendMessage(TextUtil.string("&a����� ������: |"+pi.conf.conf.getInt("mins")+" ���."));
				
				sender.sendMessage(TextUtil.string("&6�������:"));
				for(String bst:pi.conf.getKeys("Bans")){
					BanInfo ban=new BanInfo(pi.conf.conf,"Bans."+bst);
					sender.sendMessage(TextUtil.string("&4���: &e"+main.dateByLong(ban.when)+" |("+TextUtil.times((int) ((new Date().getTime()-ban.when)/1000))+" �����, &e�� &6"+ban.by+"|)"));
					sender.sendMessage(TextUtil.string("   &c�������: &e"+ban.reason));
					sender.sendMessage(TextUtil.string("   &c����: &e"+TextUtil.times(ban.time*60)));
				}
				sender.sendMessage(TextUtil.string("����� ��������� � ������� ��������..."));
				for(String st:main.offersLog.getKeys("list")){
					if(main.offersLog.StringEqual("list."+st+".by",p.getName())){
						sender.sendMessage(TextUtil.string("&6"+st+" &fby &e"+p.getName()));
					}
					else if(main.offersLog.StringEqual("list."+st+".to",p.getName())){
						sender.sendMessage(TextUtil.string("&6"+st+" &cto &e"+p.getName()));
					}
				}
				sender.sendMessage(border);
			}
			else if(args[0].equalsIgnoreCase("offers")){
				List<String> oflist=main.offers.getStringList("list");
				if(args.length==1){
					if(oflist.size()==0){
						p.sendMessage("��� ��������.");
						return true;
					}
					String border=TextUtil.string("-~=&f<&b������� �������&f>|=~-");
					sender.sendMessage(border);
					for(String st:oflist){
						tellAboutOffer(sender, st, true);
					}
					sender.sendMessage(TextUtil.string("&a/gepers offers &e<id>&f, ����� &c������������� &f� &a����������� &f������."));
					sender.sendMessage(border);
					if(p!=null){
						p.playSound(p.getLocation(), Sound.UI_TOAST_IN, 2, 0);
					}
				}else{
					GPinfo pi=Events.plist.get(p.getName());
					if(args[1].length()==1){
						int num=GepUtil.intFromString(args[1]);
						if(pi.ofArmor==null){
							TextUtil.mes(p, main.pref, "�� ������ ������ �� �������������.");
							return true;
						}
						if(num==0){
							TextUtil.mes(p, main.pref, "�� ���������� ������������� ���� ������. ��� �����, ��� �������� ������� ������!");
							pi.addHelPoints(1);
							return true;
						}else{
							String st=args[1]+".modded."+p.getName();
							String comment="";
							if(args.length>2){
								comment=args[2];
								for(int i=3;i<args.length;i++){
									comment+=" "+args[i];
								}
							}
							boolean remove=false;
							boolean important=false;
							if(pi.ofArmor.charAt(0)=='R'){
								if(num==1){
									TextUtil.mes(p, main.pref, "������� �� ��������! ����� ��� ��������.");
									remove=true;
								}else if(num==2){
									TextUtil.mes(p, main.pref, "������� �� ��������! ������ ���-�� ������ ������� ����� ������.");
								}else if(num==3){
									TextUtil.mes(p, main.pref, "������� �� ��������! ���������� � ������� ���������.");
									remove=true;
								}else if(num==4){
									TextUtil.mes(p, main.pref, "�������, ��� ����������! ������ ������ ������ ������� ���� �����.");
								}else{
									TextUtil.mes(p, main.pref, "���������� ���� �������� �� ����������.");
									return true;
								}
							}else if(pi.ofArmor.charAt(0)=='B'){
								if(num==1){
									TextUtil.mes(p, main.pref, "���� ��� ���������, �� ����������� ������������ ����������, ����� �� ��������������� ��� ����� ������ ������� �� �������� �����! �� �� ���������� ���� �� ���� ����?");
									remove=true;
									important=true;
								}else if(num==2){
									TextUtil.mes(p, main.pref, "������� �� ��������! ���� ��� �������� � ��������� �������, � ���� ������ ��� ���������.");
									remove=true;
									important=true;
								}else if(num==3){
									TextUtil.mes(p, main.pref, "������� �� ��������! ���������� � �������/������ ���������.");
									remove=true;
								}else if(num==4){
									TextUtil.mes(p, main.pref, "�������, ��� ����������! ������ ������ ������ ������� ���� �����.");
								}else{
									TextUtil.mes(p, main.pref, "���������� ���� �������� �� ����������.");
									return true;
								}
							}else if(pi.ofArmor.charAt(0)=='O'){
								if(num==1){
									TextUtil.mes(p, main.pref, "��������� ��� ��� ���� ����� � ���-�� ����������. ����������� &2=3");
									remove=true;
								}else if(num==2){
									TextUtil.mes(p, main.pref, "��� ����� ����� ������, ��������� ����� ������ � ���, �� ������ �������� &2=3");
									remove=true;
									important=true;
								}else if(num==3){
									TextUtil.mes(p, main.pref, "� ��� � ������� ������� �����, ����� ������� ��, ��� �����. � �������� ����� � ����� ���������� - �������� for Gepiroy &2=3");
									remove=true;
									important=true;
								}else if(num==4){
									TextUtil.mes(p, main.pref, "���������, ������������� ����� ����������...");
									remove=true;
									important=true;
								}else if(num==5){
									TextUtil.mes(p, main.pref, "�� �� ��� �������� ��� ���������� ��� � &bVK&f?");
									remove=true;
									important=true;
								}else if(num==6){
									TextUtil.mes(p, main.pref, "�������, �� ���� �� ����������&2)");
									remove=true;
								}else if(num==7){
									TextUtil.mes(p, main.pref, "� ���������, ����� ������� �� �� �������, ��� �������� ������. ���� �������������� ���� ����� �� �����...");
								}else{
									TextUtil.mes(p, main.pref, "���������� ���� �������� �� ����������.");
									return true;
								}
							}else{
								TextUtil.mes(p, main.pref, "&c������ ���������: &e�������������� ������� ���: &6"+pi.ofArmor);
							}
							main.offersLog.conf.set("list."+st+".ans."+p.getName()+".num", num);
							main.offersLog.conf.set("list."+st+".ans."+p.getName()+".comment", comment);
							if(remove){
								oflist.remove(pi.ofArmor);
								main.offers.conf.set("list", oflist);
								if(important){
									main.offers.conf.set("important."+pi.ofArmor, num);
								}
								main.offers.save();
								pi.addHelPoints(6);
							}
							pi.addHelPoints(2);
							pi.ofArmor=null;
							return true;
						}
					}
					boolean ret=false;
					String armer=checkArmor(args[1]);
					if(armer!=null&&!armer.equals(p.getName())){
						TextUtil.mes(sender, main.pref, "���� ������ ������������ ������� &e"+armer+"&f.");
						return true;
					}
					if(oflist.contains(args[1])){
						String key=args[1];
						if(main.offersLog.conf.contains("list."+key+".ans."+p.getName())){
							TextUtil.mes(sender, main.pref, "|�� ��� �������� � ���� ��������.");
							return true;
						}
						tellAboutOffer(sender, key, false);
						if(key.charAt(0)=='R'){
							TextUtil.mes(sender, main.pref, "� ����� ������� �� ������������� ������ � ����������� ���� �����. � ����� ������ ������� �� ������ ������, ��� ������ � ��������.");
							TextUtil.mes(sender, main.pref, "&b��� ������ � ��������? |(/gepers offers &b<��������� ��������> <�������� ����������� ������ ��� ��������������>|)");
							TextUtil.mes(sender, "&b1", "����� �������, ��������� ������. |[�������������� ����]");
							TextUtil.mes(sender, "&b2", "����� �������, ������� �������������� ������ ���������. |[�������������� ����]");
							TextUtil.mes(sender, "&b3", "����� ���������, ��� ��������������:");
							TextUtil.mes(sender, "&b4", "� �� ���� �������� ���� ��� ������������, |[������ ��� ��� ������� ������]");
							ret=true;
						}
						else if(key.charAt(0)=='B'){
							TextUtil.mes(sender, main.pref, "� ����� ������� �� ������������� ��������� � ����������� ���� �����. � ����� ������ ������� �� ������ ������, ��� ������ � ���.");
							TextUtil.mes(sender, main.pref, "&b��� ������ � ����������? |(/gepers offers &b<��������� ��������> <�������� ����������� ������ ��� �����������>|)");
							TextUtil.mes(sender, "&b1", "��� ����, � �� ���������! ��� ������� ���� ����� ����� &bVK: https://vk.com/gepiroy");
							TextUtil.mes(sender, "&b2", "��� ����, �� �� �� ������ �� ������ � �� ������ �������. |(���������� � �� ����� ���������, �� ������ ���-�� ��������)");
							TextUtil.mes(sender, "&b3", "���� ���, ����� ������ ��� ������. |(� ���� ������ ����������� ��������, ������ �� ��� ������)");
							TextUtil.mes(sender, "&b4", "� �� ���� ��������� ���� ���.");
							ret=true;
						}
						else if(key.charAt(0)=='O'){
							TextUtil.mes(sender, main.pref, "� ����� ������� �� ������������� ��������� � ���� �������, ��� � ��� ������.");
							TextUtil.mes(sender, main.pref, "&b��� ������ � ����������? |(/gepers offers &b<��������� ��������> <�������� ����������� ������ ��� �����������>|)");
							TextUtil.mes(sender, "&b1", "� ���������/�������� ������ �����������, ���� �������.");
							TextUtil.mes(sender, "&b2", "����� ��������� ���� ��� �������� �������, ��������� � ��������� ����� ��� ��������.");
							TextUtil.mes(sender, "&b3", "����� ��������� ���-��, ���-�� ���������. ��������� � ��������� ����� ��� ��������.");
							TextUtil.mes(sender, "&b4", "���� �� ���-�� ������ ��� ������ ������, ��������� � ��������� �����.");
							TextUtil.mes(sender, "&b5", "����������, ������� ����� �������� ����� ���� &bVK: https://vk.com/gepiroy");
							TextUtil.mes(sender, "&b6", "� ��������� ��� ������, ����� ����������� ��� ������ ����� � �� ��� �������� ����� �� ��, ��� ���� �������.");
							TextUtil.mes(sender, "&b7", "� �� ���� ������������, ����� ������ ������ � ���� �������. |(��� ������� �������: ����� ���������, ��� ������ ������!)");
							ret=true;
						}
					}
					if(ret){
						if(p!=null){
							p.playSound(p.getLocation(), Sound.UI_TOAST_IN, 2, 0);
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
							pi.ofArmor=args[1];
							TextUtil.mes(sender, "&b7", "&a/gepers offers &e0 &f��� ������ ������������.");
						}
					}else{
						if(p!=null){
							p.playSound(p.getLocation(), Sound.UI_TOAST_IN, 2, 0);
							p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 0);
						}
						TextUtil.mes(sender, main.pref, "��������� ���� &eid &c�� ����������&f.");
					}
				}
			}else if(args[0].equalsIgnoreCase("offer")){
				tellAboutOffer(sender, args[1], false);
			}
			else if(args[0].equalsIgnoreCase("important")){
				for(String st:main.offers.getKeys("important")){
					tellAboutOffer(sender, st, false);
				}
			}
			if(sender.getName().equals("Gepiroy")||sender.getName().equals("CONSOLE")){
				if(args[0].equalsIgnoreCase("chin")){
					if(args.length<3){
						TextUtil.mes(sender, main.pref, "&a/gepers chin &e<player> <chin> - toggles chin for player.");
						return true;
					}
					GPinfo tpi;
					Player target=Bukkit.getPlayer(args[1]);
					if(target!=null){
						tpi=Events.plist.get(args[1]);
					}else if(main.is.PIexists(args[1])){
						tpi=new GPinfo(args[1]);
					}else{
						TextUtil.mes(sender, pref, "player not found.");
						return true;
					}
					if(tpi.haveChin(args[2])){
						tpi.remChin(args[2]);
						if(target!=null){
							TextUtil.mes(target, main.pref, "&c�� �������� ��� &6"+main.is.helpChins.get(args[2]).name+"&f �� &4"+sender.getName());
							target.playSound(target.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2, 0.75f);
						}
					}else{
						tpi.addChin(args[2]);
						if(target!=null){
							TextUtil.mes(target, main.pref, "&b�� �������� ��� &6"+main.is.helpChins.get(args[2]).name+"&f �� &4"+sender.getName());
							target.playSound(target.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 2, 0.75f);
							target.playSound(target.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 2, 0.75f);
						}
					}
				}
			}
		}
		return true;
	}
	public void tellAboutOffer(CommandSender sender, String id, boolean shortLore){
		//List<String>
		if(main.offersLog.conf.contains("list."+id)){
			Offer o=new Offer(id);
			sender.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"--------------------");
			if(o.type==1){
				sender.sendMessage(TextUtil.string("&c������ &f�� &e"+o.by+" &f�� &c"+o.to));
			}
			else if(o.type==2){
				sender.sendMessage(TextUtil.string("&6��������� � ���� &f�� &e"+o.by));
			}
			else if(o.type==3){
				sender.sendMessage(TextUtil.string("&b��������� &f�� &e"+o.by));
			}else{
				sender.sendMessage(TextUtil.string("&c������ ����������� ����: &6"+id));
			}
			if(shortLore){
				String someFrom="  "+o.lore.substring(0, (int) Math.min(o.lore.length()*0.5, 35))+ChatColor.GRAY+"...";
				sender.sendMessage(someFrom);
			}
			else sender.sendMessage("  "+o.lore);
			List<Ans> answers=o.answers;
			for(Ans ans:answers){
				TextUtil.mes(sender, "&b"+ans.by, "[&b"+ans.ans+"&f], |"+ans.comment);
			}
			for(GPinfo pi:Events.plist.values()){
				if(pi.ofArmor.equals(id)){
					sender.sendMessage(TextUtil.string("&c�������������� &f������� &b"+pi.nick));
					break;
				}
			}
			sender.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"--------------------");
		}else{
			TextUtil.mes(sender, main.pref, "id �� ������.");
		}
	}
	public String checkArmor(String id){
		for(GPinfo pi:Events.plist.values()){
			if(pi.ofArmor!=null&&pi.ofArmor.equals(id))return pi.nick;
		}
		return null;
	}
}
