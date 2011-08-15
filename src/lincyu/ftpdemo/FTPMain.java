package lincyu.ftpdemo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class FTPMain extends Activity {
	
	FTPClient client;
	boolean isConnected;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ftpmain);
        
        Intent intent = getIntent();
        
        String username = intent.getStringExtra("KEY_USERNAME");
        String password = intent.getStringExtra("KEY_PASSWORD");
        
        Button btn = (Button)findViewById(R.id.upload);
        btn.setOnClickListener(upload_l);
        btn = (Button)findViewById(R.id.download);
        btn.setOnClickListener(download_l);
        
        client = new FTPClient();
		
        isConnected = false;
		try {
			client.connect("asia.edu.tw");
			int replycode = client.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replycode)) {
				client.disconnect();
				showToast(this, "Connection fail");
				return;
			}
			if (!client.login(username, password)) {
				showToast(this, "Login fail");
				return;
			} else {
				client.enterLocalPassiveMode();
				isConnected = true;
			}
        } catch (Exception e) {
        	showToast(this, e.toString());
        }
    }
    
    OnClickListener upload_l = new OnClickListener() {
    	public void onClick(View v) {
    		if (isConnected == false)
    			return;
    		
    		try {
    			/* Generate the file that will be uploaded */
    			FileOutputStream fos = FTPMain.this.openFileOutput(
    				"upload.txt", Context.MODE_PRIVATE);
    			final String content = "Upload"; 
    			for (int i = 0; i < content.length(); i++)
    				fos.write((int)content.charAt(i));
    			fos.close();
    			
    			/* Upload */
    			client.setFileType(FTP.ASCII_FILE_TYPE);
    			FileInputStream fis = new FileInputStream(
    				new File("/data/data/lincyu.ftpdemo/files/upload.txt"));
    			client.storeFile("~/public_html/Android/uploadfile.txt", fis);
    			showToast(FTPMain.this, "Done!");
    		} catch (Exception e) {
    			showToast(FTPMain.this, e.toString());
    		}
    	}
    };
    
    OnClickListener download_l = new OnClickListener() {
    	public void onClick(View v) {
    		if (isConnected == false)
    			return;
    		
    		try {    			
    			/* Download */
    			client.setFileType(FTP.ASCII_FILE_TYPE);
    			InputStream is = client.retrieveFileStream(
    				"~/public_html/Android/txtfile.txt"); 
    			
    			/* Write to file */
    			FileOutputStream fos = FTPMain.this.openFileOutput(
            		"download.txt", Context.MODE_PRIVATE);
        		
    			int c = -1;
            	do {
            		c = is.read();
            		if (c != -1) fos.write(c);
            	} while (c != -1);
            	is.close();
            	fos.close();
            	showToast(FTPMain.this, "Done!");
    		} catch (Exception e) {
    			showToast(FTPMain.this, e.toString());
    		}
    	}
    };
    
    private void showToast(Context mCtx, String failmsg) {
    	Toast.makeText(mCtx, failmsg, Toast.LENGTH_SHORT).show();
    }
}
