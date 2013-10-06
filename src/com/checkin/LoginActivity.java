package com.checkin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.checkin.service.MyService;
import com.checkin.utils.PreferGeter;
import com.checkin.utils.SocketUtil;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String str_mUsername;
	private String str_mPassword;
	private String str_mWorkcode;
	private String ip;
	// UI references.
	private EditText edt_mUsername;
	private EditText edt_mPassword;
	private EditText edt_mWorkcode;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private SocketUtil connect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		// Set up the login form.
		edt_mUsername = (EditText) findViewById(R.id.email);
		edt_mPassword = (EditText) findViewById(R.id.password);
		edt_mWorkcode = (EditText) findViewById(R.id.workcode);

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		findViewById(R.id.regist_button).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(LoginActivity.this,
								RegistActivity.class));
					}

				});
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		edt_mUsername.setError(null);
		edt_mPassword.setError(null);

		// Store values at the time of the login attempt.
		str_mUsername = edt_mUsername.getText().toString();
		str_mPassword = edt_mPassword.getText().toString();
		str_mWorkcode = edt_mWorkcode.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(str_mPassword)) {
			edt_mPassword.setError(getString(R.string.error_field_required));
			focusView = edt_mPassword;
			cancel = true;
		} /*
		 * else if (str_mPassword.length() < 4) { edt_mPassword
		 * .setError(getString(R.string.error_invalid_password)); focusView =
		 * edt_mPassword; cancel = true; }
		 */

		// Check for a valid email address.
		if (TextUtils.isEmpty(str_mUsername)) {
			edt_mUsername.setError(getString(R.string.error_field_required));
			focusView = edt_mUsername;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Integer> {

		protected void onPreExecute() {
			ip = new PreferGeter(LoginActivity.this).getIP();
		}

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			Looper.prepare();
			connect = new SocketUtil(ip);
			if (!connect.isConnected) {
				try {
					connect.connectServer();
				} catch (Exception e) {

					return 0;
				}
			}
			boolean isSuccess = connect.login(str_mUsername, str_mPassword,
					str_mWorkcode);// 注册用户
			connect.close();
			if (isSuccess)
				return 1;
			else
				return 2;
		}

		@Override
		protected void onPostExecute(final Integer result) {
			mAuthTask = null;
			showProgress(false);
			switch (result) {
			case 0:
				Toast.makeText(LoginActivity.this, "连接服务器失败，请检查网络状况..",
						Toast.LENGTH_LONG).show();
				break;
			case 1:
				Toast.makeText(LoginActivity.this, "帐户登录成功！", Toast.LENGTH_LONG)
						.show();
				saveUser(); // 保存用户名密码
				// 弹出对话框
				new AlertDialog.Builder(LoginActivity.this)
						.setCancelable(false)
						.setIcon(R.drawable.ic_launcher)
						.setTitle("登录成功")
						.setMessage("您已成功登录用户：" + str_mUsername)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										startService(new Intent(
												LoginActivity.this,
												MyService.class));
										LoginActivity.this.finish();
										startActivity(new Intent(
												LoginActivity.this,
												MainActivity.class));
									}
								}).show();
				break;
			case 2:
				// edt_mPassword.setError(getString(R.string.error_incorrect_password));
				// // 显示错误框提示
				edt_mPassword.requestFocus();
				Toast.makeText(LoginActivity.this, "用户名，学号或密码错误",
						Toast.LENGTH_LONG).show();
				break;
			}

		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

	/**
	 * 保存用户名密码
	 */
	private void saveUser() {
		Editor editor;
		editor = this.getSharedPreferences("user", Context.MODE_PRIVATE).edit();
		editor.putString("username", str_mUsername);
		editor.putString("password", str_mPassword);
		editor.putString("workcode", str_mWorkcode);
		editor.commit();
	}
}
