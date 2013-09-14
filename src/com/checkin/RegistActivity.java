package com.checkin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.checkin.utils.SocketUtil;

public class RegistActivity extends Activity {
	private String regist_account = null;
	private String regist_pwd = null;
	private String regist_group = null;
	private String regist_describe = null;

	boolean regist_group_rs = false;

	private EditText regist_edt_account;
	private EditText regist_edt_pwd;
	private EditText regist_edt_describe;

	private CheckBox regist_rb_group;

	private Button regist_btn_regist;
	private Button regist_btn_clean;

	private SocketUtil mynetcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);

		mynetcon = new SocketUtil(this);
		regist_edt_account = (EditText) this.findViewById(R.id.regist_account);
		regist_edt_pwd = (EditText) this.findViewById(R.id.regist_psw);
		regist_rb_group = (CheckBox) this.findViewById(R.id.regist_group);
		regist_edt_describe = (EditText) this
				.findViewById(R.id.regist_describe);

		regist_btn_regist = (Button) this.findViewById(R.id.regist_btn_account);
		regist_btn_clean = (Button) this.findViewById(R.id.regist_clean_table);

		regist_btn_regist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				regist_account = regist_edt_account.getText().toString();
				regist_pwd = regist_edt_pwd.getText().toString();
				regist_group_rs = regist_rb_group.isChecked();
				regist_describe = regist_edt_describe.getText().toString();
				


				new Thread() {

					@Override
					public void run() {

						switch (1) {
						case 0:
							

							break;
						case 1:

							Toast.makeText(RegistActivity.this, "帐户已存在，请重新注册",
									Toast.LENGTH_LONG).show();
							break;
						case 2:
							Toast.makeText(RegistActivity.this, "系统错误！",
									Toast.LENGTH_LONG).show();
							break;

						}

						super.run();
					}

				}.start();

			}

		});

		regist_btn_clean.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				regist_edt_account.setText("");
				regist_edt_pwd.setText("");
				regist_edt_describe.setText("");

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		this.finish();
		return super.onOptionsItemSelected(item);
	}
	
	private void showProgressDialog(){
		ProgressDialog pd = new ProgressDialog(this);
		
		ProgressDialog.show(getApplicationContext(), "注册中", "");
		
	}

}
