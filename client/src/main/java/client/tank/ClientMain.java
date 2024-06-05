package client.tank;

import java.security.SecureRandom;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import client.tank.utils.GameConstants;

public class ClientMain {

	private static Logger logger = Logger.getLogger(ClientMain.class);

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameConstants.SCREEN_WIDTH;
		config.height = GameConstants.SCREEN_HEIGHT;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;
		config.resizable = false;
		// config.x = 2500;

		String ip = null;
		if (arg.length == 0) {
			logger.debug("No arg has been passed. LOCALHOST ip.");
			ip = "localhost";
		} else {
			ip = arg[0];
		}

		String playerName = JOptionPane.showInputDialog("Enter your name:");
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Player" + new SecureRandom().nextInt(1000);
        }

		new LwjglApplication(new TankDomination(ip, playerName), config);
	}
}
