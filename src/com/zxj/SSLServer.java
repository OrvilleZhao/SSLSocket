package com.zxj;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyStore;

import javax.net.ssl.*;

public class SSLServer extends SSLConnect {
 
    private int port; 
    private String serverPrivateKey;
    private String serverKeyPassword;
    private String trustKey;
    private String trustKeyPassword;
    private boolean run=true;
    private SSLServerSocket sslServerSocket;
    private SSLSocket sslSocket;

   /**
    * �÷�����Ҫ��ʼ��SSLServer��͸�����
    * @param port ���÷����������Ķ˿ں�
    * @param serverPrivateKey ���÷����˽Կλ��
    * @param serverKeyPassword ���÷����˽Կ����
    * @param trustKey	��������˽Կλ��
    * @param trustKeyPassword ��������˽Կ����
    * @throws Exception	�׳��÷������쳣
    */
    public SSLServer(int port,String serverPrivateKey,String serverKeyPassword,String trustKey,String trustKeyPassword) throws Exception{
        this.port=port;
        this.serverKeyPassword=serverKeyPassword;
        this.serverPrivateKey=serverPrivateKey;
        this.trustKey=trustKey;
        this.trustKeyPassword=trustKeyPassword;
        sslServerSocket=createSSLServerSocket();
    }
    /**
     * �÷������ڽ���SSLͨ��
     * @return	����SSL��
     * @throws Exception
     */
    private SSLServerSocket createSSLServerSocket() throws Exception {
    	  SSLContext ctx = SSLContext.getInstance("SSL");  
          KeyManagerFactory kmf=KeyManagerFactory.getInstance("SunX509");
          TrustManagerFactory tmf=TrustManagerFactory.getInstance("SunX509");
          KeyStore ks=KeyStore.getInstance("JKS");
          KeyStore tks=KeyStore.getInstance("JKS");
          ks.load(new FileInputStream(serverPrivateKey),serverKeyPassword.toCharArray());
          tks.load(new FileInputStream(trustKey),trustKeyPassword.toCharArray());
          kmf.init(ks,serverKeyPassword.toCharArray());
          tmf.init(tks);
          ctx.init(kmf.getKeyManagers(),tmf.getTrustManagers() , null);
          SSLServerSocket serverSocket=(SSLServerSocket)ctx.getServerSocketFactory().createServerSocket(port);
          serverSocket.setNeedClientAuth(true);
          return serverSocket;
   }  
    /**
     * �÷�����������������������Ӧ�𣬿ͻ�����Ϣͨ��GetMessage()��ȡ��Ӧ����Ϣ��setReply()����
     * @throws Exception 
     * @throws Exception
     */
   public void startServer(SolveRequestListener listener){  
	          try{
			      while(run){  
			    	  	sslSocket=(SSLSocket)sslServerSocket.accept();  
		                BufferedReader bis=new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
		                PrintWriter bos=new PrintWriter(new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream())));
		                byte[] message=bis.readLine().getBytes();                
		                SolveRequest event=new SolveRequest(decompose(message));
		                Result reply=listener.SolveEvent(event);          
		                bos.println(new String(compose(reply.message,reply.servCode)));
		                bos.flush();              
		                bis.close();
		                bos.close();
		                sslSocket.close();
			      }
	          }catch(Exception e){
	        	  try {
	  				sslSocket.close();
		  			} catch (IOException e1) {
		  				e1.printStackTrace();
		  			}
	          }
   }
   
   public void stopServer() throws IOException{
	   	run=false;
	   	sslSocket.close();
   }
}

