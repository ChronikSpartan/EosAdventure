package chronikspartan.eosadventure.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import chronikspartan.eosadventure.EosAdventure;
import chronikspartan.eosadventure.Screens.PlayScreen;

/**
 * Created by Chronik Spartan on 28/03/2017.
 */

public class Eo extends Sprite {
    public World world;
    public Body b2Body;
    private TextureRegion eoStill;

    public Eo(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("Eo_Sprite_Pack"));
        this.world = world;
        defineEo();
        eoStill = new TextureRegion(getTexture(), 896, 0, 32, 32);
        setBounds(0, 0, 32, 32);
        setRegion(eoStill);
    }

    public void update(float dt){
        setPosition(b2Body.getPosition().x - getWidth() /2, b2Body.getPosition().y - getHeight() /2);
    }

    public void defineEo(){
        BodyDef bDef = new BodyDef();
        bDef.position.set(32, (EosAdventure.GAME_HEIGHT/2) + (EosAdventure.VIEW_HEIGHT/3) + 32);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fDef.shape = shape;
        b2Body.createFixture(fDef);
    }
}
