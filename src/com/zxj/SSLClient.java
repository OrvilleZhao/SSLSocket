package com.zxj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;


public class SSLClient extends SSLConnect {
    private String serverHost;
    private int serverPort;
    private String clientPrivateKey;
    private String clientKeyPassword;
    private String trustKey;
    private String trustKeyPassword;
    private SSLSocket sslSocket;
    /**
     * ��ʼ��SSLClient�ಢΪ��������ֵ
     * @param serverHost ������IP��ַ
     * @param serverPort ������TCPͨ�ſ��Ŷ˿�
     * @param clientPrivateKey �ͻ���˽Կ
     * @param clientKeyPassword  �ͻ���˽Կ����
     * @param trustKey  ������Կ
     * @param trustKeyPassword ������Կ����
     * @throws Exception
     */
    public SSLClient(String serverHost,int serverPort,String clientPrivateKey,
            String clientKeyPassword,String trustKey,String trustKeyPassword) throws Exception{
        this.serverHost=serverHost;
        this.serverPort=serverPort;
        this.clientPrivateKey=clientPrivateKey;
        this.clientKeyPassword=clientKeyPassword;
        this.trustKey=trustKey;
        this.trustKeyPassword=trustKeyPassword;
        sslSocket=createSocket();
    }
    /**
     * ����SSL��
     * @return
     * @throws Exception
     */
    private SSLSocket createSocket() throws Exception {
          SSLContext ctx=SSLContext.getInstance("SSL");
          KeyManagerFactory kmf=KeyManagerFactory.getInstance("SunX509");
          TrustManagerFactory tmf=TrustManagerFactory.getInstance("SunX509");
          KeyStore ks=KeyStore.getInstance("JKS");
          KeyStore tks=KeyStore.getInstance("JKS");
          ks.load(new FileInputStream(clientPrivateKey),clientKeyPassword.toCharArray());
          tks.load(new FileInputStream(trustKey),trustKeyPassword.toCharArray());
          kmf.init(ks,clientKeyPassword.toCharArray());
          tmf.init(tks);
          ctx.init(kmf.getKeyManagers(),tmf.getTrustManagers() , null);
         return (SSLSocket)ctx.getSocketFactory().createSocket(serverHost,serverPort);
    }
    /**
     * �������������Ϣ
     * @param Message
     * @throws Exception
     */
    public String sendMessage(String message,String serveCode) throws Exception{ 
    	BufferedReader br = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
    	PrintWriter bos=new PrintWriter(new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream())));
        bos.println(new String(compose(message,serveCode))); 
        bos.flush();
    	byte[] str=br.readLine().getBytes();         
    	br.close();
    	bos.close();
    	return decompose(str);
    }
   public void close() throws IOException{
	   sslSocket.close();
   }
}

