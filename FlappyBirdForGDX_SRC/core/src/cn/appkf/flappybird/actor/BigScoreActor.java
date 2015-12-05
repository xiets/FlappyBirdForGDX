package cn.appkf.flappybird.actor;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import cn.appkf.flappybird.MainGame;
import cn.appkf.flappybird.res.Res;

/**
 * 大数字分数显示
 * 
 * @xietansheng
 */
public class BigScoreActor extends NumGroup {

    public BigScoreActor(MainGame mainGame) {
        super(mainGame);
        Array<TextureAtlas.AtlasRegion> atlasRegions = mainGame.getAtlas().findRegions(Res.Atlas.IMAGE_NUM_BIG_00_TO_09);
        TextureRegion[] digitRegions = new TextureRegion[atlasRegions.size];
        for (int i = 0; i < atlasRegions.size; i++) {
            digitRegions[i] = atlasRegions.get(i);
        }
        setDigitRegions(digitRegions);
    }

}



















