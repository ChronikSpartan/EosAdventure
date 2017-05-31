package chronikspartan.eosadventure.Screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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

    private Array<TextureRegion> tiles;
    private int tileWidth = 32;
    private int tileHeight = 32;
    private int packHeight = 10;
    private int packWidth = 10;
    private TextureRegion region1, region2, region3, region4;

    public PlayScreen(EosAdventure game){
        atlas = new TextureAtlas("sprites/Eo_Baddies_Orbs.pack");
        TextureRegion region = new TextureRegion(new Texture("maps/TilePack.png"));

        tiles = new Array<TextureRegion>();
        for (int h = 0; h < packHeight; h++)
            for(int w = 0; w < packWidth; w++)
                tiles.add(new TextureRegion(region, w * tileWidth, h * tileHeight, tileWidth, tileHeight));

        region1 = tiles.get(61);
        region2 = tiles.get(62);
        region3 = tiles.get(61);
        region4 = tiles.get(62);

        region1.setRegionX(0);
        region1.setRegionY(0);
        region2.setRegionX(32);
        region2.setRegionY(0);
        region3.setRegionX(64);
        region3.setRegionY(0);
        region4.setRegionX(96);
        region4.setRegionY(0);

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
    }

    public void update(float dt){
        handleInput(dt);
        player.b2Body.applyLinearImpulse(new Vector2(1f, 0), player.b2Body.getWorldCenter(), true);

        world.step(1/10f, 6, 2);

        player.update(dt);

        gameCam.position.x = player.b2Body.getPosition().x;
        gameCam.position.y = player.b2Body.getPosition().y;

        if((gameCam.position.x - EosAdventure.VIEW_WIDTH/2) < region1.getRegionX()) {
            region1 = tiles.get(33);
            region1.setRegionX(region4.getRegionX() + 32);
        }

        if((gameCam.position.x - EosAdventure.VIEW_WIDTH/2) < region2.getRegionX()) {
            region2 = tiles.get(36);
            region2.setRegionX(region1.getRegionX() + 32);
        }

        if((gameCam.position.x - EosAdventure.VIEW_WIDTH/2) < region3.getRegionX()) {
            region3 = tiles.get(33);
            region3.setRegionX(region2.getRegionX() + 32);
        }

        if((gameCam.position.x - EosAdventure.VIEW_WIDTH/2) < region4.getRegionX()) {
            region4 = tiles.get(36);
            region4.setRegionX(region3.getRegionX() + 32);
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
        game.batch.draw(region1, region1.getRegionX(), region1.getRegionY());
        game.batch.draw(region2, region2.getRegionX(), region2.getRegionY());
        game.batch.draw(region3, region3.getRegionX(), region3.getRegionY());
        game.batch.draw(region4, region4.getRegionX(), region4.getRegionY());
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
