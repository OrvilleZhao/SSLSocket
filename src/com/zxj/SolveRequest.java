package com.zxj;


/**
 *@author  zxj 
 *@time  2017��10��14������10:36:53
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
