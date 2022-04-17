package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.InteractiveTileObject;






public class WorldContactListener implements ContactListener {


    //gets called when they start the collision
    @Override
    public void beginContact(Contact contact) {

        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();

        if (a.getUserData()== "head"||b.getUserData()=="head"){
            Fixture head;
            if(a.getUserData()=="head")//I am trying to figure out which collided object is the head
                head=a;
            else
                head=b;

            Fixture obj;

                if (a.getUserData() == "head")//the other object is not the head. Then implement the method for collision
                    obj = b;
                else
                    obj = a;
                if((obj.getUserData()!= null) && (InteractiveTileObject.class.isAssignableFrom(obj.getUserData().getClass() ))) {
                    ((InteractiveTileObject) obj.getUserData()).onHeadHit();// method for collision
                }

        }
    }

    //gets called when they stop colliding
    @Override
    public void endContact(Contact contact) {


    }

    //change characteristics of collision
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    //results of collision
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
