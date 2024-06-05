package server.network.messages;

public class GameWorldMessage {

	private float[] enemies;

	private float[] players;

	private float[] bullets;

	public float[] getEnemies() {
		return enemies;
	}

	public float[] getPlayers() {
		return players;
	}

	public float[] getBullets() {
		return bullets;
	}

	public void setEnemies(float[] enemies) {
		this.enemies = enemies;
	}

	public void setPlayers(float[] players) {
		this.players = players;
	}

	public void setBullets(float[] bullets) {
		this.bullets = bullets;
	}

}
