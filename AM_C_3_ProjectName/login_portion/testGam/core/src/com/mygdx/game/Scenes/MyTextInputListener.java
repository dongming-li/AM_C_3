package com.mygdx.game.Scenes;


import com.badlogic.gdx.Input;
import com.mygdx.game.Screens.GameStateManager;
import com.mygdx.game.Screens.PlayState;

public class MyTextInputListener implements Input.TextInputListener {
    public String test;
    public GameStateManager gsm;



    @Override
    public void input(String text) {
    test=text;
        System.out.println("test Listener"+text);
        if (gsm!=null)
            gsm.set(new PlayState(gsm));
    }

    @Override
    public void canceled() {
        System.out.println("test cancel");
    }

    public void getGsm(GameStateManager gsm){
        this.gsm=gsm;
    }

    public void canceled(GameStateManager gsm) {
        gsm.set(new PlayState(gsm));
        System.out.println("test cancel gsm");

    }
}
