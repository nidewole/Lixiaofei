package test.lixiaofei;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctvit.dev.RootActivity;
import com.ctvit.dev.tools.LogTools;
import com.ctvit.dev.tools.ToastTools;
import com.okgo.OkGo;
import com.okgo.cache.CacheMode;
import com.okgo.callback.BitmapCallback;
import com.okgo.callback.FileCallback;
import com.okgo.callback.StringCallback;
import com.okgo.request.BaseRequest;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;
import test.lixiaofei.conts.Urls;

public class OkHttpDemoActivity extends RootActivity implements OnClickListener{
	private final static String TAG = OkHttpDemoActivity.class.getSimpleName();
	private Context mContext;
	
	

	    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.okhttp_demo_activity);
		init();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
	}
	
	public void init(){
		mContext = this;
		initData();
		initView();
	}
	public void initData(){


	}
	
	private TextView mtvOkHttpTips ;//展示文本
	public void initView(){
		mtvOkHttpTips = (TextView) findViewById(R.id.tvOkHttpTips);
		
		bindListener(findViewById(R.id.btnOkHttp1));
		bindListener(findViewById(R.id.btnOkHttp2));
		bindListener(findViewById(R.id.btnOkHttp3));
		bindListener(findViewById(R.id.btnOkHttp4));
		bindListener(findViewById(R.id.btnOkHttp5));

	}
	
	public void bindListener(View view){
		if(view == null){
			new NullPointerException("view == null");
		}
		view.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		OkGo.getInstance().cancelTag(this);//取消异步请求
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnOkHttp1://get 请求 下载图片
			LogTools.d(TAG, "get 请求");	
			new ToastTools().Short(mContext, "get 请求").show();
			OkGo.get(Urls.URL_IMAGE)//
		    .tag(this)//
		    .execute(new BitmapCallback() {
		        @Override
		        public void onSuccess(Bitmap bitmap, Call call, Response response) {
		            // bitmap 即为返回的图片数据
		        	LogTools.d(TAG, "请求图片数据成功..."+response.message());
		        	ImageView ivBitmap = (ImageView) findViewById(R.id.ivBitmap);
		        	ivBitmap.setImageBitmap(bitmap);

		        }
		        
		        @Override
		        public void onError(Call call, Response response, Exception e) {
		        	// TODO Auto-generated method stub
		        	super.onError(call, response, e);
		        	LogTools.d(TAG, "请求图片异常...");
		        	e.printStackTrace();
		        	
		        }
		        @Override
		        public void parseError(Call call, Exception e) {
		        	// TODO Auto-generated method stub
		        	super.parseError(call, e);
		        	LogTools.d(TAG, "请求图片异常...");
		        	e.printStackTrace();
		        }
		    });
			

			break;
		case R.id.btnOkHttp2://post 请求
			LogTools.d(TAG, "post 请求");	
            OkGo.post(Urls.URL_JSONARRAY)//
                    .tag(this)//
                    .headers("header1", "headerValue1")//
                    .params("param1", "paramValue1")//
                    .execute(new StringCallback() {
						
						@Override
						public void onSuccess(String t, Call call, Response response) {
							// TODO Auto-generated method stub
				        	LogTools.d(TAG, "请求数据成功..."+ t,response.message());
			    			new ToastTools().Short(OkGo.getContext(), "同步请求成功..."+t).show();
						}
					});  //不传callback即为同步请求
            
			//syncPost();

			break;
		case R.id.btnOkHttp3://请求 url 使用缓存 请求
			LogTools.d(TAG, "使用 cache 请求");	
		    OkGo.get(Urls.URL_CACHE)//
            .tag(this)//
            .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)//
            .cacheKey("if_none_cache_request")//
            .cacheTime(5000)            // 单位毫秒.5秒后过期
            .headers("header1", "headerValue1")//
            .params("param1", "paramValue1")//
            .execute(new StringCallback() {
				
				@Override
				public void onSuccess(String t, Call call, Response response) {
					// TODO Auto-generated method stub
					new ToastTools().Short(mContext,  t).show();
					LogTools.d(TAG, "请求url成功"+t);
				}
				@Override
				public void onError(Call call, Response response, Exception e) {
					// TODO Auto-generated method stub
					super.onError(call, response, e);
					LogTools.d(TAG, "请求url失败");
					e.printStackTrace();
				}
				@Override
				public void onCacheSuccess(String t, Call call) {
					// TODO Auto-generated method stub
					super.onCacheSuccess(t, call);
					new ToastTools().Short(mContext,  t).show();
					LogTools.d(TAG, "请求url缓存成功,是本地缓存"+t);
				}
				@Override
				public void onCacheError(Call call, Exception e) {
					// TODO Auto-generated method stub
					super.onCacheError(call, e);
					LogTools.d(TAG, "请求url缓存失败");
				}
			});

			break;
		case R.id.btnOkHttp4://下载文件 
			LogTools.d(TAG, "下载文件");	
			final Button btnOkHttp4 = (Button)this.findViewById(R.id.btnOkHttp4);
			new ToastTools().Short(mContext, "head 请求").show();
			OkGo.get(Urls.URL_DOWNLOAD)
			.tag(this)
			.headers("header1", "headerValue1")//
            .params("param1", "paramValue1")
            .execute((new FileCallback("OkGo.apk") {
            	
                @Override
                public void onBefore(BaseRequest request) {
                	LogTools.d(TAG, "下载中..");
                	btnOkHttp4.setText("正在下载中");
                }

				@Override
				public void onSuccess(File t, Call call, Response response) {
					// TODO Auto-generated method stub
                	LogTools.d(TAG, "下载完成..");
                    btnOkHttp4.setText("下载完成，点击重新下载..."+t.getPath());

				}
				@Override
				public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
					// TODO Auto-generated method stub
					super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                	LogTools.d(TAG, "下载完成..","currentSize:"+currentSize,"totalSize:"+totalSize,"progress:"+progress,"networkSpeed:"+networkSpeed);
                    String downloadLength = Formatter.formatFileSize(getApplicationContext(), currentSize);
                    String totalLength = Formatter.formatFileSize(getApplicationContext(), totalSize);
                    String netSpeed = Formatter.formatFileSize(getApplicationContext(), networkSpeed);
                    String  curProgress = (Math.round(progress * 10000) * 1.0f / 100) + "%";
                    btnOkHttp4.setText("正在下载中... \n"+"当前进度："+curProgress+ "\n下载速度："+netSpeed +"\n下载大小："+ downloadLength + "/" + totalLength);
				}
				 @Override
                 public void onError(Call call,  Response response,  Exception e) {
                     super.onError(call, response, e);
                     btnOkHttp4.setText("下载出错");
                 	 LogTools.d(TAG, "下载出错..");
                 	 e.printStackTrace();

                 }
			}));

			break;
		case R.id.btnOkHttp5://delete 请求
			LogTools.d(TAG, "delete 请求");	
			new ToastTools().Short(mContext, "head 请求").show();
			break;
		default:
			break;
		}
	}
	

}
