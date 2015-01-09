package com.china.acetech.ToolPackage.funccontext;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.china.acetech.ToolPackage.R;

public class AbsManuControlActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch ( item.getItemId()){
		case android.R.id.home:
			this.onBackPressed();
			//this.finish();
			break;
		case R.id.action_log_out:
//			MySavedState.UserLoginInfo.setLoginState(false);
//			Intent intent = new Intent(this, LuncherActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//			this.startActivity(intent);
			this.finish();
			break;
		default:
			break;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		//getActionBar().setIcon(R.drawable.empty_title);
	}
	
}
