package com.cydeon.smirkit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class Remove extends Activity {
	
	public class Copy extends AsyncTask<Void, Void, Void>{	
		private ProgressDialog progress;

		public Copy(ProgressDialog progress){
				this.progress = progress;
			}
		
		public void onPreExecute(){
			progress.show();
		}

		
		@Override
		protected Void doInBackground(Void... arg0) {
			CommandCapture remove = new CommandCapture(0, "su", "cd /system/xbin", "rm smirkit", "rm -r smirkitfiles", "cd /sdcard", "rm smirkit.zip", "rm -r smirkit");
			try {
				RootTools.getShell(true).add(remove).waitForFinish();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (RootDeniedException e) {
				e.printStackTrace();
			}
			return null;
			
		}
		
		public void onPostExecute(Void unused){
			progress.dismiss();
			Intent i = new Intent(Remove.this, Removed.class);
			startActivity(i);
			finish();
		}
		
		}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		TextView smirkit = (TextView) findViewById(R.id.tvSmirkit);
		String sdcard = "/sdcard/smirkit/smirkit.txt";
StringBuilder text = new StringBuilder();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(sdcard));
			String line;
			
			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		Typeface smirk = Typeface.createFromAsset(getAssets(), "3Dumb.ttf");
		smirkit.setTypeface(smirk);
		smirkit.setText(text);
		ProgressDialog progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("Removing smirkit...");
		new Copy(progress).execute();
		
	}
}
