package server.tank;

import org.apache.log4j.Logger;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class TankDominationServer extends ApplicationAdapter {

	private float time;
	private ServerWorld serverWorld;

	private Logger logger = Logger.getLogger(TankDominationServer.class);

	public TankDominationServer() {

		time = 0;
		serverWorld = new ServerWorld();

	}

	@Override
	public void create() {

		logger.debug("Server is up");

	}

	@Override
	public void render() {

		float deltaTime = Gdx.graphics.getDeltaTime();
		time += deltaTime;
		if (time >= 1) {
			time = 0;
		}

		serverWorld.update(deltaTime);
	}

	@Override
	public void dispose() {
	}

}
