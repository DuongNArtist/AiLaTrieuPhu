package com.skynet.ailatrieuphu.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Vector;

import com.skynet.ailatrieuphu.constants.Constants;

public class SocketManger {

	public static final int HEADER_LENGTH = 4;
	public static final String DOMAIN = "210.211.124.135";
	public static final int PORT = 1005;

	public static boolean mConnected = false;
	public static boolean mConnecting;
	public static Thread mInitialThread;
	public static Thread mCollectorThread;
	public static Thread mSendThread;
	public static Thread mPingThread;
	public static int mSendByteCount;
	public static int mReceiveByteCount;
	public static long mCurrTimeServer = System.currentTimeMillis();

	private static DataOutputStream mDataOutputStream;
	private static InputStream mInputStream;
	private static MessageHandler mMessageHandler = ProcessHandler
			.getInstance();
	private static Socket mSocket;
	private static MessageSender mMessageSender;
	private static int mCountPing = 0;
	private static long mTimeStartToPing = 0;
	private static boolean mIsActive = true;

	static {
		mMessageSender = new MessageSender();
	}

	public static void init() {
		if (mMessageSender == null) {
			mMessageSender = new MessageSender();
		}
	}

	public static boolean isConnected() {
		return mConnected;
	}

	public static void connect() {
		if (mConnected || mConnecting) {
			return;
		} else {
			mConnecting = true;
			mConnected = true;
			mSocket = null;
			if (mInitialThread != null) {
				mInitialThread.interrupt();
				mInitialThread = null;
			}
			mInitialThread = new Thread(new NetworkInit());
			mInitialThread.start();
		}
	}

	static class NetworkInit implements Runnable {
		private final String primary = "ailatrieuphu.mobi";
		private final String secondary = "103.1.210.51";
		private final int port = 6622;

		NetworkInit() {

		}

		public void run() {
			try {
				doConnect(primary, port);
				ProcessHandler.getInstance().onConnected();
			} catch (Exception ex) {
				ex.printStackTrace();
				try {
					doConnect(secondary, port);
					ProcessHandler.getInstance().onConnected();
				} catch (Exception e) {
					mConnected = false;
					if (mMessageHandler != null) {
						close();
						mMessageHandler.onFailed(-1);
					}
				}
			}
		}

		public void doConnect(String IP, int P) throws IOException {
			mConnecting = true;
			mSocket = new Socket();
			SocketAddress sadd = new InetSocketAddress(IP, P);
			mSocket.connect(sadd, Constants.CONNECTION_TIME_OUT);
			mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
			mInputStream = mSocket.getInputStream();
			mSendThread = new Thread(mMessageSender);
			mSendThread.start();
			mCollectorThread = new Thread(new MessageCollector());
			mCollectorThread.start();
		}
	}

	public static void sendMessage(Message message) {
		synchronized (mMessageSender) {
			mMessageSender.AddMessage(message);
			mMessageSender.notify();
			if (mPingThread == null || !mPingThread.isAlive()) {
				mPingThread = new Thread(Ping.getInstance());
				mPingThread.start();
			}
		}
	}

	public static void startPing() {
		checkPing = true;
		mPingThread = null;
		mPingThread = new Thread(Ping.getInstance());
		mPingThread.start();
		mCountPing = 0;
	}

	public static void stopPing() {
		checkPing = false;
	}

	static class MessageSender implements Runnable {

		private final Vector sendingMessage;
		private Object lock;

		public MessageSender() {
			sendingMessage = new Vector();
		}

		public void AddMessage(Message message) {
			sendingMessage.addElement(message);
		}

