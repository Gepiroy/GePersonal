package GPobj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import GPPI.GPinfo;
import GPUtils.GepUtil;
import GPUtils.TextUtil;
import GePersonal.main;

public class ChinInfo {
	public String id;
	public String name;
	public List<String> lore=new ArrayList<>();
	public int price;
	public HashMap<String,Integer> needs;
	public int slot;
	public Material mat;
	public boolean isModer=false;
	
	public ChinInfo(String Id, String Name, List<String> Lore, int Price, HashMap<String,Integer> Needs, int Slot, Material Mat){
		id=Id;
		name=Name;
		if(Lore!=null)for(String st:Lore){
			lore.add(TextUtil.string(st));
		}
		price=Price;
		needs=Needs;
		slot=Slot;
		mat=Mat;
	}
	
	public ItemStack createItem(GPinfo pi){
		ItemStack ret=new ItemStack(mat);
		ItemMeta meta=ret.getItemMeta();
		List<String> Lore=new ArrayList<>(lore);
		Lore.add(ChatColor.GRAY+"   ----------");
		int hp=pi.getHelPoints();
		if(!pi.haveChin(id)){
			if(price<0)Lore.add("&6�������� ������ ������ ������.");
			else Lore.add("&f����: &"+GepUtil.boolString("a", "c", hp>=price)+price+" |(&f� ��� &e"+hp+"|)");
			if(needs!=null){
				Lore.add(TextUtil.string("&f������ &c����������&f:"));
				List<Integer> checksNum=checksNum(pi);
				int i=0;
				for(String st:needs.keySet()){
					int need=needs.get(st);
					int have=checksNum.get(i);
					String pre=" "+GepUtil.boolString("&a+", "|-", have>=need)+" ";
					i++;
					if(st.equals("mins")){
						Lore.add(TextUtil.string(pre+have+"&e/"+need+" ���. &f�������, ������ ��������"));
					}else if(st.equals("offers")){
						Lore.add(TextUtil.string(pre+have+"&e/"+need+" &f������������ ��������"));
					}else{
						Lore.add(TextUtil.string(pre+"&c������ ����������� �������! &6"+st));
					}
				}
			}
		}else{
			meta.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
			Lore.add(TextUtil.string("&a�������"));
		}
		meta.setLore(Lore);
		ret.setItemMeta(meta);
		return ret;
	}
	public List<Integer> checksNum(GPinfo pi){
		List<Integer> ret=new ArrayList<>();
		for(String st:needs.keySet()){
			if(st.equals("mins"))ret.add(pi.conf.conf.getInt("helpmins"));
			else if(st.equals("offers"))ret.add(pi.conf.conf.getInt("offered"));
			else ret.add(0);
		}
		return ret;
	}
	public boolean buy(Player p, GPinfo pi){
		if(price<0){
			TextUtil.mes(p, main.pref, "|���� ��� ���������� ������ ����� ������ ������. ��� ������ ������ ������.");
			return false;
		}
		if(pi.haveChin(id)){
			TextUtil.mes(p, main.pref, "|� ��� ��� ���� ���� ���.");
			return false;
		}
		if(pi.getHelPoints()<price){
			TextUtil.mes(p, main.pref, "|����� &6&l������ |����� �������...");
			return false;
		}
		pi.addHelPoints(-price);
		pi.addChin(id);
		return true;
	}
}
