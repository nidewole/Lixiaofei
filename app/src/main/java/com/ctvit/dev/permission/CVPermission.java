package com.ctvit.dev.permission;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;

import com.ctvit.dev.tools.LogTools;
import com.ctvit.dev.tools.Tools;

import java.util.ArrayList;
import java.util.List;

public class CVPermission {
	private final static String TAG = CVPermission.class.getSimpleName();
    private static final String SUFFIX = "PermissionProxy";
	
    /**
     * 申请权限
     * @param object
     * @param requestCode
     * @param permissions
     */
	 public static void requestPermissions(Activity object, int requestCode, String... permissions)
	    {
	        _requestPermissions(object, requestCode, permissions);
	    }

	    public static void requestPermissions(Fragment object, int requestCode, String... permissions)
	    {
	        _requestPermissions(object, requestCode, permissions);
	    }

	/*    
	 * 
	 * 
	 * 是否需要弹出解释
	 * 
	 * public static boolean shouldShowRequestPermissionRationale(Activity activity, String permission, int requestCode)
	    {
	        PermissionProxy proxy = findPermissionProxy(activity);
	        if (!proxy.needShowRationale(requestCode)) return false;

	        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
	                permission))
	        {
	            proxy.rationale(activity, requestCode);
	            return true;
	        }
	        return false;
	    }*/

	    @TargetApi(value = 23)
	    private static void _requestPermissions(Object object, int requestCode, String... permissions)
	    {
	    	
	        if (!Tools.isOverMarshmallow())
	        {
	        	LogTools.d(TAG, "并非6.0以上系统，无需进行权限检查。");
	            doExecuteSuccess(object, requestCode);
	            return;
	        }
	        List<String> deniedPermissions = Tools.findDeniedPermissions(Tools.getActivity(object), permissions);

	        if (deniedPermissions.size() > 0)
	        {
	            if (object instanceof Activity)
	            {
		        	LogTools.d(TAG, "6.0以上系统，Activity##权限检查。");
	                ((Activity) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
	            } else if (object instanceof Fragment)
	            {
		        	LogTools.d(TAG, "6.0以上系统，Fragment##权限检查。");
	                ((Fragment) object).requestPermissions(deniedPermissions.toArray(new String[deniedPermissions.size()]), requestCode);
	            } else
	            {
	                throw new IllegalArgumentException(object.getClass().getName() + " is not supported!");
	            }
	        } else
	        {
	        	LogTools.d(TAG, "6.0以上系统，传入权限有误。");
	            doExecuteSuccess(object, requestCode);
	        }
	    }


	    private static PermissionProxy findPermissionProxy(Object activity)
	    {
            Class clazz = activity.getClass();
            String tmpClass = clazz.getName();
            LogTools.d(TAG, tmpClass);
            
	        try
	        {
	            Class injectorClazz = Class.forName(tmpClass);
	            return (PermissionProxy) injectorClazz.newInstance();
	        } catch (ClassNotFoundException e)
	        {
	            e.printStackTrace();
	        } catch (InstantiationException e)
	        {
	            e.printStackTrace();
	        } catch (IllegalAccessException e)
	        {
	            e.printStackTrace();
	        }
	        throw new RuntimeException(String.format("can not find %s , something when compiler.", activity.getClass().getSimpleName() + SUFFIX));
	    }


	    private static void doExecuteSuccess(Object activity, int requestCode)
	    {
	        findPermissionProxy(activity).grant(activity, requestCode);

	    }

	    private static void doExecuteFail(Object activity, int requestCode)
	    {
	        findPermissionProxy(activity).denied(activity, requestCode);
	    }

	    /**
	     * 处理权限回调
	     * @param activity
	     * @param requestCode
	     * @param permissions
	     * @param grantResults
	     */
	    public static void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions,
	                                                  int[] grantResults)
	    {
	        requestResult(activity, requestCode, permissions, grantResults);
	    }

	    public static void onRequestPermissionsResult(Fragment fragment, int requestCode, String[] permissions,
	                                                  int[] grantResults)
	    {
	        requestResult(fragment, requestCode, permissions, grantResults);
	    }

	    private static void requestResult(Object obj, int requestCode, String[] permissions,
	                                      int[] grantResults)
	    {
	        List<String> deniedPermissions = new ArrayList<String>();
	        for (int i = 0; i < grantResults.length; i++)
	        {
	            if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
	            {
	                deniedPermissions.add(permissions[i]);
	            }
	        }
	        if (deniedPermissions.size() > 0)
	        {
	            doExecuteFail(obj, requestCode);
	        } else
	        {
	            doExecuteSuccess(obj, requestCode);
	        }
	    }
	    


	    /**
	     * 判断权限集合
	     * @param permissions 权限
	     * @return
	     */
	    public static boolean lacksPermissions(Activity context,String... permissions) {
	        if (!Tools.isOverMarshmallow())
	        {
	        	LogTools.d(TAG, "并非6.0以上系统，无需进行权限检查。");
	            return false;
	        }
	        for (String permission : permissions) {
	            if (checksPermission(context,permission)) {
	                return true;
	            }
	        }
	        return false;
	    }

	    /**
	     * 判断是否缺少权限
	     * @param permission 权限
	     * @return
	     */
	    @SuppressLint("NewApi")
		public static boolean checksPermission(Activity context,String permission) {
	        if (!Tools.isOverMarshmallow())
	        {
	        	LogTools.d(TAG, "并非6.0以上系统，无需进行权限检查。");
	          
	            return false;
	        }
	    	boolean isPerm = (context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED);
	    	LogTools.d(TAG, permission,context.checkSelfPermission(permission),isPerm);
	        return false;
	    }
	    

}
