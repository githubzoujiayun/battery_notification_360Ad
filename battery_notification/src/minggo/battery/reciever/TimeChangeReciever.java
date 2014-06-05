package minggo.battery.reciever;

import java.io.IOException;

import minggo.battery.BatteryService;
import minggo.battery.util.MinggoDate;
import minggo.battery.util.PlaySound;
import minggo.battery.util.PreferenceShareUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 时间广播监听器 
 * @author minggo
 * @date 2013-8-7下午01:01:13
 */
public class TimeChangeReciever extends BroadcastReceiver {

	private MinggoDate date;
	@Override
	public void onReceive(Context context, Intent intent) {
		String action  = intent.getAction();
		if (action.equals(Intent.ACTION_TIME_TICK)) {
			
			date = new MinggoDate();
			//System.out.println("时间[分]监听器进来---"+date.get24Hour());
			
			if (date.getMinutes()==00&&date.get24Hour()>5) {
				//System.out.println("整点报时--->"+date.get24Hour()+"点");
				try {
					if(!PreferenceShareUtil.getZhengTimeFlag(context))
					PlaySound.getInstance().play("sound/"+date.get24Hour()+".mp3", context.getAssets());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
