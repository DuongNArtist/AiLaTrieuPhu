package com.skynet.ailatrieuphu.sockets;

import java.util.HashMap;

public class ProtocolCallback {
	private HashMap<String, SocketCallback> mListCallback = new HashMap<String, SocketCallback>();

	public ProtocolCallback(SocketCallback callback) {
		add(callback);
	}

	public void add(SocketCallback callback) {
		mListCallback.put(callback.getClass().toString(), callback);
	}

	public void remove(SocketCallback callback) {
		mListCallback.remove(callback.getClass().toString());
	}

	public int size() {
		return mListCallback.size();
	}

	public void clear() {
		mListCallback.clear();
	}
}
