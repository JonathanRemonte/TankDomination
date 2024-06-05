package client.states;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import client.tank.utils.GameConstants;
import client.tank.utils.GameUtils;

public abstract class State {

	protected OrthographicCamera camera;
	protected InputProcessor ip;
	protected ShapeRenderer sr;
	protected SpriteBatch sb;
	protected BitmapFont bitmapFont;
	protected GlyphLayout glyphLayout;

	protected StateController sc;

	protected State(StateController sc) {

		this.sc = sc;

		camera = new OrthographicCamera(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
		camera.setToOrtho(true);

		sr = new ShapeRenderer();
		sb = new SpriteBatch();

		sr.setProjectionMatrix(camera.combined);
		sb.setProjectionMatrix(camera.combined);

		bitmapFont = GameUtils.generateBitmapFont(70, Color.WHITE);
		glyphLayout = new GlyphLayout();

	}

	public abstract void render();
	public abstract void update(float deltaTime);
	public abstract void dispose();

	public StateController getSc() {
		return sc;
	}

	public enum StateEnum {

		PLAY_STATE, MENU_STATE, GAME_OVER_STATE, PAUSE_STATE

	}

}
