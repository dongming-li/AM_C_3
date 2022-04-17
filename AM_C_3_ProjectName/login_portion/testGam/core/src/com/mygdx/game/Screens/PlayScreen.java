package com.mygdx.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Enemies.Enemy;
import com.mygdx.game.Sprites.Items.Item;
import com.mygdx.game.Sprites.Items.ItemDef;
import com.mygdx.game.Sprites.Items.Mushroom;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Sprites.TileObjects.Brick;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.PriorityQueue;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by lebea on 10/22/2017.
 */

public class PlayScreen implements Screen{
    //Reference to our Game, used to set Screens
    private MyGdxGame game;
    private TextureAtlas atlas;
    public static boolean alreadyDestroyed = false;

    //basic playscreen variables
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    //sprites
    public static Mario player;

    private Music music;

    private Array<Item> items;
    private PriorityQueue<ItemDef> itemsToSpawn;

    //mutiplay
    private Socket socket;
    String id;
    String playerId;
    HashMap<String, Mario> friendlyPlayers;
    HashMap<String, Brick> friendlyitems;
    Mario coPlayer;
    float timer;
    final float UDT=1/30;
    Texture mario;
    Vector2 position;



    public PlayScreen(MyGdxGame game){
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        this.game = game;
        //create cam used to follow mario through cam world
        gamecam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gamecam);

        //create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        //Load our map and setup our map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1  / MyGdxGame.PPM);

        //initially set our gamcam to be centered correctly at the start of of map
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y, and allow bodies to sleep
        world = new World(new Vector2(0, -10), true);
        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        //create mario in our game world
        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

       // music = MyGdxGame.manager.get("audio/music/mario_music.ogg", Music.class);
//        music.setLooping(true);
        //music.play();

        items = new Array<Item>();
        itemsToSpawn = new PriorityQueue<ItemDef>();
        //Socket shit
        friendlyPlayers = new HashMap<String, Mario>();
        //mario =  new Texture("Mario_and_Enemies.png");
        position = player.b2body.getPosition();
        //float x = player.getX();
        //connectSocket();
        //configSocketEvents();

    }

    public void spawnItem(ItemDef idef){
        itemsToSpawn.add(idef);
    }


    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this, idef.position.x, idef.position.y));
            }
        }
    }


    public void connectSocket(){
        //test.connectSocket();
        try {
//            socket = IO.socket("http://localhost:8080");
                socket = IO.socket("10.26.178.38");

            socket.connect();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public Mario gen(){
        Mario x = new Mario(this);
        return x;
    };


    public void configSocketEvents( ){

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            //reply to server
            @Override
            public void call(Object... args) {
                Gdx.app.log("SocketIO", "Connected");


            }
        }).on("socketID", new Emitter.Listener() { //when socket ID event happens
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];//get data from jason object
                try {
                    //String playerId = data.getString("id");
                    playerId = data.getString("id");
                    Gdx.app.log("SocketIO", "My ID: " + playerId);
                } catch (JSONException e) {
                    Gdx.app.log("SocketIO", "Error getting ID");
                }
            }
        }).on("newPlayer", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String friendId = data.getString("id");
                    Gdx.app.log("SocketIO", "New Player Connect: " + friendId);
                    //Luigi x = new Luigi(mario);
                    Mario x =gen();
                    friendlyPlayers.put(friendId, new Mario(mario)); //Problem
                    friendlyPlayers.put(friendId, x); //Problem
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting New PlayerID");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    if(friendlyPlayers.get(playerId)!=null)
                    {
                        friendlyPlayers.get(playerId).setPosition(x.floatValue()
                                ,y.floatValue());
                    }

                    Double xv = data.getDouble("xv");
                    Double yv = data.getDouble("yv");
                    float xvv=  xv.floatValue();
                    float yvv= yv.floatValue();
                    if(friendlyPlayers.get(playerId)!=null)
                    {player.b2body.applyLinearImpulse(new Vector2(0,.15f),player.b2body.getWorldCenter(),true);
                        friendlyPlayers.get(playerId).b2body.applyLinearImpulse(new Vector2(xvv,yvv),player.b2body.getWorldCenter(),true);
                    }

                }catch(JSONException e){

                }
            }
        }).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    id = data.getString("id");  //id
                    friendlyPlayers.remove(id);
                }catch(JSONException e){
                    Gdx.app.log("SocketIO", "Error getting disconnected PlayerID");
                }
            }
        })

                .on("getPlayers", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONArray objects = (JSONArray) args[0];
                        try {
                            for(int i = 0; i < objects.length(); i++){


                                Mario y = gen();
                                Vector2 position = new Vector2();
                                position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();
                                position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();
                                y.setPosition(position.x, position.y);


                                friendlyPlayers.put(objects.getJSONObject(i).getString("id"), y);
                            }
                        } catch(JSONException e){

                        }
                    }
                });
    }
    public void updateServer(float dt){
        timer+= dt;

        if(timer>= UDT && player!=null && player.hasMoved() ){
            JSONObject data = new JSONObject();

            try{
                data.put("x",player.getX());
                data.put("y",player.getY());


                socket.emit("playerMoved", data);

            }
            catch (JSONException e){
                Gdx.app.log("Socket.io","Error sending update data");
            }

        }


    }


    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {


    }



    public boolean playerDied(){
        if(player.currentState == Mario.State.DEAD && player.getStateTimer() > 3){
            return true;
        }
        return false;
    }

    public void handleInput(float dt){
        //control our player using immediate impulses
        if(player.currentState != Mario.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);

            if (player.getY() < -.5) {
                System.out.printf("mario fell");
                player.die();
            }
        }
    }

    public static Mario getPlayer(){
        return player;
    }

    public void update(float dt){
        //handle user input first
        handleInput(dt);
        handleSpawningItems();

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        player.update(dt);
        for(Enemy enemy : creator.getGoombas()) {
            enemy.update(dt);
            if(enemy.getX() < player.getX() + 224 / MyGdxGame.PPM)
                enemy.b2body.setActive(true);
        }

        for(Item item : items)
            item.update(dt);

        hud.update(dt);

        //attach our gamecam to our players.x coordinate
        if(player.currentState != Mario.State.DEAD) {
            gamecam.position.x = player.b2body.getPosition().x;
        }



        //update our gamecam with correct coordinates after changes
        gamecam.update();
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gamecam);

    }

    @Override
    public void render(float delta) {
        //separate our update logic from render
        update(delta);

        //Clear the game screen with Black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //updateServer(Gdx.graphics.getDeltaTime());
        //render our game map
        renderer.render();

        //renderer our Box2DDebugLines
        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        //for(HashMap.Entry<String, Mario> entry : friendlyPlayers.entrySet()){
          //  entry.getValue().draw(game.batch);
        //}


        for(Enemy enemy : creator.getGoombas())
            enemy.draw(game.batch);
        for(Item item : items)
            item.draw(game.batch);
        game.batch.end();

        //Set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (playerDied()){
            game.setScreen(new DeathScreen(game));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {
        //updated our game viewport
        gamePort.update(width,height);

    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //dispose of all our opened resources
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    public Hud getHud() {
        return hud;
    }
}
