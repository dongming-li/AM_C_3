package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;

/**
 * Created by lebea on 10/24/2017.
 */

public class Mario extends Sprite {

    public enum State { FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD };
    public State currentState;
    public State previousState;
    private Animation<TextureRegion> marioRun;
    private Animation<TextureRegion> marioJump;

    //private Animation marioRun;
    //private TextureRegion marioJump;
    private float stateTimer;
    private boolean runningRight;


    public World world;
    public Body b2body;
    private TextureRegion marioStand;//produces mario just standing


    public Mario( PlayScreen screen){
        super(screen.getAtlas().findRegion("little_mario")); // to grab little mario
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion>frames = new Array<TextureRegion>();
        for (int i=1; i<4; i++)
            frames.add(new TextureRegion(getTexture(), i*16,11,16,16 ));
            //creates animation
        marioRun = new Animation<TextureRegion>(0.1f, frames);
            frames.clear();

            //jump animation
            for(int i = 4; i < 6; i++)
                frames.add(new TextureRegion(getTexture(), i * 16, 11, 16, 16));
        marioJump = new Animation<TextureRegion>(0.1f, frames);





        defineMario();
        marioStand = new TextureRegion(getTexture(),1,11,16,16); // produces mario just standing
        setBounds(0,0,16/MyGdxGame.PPM, 16/MyGdxGame.PPM);
        setRegion(marioStand);
    }

    public void update(float dt){
        //give the player body the animation
        setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);

        //displays the sprite
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt){

            //get marios current state. ie. jumping, running, standing...
            currentState = getState();

            TextureRegion region;

            //depending on the state, show animation.
            switch(currentState){
//                case DEAD:
//                    region = marioDead;
//                    break;
//                case GROWING:
//                    region = growMario.getKeyFrame(stateTimer);
//                    if(growMario.isAnimationFinished(stateTimer)) {
//                        runGrowAnimation = false;
//                    }
                    //break;
                case JUMPING:
                    region = marioJump.getKeyFrame(stateTimer);
                    break;
                case RUNNING:
                    region = marioRun.getKeyFrame(stateTimer,true);
                    break;
                case FALLING:
                case STANDING:
                default:
                    region = marioStand;
                    break;
            }

            //if mario is running left flip player to left.
            if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
                region.flip(true, false);
                runningRight = false;
            }

            //if mario is running right, flip it.
            else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
                region.flip(true, false);
                runningRight = true;
            }

            //reset state timer
            if(currentState == previousState){
                stateTimer= stateTimer + dt;
            }
            else
                stateTimer =0;
            //reset state timer
        //stateTimer = currentState == previousState ? stateTimer + dt : 0;
            //update previous state
            previousState = currentState;
            //return our final adjusted frame
            return region;

        }

    public State getState() {

         if((b2body.getLinearVelocity().y > 0  ))
            return State.JUMPING;
            // falling
        else if(b2body.getLinearVelocity().y < 0)
            return State.FALLING;
            // running
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;

        else
            return State.STANDING;


    }


    public void defineMario(){

        //catatory: what is this fixture
        //mask: what can the fixture colide with


        BodyDef bdef = new BodyDef();
        bdef.position.set(32/ MyGdxGame.PPM ,32/ MyGdxGame.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ MyGdxGame.PPM);
        fdef.filter.categoryBits = MyGdxGame.MARIO_BIT;
        //what can mario collide with
        fdef.filter.maskBits = MyGdxGame.GROUND_BIT | MyGdxGame.COIN_BIT | MyGdxGame.BRICK_BIT | MyGdxGame.ENEMY_BIT| MyGdxGame.OBJECT_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();//line between 2 points
        head.set(new Vector2(-2/MyGdxGame.PPM,7/MyGdxGame.PPM),new Vector2(2/MyGdxGame.PPM,7/MyGdxGame.PPM));//above his head is the line
        fdef.shape=head;
        fdef.isSensor=true;//no longer collides with other stuff

        b2body.createFixture(fdef).setUserData("head");

    }








}
