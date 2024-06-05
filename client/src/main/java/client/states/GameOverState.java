package client.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import client.tank.input.GameOverInput;
import client.tank.utils.GameUtils;

public class GameOverState extends State {

	private BitmapFont smallFont;

	public GameOverState(StateController sc) {
		super(sc);
		ip = new GameOverInput(this);
		smallFont = GameUtils.generateBitmapFont(32, Color.WHITE);

	}

	@Override
	public void render() {
		float red = 255f;
		float green = 0f;
		float blue = 0f;
		Gdx.gl.glClearColor(red / 255f, green / 255f, blue / 255f, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.begin();
		GameUtils.renderCenter("Game Over", sb, bitmapFont);
		GameUtils.renderCenter("Press R to Restart", sb, smallFont, 0.6f);
		sb.end();
	}

	@Override
	public void update(float deltaTime) {
	}

	@Override
	public void dispose() {
	}

	public void restart() {
		PlayState playState = (PlayState) this.sc.getStateMap().get(StateEnum.PLAY_STATE.ordinal());
		playState.restart();
	}

}
