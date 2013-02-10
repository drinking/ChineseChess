package org.drinking.customui;

import android.content.Context;
import android.content.DialogInterface;
import drinking.android.chess.R;
public class DialogCreator {

	public static void showCustomDialog(String title,String msg,String positive,
			DialogInterface.OnClickListener poslistener,String negative,
			DialogInterface.OnClickListener neglistener,Context context){
	
			CustomDialog.Builder builder=new CustomDialog.Builder(context);
			if(title!=null)
				builder.setTitle(title);
			else
				builder.setTitle(R.string.app_name);
			
			if(msg!=null)
				builder.setMessage(msg);
			if(positive!=null){
				builder.setPositiveButton(positive, poslistener);
			}
			if(negative!=null){
				builder.setNegativeButton(negative, neglistener);
			}
			builder.create().show();
	}
	/**
	 * this dialog can't cancelable
	 */
	public static void showForceDialog(String title,String msg,String positive,
			DialogInterface.OnClickListener poslistener,Context context){
	
			CustomDialog.Builder builder=new CustomDialog.Builder(context);
			if(title!=null)
				builder.setTitle(title);
			else
				builder.setTitle(R.string.app_name);
			
			if(msg!=null)
				builder.setMessage(msg);
			if(positive!=null){
				builder.setPositiveButton(positive, poslistener);
			}
			CustomDialog dialog=builder.create();
			dialog.setCancelable(false);
			dialog.show();
	}

}
