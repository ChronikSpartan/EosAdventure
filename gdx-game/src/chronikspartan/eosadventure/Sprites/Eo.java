package chronikspartan.eosadventure.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import chronikspartan.eosadventure.EosAdventure;
import chronikspartan.eosadventure.Screens.PlayScreen;

/**
 * Created by Chronik Spartan on 28/03/2017.
 */

public class Eo extends Sprite {
    public enum State {FALLING, FLYING, FLYING_UP, FLYING_DOWN};
    public State currentState, previousState;
    public World world;
    public Body b2Body;

    private TextureRegion eoStill;
    private Animation eoFly, eoFlyUp, eoFlyDown, eoFalling;
    private float stateTimer;
    private int fallCounter = 0;

    public Eo(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion("Eo_Sprite_Pack"));
        this.world = world;
        currentState = State.FLYING;
        previousState = State.FLYING;
        stateTimer = 0;

        eoFly = createAnimation(28, 31);
        eoFlyUp = createAnimation(32, 35);
        eoFlyDown = createAnimation(24, 27);
        eoFalling = createAnimation(20, 23);


        eoStill = new TextureRegion(getTexture(), 896, 0, 32, 32);

        defineEo();
        setBounds(0, 0, 32, 32);
        setRegion(eoStill);
    }

    public Animation createAnimation(int firstFrame, int lastFrame){
        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.clear();
        for(int i = firstFrame; i <= lastFrame; i++)
            frames.add(new TextureRegion(getTexture(), i * 32, 0, 32, 32));

        return new Animation(0.1f, frames);
    }

    public void update(float dt){
        setPosition(b2Body.getPosition().x - getWidth() /2, b2Body.getPosition().y - getHeight() /2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case FLYING_UP:
                region = (TextureRegion) eoFlyUp.getKeyFrame(stateTimer, true);
                fallCounter = 0;
                break;
            case FLYING_DOWN:
                region = (TextureRegion) eoFlyDown.getKeyFrame(stateTimer, true);
                fallCounter++;
                break;
            case FALLING:
                region = (TextureRegion) eoFalling.getKeyFrame(stateTimer, true);
                fallCounter++;
                break;
            case FLYING:
            default:
                region = (TextureRegion) eoFly.getKeyFrame(stateTimer, true);
                fallCounter = 0;
                break;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

    public State getState(){
        if(b2Body.getLinearVelocity().y > 0)
            return State.FLYING_UP;
        else if(b2Body.getLinearVelocity().y < 0 && fallCounter > 200)
            return State.FALLING;
        else if(b2Body.getLinearVelocity().y < 0)
            return State.FLYING_DOWN;
        else
            return State.FLYING;
    }

    public void defineEo(){
        BodyDef bDef = new BodyDef();
        bDef.position.set(32, /*(EosAdventure.GAME_HEIGHT/2) + (EosAdventure.VIEW_HEIGHT/3)*/ + 32);
        bDef.type = BodyDef.BodyType.DynamicBody;
        b2Body = world.createBody(bDef);

        FixtureDef fDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        fDef.shape = shape;
        b2Body.createFixture(fDef);
    }
}
