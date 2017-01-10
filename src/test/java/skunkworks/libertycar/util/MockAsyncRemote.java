package skunkworks.libertycar.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

import javax.websocket.RemoteEndpoint.Async;
import javax.websocket.SendHandler;

import org.junit.Assert;

public class MockAsyncRemote implements Async {
	
	private volatile String lastMessage = null;

	@Override
	public void setBatchingAllowed(boolean allowed) throws IOException {
		Assert.fail("Method on MockAsyncRemote not implemented");

	}

	@Override
	public boolean getBatchingAllowed() {
		Assert.fail("Method on MockAsyncRemote not implemented");
		return false;
	}

	@Override
	public void flushBatch() throws IOException {
		Assert.fail("Method on MockAsyncRemote not implemented");

	}

	@Override
	public void sendPing(ByteBuffer applicationData) throws IOException, IllegalArgumentException {
		Assert.fail("Method on MockAsyncRemote not implemented");

	}

	@Override
	public void sendPong(ByteBuffer applicationData) throws IOException, IllegalArgumentException {
		Assert.fail("Method on MockAsyncRemote not implemented");

	}

	@Override
	public long getSendTimeout() {
		Assert.fail("Method on MockAsyncRemote not implemented");
		return 0;
	}

	@Override
	public void setSendTimeout(long timeoutmillis) {
		Assert.fail("Method on MockAsyncRemote not implemented");

	}

	@Override
	public void sendText(String text, SendHandler handler) {
		Assert.fail("Method on MockAsyncRemote not implemented");

	}

	@Override
	public Future<Void> sendText(String text) {
		lastMessage = text;
		return null;
	}

	@Override
	public Future<Void> sendBinary(ByteBuffer data) {
		Assert.fail("Method on MockAsyncRemote not implemented");
		return null;
	}

	@Override
	public void sendBinary(ByteBuffer data, SendHandler handler) {
		Assert.fail("Method on MockAsyncRemote not implemented");

	}

	@Override
	public Future<Void> sendObject(Object data) {
		Assert.fail("Method on MockAsyncRemote not implemented");
		return null;
	}

	@Override
	public void sendObject(Object data, SendHandler handler) {
		Assert.fail("Method on MockAsyncRemote not implemented");

	}
	
	public String getLastMessage() {
		return lastMessage;
	}

}
