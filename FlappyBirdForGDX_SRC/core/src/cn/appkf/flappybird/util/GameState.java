package cn.appkf.flappybird.util;

/**
 * 游戏状态
 * 
 * @xietansheng
 */
public enum GameState {

    /** 准备状态 */
    ready,

    /** 游戏状态(小鸟处于飞翔状态) */
    fly,

    /** 小鸟死亡状态(撞到水管, 掉落到地板前) */
    die,

    /** 游戏结束(撞到地板或撞到水管后掉落到地板) */
    gameOver;
}
