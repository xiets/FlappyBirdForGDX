package cn.appkf.flappybird.actor;

import com.badlogic.gdx.graphics.g2d.Animation;

import cn.appkf.flappybird.MainGame;
import cn.appkf.flappybird.actor.base.BaseAnimationActor;
import cn.appkf.flappybird.res.Res;
import cn.appkf.flappybird.util.GameState;

/**
 * 小鸟, 小鸟可以看做是在竖直方向上跳跃, 水平方向上不动
 * 
 * @xietansheng
 */
public class BirdActor extends BaseAnimationActor {

    /** 当前游戏状态 */
    private GameState gameState;
    
    /** 小鸟竖直方向上的速度 */
    private float velocityY;
    
    /** 小鸟竖直方向上的重力加速度 */
    private float gravity = Res.Physics.GRAVITY;

    public BirdActor(MainGame mainGame) {
        super(mainGame);

        // 创建小鸟动画
        Animation animation = new Animation(
                0.2F,
                getMainGame().getAtlas().findRegions(Res.Atlas.IMAGE_BIRD_YELLOW_01_TO_03)
        );
        // 动画循环播放
        animation.setPlayMode(Animation.PlayMode.LOOP);
        // 设置小鸟动画
        setAnimation(animation);

        // 初始化为准备状态
        refreshFrameAndRotation(GameState.ready);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // 在 飞翔状态 或 状态水管后掉落到地板前 才应用物理效应
        if (gameState == GameState.fly || gameState == GameState.die) {
	        /*
	         * 应用物理效应（简单模拟物理效果, 帧率较低时物理效果的误差可能较大）
	         * v = v0 + a * t
	         * s = v0 * t + a * t^2
	         */
	        // 递增速度
	        velocityY += gravity * delta;
	        // 递增位移
	        setY(getY() + velocityY * delta);
        }

        // 正在飞翔状态时改变小鸟的角度
        if (gameState == GameState.fly) {
            changeBirdRotation(delta);
        }
    }

    /**
     * 根据游戏状态刷新小鸟状态
     * @param gameState
     */
    public void refreshFrameAndRotation(GameState gameState) {
        if (gameState == null || this.gameState == gameState) {
            return;
        }
        
        this.gameState = gameState;
        
        switch (this.gameState) {
            case ready: {
            	// 准备状态循环播放动画, 帧持续时间为 0.2 秒
                setPlayAnimation(true);
                setRotation(0);
                getAnimation().setFrameDuration(0.2F);
                break;
            }
            case fly: {
            	// 准备状态循环播放动画, 帧持续时间为 0.18 秒
                setPlayAnimation(true);
                getAnimation().setFrameDuration(0.18F);
                break;
            }
            case die: {
                break;
            }
            case gameOver: {
            	// 游戏结束状态停止播放动画, 并固定显示第1帧
                setPlayAnimation(false);
                setFixedShowKeyFrameIndex(1);
                setRotation(-90);
                break;
            }
        }
    }
    
    public float getVelocityY() {
		return velocityY;
	}

	public void setVelocityY(float velocityY) {
		this.velocityY = velocityY;
	}

	/**
     * 根据数值方向速度变化值改变小鸟的旋转角度
     * @param delta
     */
    private void changeBirdRotation(float delta) {

        float rotation = getRotation();

        rotation += (velocityY * delta);

        if (velocityY > 0) {
            // 向上飞时稍微加大角度旋转的速度
            rotation += (velocityY * delta) * 1.5F;
        } else {
            // 向下飞时稍微减小角度旋转的速度
            rotation += (velocityY * delta) * 0.2F;
        }

        // 校准旋转角度: -75 <= rotation <= 45
        if (rotation < -75) {
            rotation = -75;
        } else if (rotation > 45) {
            rotation = 45;
        }

        // 设置小鸟的旋转角度
        setRotation(rotation);
    }

}













