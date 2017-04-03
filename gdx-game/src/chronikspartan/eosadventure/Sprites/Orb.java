package chronikspartan.eosadventure.Sprites;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by Chronik Spartan on 28/03/2017.
 */

public class Orb extends InteractiveTileObject {
    public Orb(World world, TiledMap map, Rectangle bounds){
        super(world, map, bounds);
    }
}
