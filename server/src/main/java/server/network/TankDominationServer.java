package server.network;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import server.network.messages.ChatMessage;
import server.network.messages.GameWorldMessage;
import server.network.messages.LoginMessage;
import server.network.messages.LogoutMessage;
import server.network.messages.PlayerDied;
import server.network.messages.PositionMessage;
import server.network.messages.ShootMessage;
import server.tank.MessageListener;

public class TankDominationServer {

	private Server server;
	private static final int TCP_PORT = 1234;
	private static final int UDP_PORT = 1235;
	private MessageListener messageListener;
	private Queue<Object> messageQueue;
	private Queue<Connection> connectionQueue;
	private Logger logger = Logger.getLogger(TankDominationServer.class);

	public TankDominationServer(MessageListener cmo) {

		this.messageListener = cmo;

		init();
	}

	private void init() {

		server = new Server();
		registerClasses();

		messageQueue = new LinkedList<>();
		connectionQueue = new LinkedList<>();

		server.addListener(new Listener() {

			@Override
			public void received(Connection connection, Object object) {

				messageQueue.add(object);
				connectionQueue.add(connection);

			}
		});
		server.start();
		try {
			server.bind(TCP_PORT, UDP_PORT);
			logger.debug("Server has ben started on TCP_PORT: " + TCP_PORT + " UDP_PORT: " + UDP_PORT);
		} catch (IOException e) {
			logger.log(Level.ALL, e);
		}

	}

	public void parseMessage() {

		if (connectionQueue.isEmpty() || messageQueue.isEmpty())
			return;

		for (int i = 0; i < messageQueue.size(); i++) {

			Connection con = connectionQueue.poll();
			Object message = messageQueue.poll();

			if (message instanceof ChatMessage) {
                ChatMessage m = (ChatMessage) message;
                messageListener.chatMessageReceived(m);
            } else if (message instanceof LoginMessage) {

				LoginMessage m = (LoginMessage) message;
				messageListener.loginReceived(con, m);

			} else if (message instanceof LogoutMessage) {
				LogoutMessage m = (LogoutMessage) message;
				messageListener.logoutReceived(m);

			} else if (message instanceof PositionMessage) {
				PositionMessage m = (PositionMessage) message;
				messageListener.playerMovedReceived(m);

			} else if (message instanceof ShootMessage) {
				ShootMessage m = (ShootMessage) message;
				messageListener.shootMessageReceived(m);
			}

		}

	}

	private void registerClasses() {
		this.server.getKryo().register(LoginMessage.class);
		this.server.getKryo().register(LogoutMessage.class);
		this.server.getKryo().register(GameWorldMessage.class);
		this.server.getKryo().register(PositionMessage.class);
		this.server.getKryo().register(PositionMessage.DIRECTION.class);
		this.server.getKryo().register(ShootMessage.class);
		this.server.getKryo().register(PlayerDied.class);
		this.server.getKryo().register(float[].class);
		this.server.getKryo().register(ChatMessage.class);
	}

	public void sendToAllUDP(Object m) {
		server.sendToAllUDP(m);
	}

	public void sendToUDP(int id, Object m) {
		server.sendToUDP(id, m);
	}
}
