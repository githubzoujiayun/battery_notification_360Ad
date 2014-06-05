package minggo.battery;

import java.io.IOException;

import minggo.battery.activity.MainActivity;
import minggo.battery.reciever.TimeChangeReciever;
import minggo.battery.util.MinggoDate;
import minggo.battery.util.PlaySound;
import minggo.battery.util.PreferenceShareUtil;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.widget.RemoteViews;
/**
 * 电池后台服务
 * @author minggo
 * @date 2013-8-7上午09:11:27
 */
public class BatteryService extends Service {
	/**
	 * 通告管理器
	 */
	private NotificationManager notificationManager;
	/**
	 * 通告
	 */
	private Notification notification;
	/**
	 * 通告下拉view
	 */
	private RemoteViews remoteViews;
	/**
	 * 上下文
	 */
	private Context context;
	/**
	 * 通告栏电量图片
	 */
	private int[] image;
	/**
	 * 下拉通告电量图
	 */
	private int[] image1;
	/**
	 * 资产管理器
	 */
	private AssetManager asm;
	/**
	 * 当前电量值
	 */
	private int cuurentLevel = -1;
	/**
	 * 时间操作类
	 */
	private MinggoDate date;
	/**
	 * 低电量声音提示
	 */
	public static boolean lowPowerSoundFlag;
	/**
	 * 整点报时声音
	 */
	public static boolean zhengSoundFlag;
	/**
	 * 时间变化监听器
	 */
	public TimeChangeReciever timeChangeReciever;
	
	@Override
	public void onCreate() {
		super.onCreate();
		timeChangeReciever = new TimeChangeReciever();
		context = this.getApplicationContext();
		this.registerReceiver(broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		this.registerReceiver(timeChangeReciever, new IntentFilter(Intent.ACTION_TIME_TICK));
		date = new MinggoDate();
		initImage();
		initBattery();
	}
	
	/**
	 * 初始化电池图片
	 */
	private void initImage(){
		image1 = new int[9];
		image = new int[101];
		for (int i = 0; i < image.length; i++) {
			image[i] = R.drawable.battery_1_bg_0+i;
			
		}
		for (int i = 0; i < image1.length; i++) {
			image1[i] = R.drawable.notification_battery_logo_15+i;
		}
		
	}
	/**
	 * 每天的提示语
	 * @return
	 */
	public String getDayPrompt(){
		String feeling = "";
		switch (date.getDayOfWeek()) {
		case 1:
			feeling = this.getString(R.string.sunday);
			break;
		case 2:
			feeling = this.getString(R.string.monday);
			break;
		case 3:
			feeling = this.getString(R.string.tuestday);
			break;
		case 4:
			feeling = this.getString(R.string.wednesday);
			break;
		case 5:
			feeling = this.getString(R.string.thursday);
			break;
		case 6:
			feeling = this.getString(R.string.friday);
			break;
		case 7:
			feeling = this.getString(R.string.saturday);
			break;
		default:
			break;
		}
		return feeling;
	}
	/**
	 * 初始化电池
	 */
	private void initBattery(){
		//获取资产管理实例
		asm = this.getAssets();
		//获取通知栏管理实例
		notificationManager = (NotificationManager) context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		//新建通知
		notification = new Notification();
		//通知设置不能删除
		notification.flags = Notification.FLAG_NO_CLEAR;
		//通知下滑呈现的view
		remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification);
		//下滑view的图片
		remoteViews.setImageViewResource(R.id.battery_iv, image1[0]);
		//下滑view的文字
		remoteViews.setTextViewText(R.id.feelings_tv, getDayPrompt()); 
		//把下滑view绑定在通知上
		notification.contentView = remoteViews;
		
		//点击下滑view处理的跳转事件
		Intent intent = new Intent(context, MainActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intent, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.contentIntent = pendingIntent;
         
		notificationManager.notify(1, notification);
		
	}
	/**
	 * 更新电池电量图片 
	 */
	private void modifyBattery(int level){
		
		int battery = 0;
		
		if (0<=level&&level<15) {
			battery = image1[0];
		}else if (15<=level&&level<25) {
			battery = image1[1];
		}else if (25<=level&&level<50) {
			battery = image1[2];
		}else if (50<=level&&level<65) {
			battery = image1[3];
		}else if (65<=level&&level<70){
			battery = image1[4];
		}else if (70<=level&&level<75){
			battery = image1[5];
		}else if (75<=level&&level<80){
			battery = image1[6];
		}else if (80<=level&&level<=99){
			battery = image1[7];
		}else if (level==100){
			battery = image1[8];
		}
		
		// 添加声音提示
        //notification.defaults=Notification.DEFAULT_SOUND;
        // audioStreamType的值必须AudioManager中的值，代表着响铃的模式
        //notification.audioStreamType= android.media.AudioManager.ADJUST_LOWER;

        //下边的两个方式可以添加音乐
        //notification.sound = Uri.parse("file:///android_asset/sound/shake_sound_male.mp3"); 
        //notification.sound = Uri.parse("file:///sdcard/Mp3/earth_song.mp3"); 
        //notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6"); 
		
		if (level%10==0&&cuurentLevel!=level&&level<=30) {
			cuurentLevel = level;
			try {
				if(!PreferenceShareUtil.getLowPowerFlag(this))
				PlaySound.getInstance().play("sound/failShout.mp3", asm);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		remoteViews.setImageViewResource(R.id.battery_iv, battery);
		notification.contentView = remoteViews;
		notification.icon = image[level];
		notificationManager.notify(1, notification);
		
	}
	/**
	 * 电池广播接收器
	 */
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		int level = 0;
		int scale = 0;
		@Override
		public void onReceive(Context context, Intent intent) {
			
			String action = intent.getAction();
			if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
				level = intent.getIntExtra("level", 0);
				scale = intent.getIntExtra("scale", 0);
				modifyBattery(level);
				//String BatteryV = "当前电压为：" + intent.getIntExtra("voltage", 0); 
				//String BatteryT = "当前温度为：" + intent.getIntExtra("temperature", 0); 
				//System.out.println(level+"----"+scale+"-----"+BatteryV+"-----"+BatteryT);
			}
		}
	};
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		notificationManager.cancelAll();
		this.unregisterReceiver(broadcastReceiver);
		this.unregisterReceiver(timeChangeReciever);
		stopSelf();
		
		System.exit(0);
		super.onDestroy();
	}
	
}
