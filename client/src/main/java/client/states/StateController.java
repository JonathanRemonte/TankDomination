package client.states;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;

import client.states.State.StateEnum;

public class StateController {

	private Map<Integer, State> stateMap;
	private State currentState;
	private String inetAddress;
	private String playerName;

	public StateController(String ip, String playerName) {

		this.inetAddress = ip;
		this.playerName = playerName;
		stateMap = new HashMap<>();

	}

	public void setState(StateEnum stateEnum) {

		currentState = stateMap.get(stateEnum.ordinal());
		if (currentState == null) {
			switch (stateEnum) {
			case PLAY_STATE:
				currentState = new PlayState(this, playerName);
				break;
			case GAME_OVER_STATE:
				currentState = new GameOverState(this);
				break;
			case MENU_STATE:
				currentState = new MenuState(this);
				break;

			default:
				currentState = new MenuState(this);
				break;
			}
			stateMap.put(stateEnum.ordinal(), currentState);
		}
		Gdx.input.setInputProcessor(currentState.ip);

	}

	public void render() {

		currentState.render();
	}

	public void update(float deltaTime) {
		currentState.update(deltaTime);
	}

	public void dispose() {
		stateMap.forEach((k, v) -> v.dispose());
	}

	public Map<Integer, State> getStateMap() {
		return stateMap;
	}

	public String getInetAddress() {
		return inetAddress;
	}

}
