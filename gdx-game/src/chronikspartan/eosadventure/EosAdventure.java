package chronikspartan.eosadventure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import chronikspartan.eosadventure.Screens.PlayScreen;

public class EosAdventure extends Game {
	public static final int GAME_WIDTH = 32000;
	public static final int GAME_HEIGHT = 32000;
	public static final int VIEW_WIDTH = 600;
	public static final int VIEW_HEIGHT = 400;
	public static final float PPM = 100;

	public SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
