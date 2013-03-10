package com.cydeon.smirkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import android.R.color;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Finished extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.finished);
		TextView smirkit = (TextView) findViewById(R.id.textView1);
		String sdcard = "/sdcard/smirkit/smirkit.txt";
		RootTools.remount("/system", "rw");
		CommandCapture command = new CommandCapture(0, "su", "cd /sdcard", "rm smirkit.zip", "rm -r smirkit/smirkitfiles", "rm smirkit/smirkit", "rm 3Dumb.ttf", "rm Honey.TTF");
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
	Typeface smirk = Typeface.createFromAsset(getAssets(), "Honey.TTF");
	smirkit.setTypeface(smirk);
	smirkit.setText(text + "has been installed successfully!");
	Button Terminal = (Button) findViewById(R.id.bLaunch);
	Terminal.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
			case R.id.bLaunch:
				Intent i = new Intent("jackpal.androidterm.RUN_SCRIPT");
				i.addCategory(Intent.CATEGORY_DEFAULT);
				i.putExtra("jackpal.androidterm.iInitialCommand", "su");
				startActivity(i);
				
				
				break;
		}
		
		
		
	}

	
	
}
