package com.mygdx.game.Sprites;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.Items.Mushroom;
import com.mygdx.game.Items.itemDef;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Screens.PlayScreen;

public class Coin extends InteractiveTileObject {

    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;//finds the tile set of blank coin. Use the ID from tile set and add one to it. andriod.ID =tileset.ID+1

    public Coin (PlayScreen screen, MapObject object){
        super(screen,object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(MyGdxGame.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        System.out.println("Coin");

        if(getCell().getTile().getId() == BLANK_COIN){
            MyGdxGame.manager.get("audio/sounds/bump.wav", Sound.class).play();
            System.out.println("Should not spawn mush Coin");
        }
        else {
            if (object.getProperties().containsKey("mushroom"))
                MyGdxGame.manager.get("audio/sounds/coin.wav", Sound.class).play();
            screen.spawnItem(new itemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / MyGdxGame.PPM), Mushroom.class));


        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);


    }


}
