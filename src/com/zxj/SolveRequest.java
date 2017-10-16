package com.zxj;


/**
 *@author  zxj 
 *@time  2017年10月14日上午10:36:53
 *@describe
 */
public class SolveRequest {
	private String str;
	public SolveRequest(String message) {
		// TODO Auto-generated constructor stub
		if(!message.equals(""))
			this.str=message;
		else
			this.str="";
	}
	public String GetMessage(){
		return str;
	}
}
