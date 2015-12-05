package cn.appkf.flappybird.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import cn.appkf.flappybird.MainGame;
import cn.appkf.flappybird.stage.GameOverStage;
import cn.appkf.flappybird.stage.GameStage;

/**
 * 主游戏场景
 * 
 * @xietansheng
 */
public class GameScreen extends ScreenAdapter {

    private MainGame mainGame;

    /** 主游戏舞台 */
    private GameStage gameStage;

    /** 游戏结束舞台 */
    private GameOverStage gameOverStage;

    public GameScreen(MainGame mainGame) {
        this.mainGame = mainGame;
        init();
    }

    private void init() {
        // 创建主游戏舞台
        gameStage = new GameStage(
                getMainGame(),
                new StretchViewport(
                        mainGame.getWorldWidth(),
                        mainGame.getWorldHeight()
                )
        );

        // 创建游戏结束舞台
        gameOverStage = new GameOverStage(
                getMainGame(),
                new StretchViewport(
                        mainGame.getWorldWidth(),
                        mainGame.getWorldHeight()
                )
        );
        gameOverStage.setVisible(false);

        // 将输入处理设置到主游戏舞台
        Gdx.input.setInputProcessor(gameStage);
    }

    /**
     * 显示游戏结束舞台
     */
    public void showGameOverStage(int currScore) {
        // 游戏结束舞台可见
        gameOverStage.setVisible(true);

        // 将输入处理设置到游戏结束舞台
        Gdx.input.setInputProcessor(gameOverStage);

        // 设置当前分数
        gameOverStage.setCurrentScore(currScore);
    }

    /**
     * 重新开始准备游戏
     */
    public void restartReadyGame() {
        // 游戏结束舞台不可见
        gameOverStage.setVisible(false);

        // 将输入处理设置回主游戏舞台
        Gdx.input.setInputProcessor(gameStage);

        // 更新游戏准备状态
        gameStage.ready();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        // 更新并绘制舞台（主游戏舞台优先处理）

        if (gameStage.isVisible()) {
            gameStage.act();
            gameStage.draw();
        }

        if (gameOverStage.isVisible()) {
            gameOverStage.act();
            gameOverStage.draw();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        // 场景销毁时, 同时销毁所有的舞台
        if (gameStage != null) {
            gameStage.dispose();
        }
        if (gameOverStage != null) {
            gameOverStage.dispose();
        }
    }

    public MainGame getMainGame() {
        return mainGame;
    }

    public GameStage getGameStage() {
        return gameStage;
    }

    public GameOverStage getGameOverStage() {
        return gameOverStage;
    }

}


























