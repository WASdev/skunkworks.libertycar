package skunkworks.libertycar.util;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.Extension;
import javax.websocket.MessageHandler;
import javax.websocket.MessageHandler.Partial;
import javax.websocket.MessageHandler.Whole;
import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.junit.Assert;

public class MockWebSocket implements Session {
	
	private MockAsyncRemote mockRemoteAsync = new MockAsyncRemote();

	@Override
	public WebSocketContainer getContainer() {
		Assert.fail("Method on MockWebSocket not implemented");
		
		return null;
	}

	@Override
	public void addMessageHandler(MessageHandler handler) throws IllegalStateException {
		Assert.fail("Method on MockWebSocket not implemented");
		
	}

	@Override
	public <T> void addMessageHandler(Class<T> clazz, Whole<T> handler) {
		Assert.fail("Method on MockWebSocket not implemented");
		
	}

	@Override
	public <T> void addMessageHandler(Class<T> clazz, Partial<T> handler) {
		Assert.fail("Method on MockWebSocket not implemented");
		
	}

	@Override
	public Set<MessageHandler> getMessageHandlers() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public void removeMessageHandler(MessageHandler handler) {
		Assert.fail("Method on MockWebSocket not implemented");
		
	}

	@Override
	public String getProtocolVersion() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public String getNegotiatedSubprotocol() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public List<Extension> getNegotiatedExtensions() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public boolean isSecure() {
		Assert.fail("Method on MockWebSocket not implemented");
		return false;
	}

	@Override
	public boolean isOpen() {
		Assert.fail("Method on MockWebSocket not implemented");
		return false;
	}

	@Override
	public long getMaxIdleTimeout() {
		Assert.fail("Method on MockWebSocket not implemented");
		return 0;
	}

	@Override
	public void setMaxIdleTimeout(long milliseconds) {
		Assert.fail("Method on MockWebSocket not implemented");
		
	}

	@Override
	public void setMaxBinaryMessageBufferSize(int length) {
		Assert.fail("Method on MockWebSocket not implemented");
		
	}

	@Override
	public int getMaxBinaryMessageBufferSize() {
		Assert.fail("Method on MockWebSocket not implemented");
		return 0;
	}

	@Override
	public void setMaxTextMessageBufferSize(int length) {
		Assert.fail("Method on MockWebSocket not implemented");
		
	}

	@Override
	public int getMaxTextMessageBufferSize() {
		Assert.fail("Method on MockWebSocket not implemented");
		return 0;
	}

	@Override
	public Async getAsyncRemote() {
		return mockRemoteAsync;
	}

	@Override
	public Basic getBasicRemote() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public String getId() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public void close() throws IOException {
		Assert.fail("Method on MockWebSocket not implemented");
		
	}

	@Override
	public void close(CloseReason closeReason) throws IOException {
		Assert.fail("Method on MockWebSocket not implemented");
		
	}

	@Override
	public URI getRequestURI() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public Map<String, List<String>> getRequestParameterMap() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public String getQueryString() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public Map<String, String> getPathParameters() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public Map<String, Object> getUserProperties() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public Principal getUserPrincipal() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}

	@Override
	public Set<Session> getOpenSessions() {
		Assert.fail("Method on MockWebSocket not implemented");
		return null;
	}
	
	public String getLastMessage() {
		return mockRemoteAsync.getLastMessage();
	}

}
