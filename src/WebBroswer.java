import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;


public class WebBroswer implements Runnable {
	private Socket webSocket;
	private final String webURL = "www.naver.com";// http://www.seoul.go.kr/main/index.html
	private final String webPath = "/index.html";
	private final int webPort = 443;//https는 443
	private BufferedWriter writer;
	private BufferedReader reader;

	public WebBroswer() {
		Thread broswer = new Thread(this);
		broswer.start();
	}

	@Override
	public void run() {
		httpsConnection();
		receiveMessage();
	}

	public void httpConnection() {
		try {
			webSocket = new Socket(webURL, webPort);
			writer = new BufferedWriter(new OutputStreamWriter(webSocket.getOutputStream(), "UTF8"));
			reader = new BufferedReader(new InputStreamReader(webSocket.getInputStream(), "UTF8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void httpsConnection() {
		try {
			webSocket = HttpsURLConnection.getDefaultSSLSocketFactory().createSocket(webURL, webPort);
			writer = new BufferedWriter(new OutputStreamWriter(webSocket.getOutputStream(), "UTF8"));
			reader = new BufferedReader(new InputStreamReader(webSocket.getInputStream(), "UTF8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveMessage() {
		String message = null;
		FileWriter fileWriter = null;
		long time = System.currentTimeMillis(); 

		SimpleDateFormat dayTime = new SimpleDateFormat("yyMMddhhmm");

		String str = dayTime.format(new Date(time));


		
		Logger.println("Connected!");

		try {
			fileWriter = new FileWriter(new File(webURL+str + ".txt"));
			writer.write("GET " + webPath + " HTTP/1.1\r\n");
			writer.write("Content-Type: text/html\r\n");
			writer.write("Host: " + webURL + "\r\n\r\n");
			writer.flush();

			Logger.println(webSocket.getLocalAddress() + " " + webSocket.getLocalSocketAddress());
			while ((message = reader.readLine()) != null) {
				System.out.println(message);
				fileWriter.write(message + "\r\n");
				if(message.equals("0"))
					break;
			}
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}
