package server.tank;

import com.esotericsoftware.kryonet.Connection;

import server.network.messages.ChatMessage;
import server.network.messages.LoginMessage;
import server.network.messages.LogoutMessage;
import server.network.messages.PositionMessage;
import server.network.messages.ShootMessage;

public interface MessageListener {
	public void shootMessageReceived(ShootMessage pp);
	public void loginReceived(Connection con, LoginMessage m);
	public void logoutReceived(LogoutMessage m);
	public void playerMovedReceived(PositionMessage move);
	public void chatMessageReceived(ChatMessage m);

}
