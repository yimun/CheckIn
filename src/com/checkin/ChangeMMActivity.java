package com.checkin;

import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeMMActivity extends Activity {

	EditText edt_username, edt_new_pass, edt_old_pass,edt_new_pass2;
	Button bt_action;
	ProgressDialog pd;
	private SocketUtil connect;
	private String username, new_password, old_password,new_password2 ,ip;

	public void onCreate(Bundle bd) {
		super.onCreate(bd);
		setContentView(R.layout.activity_changemm);
		edt_username = (EditText) findViewById(R.id.username);
		edt_new_pass = (EditText) findViewById(R.id.new_psw);
		edt_new_pass2 = (EditText) findViewById(R.id.new_psw2);
		edt_old_pass = (EditText) findViewById(R.id.old_psw);
		bt_action = (Button) findViewById(R.id.action);

		bt_action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new_password = edt_new_pass.getText().toString();
				new_password2 = edt_new_pass2.getText().toString();
				old_password = edt_old_pass.getText().toString();
				if (new_password.trim().length() == 0|new_password2.trim().length() == 0
						| old_password.trim().length() == 0) {
					Toast.makeText(ChangeMMActivity.this, "输入框不能为空^^",
							Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(new_password.equals(old_password)){
					Toast.makeText(ChangeMMActivity.this, "新密码与旧密码不能相同",
							Toast.LENGTH_SHORT).show();
					edt_old_pass.setText("");
					edt_new_pass.setText("");
					edt_new_pass2.setText("");
					return;
				}
				
				if(!new_password.equals(new_password2)){
					Toast.makeText(ChangeMMActivity.this, "两次密码输入不一致",
							Toast.LENGTH_SHORT).show();
					edt_new_pass.setText("");
					edt_new_pass2.setText("");
					return;
				}
				new ChangeTask().execute((Void) null);
			}

		});
	}

	public void onResume() {
		super.onResume();
		username = ip = new PreferGeter(this).getUnm();
		edt_username.setText(username);
	}

	/**
	 * 执行注册的后台任务
	 * 
	 * @author Administrator
	 * 
	 */
	public class ChangeTask extends AsyncTask<Void, Void, Integer> {

		// 预执行
		protected void onPreExecute() {

			initProgressDialog();
			pd.show();
			ip = new PreferGeter(ChangeMMActivity.this).getIP();

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
			boolean isSuccess = connect.changeMM(username, old_password, new_password);// 注册用户
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
				Toast.makeText(ChangeMMActivity.this, "连接服务器失败，请检查网络状况..",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(ChangeMMActivity.this,
						"密码修改成功！", Toast.LENGTH_LONG).show();

				// 弹出对话框
				new AlertDialog.Builder(ChangeMMActivity.this)
						.setCancelable(false)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("密码修改成功！")
						.setMessage("您已成功修改用户 " + username + " 的密码")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										ChangeMMActivity.this.finish();
										startActivity(new Intent(
												ChangeMMActivity.this,
												MainActivity.class));
									}
								}).show();
				break;
			case 2:
				Toast.makeText(ChangeMMActivity.this, "旧密码错误或用户不存在",
						Toast.LENGTH_LONG).show();
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

	/**
	 * 初始化进度条
	 */
	private void initProgressDialog() {

		pd = new ProgressDialog(this);// 创建ProgressDialog对象
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置进度条风格，风格为圆形，旋转的
		pd.setTitle("修改密码");// 设置ProgressDialog 标题
		pd.setMessage("连接中...");// 设置ProgressDialog提示信息
		/*
		 * pd.setIcon(R.drawable.ic_la);// 设置ProgressDialog标题图标 //
		 * 设置ProgressDialog
		 */// 的进度条是否不明确 false 就是不设置为不明确
		pd.setIndeterminate(false);
		pd.setCancelable(true); // 设置ProgressDialog 是否可以按退回键取消
	}

}
