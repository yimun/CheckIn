package com.checkin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LauncherActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.checkin.service.MyService;
import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;

/**
 * 注册界面
 * 
 * @author Administrator
 * 
 */
public class RegistActivity extends Activity {

	boolean regist_group_rs = false;

	private EditText regist_edt_account;
	private EditText regist_edt_wcd;
	private EditText regist_edt_pwd, regist_edt_pwd2;
	private Button regist_btn_regist;
	private Button regist_btn_clean;

	private String username, password, password2, workcode,ip;
	private SocketUtil connect;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);

		// 控件初始化
		regist_edt_account = (EditText) this.findViewById(R.id.regist_account);
		regist_edt_wcd = (EditText) this.findViewById(R.id.regist_wcd);
		regist_edt_pwd = (EditText) this.findViewById(R.id.regist_psw);
		regist_edt_pwd2 = (EditText) this.findViewById(R.id.regist_psw2);
		regist_btn_regist = (Button) this.findViewById(R.id.regist_btn_account);
		regist_btn_clean = (Button) this.findViewById(R.id.regist_clean_table);

	}

	public void onResume() {

		super.onResume();
		// 注册按钮的监听
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

				if (!password.equalsIgnoreCase(password2)) {
					Toast.makeText(RegistActivity.this, "两次密码输入不一致",
							Toast.LENGTH_SHORT).show();
					regist_edt_pwd.setText("");
					regist_edt_pwd2.setText("");
					return;
				}
				new RegistTask().execute((Void) null);

			}

		});

		regist_btn_clean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clean();
			}
		});

	}

	/**
	 * 重置输入框
	 */
	private void clean() {
		regist_edt_account.setText("");
		regist_edt_wcd.setText("");
		regist_edt_pwd.setText("");
		regist_edt_pwd2.setText("");
	}

	

	/**
	 * 初始化进度条
	 */
	private void initProgressDialog() {

		pd = new ProgressDialog(this);// 创建ProgressDialog对象
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		pd.setTitle("注册");// 设置ProgressDialog 标题
		pd.setMessage("连接中...");// 设置ProgressDialog提示信息
		/*
		 * pd.setIcon(R.drawable.ic_la);// 设置ProgressDialog标题图标 //
		 * 设置ProgressDialog
		 */// 的进度条是否不明确 false 就是不设置为不明确
		pd.setIndeterminate(false);
		pd.setCancelable(true); // 设置ProgressDialog 是否可以按退回键取消
	}

	/**
	 * 执行注册的后台任务
	 * 
	 * @author Administrator
	 * 
	 */
	public class RegistTask extends AsyncTask<Void, Void, Integer> {

		// 预执行
		protected void onPreExecute() {

			initProgressDialog();
			pd.show();
			ip = new PreferGeter(RegistActivity.this).getIP();

		}

		// 后台线程
		@Override
		protected Integer doInBackground(Void... params) {

			Looper.prepare();
			connect = new SocketUtil(ip);
			if (!connect.isConnected) {
				try {
					connect.connectServer();
				} catch (Exception e) {

					return 0;
				}
			}
			boolean isSuccess = connect.register(username, password, workcode);// 注册用户
			connect.close();
			if (isSuccess)
				return 1;
			else
				return 2;

		}

		// 结果处理
		@Override
		protected void onPostExecute(final Integer result) {

			pd.cancel();
			switch (result) {
			case 0:
				Toast.makeText(RegistActivity.this, "连接服务器失败，请检查网络状况..",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(RegistActivity.this, "帐户" + username + "注册成功！",
						Toast.LENGTH_LONG).show();

				// 弹出对话框
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
									public void onClick(DialogInterface dialog,
											int which) {
										startActivity(new Intent(
												RegistActivity.this,
												LoginActivity.class));
									}
								}).show();
				break;
			case 2:
				Toast.makeText(RegistActivity.this, "帐户已存在，请重新注册",
						Toast.LENGTH_LONG).show();
				clean();
				break;
			}

		}

		// 取消
		@Override
		protected void onCancelled() {
			System.out.println("onCancelled");
			pd.cancel();
		}
	}

}
