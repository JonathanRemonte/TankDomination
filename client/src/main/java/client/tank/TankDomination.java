package client.tank;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import client.states.StateController;
import client.states.State.StateEnum;

public class TankDomination extends ApplicationAdapter {

	private StateController sc;
	private String playerName;
	private String inetAddress;

	public TankDomination(String inetAddress, String playerName) {
		this.inetAddress = inetAddress;
		this.playerName = playerName;
	}

	@Override
	public void create() {

		sc = new StateController(inetAddress, playerName);
		sc.setState(StateEnum.MENU_STATE);

	}

	@Override
	public void render() {

		sc.render();
		sc.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void dispose() {
		super.dispose();
		sc.dispose();
	}

}
