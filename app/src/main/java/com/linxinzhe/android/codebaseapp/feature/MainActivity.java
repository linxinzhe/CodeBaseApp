package com.linxinzhe.android.codebaseapp.feature;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.linxinzhe.android.codebaseapp.R;
import com.linxinzhe.android.codebaseapp.base.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_go_splash)
    Button mBtnGoSplash;
    @BindView(R.id.btn_go_guide)
    Button mBtnGoGuide;
    @BindView(R.id.btn_go_viewpager_tabs)
    Button mBtnGoViewpagerTabs;
    @BindView(R.id.btn_go_refresh_and_load_more)
    Button mBtnGoRefreshAndLoadMore;
    @BindView(R.id.btn_go_qr_scan)
    Button mBtnGoQrScan;
    @BindView(R.id.btn_go_upload_img)
    Button mBtnGoUploadImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        umeng share permission
        String[] mPermissionList = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS};
        ActivityCompat.requestPermissions(MainActivity.this, mPermissionList, 100);
//        umeng share permission

    }

    @OnClick(R.id.btn_go_splash)
    public void goSplash(View view) {
        startActivity(SplashActivity.class);
    }

    @OnClick(R.id.btn_go_guide)
    public void goGuide(View view) {
        startActivity(GuideActivity.class);
    }

    @OnClick(R.id.btn_go_viewpager_tabs)
    public void goViewpagerTabs(View view) {
        startActivity(ViewPagerTabsActivity.class);
    }

    @OnClick(R.id.btn_go_refresh_and_load_more)
    public void goRefreshAndLoadMore(View view) {
        startActivity(RefreshAndLoadMoreActivity.class);
    }

    @OnClick(R.id.btn_go_qr_scan)
    public void goQrScan(View view) {
        startActivity(QrScanActivity.class);
    }

    @OnClick(R.id.btn_go_upload_img)
    public void goUploadImg(View view) {
        startActivity(UploadImgActivity.class);
    }

    @OnClick(R.id.btn_umeng_share)
    public void umengShare(View view) {
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
                };
        new ShareAction(this).setDisplayList(displaylist)
                .withText("内容")
                .withTitle("标题")
                .withTargetUrl("http://www.baidu.com")
//                .withMedia( image )
                .setListenerList(umShareListener)
                .open();
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToast(platform + " 分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showToast(platform + " 分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showToast(platform + " 分享取消了");
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //点击其它区域则键盘隐藏
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        //点击其它区域则键盘隐藏
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private boolean isOnKeyBacking;
    private Toast mBackToast;
    private Runnable onBackTimeRunnable = new Runnable() {

        @Override
        public void run() {
            isOnKeyBacking = false;
            if (mBackToast != null) {
                mBackToast.cancel();
            }
        }
    };

    private static final int MSG_DO1 = 1001;
    private final MyHandler mHandler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case MSG_DO1:
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != KeyEvent.KEYCODE_BACK) {
            return false;
        }
        if (isOnKeyBacking) {
            mHandler.removeCallbacks(onBackTimeRunnable);
            if (mBackToast != null) {
                mBackToast.cancel();
            }
            // double click log out
            setResult(Activity.RESULT_OK);
            finish();
            return true;
        } else {
            isOnKeyBacking = true;
            if (mBackToast == null) {
                mBackToast = Toast.makeText(this, getString(R.string.tip_double_click_exit), Toast.LENGTH_LONG);
            }
            mBackToast.show();
            mHandler.postDelayed(onBackTimeRunnable, 2000);
            return true;
        }
    }
}
