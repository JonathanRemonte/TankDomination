package client.network.messages;

public class GameWorldMessage {

	private float[] enemies;
	private float[] players;
	private float[] bullets;

	public float[] getEnemies() {
		return enemies;
	}

	public void setEnemies(float[] enemies) {
		this.enemies = enemies;
	}

	public float[] getPlayers() {
		return players;
	}

	public void setPlayers(float[] players) {
		this.players = players;
	}

	public float[] getBullets() {
		return bullets;
	}

	public void setBullets(float[] bullets) {
		this.bullets = bullets;
	}

}
