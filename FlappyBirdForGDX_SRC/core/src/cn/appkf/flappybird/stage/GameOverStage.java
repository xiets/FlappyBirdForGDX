package cn.appkf.flappybird.stage;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import cn.appkf.flappybird.MainGame;
import cn.appkf.flappybird.actor.ResultGroup;
import cn.appkf.flappybird.actor.framework.ImageActor;
import cn.appkf.flappybird.res.Res;
import cn.appkf.flappybird.stage.base.BaseStage;

/**
 * 游戏结束时显示的舞台
 * 
 * @xietansheng
 */
public class GameOverStage extends BaseStage {

    /** 游戏结束提示 */
    private ImageActor gameOverActor;

    /** 分数结果显示 */
    private ResultGroup resultGroup;

     /** 重新开始按钮 */
    private ImageButton restartButton;

    /** 点击按钮恢复到初始状态时播放的音效 */
    private Sound restartSound;

    public GameOverStage(MainGame mainGame, Viewport viewport) {
        super(mainGame, viewport);
        init();
    }

    private void init() {
        // 获取音效
        restartSound = getMainGame().getAssetManager().get(Res.Audios.AUDIO_RESTART, Sound.class);

        // 创建游戏结束提示
        gameOverActor = new ImageActor(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_OVER));
        gameOverActor.setCenterX(getWidth() / 2);
        gameOverActor.setTopY(getHeight() - 150);
        addActor(gameOverActor);

        // 创建游戏结果显示组合
        resultGroup = new ResultGroup(getMainGame());
        resultGroup.setCenterX(getWidth() / 2);
        resultGroup.setTopY(gameOverActor.getY() - 30);
        addActor(resultGroup);

        // 创建重新开始按钮
        restartButton = new ImageButton(
                new TextureRegionDrawable(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_START_01_TO_02, 1)),
                new TextureRegionDrawable(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_START_01_TO_02, 2))
        );
        restartButton.setX(getWidth() / 2 - restartButton.getWidth() / 2);
        restartButton.setY(resultGroup.getY() - 30 - restartButton.getHeight());
        addActor(restartButton);

        // 按钮添加事件监听器
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // 按钮点击, 播放音效
                restartSound.play();
                // 重新开始游戏
                getMainGame().getGameScreen().restartReadyGame();
            }
        });
    }

    /**
     * 设置当前分数
     */
    public void setCurrentScore(int currScore) {
        resultGroup.updateCurrScore(currScore);
    }

}
















