package cn.appkf.flappybird.res;

/**
 * 资源常量
 * 
 * @xietansheng
 */
public interface Res {

    /** 固定世界宽度为 480, 高度根据实际屏幕比例换算 */
    public static final float FIX_WORLD_WIDTH = 480;

    /** 帧率显示所需要的位图字体 文件路径 */
    public static final String FPS_BITMAP_FONT_PATH = "fps/fps24px.fnt";

    /**
     * 相关物理参数（调节物理参数可改变游戏难度）
     */
    public static interface Physics {

        /** 水管和地板的移动速度, 单位: px/s */
        public static final float MOVE_VELOCITY = -150.0F;

        /** 点击屏幕时给小鸟的竖直方向上的速度, 单位: px/s */
        public static final float JUMP_VELOCITY = 320.0F;

        /** 小鸟竖直方向上的重力加速度, 单位: px/(s*s) */
        public static final float GRAVITY = -420.0F;

        /** 撞入深度, 小鸟撞入水管或地板该深度后才算碰撞 */
        public static final float DEPTH = 0.0F;

        /** 生成水管时间间隔, 单位: s */
        public static final float GENERATE_BAR_TIME_INTERVAL = 2.2F;

        /** 上下水管之间的间隔 */
        public static final float BAR_INTERVAL = 180.0F;
    }

    /**
     * 纹理图集
     */
    public static interface Atlas {

        /** 纹理图集 文件路径 */
        public static final String ATLAS_PATH = "atlas/images.atlas";

        /* 纹理图集中的小图名称 */
        public static final String IMAGE_GAME_BG = "game_bg";
        public static final String IMAGE_GAME_FLOOR = "game_floor";
        public static final String IMAGE_GAME_RESULT_BG = "game_result_bg";
        public static final String IMAGE_BAR_DOWN = "bar_down";
        public static final String IMAGE_BAR_UP = "bar_up";
        public static final String IMAGE_GAME_TAP_TIP = "game_tap_tip";
        public static final String IMAGE_GAME_READY = "game_ready";
        public static final String IMAGE_GAME_OVER = "game_over";

        public static final String IMAGE_GAME_START_01_TO_02 = "game_start";
        public static final String IMAGE_GAME_MEDAL_01_TO_04 = "game_medal";
        public static final String IMAGE_NUM_BIG_00_TO_09 = "num_big";
        public static final String IMAGE_NUM_SCORE_00_TO_09 = "num_score";
        public static final String IMAGE_BIRD_YELLOW_01_TO_03 = "bird_yellow";
    }

    /**
     * 音效
     */
    public static interface Audios {

        /** 音效资源文件夹路径 */
        public static final String AUDIO_BASE_DIR	= "audio/";

        // 音效资源路径
        public static final String AUDIO_DIE = AUDIO_BASE_DIR + "die.ogg";
        public static final String AUDIO_HIT = AUDIO_BASE_DIR + "hit.ogg";
        public static final String AUDIO_TOUCH = AUDIO_BASE_DIR + "touch.ogg";
        public static final String AUDIO_RESTART = AUDIO_BASE_DIR + "restart.ogg";
        public static final String AUDIO_SCORE = AUDIO_BASE_DIR + "score.ogg";
    }

    /**
     * Preferences 本地存储相关
     */
    public static interface Prefs {

        public static final String PREFS_FILE_NAME = "prefs_flappy_bird";

        public static final String KEY_BEST_SCORE = "best_score";
    }

}




















