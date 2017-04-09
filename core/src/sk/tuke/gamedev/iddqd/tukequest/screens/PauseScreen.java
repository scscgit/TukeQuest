package sk.tuke.gamedev.iddqd.tukequest.screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import sk.tuke.gamedev.iddqd.tukequest.TukeQuestGame;
import sk.tuke.gamedev.iddqd.tukequest.visual.SimpleButton;

/**
 * Created by Steve on 09.04.2017.
 */
public class PauseScreen extends MenuScreen {

    private GameScreen gameScreen;
    private ImageButton resumeButton;

    public PauseScreen(TukeQuestGame game, GameScreen gameScreen) {
        super(game);
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {
        this.resumeButton = new SimpleButton("ResumeBtn.png").getButton();
        this.resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getGame().setScreen(PauseScreen.this.gameScreen);
            }
        });

        super.show();
    }

    @Override
    protected void tableButtons(Table menuTable) {
        menuTable.add(this.resumeButton).expandX().center().padTop(120).row();
        super.tableButtons(menuTable);
    }

    @Override
    protected int startButtonPadTop() {
        return 0;
    }

    @Override
    protected int exitButtonPadTop() {
        return 0;
    }

}
