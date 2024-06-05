package client.states;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

import client.network.TankDominationClient;
import client.network.messages.ChatMessage;
import client.network.messages.GameWorldMessage;
import client.network.messages.LoginMessage;
import client.network.messages.LogoutMessage;
import client.network.messages.PlayerDied;
import client.network.messages.PositionMessage;
import client.network.messages.ShootMessage;
import client.network.messages.PositionMessage.DIRECTION;
import client.tank.MessageListener;
import client.tank.input.PlayStateInput;
import client.tank.shapes.AimLine;
import client.tank.shapes.Bullet;
import client.tank.shapes.Enemy;
import client.tank.shapes.Player;
import client.tank.utils.GameConstants;
import client.tank.utils.GameUtils;
import client.tank.utils.MessageParser;

public class PlayState extends State implements MessageListener {

	private List<String> chatMessages = new ArrayList<>();
    private boolean isChatting = false;
    private StringBuilder currentMessage = new StringBuilder();
    private String playerName;

	private Player player;
	private List<Player> players;
	private List<Enemy> enemies;
	private List<Bullet> bullets;
	private AimLine aimLine;

	private TankDominationClient myclient;

	private BitmapFont healthFont;
	private BitmapFont chatFont;

	public PlayState(StateController sc, String playerName) {
		super(sc);
		this.playerName = playerName;
		init();
		ip = new PlayStateInput(this);
		healthFont = GameUtils.generateBitmapFont(20, Color.WHITE);
		chatFont = GameUtils.generateBitmapFont(16, Color.WHITE);
	}

	private void init() {

		myclient = new TankDominationClient(sc.getInetAddress(), this);
		myclient.connect();

		players = new ArrayList<>();
		enemies = new ArrayList<>();
		bullets = new ArrayList<>();

		aimLine = new AimLine(new Vector2(0, 0), new Vector2(0, 0));
		aimLine.setCamera(camera);

		LoginMessage m = new LoginMessage();
		m.setX(new SecureRandom().nextInt(GameConstants.SCREEN_WIDTH));
		m.setY(new SecureRandom().nextInt(GameConstants.SCREEN_HEIGHT));
		m.setName(playerName);
		myclient.sendTCP(m);

	}

	@Override
	public void render() {
		sr.setProjectionMatrix(camera.combined);
		camera.update();
		if (player == null)
			return;

		ScreenUtils.clear(0, 0, 0, 1);

		sr.begin(ShapeType.Line);
		sr.setColor(Color.RED);
		players.forEach(p -> p.render(sr));
		sr.setColor(Color.WHITE);
		enemies.forEach(e -> e.render(sr));
		bullets.forEach(b -> b.render(sr));
		sr.setColor(Color.BLUE);
		player.render(sr);
		sr.setColor(Color.WHITE);
		aimLine.render(sr);
		followPlayer();
		sr.end();

		sb.begin();
		GameUtils.renderCenter("HEALTH: " + player.getHealth(), sb, healthFont, 0.1f);
		sb.end();

		if (isChatting) {
            sb.begin();
            GameUtils.renderCenter(currentMessage.toString(), sb, healthFont, 0.9f);
            sb.end();
        }

        renderChatMessages();

	}

	private void renderChatMessages() {
        sb.begin();
        float y = 20;
        for (String message : chatMessages) {
            GameUtils.renderBottomLeft(message, sb, chatFont, 10, y);
            y += 20;
        }
        sb.end();
    }

	private void followPlayer() {
		float lerp = 0.05f;
		camera.position.x += (player.getPosition().x - camera.position.x) * lerp;
		camera.position.y += (player.getPosition().y - camera.position.y) * lerp;
	}

	@Override
	public void update(float deltaTime) {
		if (player == null)
			return;
		if (isChatting) {
            return;
        }
		aimLine.setBegin(player.getCenter());
		aimLine.update(deltaTime);
		processInputs();

	}

	public void scrolled(float amountY) {
		if (amountY > 0) {
			camera.zoom += 0.2;
		} else {
			if (camera.zoom >= 0.4) {
				camera.zoom -= 0.2;
			}
		}
	}

	public void shoot() {

		ShootMessage m = new ShootMessage();
		m.setId(player.getId());
		m.setAngleDeg(aimLine.getAngle());
		myclient.sendUDP(m);

	}

	private void processInputs() {

		PositionMessage p = new PositionMessage();
		p.setId(player.getId());
		if (Gdx.input.isKeyPressed(Keys.S)) {
			p.setDirection(DIRECTION.DOWN);
			myclient.sendUDP(p);
		}
		if (Gdx.input.isKeyPressed(Keys.W)) {
			p.setDirection(DIRECTION.UP);
			myclient.sendUDP(p);
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			p.setDirection(DIRECTION.LEFT);
			myclient.sendUDP(p);
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			p.setDirection(DIRECTION.RIGHT);
			myclient.sendUDP(p);
		}

	}

	@Override
	public void loginReceieved(LoginMessage m) {
		player = new Player(m.getX(), m.getY(), 50);
		player.setId(m.getId());
	}

	@Override
	public void logoutReceieved(LogoutMessage m) {
	}

	@Override
	public void playerDiedReceived(PlayerDied m) {
		if (player.getId() != m.getId())
			return;

		LogoutMessage mm = new LogoutMessage();
		mm.setId(player.getId());
		myclient.sendTCP(mm);
		myclient.close();
		this.getSc().setState(StateEnum.GAME_OVER_STATE);

	}

	@Override
	public void gwmReceived(GameWorldMessage m) {

		enemies = MessageParser.getEnemiesFromGWM(m);
		bullets = MessageParser.getBulletsFromGWM(m);

		players = MessageParser.getPlayersFromGWM(m);

		if (player == null)
			return;
		players.stream().filter(p -> p.getId() == player.getId()).findFirst().ifPresent(p -> player = p);
		players.removeIf(p -> p.getId() == player.getId());

	}

	@Override
    public void chatMessageReceived(ChatMessage m) {
        chatMessages.add(m.getName() + ": " + m.getMessage());
        if (chatMessages.size() > 10) {
            chatMessages.remove(0);
        }
    }

	public void toggleChat() {
	    isChatting = !isChatting;
	    if (isChatting) {
	        Gdx.input.setInputProcessor(new InputAdapter() {
	            @Override
	            public boolean keyTyped(char character) {
	                if (character == '\r' || character == '\n') {
	                    sendChatMessage();
	                    toggleChat();
	                } else if (character == '\b') {
	                    if (currentMessage.length() > 0) {
	                        currentMessage.setLength(currentMessage.length() - 1);
	                    }
	                } else {
	                    currentMessage.append(character);
	                }
	                return true;
	            }
	        });
	    } else {
	        Gdx.input.setInputProcessor(ip);
	    }
	}


    private void sendChatMessage() {
    	String message = currentMessage.toString().trim();
        if (!message.isEmpty()) {
            ChatMessage chatMessage = new ChatMessage(playerName, message);
            myclient.sendUDP(chatMessage);
        }
        currentMessage.setLength(0);
    }

	public void restart() {
		init();
	}

	@Override
	public void dispose() {

		LogoutMessage m = new LogoutMessage();
		m.setId(player.getId());
		myclient.sendTCP(m);
	}

}
