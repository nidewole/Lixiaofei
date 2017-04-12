package test.lixiaofei;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctvit.dev.RootActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test.lixiaofei.util.FileUtils;

public class AccusationActivity extends RootActivity {

    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.tv_6)
    TextView tv6;
    @BindView(R.id.tv_7)
    TextView tv7;
    @BindView(R.id.tv_8)
    TextView tv8;
    @BindView(R.id.et_remarks)
    EditText etRemarks;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.activity_accusation)
    LinearLayout activityAccusation;
    @BindView(R.id.back_1)
    TextView back1;
    private List<TextView> list = null;
    private int[] array = null;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accusation);
        ButterKnife.bind(this);
        mContext = this;
        FileUtils.initCacheFile(mContext);
    }

    @SuppressLint("NewApi")
    @OnClick({R.id.back_1 , R.id.btn_commit , R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5, R.id.tv_6, R.id.tv_7, R.id.tv_8})
    public void onViewClicked(View view) {
        if (list == null) {
            list = new ArrayList();
        }
        if (array == null) {
            array = new int[8];

        }
        switch (view.getId()) {
            case R.id.tv_1:
                clickCommon(0, tv1);
                break;
            case R.id.tv_2:
                clickCommon(1, tv2);
                break;
            case R.id.tv_3:
                clickCommon(2, tv3);
                break;
            case R.id.tv_4:
                clickCommon(3, tv4);
                break;
            case R.id.tv_5:
                clickCommon(4, tv5);
                break;
            case R.id.tv_6:
                clickCommon(5, tv6);
                break;
            case R.id.tv_7:
                clickCommon(6, tv7);
                break;
            case R.id.tv_8:
                clickCommon(7, tv8);
                break;
            case R.id.back_1:
                finish();
                break;
            case R.id.btn_commit:
                commitAccusation();
                break;
        }
    }

    /**
     * 提交举报
     */
    private void commitAccusation() {
        StringBuilder sb = new StringBuilder();
        for (TextView tv: list) {
            String trim = tv.getText().toString().trim();
            sb.append(trim).append(",");
        }
        String trim = etRemarks.getText().toString().trim();
        if (!TextUtils.isEmpty(trim)) {
            sb.append(trim);
        }
        try {
            byte[] bytes = sb.toString().getBytes("UTF-8");
            FileUtils.writeStringToFile(new String(bytes , "UTF-8"),
                    FileUtils.messageFileDir.getAbsolutePath(), "yong", true);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 点击的通用设置
     *
     * @param falg
     * @param tv
     */
    @SuppressLint("NewApi")
    private void clickCommon(int falg, TextView tv) {
        if (array[falg] ==0) {
            tv.setBackground(getResources().getDrawable(R.drawable.background_solid));
            list.add(tv);
            array[falg] = 1;
            commitChange(true);

        } else {
            tv.setBackground(getResources().getDrawable(R.drawable.background_glidlines));
            list.remove(tv);
            array[falg] = 0;
            commitChange(false);
        }
    }

    @SuppressLint("NewApi")
    private void commitChange(boolean falg) {
        if (falg && list.size() > 0 && btnCommit.getBackground() != getResources().getDrawable(R.drawable.background_solid)) {
            btnCommit.setBackground(getResources().getDrawable(R.drawable.background_solid));
        }
        if (!falg && list.size() == 0 && btnCommit.getBackground() != getResources().getDrawable(R.drawable.background_glidlines)) {
            btnCommit.setBackground(getResources().getDrawable(R.drawable.background_glidlines));
        }
    }


}
