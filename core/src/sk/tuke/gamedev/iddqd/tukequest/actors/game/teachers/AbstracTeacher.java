package sk.tuke.gamedev.iddqd.tukequest.actors.game.teachers;

import com.badlogic.gdx.audio.Music;
import sk.tuke.gamedev.iddqd.tukequest.actors.AbstractAnimatedActor;
import sk.tuke.gamedev.iddqd.tukequest.visual.Animation;

/**
 * Created by Ruza on 5.4.2017.
 */
public abstract class AbstracTeacher extends AbstractAnimatedActor {
    private Music welcomeSound;
    private Music otherSound;
    private Boolean isVisited=false;
    private Boolean playingSound=false;

    protected AbstracTeacher(Animation animation, float x, float y,Music welcomeSound) {
        super(animation, x, y);
        this.welcomeSound=welcomeSound;
        this.otherSound=welcomeSound;
    }
    protected AbstracTeacher(Animation animation, float x, float y,Music welcomeSound, Music otherSound) {
        super(animation, x, y);
        this.welcomeSound=welcomeSound;
        this.otherSound=otherSound;
    }

    protected void playSound(){
        if(isVisited){
            otherSound.play();
        }else{
            welcomeSound.play();
            isVisited=false;
        }
    }

    @Override
    public void act() {
        //todo: zkontrolovat kolize
        if(!playingSound){
            playSound();
            playingSound = true;
        }

    }
}
