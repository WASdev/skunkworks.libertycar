package skunkworks.libertycar;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import javax.swing.text.AsyncBoxView;
import javax.websocket.CloseReason;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.SendHandler;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.MessageHandler.Partial;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.RemoteEndpoint.Basic;

import org.junit.Test;

import com.ibm.pi.libertycar.webapp.CarControlEndpoint;
import com.ibm.pi.libertycar.webapp.CarController;

public class TestSuite {

	@Test
	public void test() {
		CarController carController = new CarController();
		CarControlEndpoint cce = new CarControlEndpoint();
		Session session = new Session() {
			
			@Override
			public void setMaxTextMessageBufferSize(int length) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setMaxIdleTimeout(long milliseconds) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setMaxBinaryMessageBufferSize(int length) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void removeMessageHandler(MessageHandler handler) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isSecure() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isOpen() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Map<String, Object> getUserProperties() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Principal getUserPrincipal() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public URI getRequestURI() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Map<String, List<String>> getRequestParameterMap() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getQueryString() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getProtocolVersion() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Map<String, String> getPathParameters() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<Session> getOpenSessions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getNegotiatedSubprotocol() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public List<Extension> getNegotiatedExtensions() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Set<MessageHandler> getMessageHandlers() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getMaxTextMessageBufferSize() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public long getMaxIdleTimeout() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public int getMaxBinaryMessageBufferSize() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getId() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public WebSocketContainer getContainer() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Basic getBasicRemote() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Async getAsyncRemote() {
				// TODO Auto-generated method stub
				return new Async() {
					
					@Override
					public void setBatchingAllowed(boolean allowed) throws IOException {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void sendPong(ByteBuffer applicationData) throws IOException, IllegalArgumentException {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void sendPing(ByteBuffer applicationData) throws IOException, IllegalArgumentException {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public boolean getBatchingAllowed() {
						// TODO Auto-generated method stub
						return false;
					}
					
					@Override
					public void flushBatch() throws IOException {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void setSendTimeout(long timeoutmillis) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void sendText(String text, SendHandler handler) {
						System.out.println("Sednign test");
						
					}
					
					@Override
					public Future<Void> sendText(String text) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public void sendObject(Object data, SendHandler handler) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public Future<Void> sendObject(Object data) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public void sendBinary(ByteBuffer data, SendHandler handler) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public Future<Void> sendBinary(ByteBuffer data) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public long getSendTimeout() {
						// TODO Auto-generated method stub
						return 0;
					}
				};
			}
			
			@Override
			public void close(CloseReason closeReason) throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void close() throws IOException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void addMessageHandler(Class<T> clazz, Partial<T> handler) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public <T> void addMessageHandler(Class<T> clazz, Whole<T> handler) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void addMessageHandler(MessageHandler handler) throws IllegalStateException {
				// TODO Auto-generated method stub
				
			}
		};
		cce.onOpen(session, null);
		cce.setControl(carController);
		cce.receiveMessage("{\"throttle\": 100, \"turning\": 100, \"id\": \"wibble\"}");
	}
}
