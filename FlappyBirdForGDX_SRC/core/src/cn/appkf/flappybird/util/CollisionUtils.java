package cn.appkf.flappybird.util;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * 碰撞检测工具
 * 
 * @xietansheng
 */
public class CollisionUtils {
	
	private static final Rectangle rect1 = new Rectangle();
	private static final Rectangle rect2 = new Rectangle();
	private static final Rectangle tempRect = new Rectangle();

	/**
	 * 判断两个演员是否碰撞
	 * 
	 * @param actor1
	 * @param actor2
	 * @param depth 碰撞深度, 两个演员的包围矩阵重叠部分的矩形宽高均超过 depth 才算碰撞
	 * @return
	 */
	public static synchronized boolean isCollision(Actor actor1, Actor actor2, float depth) {
        if (actor1 == null || actor2 == null) {
            return false;
        }
        
        /* 这里只是粗略地判断两个演员的碰撞, 没有考虑演员自身旋转等导致的误差, 精确的碰撞检测可使用 Box2D */

        // 获取 演员1 缩放后的包围矩阵
        rect1.setSize(
                actor1.getWidth() * actor1.getScaleX(),
                actor1.getHeight() * actor1.getScaleY()
        );
        rect1.setPosition(
                actor1.getX() - (actor1.getOriginX() * actor1.getScaleX() - actor1.getOriginX()),
                actor1.getY() - (actor1.getOriginY() * actor1.getScaleY() - actor1.getOriginY())
        );

        // 获取 演员2 缩放后的包围矩阵
        rect2.setSize(
                actor2.getWidth() * actor2.getScaleX(),
                actor2.getHeight() * actor2.getScaleY()
        );
        rect2.setPosition(
                actor2.getX() - (actor2.getOriginX() * actor2.getScaleX() - actor2.getOriginX()),
                actor2.getY() - (actor2.getOriginY() * actor2.getScaleY() - actor2.getOriginY())
        );

        float doubleDepth = 2 * depth;

        if (rect2.width > doubleDepth && rect2.height > doubleDepth) {
            // 将矩形的四条边分别向中心缩减 depth 后再判断是否碰撞
            tempRect.set(
                    rect2.x + depth, rect2.y + depth,
                    rect2.width - doubleDepth, rect2.height - doubleDepth
            );
            return tempRect.overlaps(rect1);

        } else if (rect1.width > doubleDepth && rect1.height > doubleDepth) {
            tempRect.set(
                    rect1.x + depth, rect1.y + depth,
                    rect1.width - doubleDepth, rect1.height - doubleDepth
            );
            return tempRect.overlaps(rect2);
        }

        // 如果两个演员的包围矩阵宽高均比两倍的碰撞深度还小, 则不考虑碰撞深度
        return rect1.overlaps(rect2);
	}
	
}




















