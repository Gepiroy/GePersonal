package GPobj;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import GPUtils.GepUtil;
import GPUtils.TextUtil;
import GePersonal.main;
import net.minecraft.server.v1_13_R2.EntityHuman;
import net.minecraft.server.v1_13_R2.IBlockData;

public class NoFastBreak {
	public long updated=0;
	public boolean handling=false;
	List<Break> breaks=new ArrayList<>();
	//x1.3/per lvl (cycle)
	public long sbr=0;//когда начал ломать блок
	public int brwarns=0;//колво обгонов
	public long lastMined=0;//последний раз ломал блок
	public float lm=100;//время последнего слома
	
	public void check(Player p){
		//if(breaks.size()>0)return;//TODO freezed
		double t=0;
		double rt=0;
		for(Break br:breaks){
			if(t>0){
				t+=0.25;//1/4 секунды задержки добавляется после ломания блока.
			}
			t+=main.NFB.normalTimeBreak(br);
			if(rt>0){
				rt+=0.25;//1/4 секунды задержки добавляется после ломания блока.
			}
			rt+=br.real;
		}
		breaks.clear();
		String end=TextUtil.string(GepUtil.boolCol(rt>=t)+""+rt+" |(&f"+GepUtil.CylDouble((100.0/t*rt),"#0.0")+"%|)");
		p.sendMessage("YourTimeMustBe "+t+"\nbut this is "+end);
		handling=false;
	}
	public Break breakEvent(BlockBreakEvent e){
		if(!handling){
			handling=true;
			updated=new Date().getTime();
			//return;
		}
		long now=new Date().getTime();
		float time=(float) ((now-sbr)/1000.0);//in secs
		Player p=e.getPlayer();
		IBlockData data=CraftMagicNumbers.getBlock(e.getBlock().getType()).getBlockData();
		EntityHuman hum=null;
		try {//autogen
			hum = (EntityHuman) p.getClass().getMethod("getHandle").invoke(p);
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
		} catch (SecurityException ex) {
			ex.printStackTrace();
		}
		Break br=new Break(e.getBlock().getType().getHardness(),hum.b(data),time);
		breaks.add(br);
		return br;
	}
}
