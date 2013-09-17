package com.checkin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.content.Context;

/**
 * socket连接类
 * @author linwei
 *
 */
public class SocketUtil {

	Socket socket;
	PreferGeter geter;
	String IP;
	String username, password, workcode;
	BufferedReader in;
	PrintWriter out;

	public boolean isConnected = false;	

	public SocketUtil(Context con) {

		geter = new PreferGeter(con);
		IP = geter.getIP();
		username = geter.getUnm();
		password = geter.getPwd();
		workcode = geter.getWcd();
	}

	/**
	 * 连接至服务器
	 * @throws Exception
	 */
	public void connectServer() throws Exception {

		if (socket != null) {
			return;
		}
		try {
			// socket = new Socket("192.168.0.1",9999);
			socket = new Socket(IP, 9000);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream());
			isConnected = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("connect fail...check connectServer() method");
			isConnected = false;
			throw e;
			// 看看server是否已经启动
		}
	}

	/**
	 * 注册用户
	 * @param username
	 * @param pwd
	 * @param workcode
	 * @return isSuccess
	 */
	public boolean register(String username, String pwd, String workcode) {
		boolean isSuccess = false;
		try {
			out.println("create;" + username + ";" + pwd + ";" + workcode);
			out.flush();
			String getstr = in.readLine();
			System.out.println("registget="+getstr);
			if (getstr.equals("USERCREATED")) {
				isSuccess = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * 发送签到信息
	 * @return isSuccess
	 */
	public boolean sendCheck() {

		boolean isSuccess = false;

		try {
			out.println("check;" + username + ";" + password + ";" + workcode);
			out.flush();
			String getstr = in.readLine();
			System.out.println("checkget="+getstr);
			if(getstr.equals("LOGINSUCCESS")){
				isSuccess = true;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}

	/**
	 * 关闭服务器连接
	 */
	public void close() {
		try {
			if (socket != null) {
				socket.close();
			}
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
