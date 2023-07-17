package GPUtils;

import GPobj.Break;

public class NFBmanager {
	public float normalTimeBreak(Break br){
		float seconds=br.hard;
		seconds/=br.speed;
		//20 ticks=1sec, =>...
		seconds=(float) Math.round(seconds*30);
		seconds/=20.0;
		if(seconds<0.05)seconds=0;
		//TextUtil.globMessage(null, "hrd="+br.hard+";speed="+br.speed+";ret="+seconds);
		return seconds;
	}
}
