package cn.appkf.flappybird.actor.framework;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * 自定义演员
 * 
 * @xietansheng
 */
public class ImageActor extends Actor {

    private TextureRegion region;

    public ImageActor() {
    }

    public ImageActor(TextureRegion region) {
        this.region = region;
        setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
    }

    public TextureRegion getRegion() {
        return region;
    }

    public void setRegion(TextureRegion region) {
        this.region = region;
        if (this.region != null) {
            setSize(this.region.getRegionWidth(), this.region.getRegionHeight());
        } else {
            setSize(0, 0);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (region == null || !isVisible()) {
            return;
        }

        // 备份 batch 原本的 Color
        Color tempBatchColor = batch.getColor();

        // 获取演员的 Color
        Color color = getColor();

        // 将演员的 Color 结合 parentAlpha 设置到 batch
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        // 结合演员的属性绘制表示演员的纹理区域
        batch.draw(
                region,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation()
        );

        // 还原 batch 原本的 Color
        batch.setColor(tempBatchColor);
    }

    /****** 下面是便捷设置坐标的方法封装 ******/

    /**
     * 设置上边界的坐标
     * @param topY
     */
    public void setTopY(float topY) {
        setY(topY - getHeight());
    }

    /**
     * 获取上边界的坐标
     * @return
     */
    public float getTopY() {
        return getY() + getHeight();
    }

    /**
     * 设置右边界的坐标
     * @param rightX
     */
    public void setRightX(float rightX) {
        setX(rightX - getWidth());
    }

    /**
     * 获取右边的坐标
     * @return
     */
    public float getRightX() {
        return getX() + getWidth();
    }

    /**
     * 设置中心点坐标
     * @param centerX
     * @param centerY
     */
    public void setCenter(float centerX, float centerY) {
        setCenterX(centerX);
        setCenterY(centerY);
    }

    /**
     * 设置水平方向中心点坐标
     * @param centerX
     */
    public void setCenterX(float centerX) {
        setX(centerX - getWidth() / 2.0F);
    }

    /**
     * 设置竖直方向中心点坐标
     * @param centerY
     */
    public void setCenterY(float centerY) {
        setY(centerY - getHeight() / 2.0F);
    }

}






























