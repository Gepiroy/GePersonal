package GePersonal;

import java.util.Date;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import GPPI.GPinfo;
import GPUtils.GepUtil;
import GPUtils.TextUtil;
import GPobj.BanInfo;
import GPobj.Break;
import GPobj.punishInfo;

public class Events implements Listener{
	public static HashMap<String, GPinfo> plist=new HashMap<>();
	@EventHandler
	public void login(AsyncPlayerPreLoginEvent e){
		String nick=e.getName();
		GPinfo pi=new GPinfo(nick);
		BanInfo binf=pi.checkBanned();
		if(binf!=null){
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, binf.banTitle());
			return;
		}
		plist.put(nick, pi);
	}
	@EventHandler
	public void inter(PlayerInteractEvent e){
		if(e.getClickedBlock()!=null){
			GPinfo pi=plist.get(e.getPlayer().getName());
			pi.NFB.sbr=new Date().getTime();
		}
	}
	@EventHandler
	public void bb(BlockBreakEvent e){
		Player p=e.getPlayer();
		GPinfo pi=plist.get(p.getName());
		long now=new Date().getTime();
		Break br=pi.NFB.breakEvent(e);
		float must=main.NFB.normalTimeBreak(br)-0.001f;
		if(pi.NFB.brwarns>0&&((now-pi.NFB.lastMined)/2500)>must){
			p.sendMessage("cleared");
			pi.NFB.brwarns=0;
		}
		pi.NFB.lastMined=now;
		float time=(float) ((now-pi.NFB.sbr)/1000.0);//in secs
		p.sendMessage("must="+must+"\ntime="+GepUtil.boolCol(must<=time)+time);
		if(must>time){
			pi.NFB.brwarns++;
			int NTW=(int) (20/(must*20));
			if(NTW<5)NTW=5;
			p.sendMessage(ChatColor.YELLOW+"needToWarn: "+NTW);
			if(pi.NFB.brwarns>=NTW){
				for(Player pl:Bukkit.getOnlinePlayers()){
					if(pl.isOp()){
						TextUtil.mes(pl, main.pref, "&c"+p.getName()+"&f: обнаружен &6FastBreak&f.");
					}
				}
				pi.NFB.brwarns=0;
			}
		}else if(pi.NFB.brwarns>0){
			pi.NFB.brwarns--;
		}
	}
	@EventHandler
	public void join(PlayerJoinEvent e){
	}
	public static void doJoin(Player p){
		addPI(p.getName());
	}
	public static void addPI(String name){
		if(plist.containsKey(name))return;
		GPinfo pi=new GPinfo(name);
		pi.conf.conf.set("LastLogin", new Date().getTime());
		pi.conf.save();
		plist.put(name, pi);
	}
	@EventHandler
	public void leave(PlayerQuitEvent e){
		Player p=e.getPlayer();
		doLeave(p);
	}
	public static void doLeave(Player p){
		GPinfo pi=plist.get(p.getName());
		pi.conf.conf.set("LastLogout", new Date().getTime());
		pi.conf.save();
		plist.remove(p.getName());
	}
	
	@EventHandler(priority=EventPriority.HIGH,ignoreCancelled=false)
	public void chat(AsyncPlayerChatEvent e){
		Player p=e.getPlayer();
		String mes=e.getMessage();
		GPinfo pi=plist.get(p.getName());
		if(pi.test!=null){
			e.setCancelled(true);
			String ans=pi.test.ans(mes);
			if(ans==null){
				pi.test.ans.add(mes);
				pi.test.update();
				if(!pi.test.talkAbout(p)){
					p.sendMessage("Тест окончен, ansLog:");
					int i=0;
					for(String st:pi.test.ans){
						String q=pi.test.getQ(i);
						if(pi.test.conf.getString(q+".AnsType").equals("num"))TextUtil.mes(p, "&f"+pi.test.conf.getString(q+".Name"), st+" &b(&f"+pi.test.conf.getString(q+".Ans."+st)+"&b)");
						else if(pi.test.conf.getString(q+".AnsType").equals("nums")){
							String ret="&f"+st+" &b(";
							for(int n=0;n<st.length();n++){
								char a=st.charAt(n);
								if(n>0)ret+="&7;";
								ret+="&f"+pi.test.conf.getString(q+".Ans."+a);
							}
							ret+="&b)";
							TextUtil.mes(p, "&f"+pi.test.conf.getString(q+".Name"), ret);
						}
						else TextUtil.mes(p, "&f"+pi.test.conf.getString(q+".Name"), st);
						i++;
					}
					pi.conf.conf.set("Tests."+pi.test.codeName, pi.test.ans);
					pi.test=null;
				}
			}else{
				if(ans.equals("int")){
					TextUtil.mes(p, "&c!", "Ответ &cне должен&f содержать в себе ничего кроме &bцелых чисел&f. Если разрешено указывать несколько ответов, ответ пишется в формате &a123&f.");
				}else if(ans.equals("long")){
					TextUtil.mes(p, "&c!", "Слишком длинный ответ.");
				}else if(ans.equals("range")){
					TextUtil.mes(p, "&c!", "В ответе указан &cнесуществующий&f вариант ответа.");
				}
			}
			return;
		}
		e.setMessage(main.cm.repl(e.getMessage()));
		p.sendMessage("mes="+e.getMessage());
		p.sendMessage("remes="+main.cm.repl(e.getMessage()));
		for(String key:main.cm.replaces.keySet()){
			mes.replaceAll(Pattern.quote(key), Pattern.quote(main.cm.replaces.get(key)));
		}
		p.sendMessage(mes);
		punishInfo mute=pi.checkMuted();
		if(mute!=null){
			TextUtil.mes(p, "&cМут", "Вам &cзапрещено &fписать в чат |(ещё "+mute.remain()+")");
			e.setCancelled(true);
		}
		if(pi.fastTimers.containsKey("nochat")){//TODO В ПРИЗОНЕ ПЕРЕВЕСТИ СИСТЕМУ НА НОРМАЛЬНУЮ!
			TextUtil.mes(p, "&6Анти-спам", "Просим вас быть спокойнее и &6не торопиться&f отправлять сообщения.");
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void GUIclick(InventoryClickEvent e){
		
	}
}
