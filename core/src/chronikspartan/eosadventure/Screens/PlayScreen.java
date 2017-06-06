package chronikspartan.eosadventure.Screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.concurrent.ThreadLocalRandom;

import chronikspartan.eosadventure.EosAdventure;
import chronikspartan.eosadventure.Scenes.Hud;
import chronikspartan.eosadventure.Sprites.Eo;
import chronikspartan.eosadventure.Tools.B2WorldCreator;

/**
 * Created by Chronik Spartan on 23/03/2017.
 */

public class PlayScreen implements Screen {
    private static Integer TILE_SIZE = 32;
    private static Integer NUMBER_OF_TILES = 500;

    private EosAdventure game;
    private TextureAtlas atlas;

    // basic playscreen variables
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Eo player;

    private Array<TextureRegion> tileSet;
	private Array<Vector2> tilePos;
	private int[][] tiles;
    private int tileWidth = 32;
    private int tileHeight = 32;
    private int packHeight = 10;
    private int packWidth = 10;
	private int screenTileWidth = 20;
	private int screenTileHeight = 15;
    private TextureRegion region1, region2, region3, region4;
	private Vector2 region1Pos, region2Pos, region3Pos, region4Pos;

    public PlayScreen(EosAdventure game){
        atlas = new TextureAtlas("sprites/Eo_Baddies_Orbs.pack");
        TextureRegion region = new TextureRegion(new Texture("maps/TilePack.png"));

        tileSet = new Array<TextureRegion>();
        for (int h = 0; h < packHeight; h++)
            for(int w = 0; w < packWidth; w++)
                tileSet.add(new TextureRegion(region, w * tileWidth, h * tileHeight, tileWidth, tileHeight));
		
		tiles = new int[screenTileWidth][screenTileHeight];
		tilePos = new Array<Vector2>();
		tiles[0][0] = 60;
		tilePos.add(new Vector2(0,0));
		for (int i = 1; i < screenTileWidth; i++)
		{
			// nextInt is normally exclusive of the top value,
			// so add 1 to make it inclusive
			int randomNum = ThreadLocalRandom.current().nextInt(1, 3);
		
			if(randomNum == 1)
				tiles[i][0] = 61;
			else
				tiles[i][0] = 62;
				
			tilePos.add(new Vector2(i * 32, 0));
		}

        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(EosAdventure.VIEW_WIDTH, EosAdventure.VIEW_HEIGHT, gameCam);
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("maps/EosAdventure.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        // Set cam position offset for width and at center of map for height
        gameCam.position.set(EosAdventure.VIEW_WIDTH/2,
                (EosAdventure.GAME_HEIGHT/2) + (EosAdventure.VIEW_HEIGHT/3), 0);

        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        // create Eo in aour game world
        player = new Eo(world, this);
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt){
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            player.b2Body.applyLinearImpulse(new Vector2(0, 2f), player.b2Body.getWorldCenter(), true);
        }
		if(Gdx.input.isTouched()){
            player.b2Body.applyLinearImpulse(new Vector2(0, 2f), player.b2Body.getWorldCenter(), true);
        }
    }

    public void update(float dt){
        handleInput(dt);
        player.b2Body.applyLinearImpulse(new Vector2(1f, 0), player.b2Body.getWorldCenter(), true);

        world.step(1/10f, 6, 2);

        player.update(dt);

        gameCam.position.x = player.b2Body.getPosition().x;
        gameCam.position.y = player.b2Body.getPosition().y;
		
		for (int y = 0; y < screenTileHeight; y++)
		{
			for (int x = 0; x < screenTileWidth; x++)
			{
				if((gameCam.position.x - EosAdventure.VIEW_WIDTH/2 - tileWidth) > tilePos.get(x).x ) {
					int endTile;
					if (x == 0)
						endTile = screenTileWidth - 1;
					else
						endTile = x - 1;

					if((x > 0) && (y > 0) && (tiles[x - 1][y - 1] == 63))
						tiles[x][y] = 54;
					else
					if((tiles[endTile][y] == 60) || (tiles[endTile][y] == 61) || (tiles[endTile][y] == 62))
					{
						int nextTile = ThreadLocalRandom.current().nextInt(1, 4);

						switch(nextTile){
							case 1:
								tiles[x][y] = 61;
								break;
							case 2:
								tiles[x][y] = 62;
								break;
							case 3:
								tiles[x][y] = 63;
								break;
							default:
								tiles[x][y] = 61;
						}
					}
					else
					if((tiles[endTile][y] == 66) || (tiles[endTile][y] == 67) || (tiles[endTile][y] == 68))
					{
						int nextTile = ThreadLocalRandom.current().nextInt(1, 5);

						switch(nextTile){
							case 1:
								tiles[x][y] = 67;
								break;
							case 2:
								tiles[x][y] = 68;
								break;
							case 3:
								tiles[x][y] = 69;
								break;
							case 4:
								tiles[x][y] = 63;
								break;
							default:
								tiles[x][y] = 63;
						}
					}
					else
					if((tiles[endTile][y] == 63) || (tiles[endTile][y] == 64) || (tiles[endTile][y] == 65))
					{
						int nextTile = ThreadLocalRandom.current().nextInt(1, 4);

						switch(nextTile){
							case 1:
								tiles[x][y] = 64;
								break;
							case 2:
								tiles[x][y] = 65;
								break;
							case 3:
								tiles[x][y] = 66;
								break;
							default:
								tiles[x][y] = 64;
						}
					}
					else
					if((tiles[endTile][y] == 69) || (tiles[endTile][y] == 0))
					{
						int nextTile = ThreadLocalRandom.current().nextInt(1, 3);

						switch(nextTile){
							case 1:
								tiles[x][y] = 0;
								break;
							case 2:
								tiles[x][y] = 60;
								break;
							default:
								tiles[x][y] = 0;
						}
					}

					tilePos.get(x).x = tilePos.get(endTile).x + 32;
				}
			}
		}

        gameCam.update();
        renderer.setView(gameCam);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

       // renderer.render();

        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
		for(int y = 0; y < screenTileHeight  ; y++)
			for(int x = 0; x < screenTileWidth; x++)
        	game.batch.draw(tileSet.get(tiles[x][y]), tilePos.get(x).x, tilePos.get(x).y);
        player.draw(game.batch);
        game.batch.end();

        // set batch to draw HUD
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