		public void run() {
			while (SocketManger.isConnected()) {
				synchronized (mMessageSender) {
					try {
						while (sendingMessage.size() > 0) {
							long startTime = System.currentTimeMillis();
							Message msg = (Message) sendingMessage.elementAt(0);
							sendingMessage.removeElementAt(0);
							byte[] buffer = msg.getBuffer().getData();
							int cmd = msg.getServerCommand();
							if (buffer.length > 0) {
								int packageLength = buffer.length;
								mDataOutputStream.writeInt(packageLength);
								mDataOutputStream.writeInt(cmd);
								mDataOutputStream.write(buffer, 0,
										packageLength);
								mDataOutputStream.flush();
							}
							long delayTime = 100 - (System.currentTimeMillis() - startTime);
							if (delayTime > 0) {
								Thread.sleep(delayTime);
							}
						}
						mMessageSender.wait();
					} catch (IOException ex) {
						ex.printStackTrace();
						SocketManger.close();
					} catch (Exception ex) {
						SocketManger.close();
					}
				}
			}
			if (mConnected) {
				if (mMessageHandler != null) {
					mMessageHandler.onDisconnected();
				}
			}
		}

	}

	static class MessageCollector implements Runnable {

		public void run() {
			Message message;
			try {
				while (SocketManger.isConnected()) {
					message = readMessage();
					if (message != null) {
						mTimeStartToPing = System.currentTimeMillis();
						mCountPing = 0;
						mMessageHandler.processMessage(message);
					} else {
						mMessageHandler.onDisconnected();
						break;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			if (mConnected) {
				if (mMessageHandler != null) {
					mMessageHandler.onDisconnected();
				}
				cleanNetwork();
			}
		}

		private Message readMessage() throws Exception {
			DataInputStream useInputStream = null;
			try {
				useInputStream = new DataInputStream(mInputStream);

			} catch (Exception e) {
				e.printStackTrace();
			}
			int protocol;
			int cmdType;
			int byteRead = 0;
			int allData;
			byte[] header = new byte[HEADER_LENGTH];
			while (byteRead > -1 && byteRead < HEADER_LENGTH) {
				int len = useInputStream.read(header, byteRead, HEADER_LENGTH
						- byteRead);
				byteRead += len;
			}
			if (byteRead == -1) {
				return null;
			}
			cmdType = useInputStream.readInt();
			mCurrTimeServer = useInputStream.readLong();
			System.out.println(cmdType + " CMD");
			if (cmdType == -1) {
				return null;
			}
			byteRead = 0;
			allData = bytes2Int(header);
			byteRead = 0;
			byte[] data = new byte[allData];
			while (byteRead > -1 && byteRead < allData) {
				int len = useInputStream.read(data, byteRead, allData
						- byteRead);
				byteRead += len;
			}
			if (byteRead == -1) {
				return null;
			}
			Message message = new Message(cmdType, data);
			return message;
		}

		private int bytes2Int(byte[] data) {
			int res = 0;
			for (int i = 0; i < 4; i++) {
				res <<= 8;
				res |= (0xFF & data[i]);
			}
			return res;
		}
	}

	private static boolean checkPing;

	public static boolean isCheckPing() {
		return checkPing;
	}

	public static void setCheckPing(boolean ischeckPing) {
		checkPing = ischeckPing;
	}

	static class Ping implements Runnable {

		private static Ping instance;

		private Ping() {

		}

		public static Ping getInstance() {
			if (instance == null) {
				instance = new Ping();
			}
			return instance;

		}

		public void run() {
			try {
				Message message = new Message(Protocol.CMD_REQUEST_PING);
				while (checkPing) {
					Thread.sleep(1000);
					long t = System.currentTimeMillis();
					if (mIsActive) {
						mTimeStartToPing = t;
						mCountPing++;
						if (mCountPing <= 8) {
							sendMessage(message);
						} else {
							if (mMessageHandler != null) {
								mMessageHandler.onFailed(1);
							}
							cleanNetwork();
						}
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void close() {
		cleanNetwork();
	}

	private static void cleanNetwork() {
		try {
			mConnected = false;
			mConnecting = false;
			if (mDataOutputStream != null) {
				mDataOutputStream.close();
				mDataOutputStream = null;
			}
			if (mInputStream != null) {
				mInputStream.close();
				mInputStream = null;
			}
			if (mSocket != null) {
				mSocket.close();
				mSocket = null;
			}
			mSendThread = null;
			mCollectorThread = null;
			if (mInitialThread != null && mInitialThread.isAlive()) {
				mInitialThread = null;
			}
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void onPause() {
		mIsActive = false;
	}

	public static void onResume() {
		mIsActive = true;
	}
}
