package com.mygdx.game.Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayState extends State  {

    private Texture sans;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        sans = new Texture("pan.png");
        Gdx.gl.glClearColor(0,1,0,1);
    }

    @Override
    public void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {

        sb.begin();
        sb.draw(sans,750,100);
        sb.end();

    }
}
