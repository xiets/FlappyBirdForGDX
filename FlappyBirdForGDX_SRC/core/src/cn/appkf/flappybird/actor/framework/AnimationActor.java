package cn.appkf.flappybird.actor.framework;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * 动画演员
 * 
 * @xietansheng
 */
public class AnimationActor extends ImageActor {

    private Animation animation;

    /** 是否播放动画, 如果不播放动画, 则固定显示指定的帧 */
    private boolean isPlayAnimation = true;
    
    /** 不播放动画时固定显示的帧索引 */
    private int fixedShowKeyFrameIndex;

    /** 时间步 delta 的累加值 */
    private float stateTime;

    public AnimationActor() {
    }

    public AnimationActor(float frameDuration, Array<? extends TextureRegion> keyFrames) {
        this(new Animation(frameDuration, keyFrames));
    }

    public AnimationActor(float frameDuration, TextureRegion... keyFrames) {
        this(new Animation(frameDuration, keyFrames));
    }

    public AnimationActor(Animation animation) {
    	setAnimation(animation);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (animation != null) {
            TextureRegion region = null;
            if (isPlayAnimation) {
            	// 如果需要播放动画, 则累加时间步, 并按累加值获取需要显示的关键帧
                stateTime += delta;
                region = animation.getKeyFrame(stateTime);
            } else {
            	// 不需要播放动画, 则获取 fixedShowKeyFrameIndex 指定的关键帧 
                TextureRegion[] keyFrames = animation.getKeyFrames();
                if (fixedShowKeyFrameIndex >= 0 && fixedShowKeyFrameIndex < keyFrames.length) {
                    region = keyFrames[fixedShowKeyFrameIndex];
                }
            }
            // 设置当前需要显示的关键帧
            setRegion(region);
        }
    }

    public Animation getAnimation() {
        return animation;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
        // 默认先显示第 0 帧
        if (this.animation != null) {
            TextureRegion[] keyFrames = this.animation.getKeyFrames();
            if (keyFrames.length > 0) {
                setRegion(keyFrames[0]);
            }
        }
    }

    public boolean isPlayAnimation() {
        return isPlayAnimation;
    }

    /**
     * 设置是否需要播放动画 <br/>
     * 
     * true: 按 Animation 对象指定的播放模式播放动画 <br/>
     * false: 不播放动画, 始终显示 fixedShowKeyFrameIndex 指定的关键帧 <br/>
     */
    public void setPlayAnimation(boolean isPlayAnimation) {
        this.isPlayAnimation = isPlayAnimation;
    }

    public int getFixedShowKeyFrameIndex() {
        return fixedShowKeyFrameIndex;
    }

    /**
     * 设置不播放动画时固定显示的关键帧索引
     */
    public void setFixedShowKeyFrameIndex(int fixedShowKeyFrameIndex) {
        this.fixedShowKeyFrameIndex = fixedShowKeyFrameIndex;
    }

}























