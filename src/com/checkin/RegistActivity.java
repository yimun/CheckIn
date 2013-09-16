package com.checkin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;

public class RegistActivity extends Activity {

	boolean regist_group_rs = false;

	private EditText regist_edt_account;
	private EditText regist_edt_wcd;
	private EditText regist_edt_pwd, regist_edt_pwd2;
	private Button regist_btn_regist;
	private Button regist_btn_clean;

	String username, password, password2, workcode;
	private SocketUtil connect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);

		if (new PreferGeter(this).getIP().equalsIgnoreCase("NULL")) {
			Toast.makeText(this, "尚未设置公司wifi信息,请先设置", Toast.LENGTH_LONG).show();
		}

		regist_edt_account = (EditText) this.findViewById(R.id.regist_account);
		regist_edt_wcd = (EditText) this.findViewById(R.id.regist_wcd);
		regist_edt_pwd = (EditText) this.findViewById(R.id.regist_psw);
		regist_edt_pwd2 = (EditText) this.findViewById(R.id.regist_psw2);

		regist_btn_regist = (Button) this.findViewById(R.id.regist_btn_account);
		regist_btn_clean = (Button) this.findViewById(R.id.regist_clean_table);

		regist_btn_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = regist_edt_account.getText().toString();
				workcode = regist_edt_wcd.getText().toString();
				password = regist_edt_pwd.getText().toString();
				password2 = regist_edt_pwd2.getText().toString();

				if (username.trim().length() == 0
						| workcode.trim().length() == 0
						| password.trim().length() == 0
						| password2.trim().length() == 0) {
					Toast.makeText(RegistActivity.this, "输入框不能为空^^",
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
				boolean isSuccess = connect.register(username, password,
						workcode);// 注册用户
				connect.close();
				if (!isSuccess) {

					Toast.makeText(RegistActivity.this, "帐户已存在，请重新注册",
							Toast.LENGTH_LONG).show();
					clean();

				} else {
					Toast.makeText(RegistActivity.this,
							"帐户" + username + "注册成功！", Toast.LENGTH_LONG)
							.show();

					new AlertDialog.Builder(RegistActivity.this)
							.setCancelable(false)
							.setIcon(R.drawable.ic_launcher)
							.setTitle("注册成功")
							.setMessage(
									"您已成功注册用户：" + username + "\n" + "工号："
											+ workcode)
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

											saveUser(); // 保存用户名密码
											startActivity(new Intent(
													RegistActivity.this,
													MainActivity.class));
										}
									}).show();

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

	void clean() {
		regist_edt_account.setText("");
		regist_edt_wcd.setText("");
		regist_edt_pwd.setText("");
		regist_edt_pwd2.setText("");
	}

	void saveUser() {
		Editor editor;
		editor = this.getSharedPreferences("user", Context.MODE_PRIVATE).edit();
		editor.putString("username", username);
		editor.putString("password", password);
		editor.putString("workcode", workcode);
		editor.commit();
	}

	private void showProgressDialog() {
		ProgressDialog pd = new ProgressDialog(this);

		ProgressDialog.show(getApplicationContext(), "注册中", "");

	}

}
