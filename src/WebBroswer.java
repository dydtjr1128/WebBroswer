import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.net.ssl.HttpsURLConnection;

public class WebBroswer implements Runnable {
	private Socket webSocket;
	private final String webURL = "www.naver.com";// http://www.seoul.go.kr/main/index.html
	private final int webPort = 443;
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
		Logger.println("Connected!");

		try {
			writer.write("GET /index.html HTTP/1.1\r\n");
			writer.write("Content-Type: text/html\r\n");
			writer.write("Host: " + webURL + "\r\n\r\n");
			writer.flush();

			Logger.println(webSocket.getLocalAddress() + " " + webSocket.getLocalSocketAddress());
			while ((message = reader.readLine()) != null) {
				System.out.println(message);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}
