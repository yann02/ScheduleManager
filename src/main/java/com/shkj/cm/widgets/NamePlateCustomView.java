package com.shkj.cm.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.dosmono.platecommon.util.PreferencesUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.shkj.cm.R;

/**
 * Copyright (C), 2015-2021, 海南双猴科技有限公司
 *
 * @Description: 暂无
 * @Author: Yingyan Wu
 * @CreateDate: 2021/4/28 11:22
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class NamePlateCustomView extends RelativeLayout {
    private Context mContext;
    private View mView;
    private TextView tv_company_name;
    private TextView tv_user_name;
    private TextView tv_positional_name;

    public NamePlateCustomView(Context context) {
        this(context, (AttributeSet)null);
    }

    public NamePlateCustomView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NamePlateCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.initData();
    }

    private void initData() {
        LayoutInflater mInflater = LayoutInflater.from(this.mContext);
        this.mView = mInflater.inflate(R.layout.layout_nameplate_display_custom_view, (ViewGroup)null);
        this.addView(this.mView);
        this.tv_company_name = (TextView)this.mView.findViewById(R.id.tv_company_name);
        this.tv_user_name = (TextView)this.mView.findViewById(R.id.tv_user_name);
        this.tv_positional_name = (TextView)this.mView.findViewById(R.id.tv_positional_name);
        this.refreshData();
        LiveEventBus.get("refresh_nameplate_display").observe((LifecycleOwner)this.mContext, new Observer<Object>() {
            public void onChanged(Object o) {
                NamePlateCustomView.this.refreshData();
            }
        });
    }

    private void refreshData() {
        try {
            this.tv_company_name.setText((CharSequence) PreferencesUtil.getValue("key_company_name_nameplate_display", ""));
            this.tv_user_name.setText((CharSequence)PreferencesUtil.getValue("key_user_name_nameplate_display", ""));
            this.tv_positional_name.setText((CharSequence)PreferencesUtil.getValue("KEY_POSITIONAL_NAME_NAMEPLATE_DISPLAY", ""));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
