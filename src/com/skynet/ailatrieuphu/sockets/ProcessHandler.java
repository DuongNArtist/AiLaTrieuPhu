package com.skynet.ailatrieuphu.sockets;

import android.util.Log;

public class ProcessHandler extends MessageHandler {

	public final static String TAG = ProcessHandler.class.getSimpleName();
	public static int ID = 0;
	private static ProcessHandler mInstance;
	int send = 0;
	static int step;
	private SocketListenner mListenner;
	private Thread threadReceiver;

	public ProcessHandler() {
	}

	public void onConnected() {
		SendData.sendProvider();
		if (mListenner != null) {
			mListenner.onConnected();
		}
	}

	public void onDisconnected() {
		mListenner.onDisconnected();
	}

	public static ProcessHandler getInstance() {
		if (mInstance == null) {
			mInstance = new ProcessHandler();
		}
		return mInstance;
	}

	public void processMessage(Message message, int messageId) {
		if (mListenner == null) {
			threadReceiver = Thread.currentThread();
			synchronized (threadReceiver) {
				try {
					threadReceiver.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		Log.d(TAG,
				"Id = " + messageId + "; ServerCommand = "
						+ message.getServerCommand());
		mListenner.onReceived(message);
	}

	@Override
	protected void serviceMessage(Message message, int messageId) {
		processMessage(message, messageId);
	}

	public void setListenner(SocketListenner listener) {
		this.mListenner = listener;
		if (threadReceiver != null && this.mListenner != null) {
			synchronized (threadReceiver) {
				threadReceiver.notify();
			}
		}
	}

	@Override
	public void onFailed(int reason) {
		if (this.mListenner != null) {
			this.mListenner.onFailed(reason);
		}
	}
}
