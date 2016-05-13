package com.jojo.readtopactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <p>Title: 视频录制SDK</p>
 * <p/>
 * <p>Description: </p>
 * 解析中间件协议
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: kuaiyouxi.com</p>
 * <p>Created on 2016/5/13.
 *
 * @author jalen-pc
 * @version 1.1.1
 */

public class FloatViewManager {
    private static volatile FloatViewManager singleton;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mToastViewParams;
    private LinearLayout mToastView;
    private TextView mPackageNameView;
    private TextView mShareView;


    private FloatViewManager(Context pContext) {
        mContext = pContext;
        mWindowManager = (WindowManager) mContext.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        mToastViewParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST);
        mToastViewParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        mToastViewParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        mToastViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; /*|
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;*/
        mToastViewParams.format = PixelFormat.TRANSPARENT;
        mToastViewParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mToastViewParams.y = 200;

        mToastView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_floatview, null, false);
        mToastView.setTag(false);
        mPackageNameView = (TextView) mToastView.findViewById(R.id.packagename);
        mShareView = (TextView) mToastView.findViewById(R.id.share);
        mShareView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content = mPackageNameView.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_TEXT, content);
                    mContext.startActivity(intent);
                }
            }
        });
    }
    public static FloatViewManager getInstance(Context pContext) {
        if (singleton == null) {
            synchronized (FloatViewManager.class) {
                if (singleton == null) {
                    singleton = new FloatViewManager(pContext);
                }
            }
        }
        return singleton;
    }


    public void show(String pS) {
        if (!(boolean) mToastView.getTag()) {
            mWindowManager.addView(mToastView, mToastViewParams);
            mToastView.setTag(true);
        }
        mPackageNameView.setText(pS);
    }

    public void dismiss() {
        if (mToastView != null && (boolean)mToastView.getTag()) {
            try {
                mWindowManager.removeViewImmediate(mToastView);
            } catch (Throwable pE) {
                pE.printStackTrace();
            }
            mToastView.setTag(false);
        }

    }
}
