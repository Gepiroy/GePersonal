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
			sender.sendMessage(TextUtil.string("&aВерсия: &f"+main.instance.getDescription().getVersion()));
			sender.sendMessage(TextUtil.string("&aДля списка команд &e/gepers list"));
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
						TextUtil.mes(sender, pref, "|Не найдена информация по запросу &6"+args[1].toLowerCase());
					}
				}
			}else if(args[0].equalsIgnoreCase("info")){
				if(args.length<2){
					sender.sendMessage(TextUtil.string("&a/gepers info &e<Ник>"));
					return true;
				}
				if(!main.is.PIexists(args[1])){
					TextUtil.mes(sender, pref, "|Игрока нет в базе данных.");
					return true;
				}
				GPinfo pi=new GPinfo(args[1]);
				String border=TextUtil.string("-~=&f<&6"+args[1]+"&e's &cPERSONAL &bINFO&f>|=~-");
				sender.sendMessage(border);
				sender.sendMessage(TextUtil.string("&aЗарегистрирован: &f"+main.dateByLong(pi.conf.conf.getLong("Reged"))));
				sender.sendMessage(TextUtil.string("&aВ последний раз заходил: |"+TextUtil.times((int) ((new Date().getTime()-pi.conf.conf.getLong("LastLogin"))/1000))+" назад"));
				sender.sendMessage(TextUtil.string("&aВсего онлайн: |"+pi.conf.conf.getInt("mins")+" мин."));
				
				sender.sendMessage(TextUtil.string("&6История:"));
				for(String bst:pi.conf.getKeys("Bans")){
					BanInfo ban=new BanInfo(pi.conf.conf,"Bans."+bst);
					sender.sendMessage(TextUtil.string("&4БАН: &e"+main.dateByLong(ban.when)+" |("+TextUtil.times((int) ((new Date().getTime()-ban.when)/1000))+" назад, &eот &6"+ban.by+"|)"));
					sender.sendMessage(TextUtil.string("   &cПричина: &e"+ban.reason));
					sender.sendMessage(TextUtil.string("   &cСрок: &e"+TextUtil.times(ban.time*60)));
				}
				sender.sendMessage(TextUtil.string("Поиск связанных с игроком запросов..."));
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
						p.sendMessage("Нет запросов.");
						return true;
					}
					String border=TextUtil.string("-~=&f<&bЗапросы игроков&f>|=~-");
					sender.sendMessage(border);
					for(String st:oflist){
						tellAboutOffer(sender, st, true);
					}
					sender.sendMessage(TextUtil.string("&a/gepers offers &e<id>&f, чтобы &cзабронировать &fи &aрассмотреть &fзапрос."));
					sender.sendMessage(border);
					if(p!=null){
						p.playSound(p.getLocation(), Sound.UI_TOAST_IN, 2, 0);
					}
				}else{
					GPinfo pi=Events.plist.get(p.getName());
					if(args[1].length()==1){
						int num=GepUtil.intFromString(args[1]);
						if(pi.ofArmor==null){
							TextUtil.mes(p, main.pref, "Вы сейчас ничего не обрабатываете.");
							return true;
						}
						if(num==0){
							TextUtil.mes(p, main.pref, "Вы отказались рассматривать этот запрос. Это лучше, чем выдавать решения наугад!");
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
									TextUtil.mes(p, main.pref, "Спасибо за проверку! Игрок это заслужил.");
									remove=true;
								}else if(num==2){
									TextUtil.mes(p, main.pref, "Спасибо за проверку! Вскоре кто-то другой накажет этого игрока.");
								}else if(num==3){
									TextUtil.mes(p, main.pref, "Спасибо за проверку! Информация о клевете сохранена.");
									remove=true;
								}else if(num==4){
									TextUtil.mes(p, main.pref, "Спасибо, что попытались! Вскоре другой хелпер займётся этим делом.");
								}else{
									TextUtil.mes(p, main.pref, "Выбранного вами варианта не существует.");
									return true;
								}
							}else if(pi.ofArmor.charAt(0)=='B'){
								if(num==1){
									TextUtil.mes(p, main.pref, "Если баг серьёзный, по возможности постарайтесь проследить, чтобы им воспользовалось как можно меньше игроков до создания фикса! Вы же оповестили Гепи об этом баге?");
									remove=true;
									important=true;
								}else if(num==2){
									TextUtil.mes(p, main.pref, "Спасибо за проверку! Этот баг сохранен в отдельную папочку, и Гепи вскоре его прочитает.");
									remove=true;
									important=true;
								}else if(num==3){
									TextUtil.mes(p, main.pref, "Спасибо за проверку! Информация о клевете/ошибке сохранена.");
									remove=true;
								}else if(num==4){
									TextUtil.mes(p, main.pref, "Спасибо, что попытались! Вскоре другой хелпер займётся этим делом.");
								}else{
									TextUtil.mes(p, main.pref, "Выбранного вами варианта не существует.");
									return true;
								}
							}else if(pi.ofArmor.charAt(0)=='O'){
								if(num==1){
									TextUtil.mes(p, main.pref, "Благодаря вам ещё один игрок в чём-то разобрался. Поздравляем &2=3");
									remove=true;
								}else if(num==2){
									TextUtil.mes(p, main.pref, "Ещё одной идеей больше, благодаря этому игроку и вам, за услуги грузчика &2=3");
									remove=true;
									important=true;
								}else if(num==3){
									TextUtil.mes(p, main.pref, "У нас в проекте свобода слова, пусть говорят всё, что хотят. А возиться позже с этими проблемами - геморрой for Gepiroy &2=3");
									remove=true;
									important=true;
								}else if(num==4){
									TextUtil.mes(p, main.pref, "Сохранено, беспомощность нужно исправлять...");
									remove=true;
									important=true;
								}else if(num==5){
									TextUtil.mes(p, main.pref, "Вы же уже передали эту информацию мне в &bVK&f?");
									remove=true;
									important=true;
								}else if(num==6){
									TextUtil.mes(p, main.pref, "Надеюсь, вы хотя бы посмеялись&2)");
									remove=true;
								}else if(num==7){
									TextUtil.mes(p, main.pref, "И правильно, лучше скинуть всё на другого, чем наделать ошибок. Хотя злоупотреблять этим делом не стоит...");
								}else{
									TextUtil.mes(p, main.pref, "Выбранного вами варианта не существует.");
									return true;
								}
							}else{
								TextUtil.mes(p, main.pref, "&cОшибка обработки: &eнераспознанное кодовое имя: &6"+pi.ofArmor);
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
						TextUtil.mes(sender, main.pref, "Этот запрос забронирован игроком &e"+armer+"&f.");
						return true;
					}
					if(oflist.contains(args[1])){
						String key=args[1];
						if(main.offersLog.conf.contains("list."+key+".ans."+p.getName())){
							TextUtil.mes(sender, main.pref, "|Вы уже работали с этим репортом.");
							return true;
						}
						tellAboutOffer(sender, key, false);
						if(key.charAt(0)=='R'){
							TextUtil.mes(sender, main.pref, "С этого момента вы забронировали репорт и занимаетесь этим делом. В любой момент времени вы должны решить, что делать с репортом.");
							TextUtil.mes(sender, main.pref, "&bЧто делать с репортом? |(/gepers offers &b<выбранное действие> <Описание проделанной работы или доказательства>|)");
							TextUtil.mes(sender, "&b1", "Игрок виновен, наказание выдано. |[доказательства вины]");
							TextUtil.mes(sender, "&b2", "Игрок виновен, просьба уполномоченных выдать наказание. |[доказательства вины]");
							TextUtil.mes(sender, "&b3", "Игрок невиновен, вот доказательства:");
							TextUtil.mes(sender, "&b4", "Я не могу доказать вину или невиновность, |[почему или что удалось узнать]");
							ret=true;
						}
						else if(key.charAt(0)=='B'){
							TextUtil.mes(sender, main.pref, "С этого момента вы забронировали сообщение и занимаетесь этим делом. В любой момент времени вы должны решить, что делать с ним.");
							TextUtil.mes(sender, main.pref, "&bЧто делать с сообщением? |(/gepers offers &b<выбранное действие> <Описание проделанной работы или комментарий>|)");
							TextUtil.mes(sender, "&b1", "Баг есть, и он серьёзный! Уже сообщил Гепи лично через &bVK: https://vk.com/gepiroy");
							TextUtil.mes(sender, "&b2", "Баг есть, но он не влияет на баланс и не мешает игрокам. |(Информация о нём будет сохранена, вы можете что-то добавить)");
							TextUtil.mes(sender, "&b3", "Бага нет, игрок наврал или ошибся. |(В этом случае обязательно напишите, почему вы так решили)");
							TextUtil.mes(sender, "&b4", "Я не могу проверить этот баг.");
							ret=true;
						}
						else if(key.charAt(0)=='O'){
							TextUtil.mes(sender, main.pref, "С этого момента вы забронировали обращение и сами решаете, что с ним делать.");
							TextUtil.mes(sender, main.pref, "&bЧто делать с обращением? |(/gepers offers &b<выбранное действие> <Описание проделанной работы или комментарий>|)");
							TextUtil.mes(sender, "&b1", "С проблемой/просьбой игрока разобрались, дело закрыто.");
							TextUtil.mes(sender, "&b2", "Игрок предложил идею для развития проекта, сохранить в отдельное место для разрабов.");
							TextUtil.mes(sender, "&b3", "Игрок критикует что-то, чем-то недоволен. Сохранить в отдельное место для разрабов.");
							TextUtil.mes(sender, "&b4", "Вряд ли кто-то сможет ему помочь сейчас, сохранить в отдельное место.");
							TextUtil.mes(sender, "&b5", "Информация, которую нужно передать лично Гепи &bVK: https://vk.com/gepiroy");
							TextUtil.mes(sender, "&b6", "В обращении нет смысла, кроме оскорблений или пустых шуток в нём нет никакого намёка на то, что надо сделать.");
							TextUtil.mes(sender, "&b7", "Я не могу определиться, пусть другой хелпер с этим возится. |(Нет никаких штрафов: лучше передайте, чем делать наугад!)");
							ret=true;
						}
					}
					if(ret){
						if(p!=null){
							p.playSound(p.getLocation(), Sound.UI_TOAST_IN, 2, 0);
							p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
							pi.ofArmor=args[1];
							TextUtil.mes(sender, "&b7", "&a/gepers offers &e0 &fдля отмены рассмотрения.");
						}
					}else{
						if(p!=null){
							p.playSound(p.getLocation(), Sound.UI_TOAST_IN, 2, 0);
							p.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 2, 0);
						}
						TextUtil.mes(sender, main.pref, "Введённого вами &eid &cне существует&f.");
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
							TextUtil.mes(target, main.pref, "&cВы потеряли чин &6"+main.is.helpChins.get(args[2]).name+"&f от &4"+sender.getName());
							target.playSound(target.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 2, 0.75f);
						}
					}else{
						tpi.addChin(args[2]);
						if(target!=null){
							TextUtil.mes(target, main.pref, "&bВы получили чин &6"+main.is.helpChins.get(args[2]).name+"&f от &4"+sender.getName());
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
				sender.sendMessage(TextUtil.string("&cРепорт &fот &e"+o.by+" &fна &c"+o.to));
			}
			else if(o.type==2){
				sender.sendMessage(TextUtil.string("&6Сообщение о баге &fот &e"+o.by));
			}
			else if(o.type==3){
				sender.sendMessage(TextUtil.string("&bОбращение &fот &e"+o.by));
			}else{
				sender.sendMessage(TextUtil.string("&cОшибка определения типа: &6"+id));
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
					sender.sendMessage(TextUtil.string("&cЗАБРОНИРОВАННО &fигроком &b"+pi.nick));
					break;
				}
			}
			sender.sendMessage(ChatColor.GOLD+""+ChatColor.BOLD+"--------------------");
		}else{
			TextUtil.mes(sender, main.pref, "id не найден.");
		}
	}
	public String checkArmor(String id){
		for(GPinfo pi:Events.plist.values()){
			if(pi.ofArmor!=null&&pi.ofArmor.equals(id))return pi.nick;
		}
		return null;
	}
}
