package test.lixiaofei;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ctvit.dev.RootActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import test.lixiaofei.util.GetIpAddress;

public class WifiSerActivity extends RootActivity{

    private TextView editText_1;
    private EditText editText_2;
    private ServerSocket serverSocket = null;
    StringBuffer stringBuffer = new StringBuffer();

    private InputStream inputStream;

    public Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 1:
                    editText_1.setText(msg.obj.toString());
    	            Toast.makeText(WifiSerActivity.this,"获取到IP地址和端口号..."+msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case 2:
//    	            Toast.makeText(WifiSerActivity.this,"收到客服的发送过来数据..."+msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    editText_2.setText(msg.obj.toString());
                    stringBuffer.setLength(0);

                    break;

            }

        }
    };
    private BufferedReader bufferedReader;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		   setContentView(R.layout.activity_wifi_ser);

	        editText_1 = (TextView) findViewById(R.id.et_1);
	        editText_2 = (EditText) findViewById(R.id.et_2);

	        receiveData();
	   }
	
	
	
	

    /*
    服务器端接收数据
    需要注意以下一点：
    服务器端应该是多线程的，因为一个服务器可能会有多个客户端连接在服务器上；
    */
    public void receiveData(){

        Thread thread = new Thread(){
            @Override
            public void run() {
                super.run();
                /*指明服务器端的端口号*/
                try {
                    serverSocket = new ServerSocket(8000);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                GetIpAddress.getLocalIpAddress(serverSocket);

                Message message_1 = handler.obtainMessage();
                message_1.what = 1;
                message_1.obj = "IP:" + GetIpAddress.getIP() + " PORT: " + GetIpAddress.getPort();
                System.out.println("IP:" + GetIpAddress.getIP() + " PORT: " + GetIpAddress.getPort());
                handler.sendMessage(message_1);

                while (true){
                    Socket socket = null;
                    try {
                        socket = serverSocket.accept();
                        inputStream  = socket.getInputStream();
                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream , "GBK"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    new ServerThread(socket,bufferedReader ).start();

                }
            }
        };
        thread.start();

    }

    class ServerThread extends Thread{

        private Socket socket;
        private BufferedReader   bufferedReader;
        private StringBuffer stringBuffer = WifiSerActivity.this.stringBuffer;


        public ServerThread(Socket socket, BufferedReader bufferedReader){
            this.socket = socket;
            this.bufferedReader = bufferedReader;
        }

//        @Override
//        public void run() {
//            int len;
//            byte[] bytes = new byte[20];
//            boolean isString = false;
//
//            try {
//                //在这里需要明白一下什么时候其会等于 -1，其在输入流关闭时才会等于 -1，
//                //并不是数据读完了，再去读才会等于-1，数据读完了，最结果也就是读不到数据为0而已；
//                while ((len = inputStream.read(bytes)) != -1) {
//                    for(int i=0; i<len; i++){
//                        if(bytes[i] != '\0'){
//                            System.out.println("AAAAAAA---------------->"+(char)bytes[i]);
//                            stringBuffer.append((char)bytes[i]);
//                        }else {
//                            isString = true;
//                            break;
//                        }
//                    }
//                    if(isString){
//                        Message message_2 = handler.obtainMessage();
//                        message_2.what = 2;
//                        message_2.obj = KeyBoardHideTool.ChangeUTF8Str(stringBuffer.toString());
//                        handler.sendMessage(message_2);
//                        isString = false;
//                    }
//
//                }
//                //当这个异常发生时，说明客户端那边的连接已经断开
//            } catch (IOException e) {
//	            Toast.makeText(WifiSerActivity.this,"客户端连接已断开，尝试断网重连...",Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//                try {
//                    inputStream.close();
//                    socket.close();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//
//            }
//
//        }
        @Override
        public void run() {
            String str ;

            try {
                //在这里需要明白一下什么时候其会等于 -1，其在输入流关闭时才会等于 -1，
                //并不是数据读完了，再去读才会等于-1，数据读完了，最结果也就是读不到数据为0而已；
                while ((str = bufferedReader.readLine()) != null) {
                        if("over".equals(str)){
                            break;
                        }

                        Message message_2 = handler.obtainMessage();
                        message_2.what = 2;
                        message_2.obj = str;
                        handler.sendMessage(message_2);

                }
                //当这个异常发生时，说明客户端那边的连接已经断开
            } catch (IOException e) {
	            Toast.makeText(WifiSerActivity.this,"客户端连接已断开，尝试断网重连...",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                try {
                    bufferedReader.close();
                    inputStream.close();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }

        }
    }


    /*当按返回键时，关闭相应的socket资源*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
        	if(serverSocket != null){
                serverSocket.close();
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	
}
