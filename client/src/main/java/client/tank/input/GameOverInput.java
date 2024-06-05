package client.tank.input;

import com.badlogic.gdx.Input.Keys;

import client.states.GameOverState;
import client.states.State.StateEnum;

import com.badlogic.gdx.InputAdapter;

public class GameOverInput extends InputAdapter {

	private GameOverState gameOver;

	public GameOverInput(GameOverState game) {
		this.gameOver = game;
	}

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Keys.R) {
			gameOver.restart();
			gameOver.getSc().setState(StateEnum.PLAY_STATE);

		}

		return true;
	}

}
