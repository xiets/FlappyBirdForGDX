package cn.appkf.flappybird.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;

import cn.appkf.flappybird.MainGame;
import cn.appkf.flappybird.actor.BarActor;
import cn.appkf.flappybird.actor.BigScoreActor;
import cn.appkf.flappybird.actor.BirdActor;
import cn.appkf.flappybird.actor.FloorActor;
import cn.appkf.flappybird.actor.framework.ImageActor;
import cn.appkf.flappybird.res.Res;
import cn.appkf.flappybird.stage.base.BaseStage;
import cn.appkf.flappybird.util.CollisionUtils;
import cn.appkf.flappybird.util.GameState;

/**
 * 主游戏舞台（主要的游戏逻辑都在这里）
 * 
 * @xietansheng
 */
public class GameStage extends BaseStage {

    /** 背景 */
    private ImageActor bgActor;

    /** 地板 */
    private FloorActor floorActor;

    /** 点击提示 */
    private ImageActor tapTipActor;

    /** 准备状态提示 */
    private ImageActor getReadyActor;

    /** 小鸟 */
    private BirdActor birdActor;

    /** 大数字的分数显示 */
    private BigScoreActor bigScoreActor;
    
    /** 当前界面可见的所有水管集合 */
 	private final ArrayList<BarActor> barActorList = new ArrayList<BarActor>();
 	/** 水管对象缓存池, 因为水管需要频繁生成和移除, 所有使用对象池减少对象的频繁创建 */
 	private Pool<BarActor> barActorPool;
 	
 	/** 小鸟初始状态时的Y轴坐标 */
    public float birdStartPositionY;
    
    /** 下方水管的 TopY 坐标的最小值 */
 	private float minDownBarTopY;
 	/** 下方水管的 TopY 坐标的最大值 */
 	private float maxDownBarTopY;
 	
 	/** 距离下次生成水管的时间累加器 */
 	private float generateBarTimeCounter;

    /** 碰撞到水管的音效 */
    private Sound hitSound;

    /** 得分音效 */
    private Sound scoreSound;

    /** 点击屏幕时播放的音效 */
    private Sound touchSound;

    /** 撞到地板时的音效 */
    private Sound dieSound;

 	/** 游戏状态 */
 	private GameState gameState;

    public GameStage(MainGame mainGame, Viewport viewport) {
        super(mainGame, viewport);
        init();
    }

    private void init() {
        /*
         * 创建背景
         */
        bgActor = new ImageActor(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_BG));
        // 位置设置到舞台中心
        bgActor.setCenter(getWidth() / 2, getHeight() / 2);
        addActor(bgActor);

        /*
         * 创建地板
         */
        floorActor = new FloorActor(getMainGame());
        // 设置地板移动速度
        floorActor.setMoveVelocity(Res.Physics.MOVE_VELOCITY);
        // 水平居中
        floorActor.setCenterX(getWidth() / 2);
        // 兼容性设置纵坐标（为了兼容不同尺寸的屏幕）
        floorActor.setTopY(
        		Math.min(
        				floorActor.getHeight(), 
        				getHeight() * (floorActor.getHeight() / 800.0F)
        		)
        );
        addActor(floorActor);

        /*
         * 创建大数字分数显示
         */
        bigScoreActor = new BigScoreActor(getMainGame());
        bigScoreActor.setCenterX(getWidth() / 2);
        bigScoreActor.setTopY(getHeight() - 50);
        addActor(bigScoreActor);

