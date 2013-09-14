package com.checkin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.checkin.utils.SocketUtil;

public class RegistActivity extends Activity {

	boolean regist_group_rs = false;

	private EditText regist_edt_account;
	private EditText regist_edt_pwd, regist_edt_pwd2;
	private Button regist_btn_regist;
	private Button regist_btn_clean;

	private SocketUtil connect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);

		regist_edt_account = (EditText) this.findViewById(R.id.regist_account);
		regist_edt_pwd = (EditText) this.findViewById(R.id.regist_psw);
		regist_edt_pwd2 = (EditText) this.findViewById(R.id.regist_psw2);

		regist_btn_regist = (Button) this.findViewById(R.id.regist_btn_account);
		regist_btn_clean = (Button) this.findViewById(R.id.regist_clean_table);

		regist_btn_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String username = regist_edt_account.getText().toString();
				String password = regist_edt_pwd.getText().toString();
				String password2 = regist_edt_pwd2.getText().toString();

				if (username.trim().length() == 0
						| password.trim().length() == 0
						| password2.trim().length() == 0) {
					Toast.makeText(RegistActivity.this, "用户名或密码不能为空^^",
							Toast.LENGTH_SHORT).show();
					return;
				}

				if (password.equalsIgnoreCase(password2)) {
					Toast.makeText(RegistActivity.this, "两次密码输入不一致",
							Toast.LENGTH_SHORT).show();
					regist_edt_pwd.setText("");
					regist_edt_pwd2.setText("");
				}

				connect = new SocketUtil(RegistActivity.this);
				if (!connect.isConnected) {
					try {
						connect.connectServer();
					} catch (Exception e) {
						Toast.makeText(RegistActivity.this,
								"连接服务器失败，请检查网络状况..", Toast.LENGTH_LONG).show();
						return;
					}
				}
				boolean isExist = connect.register(username, password);
				connect.close();
				if (isExist) {

					Toast.makeText(RegistActivity.this, "帐户已存在，请重新注册",
							Toast.LENGTH_LONG).show();
					clean();
				}

			}

		});

		regist_btn_clean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clean();
			}
		});

	}
	
	void clean(){
		regist_edt_account.setText("");
		regist_edt_pwd.setText("");
		regist_edt_pwd2.setText("");
	}

	
	private void showProgressDialog() {
		ProgressDialog pd = new ProgressDialog(this);

		ProgressDialog.show(getApplicationContext(), "注册中", "");

	}

}
