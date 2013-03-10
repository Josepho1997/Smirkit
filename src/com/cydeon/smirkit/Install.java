package com.cydeon.smirkit;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.cydeon.smirkit.MainActivity.Zip;
import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Install extends Activity {

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
			RootTools.remount("/system", "rw");
			CommandCapture command = new CommandCapture(0, "su", "cd /sdcard/smirkit", "cp smirkit /system/xbin/", "mkdir /system/xbin/smirkitfiles", "cp smirkitfiles/* /system/xbin/smirkitfiles", "chmod 777 /system/xbin/smirkit");
			try {
				RootTools.getShell(true).add(command).waitForFinish();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (RootDeniedException e) {
				e.printStackTrace();
			}
			Intent b = new Intent(Install.this, Finished.class);
			startActivity(b);
			finish();
			return null;
			
		}
		
		public void onPostExecute(Void unused){
			progress.dismiss();
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
		smirkit.setTypeface(smirk);
		smirkit.setText("Smirkit v2.6");
		ProgressDialog progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setMessage("Installing smirkit...");
		new Copy(progress).execute();
	}
	
	
	
}
