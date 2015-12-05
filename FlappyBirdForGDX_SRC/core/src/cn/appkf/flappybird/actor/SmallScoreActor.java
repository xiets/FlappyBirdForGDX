package cn.appkf.flappybird.actor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import cn.appkf.flappybird.MainGame;
import cn.appkf.flappybird.res.Res;

/**
 * 小数字分数显示
 * 
 * @xietansheng
 */
public class SmallScoreActor extends NumGroup {

    public SmallScoreActor(MainGame mainGame) {
        super(mainGame);
        Array<TextureAtlas.AtlasRegion> regions = mainGame.getAtlas().findRegions(Res.Atlas.IMAGE_NUM_SCORE_00_TO_09);
        TextureRegion[] digitRegions = new TextureRegion[regions.size];
        for (int i = 0; i < regions.size; i++) {
            digitRegions[i] = regions.get(i);
        }
        setDigitRegions(digitRegions);
    }

}



















