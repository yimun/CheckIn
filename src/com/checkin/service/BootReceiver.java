package com.checkin.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {   
	
	public static String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
	public static String UPDATE_ACTION = "android.intent.action.UPDATE_STATE";
	
    public void onReceive(Context ctx, Intent intent) {
    	
    	if(intent.getAction().equalsIgnoreCase(BOOT_ACTION))
    		Log.d("BootReceiver", "system boot completed");   
    	if(intent.getAction().equalsIgnoreCase(UPDATE_ACTION))
    		Log.d("UpdateReceiver", "update");
        
        // start activity   
        /*String action="android.intent.action.MAIN";   
        String category="android.intent.category.LAUNCHER";   
        Intent myi=new Intent(ctx,CustomDialog.class);   
        myi.setAction(action);   
        myi.addCategory(category);   
        myi.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
        ctx.startActivity(myi); */  
        
        // start service   
        Intent s=new Intent(ctx,MyService.class);   
        ctx.startService(s);   
        
    }
    
}  
