package chronikspartan.eosadventure.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import chronikspartan.eosadventure.EosAdventure;

/**
 * Created by Chronik Spartan on 23/03/2017.
 */

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private Integer energy, score, level;

    Label energyLabel, scoreLabel, levelLabel;

    public Hud(SpriteBatch sb){
        energy = 0;
        score = 0;
        level = 1;

        viewport = new FitViewport(EosAdventure.VIEW_WIDTH, EosAdventure.VIEW_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        energyLabel = new Label(String.format("%03d", energy), new Label.LabelStyle(new BitmapFont(), Color.CYAN));
        scoreLabel = new Label(String.format("%09d", energy), new Label.LabelStyle(new BitmapFont(), Color.CYAN));
        levelLabel = new Label(String.format("%02d", energy), new Label.LabelStyle(new BitmapFont(), Color.CYAN));

        table.add(energyLabel).expandX().padTop(10);
        table.add(levelLabel).expandX().padTop(10);
        table.add(scoreLabel).expandX().padTop(10);

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
