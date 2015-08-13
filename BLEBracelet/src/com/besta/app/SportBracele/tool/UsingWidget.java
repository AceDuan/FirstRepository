package com.besta.app.SportBracele.tool;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class UsingWidget {

	public static void handleAlertMessage(Context context, String title, String message)
	  {
	    AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
	    localBuilder.setTitle(title);
	    localBuilder.setMessage(message);
	    localBuilder.setCancelable(true);
	    localBuilder.setNegativeButton("OK", new DialogInterface.OnClickListener()
	    {
	      public void onClick(DialogInterface paramDialogInterface, int paramInt)
	      {
	        paramDialogInterface.cancel();
	      }
	    });
	    localBuilder.show();
	  }
}
