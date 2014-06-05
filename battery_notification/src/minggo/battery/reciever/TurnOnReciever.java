package minggo.battery.reciever;

import minggo.battery.BatteryService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 开机完成广播接收
 * @author minggo
 * @date 2013-8-7上午09:16:31
 */
public class TurnOnReciever extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		//开启监听服务
		context.startService(new Intent(context, BatteryService.class));
	}
}
