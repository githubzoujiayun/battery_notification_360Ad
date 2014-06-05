package minggo.battery.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 时间[避免时间过期方法使用]
 * @author minggo
 * @date 2013-8-7下午04:35:30
 */
public class MinggoDate{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dateStr;
	
	private Date date;
	
	private Calendar calendar;
	
	public MinggoDate(){
		date = new Date();
		this.toString();
		calendar = Calendar.getInstance();
	}
	
	/**
	 * 格式化成yyyy-MM-dd hh:mm:ss
	 */
	public String toString(){
		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		dateStr = fm.format(date);
		return dateStr;
	}
	/**
	 * 获取long类型的time
	 * @return
	 */
	public long getTimeOfLong(){
		return date.getTime();
	}
	/**
	 * 获取年份
	 */
	public int getYear(){
		return Integer.parseInt(dateStr.substring(0, 4));
	}
	/**
	 * 获取月份
	 */
	public int getMonth(){
		return Integer.parseInt(dateStr.substring(5, 7));
	}
	/**
	 * 获取日
	 */
	public int getDay(){
		return Integer.parseInt(dateStr.substring(8, 10));
	}
	/**
	 * 获取小时
	 * @return
	 */
	public int getHour(){
		return Integer.parseInt(dateStr.substring(11, 13));
	}
	/**
	 * 获取小时[24]
	 * @return
	 */
	public int get24Hour(){
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	/**
	 * 获取分
	 * @return
	 */
	public int getMinutes(){
		return Integer.parseInt(dateStr.substring(14, 16));
	}
	/**
	 * 获取秒
	 */
	public int getSeconds(){
		return Integer.parseInt(dateStr.substring(17, 19));
	}
	/**
	 * 获取星期
	 * @return
	 */
	public int getDayOfWeek(){
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	/**
	 * 获取字符串的星期
	 * @return
	 */
	public String getWeek2str(){
		String day = "";
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			day = "星期天";
			break;
		case 2:
			day = "星期一";
			break;
		case 3:
			day = "星期二";
			break;
		case 4:
			day = "星期三";
			break;
		case 5:
			day = "星期四";
			break;
		case 6:
			day = "星期五";
			break;
		case 7:
			day = "星期六";
			break;
		default:
			break;
		}
		return day;
	}
}
