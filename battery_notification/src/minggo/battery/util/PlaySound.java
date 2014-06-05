package minggo.battery.util;

import java.io.IOException;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
/**
 * 声音播放工具 
 * @author minggo
 * @date 2013-8-7上午09:17:00
 */
public class PlaySound {
	private static MediaPlayer player;
	private static PlaySound playSound;
	private boolean flag;
	private PlaySound(){
		
	}
	
	public static PlaySound getInstance(){
		if (playSound==null) {
			playSound = new PlaySound();
			
		}
		return playSound;
	}
	/**
	* 调用方式:play("across.mp3",getResources().getAssets());
	* @param filename 要播放的音频文件名
	* @param asm AssetManager
	* @throws IOException
	*/
	public final void play(String filename,AssetManager asm) throws IOException{
		if (!flag) {
			flag = true;
			AssetFileDescriptor afd = asm.openFd(filename);
			player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
			player.prepare();
			player.start();
			player.setOnCompletionListener(new OnCompletionListener() {
				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.release();
					flag = false;
				}
			});
		}
		
	}
}