        /*
         * 创建 准备提示
         */
        getReadyActor = new ImageActor(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_READY));
        getReadyActor.setCenterX(getWidth() / 2);
        getReadyActor.setTopY(getHeight() - 182);
        addActor(getReadyActor);

        /*
         * 创建 点击提示
         */
        tapTipActor = new ImageActor(getMainGame().getAtlas().findRegion(Res.Atlas.IMAGE_GAME_TAP_TIP));
        tapTipActor.setCenterX(getWidth() / 2);
        // 点击提示Y轴坐标设置到 准备提示 和 地板 之间的中心
        tapTipActor.setCenterY((getReadyActor.getY() + floorActor.getTopY()) / 2);
        addActor(tapTipActor);

        /*
         * 创建小鸟
         */
        birdActor = new BirdActor(getMainGame());
        birdActor.setX(tapTipActor.getX() - 20);
        birdActor.setY(tapTipActor.getY() + 80);
        // 保存小鸟开始位置 Y 坐标以便重新开始游戏时恢复至初始状态
        birdStartPositionY = birdActor.getY();
        birdActor.setScale(1.2F);
        // 将缩放和旋转的支点设置到小鸟中心
        birdActor.setOrigin(Align.center);
        addActor(birdActor);
        
		// 将地板设置到小鸟前面(ZIndex 必须在对象添加到舞台后设置才有效, ZIndex 越大显示越前)
		floorActor.setZIndex(birdActor.getZIndex());
		// 分数显示排在最前面
		bigScoreActor.setZIndex(getRoot().getChildren().size - 1);
		
		/*
		 * 水管
		 */
 		// 获取水管对象池(BarActor 中必须有空参构造方法用于给 Pool 通过反射实例化对象)
		// 注意: Pools.get() 方式获取的对象池会使用到反射, 而 html 平台是通过 GWT 实现的,
		// GWT 不支持反射, 因此要想能发布到 html 平台, 需要自己手动继承 Pool 抽象类实现对象池。
 		barActorPool = Pools.get(BarActor.class, 10);
		
		/*
		 * 计算下方水管TopY坐标值范围, 水管高度为400（为了兼容不同尺寸屏幕）
		 */
		float barHeight = 400;
		float maxRegion = 300;
		minDownBarTopY = Math.max(floorActor.getTopY() + 40, getHeight() - barHeight - Res.Physics.BAR_INTERVAL);
		maxDownBarTopY = Math.min(minDownBarTopY + maxRegion, getHeight() - Res.Physics.BAR_INTERVAL - 60);
		maxDownBarTopY = Math.min(maxDownBarTopY, floorActor.getTopY() + barHeight);

        /*
         * 获取音效
         */
        hitSound = getMainGame().getAssetManager().get(Res.Audios.AUDIO_HIT, Sound.class);
        scoreSound = getMainGame().getAssetManager().get(Res.Audios.AUDIO_SCORE, Sound.class);
        touchSound = getMainGame().getAssetManager().get(Res.Audios.AUDIO_TOUCH, Sound.class);
        dieSound = getMainGame().getAssetManager().get(Res.Audios.AUDIO_DIE, Sound.class);
		
		/*
		 * 初始为游戏准备状态
		 */
		ready();
    }
    
    /**
	 * 游戏状态改变方法01: 游戏准备状态
	 */
	public void ready() {
		gameState = GameState.ready;

		// 设置小鸟初始Y轴坐标
		birdActor.setY(birdStartPositionY);
		// 刷新小鸟的显示帧和旋转角度
		birdActor.refreshFrameAndRotation(gameState);

		// 地板停止移动
		floorActor.setMove(false);

		// 清空水管
		for (BarActor barActor : barActorList) {
			// 从舞台中移除水管
			getRoot().removeActor(barActor);
		}
		// 从集合中移除水管
		barActorList.clear();

		// 设置点击提示和准备提示可见
		tapTipActor.setVisible(true);
		getReadyActor.setVisible(true);

		// 分数清零
		bigScoreActor.setNum(0);
		// 更新分数后重新水平居中
        bigScoreActor.setCenterX(getWidth() / 2);
	}

	/**
	 * 游戏状态方法02: 开始游戏
	 */
	private void startGame() {
		gameState = GameState.fly;

		// 刷新小鸟显示帧和旋转角度
		birdActor.refreshFrameAndRotation(gameState);

		// 地板开始移动
		floorActor.setMove(true);

		// 隐藏提示
		tapTipActor.setVisible(false);
		getReadyActor.setVisible(false);

		// 生成水管时间计数器清零
		generateBarTimeCounter = 0.0F;
	}

	/**
	 * 游戏状态方法03: 碰撞到水管(小鸟死亡, 但游戏还没有结束, 等到小鸟掉落在地板上游戏才真正结束)
	 */
	private void collisionBar() {
		gameState = GameState.die;

		// 所有水管停止移动
		for (BarActor barActor : barActorList) {
			if (barActor.isMove()) {
				barActor.setMove(false);
			}
		}

		// 地板停止移动
		floorActor.setMove(false);
	}

	/**
	 * 游戏状态方法04: 游戏结束(小鸟直接撞到地板 或 撞到水管后掉落到地板)
	 */
	private void gameOver() {
		gameState = GameState.gameOver;

		// 刷新小鸟显示帧和旋转角度
		birdActor.refreshFrameAndRotation(gameState);

		// 地板停止移动
		floorActor.setMove(false);

		// 停止移动所有水管
		for (BarActor barActor : barActorList) {
			if (barActor.isMove()) {
				barActor.setMove(false);
			}
		}

		// 显示游戏结束舞台
		getMainGame().getGameScreen().showGameOverStage(bigScoreActor.getNum());
	}

	/**
	 * 随机生成一对水管
	 */
	private void generateBar() {
		// 随机生成下方水管的 TopY 坐标
		float downBarTopY = MathUtils.random(minDownBarTopY, maxDownBarTopY);

		// 创建下方水管（从对象池中获取）
		BarActor downBarActor = barActorPool.obtain();
		downBarActor.setMainGame(getMainGame());
		downBarActor.setUpBar(false);
		downBarActor.setX(getWidth());
		downBarActor.setTopY(downBarTopY);
        downBarActor.setMoveVelocity(Res.Physics.MOVE_VELOCITY);
		// 创建后水管立即开始移动
        downBarActor.setMove(true);
        addActor(downBarActor);
        // 将水管加入到集合, 方便进行碰撞检测
        barActorList.add(downBarActor);
        // 将水管设置到小鸟后面(必须在 actor 添加到 stage 后设置 ZIndex 才有效)
        downBarActor.setZIndex(birdActor.getZIndex());

        // 创建上方水管
        BarActor upBarActor = barActorPool.obtain();
        upBarActor.setMainGame(getMainGame());
        upBarActor.setUpBar(true);
        upBarActor.setX(getWidth());
        upBarActor.setY(downBarActor.getTopY() + Res.Physics.BAR_INTERVAL);
        upBarActor.setMoveVelocity(Res.Physics.MOVE_VELOCITY);
        upBarActor.setMove(true);
        addActor(upBarActor);
		barActorList.add(upBarActor);
		upBarActor.setZIndex(birdActor.getZIndex());
	}

	/**
	 * 逻辑校验(碰撞检测, 得分检测, 移除水管)
	 */
	private void checkLogic() {
		// 正在飞翔状态时才判断是否碰撞到水管或通过水管
		if (gameState == GameState.fly) {
			
			for (BarActor barActor : barActorList) {
				
				// 是否碰撞到水管
				if (CollisionUtils.isCollision(birdActor, barActor, Res.Physics.DEPTH)) {
					Gdx.app.log(MainGame.TAG, "Collision Bar.");
					// 小鸟碰撞到水管, 则小鸟死亡
					collisionBar();
					// 播放碰撞音效
					hitSound.play();
					break;
				}
				
				// 小鸟通过上方水管的右边, 则认为已通过水管, 增加分数 (上下两条水管只需要检测一个, 因为水管是一对的, X 轴坐标相同)
				if (!barActor.isPassByBird() && barActor.isUpBar() && birdActor.getX() > barActor.getRightX()) {
					// 更新分数
					bigScoreActor.addNum(1);
                    // 更新分数后重新水平居中
                    bigScoreActor.setCenterX(getWidth() / 2);
					// 播放得分音效
					scoreSound.play();
					// 标记该水管为已通过状态, 下次不再检测是否通过该水管
					barActor.setPassByBird(true);
					Gdx.app.log(MainGame.TAG, "Score: " + bigScoreActor.getNum());
				}
				
			}
			
		}

		// 移除移动出屏幕外的水管
		Iterator<BarActor> it = barActorList.iterator();
		BarActor barActor = null;
		while (it.hasNext()) {
			barActor = it.next();
			if (barActor.getRightX() < 0) {
				// 从舞台中移除水管
				getRoot().removeActor(barActor);
				// 从集合中移除水管
				it.remove();
				// 回收水管对象(放回到对象池中)
				barActorPool.free(barActor);
			}
		}

		// 判断小鸟是否撞到地板
		if (CollisionUtils.isCollision(birdActor, floorActor, Res.Physics.DEPTH)) {
			Gdx.app.log(MainGame.TAG, "Collision Floor.");
			// 小鸟撞到地板, 则游戏结束
			gameOver();
			// 播放游戏结束音效
			dieSound.play();
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		// 小鸟处于飞翔状态(没有碰撞到水管和地板) 或 死亡状态(已撞到水管但还未掉落到地面)时进行逻辑检测
		if (gameState == GameState.fly || gameState == GameState.die) {
			// 逻辑检测(碰撞检测, 得分检测, 移除已移动出屏幕的水管)
			checkLogic();
		}

		// 正在飞翔状态才执行生成水管的逻辑
		if (gameState == GameState.fly) {
			// 累计下一次水管生成时间
			generateBarTimeCounter += delta;
			// 累计值达到水管生成间隔时间后生成一对水管
			if (generateBarTimeCounter >= Res.Physics.GENERATE_BAR_TIME_INTERVAL) {
				generateBar();
				// 清零累计变量, 重新累计下一次水管生成时间
				generateBarTimeCounter = 0;
			}
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		barActorList.clear();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (gameState == GameState.ready) {
			// 在准备状态下触摸屏幕开始游戏
			startGame();
			// 播放触摸屏幕音效
			touchSound.play();
			// 开始游戏时给小鸟设置一个向上的速度
			birdActor.setVelocityY(Res.Physics.JUMP_VELOCITY);

		} else if (gameState == GameState.fly) {
			// 小鸟正在飞翔状态(没有碰撞到水管和地板), 并且没有飞出屏幕上方, 则响应屏幕触摸给小鸟设置一个向上的速度
			if (birdActor.getTopY() < getHeight()) {
				birdActor.setVelocityY(Res.Physics.JUMP_VELOCITY);
				// 播放触摸屏幕音效
				touchSound.play();
			}
		}

		return true;
	}

}


















