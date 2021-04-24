package com.hugo.study_toolbar;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.StateSet;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;

/**
 * 代码生成 各种selector shape
 */
public class Theme {
    /**
     * 获取按钮选择器
     * (color & 0x00ffffff) | 0x19000000,前面的相与是为了获得颜色值对应的十六进制，后面的操作是为了添加一个透明度
     * 以android.R.color.holo_red_light为例 是一个32位的颜色值
     * FFFF FFFF FFFF 4444
     * <p>
     * <p>
     * FF 4444
     * 不透明度 0x19000000 接近10%
     *
     * @param color
     * @return
     */
    public static Drawable getRoundRectSelectorDrawable(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            Drawable maskDrawable = createRoundRectDrawable(ConvertUtils.dp2px(3), 0xffffffff);
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{StateSet.WILD_CARD},
                    new int[]{(color & 0x00ffffff) | 0x19000000}
            );
            // mask 直译过来有遮罩的意思，它会限定水波纹的范围
            return new RippleDrawable(colorStateList, null, maskDrawable);
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, createRoundRectDrawable(dp2px(3), (color & 0x00ffffff) | 0x19000000));
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, createRoundRectDrawable(dp2px(3), (color & 0x00ffffff) | 0x19000000));
            stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0x00000000));
            return stateListDrawable;
        }
    }

    public static Drawable getRoundRectSelectorDrawable(int corner, int normalColor, int pressedColor) {
        if (Build.VERSION.SDK_INT >= 21) {
            Drawable maskDrawable = createRoundRectDrawable(ConvertUtils.dp2px(10), 0xffffffff);
            Drawable contentDrawable = createRoundRectDrawable(ConvertUtils.dp2px(10), normalColor);
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{StateSet.WILD_CARD},
                    new int[]{pressedColor}
            );
            // mask 直译过来有遮罩的意思，它会限定水波纹的范围
            return new RippleDrawable(colorStateList, contentDrawable, maskDrawable);
        } else {
            int i = pressedColor & 0x00ffffff;
            LogUtils.e("-->>原始颜色=" + pressedColor);
            LogUtils.e("-->>颜色&结果=" + i);
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, createRoundRectDrawable(dp2px(5), (pressedColor)));
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, createRoundRectDrawable(dp2px(5), (pressedColor)));
            // StateSet.WILD_CARD 通配符,一个状态规范，将被所有的states匹配。本质上是空状态数组
            stateListDrawable.addState(StateSet.WILD_CARD, createRoundRectDrawable(dp2px(5), normalColor));
            return stateListDrawable;
        }
    }


    public static Drawable createRoundRectDrawable(int rad, int defaultColor) {
        // RoundRectShape 需要传入有8个float的数组，每个角两个半径
        // inset 设置内边距
        ShapeDrawable defaultDrawable = new ShapeDrawable(new RoundRectShape(new float[]{rad, rad, rad, rad, rad, rad, rad, rad}, null, null));
        defaultDrawable.getPaint().setColor(defaultColor);
        return defaultDrawable;
    }

    /*-------------------------------------------------------------*/

    /**
     * item选择器
     *
     * @param color
     * @return
     */
    public static Drawable createSelectorDrawable(int color) {
        return createSelectorDrawable(color, 0, -1);
    }

    public static Drawable createSelectorDrawable(int color, int maskType, int radius) {
        if (Build.VERSION.SDK_INT >= 21) {
            //白色maskDrawable
            Drawable maskDrawable = new ColorDrawable(0xffffffff);
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{StateSet.WILD_CARD},
                    new int[]{color}
            );
            RippleDrawable rippleDrawable = new RippleDrawable(colorStateList, null, maskDrawable);
            return rippleDrawable;
        } else {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(color));
            stateListDrawable.addState(new int[]{android.R.attr.state_selected}, new ColorDrawable(color));
            stateListDrawable.addState(StateSet.WILD_CARD, new ColorDrawable(0x00000000));
            return stateListDrawable;
        }
    }

    private static int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
