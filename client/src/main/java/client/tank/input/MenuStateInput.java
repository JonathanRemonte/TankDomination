package client.tank.input;

import com.badlogic.gdx.Input.Keys;

import client.states.MenuState;
import client.states.State.StateEnum;

import com.badlogic.gdx.InputAdapter;

public class MenuStateInput extends InputAdapter {

	private MenuState menuState;

	public MenuStateInput(MenuState game) {
		this.menuState = game;
	}

	@Override
	public boolean keyDown(int keycode) {

		switch (keycode) {
		case Keys.SPACE:
			menuState.getSc().setState(StateEnum.PLAY_STATE);
			break;
		case Keys.Q:
			menuState.quit();
			break;

		default:
			break;
		}

		return true;
	}

}
