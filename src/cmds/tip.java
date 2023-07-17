package cmds;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import GPUtils.GepUtil;
import GPUtils.TextUtil;
import GePersonal.main;

public class tip implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length==0){
			TextUtil.mes(sender, main.pref, "&a/tip |<&fпомощь|>");
			TextUtil.mes(sender, main.pref, "При указании &bников&f, эти игроки получат особое оповещение.");
		}else{
			String mes=TextUtil.string("&b"+sender.getName()+"&f:&a");
			for(String st:args){
				Player p=Bukkit.getPlayer(st.replaceAll(",", ""));
				if(p!=null){
					p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 2);
				}
				mes+=" "+st;
			}
			GepUtil.globMessage(mes);
		}
		return true;
	}
	
}
