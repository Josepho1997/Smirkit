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
import java.io.OutputStream;
import java.util.concurrent.TimeoutException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.stericson.RootTools.*;
import com.stericson.RootTools.exceptions.RootDeniedException;
import com.stericson.RootTools.execution.CommandCapture;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.R.color;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;

public class MainActivity extends Activity implements OnClickListener {

		
		public class Zip extends AsyncTask<Void, Void, Void>{	
			private ProgressDialog progress;

			public Zip(ProgressDialog progress){
					this.progress = progress;
				}
			
			public void onPreExecute(){
				progress.show();
			}

			
			@Override
			protected Void doInBackground(Void... arg0) {
				copyAssest();
				CommandCapture command = new CommandCapture(0, "su");
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
				
				//Testing Git-Hub commits
				String path = "/sdcard/";
				String zipname = "smirkit.zip";
				InputStream is;
				ZipInputStream zis;
				try{
					String filename;
					is = new FileInputStream(path + zipname);
					zis = new ZipInputStream(new BufferedInputStream(is));
					ZipEntry ze;
					byte[] buffer = new byte[1024];
					int count;
					
					while((ze = zis.getNextEntry()) != null){
						filename = ze.getName();
						
						if (ze.isDirectory()){
							File fmd = new File(path + filename);
							fmd.mkdirs();
							continue;
							}
					FileOutputStream fout = new FileOutputStream(path + filename);
					
					while((count = zis.read(buffer)) != -1){
						fout.write(buffer, 0, count);
					}
					
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent i = new Intent(MainActivity.this, Install.class);
				startActivity(i);
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
		MainActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		
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

		TextView smirkit = (TextView) findViewById(R.id.tvSmirkit);
		Typeface smirk = Typeface.createFromAsset(getAssets(), "3Dumb.ttf");
		smirkit.setTypeface(smirk);
		smirkit.setText(text);
		
		File directory = new File("/system/xbin/smirkit");
	    if(directory.exists() && directory.isDirectory()){
	    	Intent a = new Intent(MainActivity.this, Finished.class);
	    	startActivity(a);
	    }else{
	    }
		if (RootTools.isBusyboxAvailable()){
		} else {
			RootTools.offerBusyBox(MainActivity.this);
		}
		Button bInstall = (Button) findViewById(R.id.bInstall);
		bInstall.setOnClickListener(this);
		Button bUninstall = (Button) findViewById(R.id.bRemove);
		bUninstall.setOnClickListener(this);
			
		
	}

	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
			case R.id.bInstall:
				ProgressDialog progress = new ProgressDialog(this);
				progress.setCancelable(false);
				progress.setMessage("Unziping smirkit...");
				new Zip(progress).execute();
				
				
					break;
			case R.id.bRemove:
				Intent remove = new Intent(MainActivity.this, Remove.class);
				startActivity(remove);
		
	}
	}
	

	
	public void copyAssest(){
		File directory = new File("/sdcard/smirkit.zip");
	    if(directory.exists() && directory.isDirectory()){
	    	//Do Nothing
	    }else{
	    	AssetManager assetManager = getAssets();
			String[] files = null;
			try {
				files = assetManager.list("");
				
			}catch (IOException e) {
				Log.e("tag", "Failed to get asset file list.", e);
			}
			for(String filename : files) {
				InputStream in = null;
				OutputStream out = null;
				try {
					in = assetManager.open(filename);
					out = new FileOutputStream("/sdcard/" + filename);
					copyFile(in, out);
					in.close();
					in = null;
					out.flush();
					out.close();
					out = null;
					
					} catch(IOException e) {
						Log.e("tag", "Failed to copy asset file: " + filename, e);
						
					}
			}}
		
		}
		
		private void copyFile(InputStream in, OutputStream out) throws IOException {
			byte[] buffer = new byte[1024];
			int read;
			while((read = in.read(buffer)) != -1){
				out.write(buffer, 0, read);
			}
	    }

}



