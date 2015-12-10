package com.getyasa.app.camera;

import android.content.Context;

import com.getyasa.R;
import com.getyasa.app.camera.effect.FilterEffect;
import com.getyasa.app.camera.util.GPUImageFilterTools;

import java.util.ArrayList;
import java.util.List;


public class EffectService {

    private static EffectService mInstance;

    public static EffectService getInst() {
        if (mInstance == null) {
            synchronized (EffectService.class) {
                if (mInstance == null)
                    mInstance = new EffectService();
            }
        }
        return mInstance;
    }

    private EffectService() {
    }

    public List<FilterEffect> getLocalFilters(Context context) {
        List<FilterEffect> filters = new ArrayList<FilterEffect>();
        filters.add(new FilterEffect(context.getString(R.string.effect_normal), GPUImageFilterTools.FilterType.NORMAL, 0));

        filters.add(new FilterEffect(context.getString(R.string.effect_ambiguous), GPUImageFilterTools.FilterType.ACV_AIMEI, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_light_blue), GPUImageFilterTools.FilterType.ACV_DANLAN, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_yolk), GPUImageFilterTools.FilterType.ACV_DANHUANG, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_retro), GPUImageFilterTools.FilterType.ACV_FUGU, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_high_cold), GPUImageFilterTools.FilterType.ACV_GAOLENG, 0));
        //filters.add(new FilterEffect(context.getString(R.string.effect_reminiscence), GPUImageFilterTools.FilterType.ACV_HUAIJIU, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_film), GPUImageFilterTools.FilterType.ACV_JIAOPIAN, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_lovely), GPUImageFilterTools.FilterType.ACV_KEAI, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_lonely), GPUImageFilterTools.FilterType.ACV_LOMO, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_strengthen), GPUImageFilterTools.FilterType.ACV_MORENJIAQIANG, 0));
        //filters.add(new FilterEffect(context.getString(R.string.effect_heart_warming), GPUImageFilterTools.FilterType.ACV_NUANXIN, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_fresh), GPUImageFilterTools.FilterType.ACV_QINGXIN, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_descent), GPUImageFilterTools.FilterType.ACV_RIXI, 0));
        filters.add(new FilterEffect(context.getString(R.string.effect_warm), GPUImageFilterTools.FilterType.ACV_WENNUAN, 0));

        return filters;
    }

}
