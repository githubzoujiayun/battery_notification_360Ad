package minggo.battery.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
/**
 * 保存用户选择声音播放的数据
 * @author minggo
 * @date 2013-8-21上午11:57:44
 */
public class PreferenceShareUtil {
	private static final String USER_DATA = "minggo_battery";
	private static final String LOW_POWER_SOUND = "low_power_sound";
	private static final String ZHENG_TIME_SOUND = "zheng_time_sound";
	/**
	 * 获取用户设置低电量的设置
	 * @param context
	 * @return
	 */
	public static boolean getLowPowerFlag(Context context) {
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_APPEND);
		if (share != null) {
			//System.out.println("数据库拿到的时候--->"+share.getBoolean(LOW_POWER_SOUND, false));
			return share.getBoolean(LOW_POWER_SOUND, false);
		}
		return false;
	}
	/**
	 * 获取用户整点报时声音的设置
	 * @param context
	 * @return
	 */
	public static boolean getZhengTimeFlag(Context context) {
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_APPEND);
		if (share != null) {
			return share.getBoolean(ZHENG_TIME_SOUND, false);
		}
		return false;
	}
	/**
	 * 保存用户低电量声音设置
	 * @param context
	 * @param flag
	 */
	public static void saveLowPowerFlag(Context context,boolean flag) {
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_APPEND);
		Editor editor = share.edit();
		editor.putBoolean(LOW_POWER_SOUND, flag);
		editor.commit();

	}
	/**
	 * 保存用户整点报时声音设置
	 * @param context
	 * @param flag
	 */
	public static void saveZhengTimeFlag(Context context,boolean flag) {
		SharedPreferences share = context.getSharedPreferences(USER_DATA, Context.MODE_APPEND);
		Editor editor = share.edit();
		editor.putBoolean(ZHENG_TIME_SOUND, flag);
		editor.commit();
	}

}
