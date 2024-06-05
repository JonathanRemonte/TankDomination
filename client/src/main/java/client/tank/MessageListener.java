package client.tank;

import client.network.messages.ChatMessage;
import client.network.messages.GameWorldMessage;
import client.network.messages.LoginMessage;
import client.network.messages.LogoutMessage;
import client.network.messages.PlayerDied;

public interface MessageListener {
	public void loginReceieved(LoginMessage m);
	public void logoutReceieved(LogoutMessage m);
	public void gwmReceived(GameWorldMessage m);
	public void playerDiedReceived(PlayerDied m);
	public void chatMessageReceived(ChatMessage m);

}
