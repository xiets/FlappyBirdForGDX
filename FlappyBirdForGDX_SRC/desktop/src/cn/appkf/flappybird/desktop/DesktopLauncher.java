package cn.appkf.flappybird.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import cn.appkf.flappybird.MainGame;

public class DesktopLauncher {
	
	/** 窗口宽度参考值 */
	public static final float PIX_WIDTH = 320;
	
	/** 窗口宽高比, 适当调节宽高比可以查看在不同屏幕上的效果, 例如设置为 9:16, 3:4, 2:3 */
	public static final float RATIO = 2.0F / 3.0F;
	
	/** 适当改变缩放比以适应自己的电脑屏幕 */
	public static final float SCALE = 1.0F;
	
	
	public static void main (String[] args) {
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
        config.width = (int) (PIX_WIDTH * SCALE);         		// 窗口宽度
        config.height = (int) ((PIX_WIDTH / RATIO) * SCALE);	// 窗口高度
        
        config.resizable = false;				// 窗口设置为大小不可改变
        
        // 手动设置窗口标题, 如果没有设置则会使用游戏程序入口类的类名作为标题
        // config.title = "Flappy Bird";
        
        // 手动设置设置窗口图标
        // config.addIcon(path, fileType);
        
        new LwjglApplication(new MainGame(), config);
	}
	
}



















