package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.MyTextInputListener;


public class MenuState extends State {

    private Texture background;

    public MenuState(GameStateManager gsm) {
        super(gsm);
        background = new Texture("login.png");
        Gdx.gl.glClearColor(1,0,0,0);
//        playBtn = new Texture("playbtn.png");
    }

    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()){

            MyTextInputListener listener = new MyTextInputListener();
            Gdx.input.getTextInput(listener, "", "", "");
            listener.getGsm(gsm);
            //System.out.println("test menu");
            //System.out.println(listener.test);
            //listener.canceled(gsm);

            //gsm.set(new PlayState(gsm));

        }
    }



    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        //sb.draw(background, 0, 0, MyGdxGame.V_WIDTH, MyGdxGame.V_HEIGHT);

        sb.draw(background, 750 ,400 ); //where to draw
        sb.end();

    }





}
