package minggo.battery.activity;

import java.io.IOException;

import minggo.battery.BatteryService;
import minggo.battery.R;
import minggo.battery.util.PlaySound;
import minggo.battery.util.PreferenceShareUtil;
import minggo.battery.util.ShakeListener;
import minggo.battery.util.ShakeListener.OnShakeListener;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.pubukeji.diandeows.adviews.DiandeBanner;
import com.pubukeji.diandeows.adviews.DiandeResultCallback;
/**
 * 电池通知点击到这个ativity
 * @author minggo
 * @date 2013-8-7上午09:17:24
 */
public class MainActivity extends Activity implements OnClickListener{
	private TextView versionTips;
	private Button lowPowerbt;
	private Button timeSoundbt;
	private Button exitbt;
	private Button submitbt;
	private Button changebt;
	private RelativeLayout rl_ad;
	private String banner_AD_ID = "1549b92eb7e0be5224e61c96af90e3c4";
	private DiandeBanner banner ;
	private ImageView cancelAdBt;
	
	public static AssetManager assetManager;
	private ShakeListener mShakeListener = null;
	private Vibrator mVibrator;
	private ImageView seZiiv;
	private int i = 0;
	private int j = 0;
	private Handler hdl;
	
	private boolean changejiawu;
	private int sourceIds[];
	private int sourceIds2[];
	
	private String telephone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		versionTips = (TextView) findViewById(R.id.app_version_tv);
		this.startService(new Intent(this, BatteryService.class));
		lowPowerbt = (Button) findViewById(R.id.low_power_bt);
		timeSoundbt = (Button) findViewById(R.id.zheng_sound_bt);
		exitbt = (Button) findViewById(R.id.exitapp);
		submitbt = (Button) findViewById(R.id.submitButton); 
		seZiiv = (ImageView) findViewById(R.id.game_shack_iv);
		changebt = (Button) findViewById(R.id.changebt);
		
		changebt.setOnClickListener(this);
		exitbt.setOnClickListener(this);
		timeSoundbt.setOnClickListener(this);
		lowPowerbt.setOnClickListener(this);
		submitbt.setOnClickListener(this);
		
		BatteryService.lowPowerSoundFlag = PreferenceShareUtil.getLowPowerFlag(this);
		BatteryService.zhengSoundFlag = PreferenceShareUtil.getZhengTimeFlag(this);
		
		lowPowerbt.setSelected(BatteryService.lowPowerSoundFlag);
		timeSoundbt.setSelected(BatteryService.zhengSoundFlag);
		
		assetManager = this.getResources().getAssets();
		hdl = new Handler();
		mVibrator = (Vibrator)getApplication().getSystemService(VIBRATOR_SERVICE);
		
		sourceIds = new int[]{R.drawable.dice_1,R.drawable.dice_2,R.drawable.dice_3,
				R.drawable.dice_4,R.drawable.dice_5,R.drawable.dice_6};
		sourceIds2 = new int[]{R.drawable.se_dice_1,R.drawable.se_dice_2,R.drawable.se_dice_3,
				R.drawable.se_dice_4,R.drawable.se_dice_5,R.drawable.se_dice_6};
		PackageInfo packageInfo = null;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		versionTips.setText(this.getString(R.string.minggo_battery)+"["+packageInfo.versionName+"]");
		
		initShock();
		initAdUI();
		
		TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		if (tm.getLine1Number()!=null&&!tm.getLine1Number().equals("")) {
			telephone = tm.getLine1Number();
			StatService.onEvent(MainActivity.this, "user", "telephone_"+telephone);
		}
		
	}
	/**
	 * 初始化广告的UI
	 */
	private void initAdUI(){
		rl_ad = (RelativeLayout) findViewById(R.id.rl_ad);
		cancelAdBt =  (ImageView) findViewById(R.id.bt_cancel_ad);
		cancelAdBt.setOnClickListener(this);
		
		banner = new DiandeBanner(this, banner_AD_ID);
		rl_ad.addView(banner);
		
		/*//xml方式添加
		 * banner = (DiandeBanner)findViewById(R.id.bannerView);
		banner.setAD_ID(banner_AD_ID);*/
		banner.show();
		banner.setRequestCallBack(new DiandeResultCallback() {
			
			@Override
			public void onSuccess(boolean result, String message) {
			}
			
			@Override
			public void onFailed(String errorMessage) {
				cancelAdBt.setVisibility(View.GONE);
			}

			@Override
			public void onAdShowSuccess(int code,String message) {
				cancelAdBt.setVisibility(View.VISIBLE);
			}
		});
		
	}
	
	/**
	 * 初始化色子甩过程
	 */
	public void initShock(){
		
		seZiiv.setImageResource(sourceIds2[0]);
		
		mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new OnShakeListener() {
			public void onShake() {
				StatService.onEvent(MainActivity.this, "play_type", changejiawu==true?"甩家务":"甩点子");
				StatService.onEvent(MainActivity.this, "shake", telephone);//统计用户使用甩色子
				mShakeListener.stop();
				if(startVibrato()){
				new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							 mVibrator.cancel();
							 mShakeListener.start();
						}
					}, 800);
				}
				if (!changejiawu) {
					hdl.post(new Runnable(){
						@Override
						public void run(){
							if (i<=50) {
								i+=j;
								if (j==0) {
									seZiiv.setImageResource(R.drawable.se_dice_action_0);
								}else if (j==1) {
									seZiiv.setImageResource(R.drawable.se_dice_action_1);
									
								}else if (j==2) {
									seZiiv.setImageResource(R.drawable.se_dice_action_2);
									
								}else if (j==3) {
									seZiiv.setImageResource(R.drawable.se_dice_action_3);
									j=-1;
								}
								j++;
								//让色子逐渐变慢
								if (i<35) {
									hdl.postDelayed(this,100);
								}else if (35<=i&&i<58) {
									hdl.postDelayed(this,100+i);
								}else{
									hdl.postDelayed(this,160);
								}
							}else{
								i=0;
								seZiiv.setImageResource(sourceIds2[(int)(Math.random()*6)]);
								hdl.removeCallbacks(this);
							}
						}
					});
				}else{
					hdl.post(new Runnable(){
						@Override
						public void run(){
							if (i<=50) {
								i+=j;
								if (j==0) {
									seZiiv.setImageResource(R.drawable.dice_action_0);
								}else if (j==1) {
									seZiiv.setImageResource(R.drawable.dice_action_1);
									
								}else if (j==2) {
									seZiiv.setImageResource(R.drawable.dice_action_2);
									
								}else if (j==3) {
									seZiiv.setImageResource(R.drawable.dice_action_3);
									j=-1;
								}
								j++;
								//让色子逐渐变慢
								if (i<35) {
									hdl.postDelayed(this,100);
								}else if (35<=i&&i<58) {
									hdl.postDelayed(this,100+i);
								}else{
									hdl.postDelayed(this,160);
								}
								
							}else{
								i=0;
								seZiiv.setImageResource(sourceIds[(int)(Math.random()*6)]);
								hdl.removeCallbacks(this);
							}
						}
					});
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		StatService.onResume(this);
		super.onResume();
	}
	@Override
	protected void onPause() {
		StatService.onPause(this);
		super.onPause();
	}
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.bt_cancel_ad:
			banner.close();
			v.setVisibility(View.GONE);
			break;
		case R.id.low_power_bt:
			BatteryService.lowPowerSoundFlag = !lowPowerbt.isSelected();
			lowPowerbt.setSelected(BatteryService.lowPowerSoundFlag);
			PreferenceShareUtil.saveLowPowerFlag(this, BatteryService.lowPowerSoundFlag);
			//统计用户设置低电量提醒
			StatService.onEvent(MainActivity.this, "battery_alert", BatteryService.lowPowerSoundFlag+"");
			break;
		case R.id.zheng_sound_bt:
			BatteryService.zhengSoundFlag = !timeSoundbt.isSelected();
			timeSoundbt.setSelected(BatteryService.zhengSoundFlag);
			PreferenceShareUtil.saveZhengTimeFlag(this, BatteryService.zhengSoundFlag);
			//统计用户设置整点报时提醒
			StatService.onEvent(MainActivity.this, "time_alert", BatteryService.zhengSoundFlag+"");
			break;
		case R.id.exitapp:
			//统计用户点击退出应用
			StatService.onEvent(MainActivity.this, "exit_app", "exit");
			stopService(new Intent(this, BatteryService.class));
			finish();
		case R.id.submitButton:
			finish();
			break;
		case R.id.changebt:
			
			if (!changejiawu) {
				changejiawu = true;
				seZiiv.setImageResource(sourceIds[0]);
				changebt.setText(R.string.shuai_dian_shu);
				
			}else{
				changejiawu = false;
				seZiiv.setImageResource(sourceIds2[0]);
				changebt.setText(R.string.shuai_jia_wu);
			}
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public boolean startVibrato(){		//定义震动
		mVibrator.vibrate( new long[]{500,200,500,200}, -1); //第一个｛｝里面是节奏数组， 第二个参数是重复次数，-1为不重复，非-1则从pattern的指定下标开始重复
		try {
			PlaySound.getInstance().play("sound/shake_sound_male.mp3", assetManager);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void shake_activity_back(View v) {     //标题栏 返回按钮
      	this.finish();
      }  
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mShakeListener != null) {
			mShakeListener.stop();
		}
	}
	
}
