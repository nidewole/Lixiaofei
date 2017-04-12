package test.lixiaofei;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ctvit.dev.Conts;
import com.ctvit.dev.RootActivity;
import com.ctvit.dev.api.CVAPI;
import com.ctvit.dev.bean.LocaInfo;
import com.ctvit.dev.factory.AppObserverFactory;
import com.ctvit.dev.interfaces.IAppObserver;
import com.ctvit.dev.permission.CVPermission;
import com.ctvit.dev.permission.PermissionProxy;
import com.ctvit.dev.tools.LogTools;
import com.ctvit.dev.tools.ToastTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemoActivity extends RootActivity implements OnClickListener, PermissionProxy<Object> {
    private final static String TAG = DemoActivity.class.getSimpleName();
    @BindView(R.id.btnTest8)
    Button btnTest8;
    private Activity mContext;
    private LocaInfo mLocalBean;


    IAppObserver mIAppObserver = new IAppObserver() {

        @Override
        public void update(int type, Object data) {
            switch (type) {
                case Conts.LocaConts.sGSPLocaSuc:
                case Conts.LocaConts.sNetLocaSuc:
                    mLocalBean = CVAPI.getInstance().getCVConfig().mCVInfo.getLocalBean();
                    String addr = TextUtils.isEmpty(mLocalBean.getmCurrAddrStr()) ? "无法定位" : "当前位置：" + mLocalBean.getmCurrAddrStr();
                    mTvLocation.setText(addr);
                    break;
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    public void init() {
        mContext = this;
        initData();
        initView();
    }

    private static final int REQUECT_CODE_SDCARD = 2;

    public void initData() {
        AppObserverFactory.getInstance().attach(mIAppObserver);
        mLocalBean = CVAPI.getInstance().getCVConfig().mCVInfo.getLocalBean();

        if (!CVPermission.lacksPermissions(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            CVAPI.getInstance().startLocaSer(mContext);
        }
        CVPermission.requestPermissions(this, REQUECT_CODE_SDCARD, Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.ACCESS_COARSE_LOCATION);


    }

    private TextView mTvLocation;//展示定位文本

    public void initView() {
        mTvLocation = (TextView) findViewById(R.id.tvLocation);
        String addr = TextUtils.isEmpty(mLocalBean.getmCurrAddrStr()) ? "无法定位" : "当前位置：" + mLocalBean.getmCurrAddrStr();
        mTvLocation.setText(addr);

        bindListener(findViewById(R.id.btnTest1));
        bindListener(findViewById(R.id.btnTest2));
        bindListener(findViewById(R.id.btnTest3));
        bindListener(findViewById(R.id.btnTest4));
        bindListener(findViewById(R.id.btnTest5));
        bindListener(findViewById(R.id.btnTest6));
        bindListener(findViewById(R.id.btnTest7));


    }


    public void bindListener(View view) {
        if (view == null) {
            new NullPointerException("view == null");
        }
        view.setOnClickListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppObserverFactory.getInstance().detach(mIAppObserver);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTest1:
                LogTools.d(TAG, "Test1");
                new ToastTools().Short(mContext, "封装H5 实例").show();
                startActivity(Test1WebViewActivity.class);
                break;
            case R.id.btnTest2:
                LogTools.d(TAG, "Test2");
                new ToastTools().Short(mContext, "封装OkHttp请求 实例").show();
                startActivity(OkHttpDemoActivity.class);
                break;
            case R.id.btnTest3:
                LogTools.d(TAG, "Test3");
                new ToastTools().Short(mContext, "Test3").show();

                break;
            case R.id.btnTest4:
                LogTools.d(TAG, "Test4");
                new ToastTools().Short(mContext, "Test4").show();

                break;
            case R.id.btnTest5:
                LogTools.d(TAG, "Test5");
                new ToastTools().Short(mContext, "Test5").show();

                break;
            case R.id.btnTest6:
                LogTools.d(TAG, "WiFi通信服务器");
                new ToastTools().Short(mContext, "WiFi通信服务器").show();
                startActivity(WifiSerActivity.class);

                break;
            case R.id.btnTest7:
                LogTools.d(TAG, "WiFi通信客户端");
                new ToastTools().Short(mContext, "WiFi通信客户端").show();
                startActivity(WifiClientActivity.class);

                break;
            default:

                break;
        }
    }


//	   @Override
//	    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
//	    {
//			LogTools.d(TAG, requestCode);
//			if(!CVPermission.lacksPermissions(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)){
//				CVAPI.getInstance().startLocaSer(mContext);
//			}
//	        CVPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//	        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//	    }


    // 含有全部的权限
    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void grant(Object source, int requestCode) {
        LogTools.d(TAG, requestCode);
        switch (requestCode) {
            case REQUECT_CODE_SDCARD:
//				if(!CVPermission.lacksPermissions(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)){
//					CVAPI.getInstance().startLocaSer(mContext);
//				}
                break;

            default:
                break;
        }
    }

    @Override
    public void denied(Object source, int requestCode) {
        LogTools.d(TAG, requestCode);
        switch (requestCode) {
            case REQUECT_CODE_SDCARD:
                break;

            default:
                break;
        }
    }

    @Override
    public void rationale(Object source, int requestCode) {
        LogTools.d(TAG, requestCode);

    }

    @Override
    public boolean needShowRationale(int requestCode) {
        LogTools.d(TAG, requestCode);

        return false;
    }


    @OnClick(R.id.btnTest8)
    public void onViewClicked() {
        startActivity(new Intent(mContext ,AccusationActivity.class));
    }
}
