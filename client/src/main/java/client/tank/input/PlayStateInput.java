package client.tank.input;

import com.badlogic.gdx.Input.Keys;

import client.states.PlayState;
import client.states.State.StateEnum;

import com.badlogic.gdx.InputAdapter;

public class PlayStateInput extends InputAdapter {

	private PlayState playState;

	public PlayStateInput() {
	}

	public PlayStateInput(PlayState playState) {
		this.playState = playState;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {

		playState.scrolled(amountY);

		return super.scrolled(amountX, amountY);
	}

	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
		case Keys.SPACE:
			playState.shoot();
			break;
		case Keys.M:
			playState.getSc().setState(StateEnum.MENU_STATE);
			break;
		case Keys.ENTER:
			playState.toggleChat();
			break;
		default:
			break;
		}

		return true;
	}

}
