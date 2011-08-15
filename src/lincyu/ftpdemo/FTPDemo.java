package lincyu.ftpdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FTPDemo extends Activity {
	
	EditText et_username, et_password;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);
        Button btn_login = (Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(listener);
    }
    
    OnClickListener listener = new OnClickListener() {
    	public void onClick(View v) {
    		String username = et_username.getText().toString();
    		String password = et_password.getText().toString();
    		
    		Intent intent = new Intent();
    		intent.setClass(FTPDemo.this, FTPMain.class);
    		intent.putExtra("KEY_USERNAME", username);
    		intent.putExtra("KEY_PASSWORD", password);
    		startActivity(intent);
    	}
    };
}