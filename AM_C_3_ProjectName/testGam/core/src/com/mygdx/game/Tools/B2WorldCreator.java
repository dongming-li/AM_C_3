package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Screens.PlayScreen;
import com.mygdx.game.Sprites.Brick;
import com.mygdx.game.Sprites.Coin;


public class B2WorldCreator {
    public B2WorldCreator(PlayScreen screen){
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;


        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)  ){

            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ MyGdxGame.PPM, (rect.getY() + rect.getHeight()/2)/ MyGdxGame.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2/ MyGdxGame.PPM,rect.getHeight()/2/ MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);



            //world = new World(new Vector2(0, -10), true);  // edited

        }

        //bricks
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)  ){

            Rectangle rect = ((RectangleMapObject)object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2)/ MyGdxGame.PPM, (rect.getY() + rect.getHeight()/2)/ MyGdxGame.PPM);

            body = world.createBody(bdef);
            shape.setAsBox(rect.getWidth()/2/ MyGdxGame.PPM,rect.getHeight()/2/ MyGdxGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);


        }

        //create bricks
        for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)  ){

            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            new Brick(screen, object);


        }



        //coins

        for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)  ){

            Rectangle rect = ((RectangleMapObject)object).getRectangle();

            new Coin(screen,object);
//            bdef.type = BodyDef.BodyType.StaticBody;
//            bdef.position.set((rect.getX() + rect.getWidth()/2)/ MyGdxGame.PPM, (rect.getY() + rect.getHeight()/2)/ MyGdxGame.PPM);
//
//            body = world.createBody(bdef);
//            shape.setAsBox(rect.getWidth()/2/ MyGdxGame.PPM,rect.getHeight()/2/ MyGdxGame.PPM);
//            fdef.shape = shape;
//            body.createFixture(fdef);





        }

    }

}
