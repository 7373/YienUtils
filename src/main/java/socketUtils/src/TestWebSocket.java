package socketUtils.src;

import org.java_websocket.WebSocket;
import org.java_websocket.client.DefaultWebSocketClientFactory;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.InvalidDataException;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.handshake.ClientHandshakeBuilder;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.security.MessageDigest;

/*
 * *********************************************************************
 * WebSocket测试类 <br/>
 * WebSocketConnect.java <br/>
 **********************************************************************
 */
public class TestWebSocket {

    /*测试刷脸事件*/
    public static final String url = "/ccbc-web/sockjs/third/pcardEvent.ws";

    public static void main(String[] args) throws Exception {
		String uri = String.format("wss://%s:%s%s", ip, port, url);
        System.out.println(uri);
        WebSocketChatClient chatclient = new WebSocketChatClient(new URI(uri));
		chatclient.setWebSocketFactory(new DefaultWebSocketClientFactory(chatclient));
		chatclient.connect();
	}
}

class WebSocketChatClient extends WebSocketClient {
	
	public WebSocketChatClient(URI serverUri) {
		super(serverUri, new Draft_17());
	}

	@Override
	public void onWebsocketHandshakeSentAsClient(WebSocket conn, ClientHandshake request) throws InvalidDataException {
		((ClientHandshakeBuilder) request).put("App-Key", APP_KEY);
		long currentTimeMillis = System.currentTimeMillis();
		String t1 = getMD5("GET" + TestWebSocket.url + currentTimeMillis + APP_SECURITY);
		((ClientHandshakeBuilder) request).put("App-Token", currentTimeMillis + t1);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		System.out.println("Connected");

	}

	@Override
	public void onMessage(String message) {
		// 接收到的信息
		System.out.println("got: ");
        System.out.println(message);

	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		System.out.println("Disconnected");
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
	}

	private static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	private static String getMD5(String src) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(src.getBytes("UTF-8"));
			byte[] b = md.digest();
			return bufferToHex(b, 0, b.length);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];
		char c1 = hexDigits[bt & 0xf];
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}
}
