package sk.tuke.gamedev.iddqd.tukequest.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by Steve on 02.04.2017.
 */
public final class InputHelper {

    public static boolean isLeft() {
        return Gdx.input.isKeyPressed(Input.Keys.A)
            || Gdx.input.isKeyPressed(Input.Keys.LEFT)
            || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_4);
    }

    public static boolean isRight() {
        return Gdx.input.isKeyPressed(Input.Keys.D)
            || Gdx.input.isKeyPressed(Input.Keys.RIGHT)
            || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_6);
    }

    public static boolean isUp() {
        return Gdx.input.isKeyPressed(Input.Keys.W)
            || Gdx.input.isKeyPressed(Input.Keys.UP)
            || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_8);
    }

    public static boolean isDown() {
        return Gdx.input.isKeyPressed(Input.Keys.S)
            || Gdx.input.isKeyPressed(Input.Keys.DOWN)
            || Gdx.input.isKeyPressed(Input.Keys.NUMPAD_2);
    }

    public static boolean isJump() {
        return Gdx.input.isKeyPressed(Input.Keys.SPACE);
    }

}
