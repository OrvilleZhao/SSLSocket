package com.zxj;

import java.io.UnsupportedEncodingException;

/**
 *@author  zxj 
 *@time  2017年10月14日上午10:50:37
 *@describe
 */
public class SSLConnect {
	protected byte[] compose(String message,String servCode) throws Exception{
    	ByteDataBuffer bdb=new ByteDataBuffer();
    	bdb.setInBigEndian(false);
    	bdb.writeInt8((byte) 0x68);
    	int len =Integer.parseInt(getLen(message,4));
    	bdb.writeInt32(len);
    	bdb.writeString(servCode,6);
    	bdb.writeBytes(message.getBytes());
    	bdb.writeInt8((byte) 0x16);
    	return bdb.getBytes();
    }
    protected String decompose(byte[] data) throws Exception{
    	ByteDataBuffer obj=new ByteDataBuffer(data);
    	obj.setEncoding("UTF-8");
    	obj.setInBigEndian(false);
    	byte sign=obj.readInt8();
    	if(sign!=0x68){
    		return "无法找到起始标记！";
    	}
    	int totalLen=obj.readInt32();
    	obj.readString(6);
    	byte[] dataBytes=new byte[totalLen];
    	obj.readBytes(dataBytes);
    	String message=new String(dataBytes);
    	return message;
    }
    protected String getLen(String text, int needlen) {
		if (text != null) {
			int len;
			try {
				len = text.getBytes("utf-8").length;
				String lenStr = String.valueOf(len);
				StringBuffer sb = new StringBuffer(lenStr);
				while (sb.length() < needlen) {
					sb.insert(0, "0");
				}
				return sb.toString();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
