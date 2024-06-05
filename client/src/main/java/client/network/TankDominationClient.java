package client.network;

import java.io.IOException;
import java.net.InetAddress;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import client.network.messages.ChatMessage;
import client.network.messages.GameWorldMessage;
import client.network.messages.LoginMessage;
import client.network.messages.LogoutMessage;
import client.network.messages.PlayerDied;
import client.network.messages.PositionMessage;
import client.network.messages.ShootMessage;
import client.tank.MessageListener;

public class TankDominationClient {

	private Client client;
	private MessageListener game;

	private String inetAddress;

	private Logger logger = Logger.getLogger(TankDominationClient.class);

	public TankDominationClient(String inetAddress, MessageListener game) {

		this.game = game;
		this.inetAddress = inetAddress;
		client = new Client();
		registerClasses();
		addListeners();

	}

	public void connect() {
		client.start();
		try {
			logger.debug("Attempting to connect args[0]: " + inetAddress);
			client.connect(5000, InetAddress.getByName(inetAddress), 1234, 1235);
		} catch (IOException e) {
			logger.log(Level.ALL, e);
		}
	}

	private void addListeners() {

		client.addListener(new Listener() {

			@Override
			public void received(Connection connection, Object object) {

				Gdx.app.postRunnable(() -> {

					if (object instanceof ChatMessage) {
                        ChatMessage m = (ChatMessage) object;
                        TankDominationClient.this.game.chatMessageReceived(m);
                    } else if (object instanceof LoginMessage) {
						LoginMessage m = (LoginMessage) object;
						TankDominationClient.this.game.loginReceieved(m);

					} else if (object instanceof LogoutMessage) {
						LogoutMessage m = (LogoutMessage) object;
						TankDominationClient.this.game.logoutReceieved(m);
					} else if (object instanceof GameWorldMessage) {

						GameWorldMessage m = (GameWorldMessage) object;
						TankDominationClient.this.game.gwmReceived(m);
					} else if (object instanceof PlayerDied) {

						PlayerDied m = (PlayerDied) object;
						TankDominationClient.this.game.playerDiedReceived(m);
					}

				});

			}

		});
	}
	private void registerClasses() {
		// messages
		this.client.getKryo().register(LoginMessage.class);
		this.client.getKryo().register(LogoutMessage.class);
		this.client.getKryo().register(GameWorldMessage.class);
		this.client.getKryo().register(PositionMessage.class);
		this.client.getKryo().register(PositionMessage.DIRECTION.class);
		this.client.getKryo().register(ShootMessage.class);
		this.client.getKryo().register(PlayerDied.class);
		// primitive arrays
		this.client.getKryo().register(float[].class);
		this.client.getKryo().register(ChatMessage.class);

	}

	public void close() {
		client.close();
	}

	public void sendTCP(Object m) {
		client.sendTCP(m);
	}

	public void sendUDP(Object m) {
		client.sendUDP(m);
	}

}
