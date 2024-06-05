package client.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import client.tank.input.MenuStateInput;
import client.tank.utils.GameUtils;

public class MenuState extends State {

	private BitmapFont smallFont;

	public MenuState(StateController sc) {
		super(sc);
		ip = new MenuStateInput(this);
		smallFont = GameUtils.generateBitmapFont(32, Color.WHITE);
	}

	@Override
	public void render() {
		float red = 0f;
		float green = 0f;
		float blue = 255f;
		Gdx.gl.glClearColor(red / 255f, green / 255f, blue / 255f, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sb.begin();
		GameUtils.renderCenter("Tank Domination", sb, bitmapFont);
		GameUtils.renderCenter("Press Space to Play", sb, smallFont, 0.5f);
		GameUtils.renderCenter("Q to Quit", sb, smallFont, 0.6f);
		sb.end();

	}

	@Override
	public void update(float deltaTime) {
	}

	public void quit() {
		Gdx.app.exit();
	}

	@Override
	public void dispose() {
	}

	public void restart() {
		PlayState playState = (PlayState) this.sc.getStateMap().get(StateEnum.PLAY_STATE.ordinal());
		playState.restart();

	}

}
