package com.checkin.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SocketUtil {

	Socket socket;
	PreferGeter geter;
	int port;
	String IP;
	String username;
	DataInputStream din;
	DataOutputStream dout;

	public boolean isConnected = false;

	public SocketUtil(Context con) {

		geter = new PreferGeter(con);
		port = geter.getPort();
		IP = geter.getIP();
		username = geter.getUsername();
	}

	// 连接服务器
	public void connectServer() throws Exception {
		
		if (socket != null) {
			return;
		}
		try {
			// socket = new Socket("192.168.0.1",9999);
			socket = new Socket(IP, port);
			din = new DataInputStream(socket.getInputStream());
			dout = new DataOutputStream(socket.getOutputStream());
			isConnected = true;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("connect fail...check connectServer() method");
			isConnected = false;
			throw e;
			// 看看server是否已经启动
		}
	}

	// 注册
	public boolean register(String id, String pwd) {
		boolean isExist = false;
		try {
			dout.writeUTF("register;" + id + ";" + pwd);
			String booleanStr = din.readUTF();
			System.out.println(booleanStr);
			isExist = Boolean.parseBoolean(booleanStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isExist;
	}

	// 登录验证
	public boolean loginValidate(String id, String pwd) {
		
		boolean isValid = false;

		try {
			dout.writeUTF("login;" + id + ";" + pwd);
			System.out.println("write" + "login;" + id + ";" + pwd);
			String temp = din.readUTF();
			String booleanStr = temp.substring(0, temp.length() - 1);
			System.out.println("client receive " + booleanStr);
			isValid = Boolean.parseBoolean(booleanStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isValid;
	}

	// 向服务器发送签到的信息
	public boolean sendCheck() {

		boolean isSuccess = false;

		try {
			dout.writeUTF("check;" + username);
			String booleanStr = din.readUTF();
			System.out.println(booleanStr);
			isSuccess = Boolean.parseBoolean(booleanStr);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	public void close(){
		try {
			if(socket!=null){
				socket.close();
			}
			if(din!=null){
				din.close();
			}
			if(dout!=null){
				dout.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
