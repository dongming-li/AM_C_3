package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.Items.Mushroom;
import com.mygdx.game.Items.item;
import com.mygdx.game.Items.itemDef;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Goomba;
import com.mygdx.game.Sprites.Mario;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingDeque;


public class PlayScreen extends State implements Screen {

    private MyGdxGame game;
    private TextureAtlas atlas;

    //Texture img;
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
    //private B2WorldCreator creator;

    private Mario player;
    private Goomba goomba;
    private Music music;
    private Array<item> items;
    //private LinkedBlockingDeque<itemDef> itemsToS;
    private PriorityQueue<itemDef> itemsToS;

    //game states
    private GameStateManager gsm;
    private SpriteBatch batch;



    public PlayScreen(MyGdxGame game){
        super();
        atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        //img = new Texture("badlogic.jpg");
        gamecam = new OrthographicCamera();
        //gamePort = new FitViewport(MyGdxGame.V_WIDTH / MyGdxGame.PPM, MyGdxGame.V_HEIGHT / MyGdxGame.PPM, gamecam);

        gsm = new GameStateManager();

        gamePort = new FitViewport(MyGdxGame.V_WIDTH/ MyGdxGame.PPM, MyGdxGame.V_HEIGHT/ MyGdxGame.PPM, gamecam);

        hud = new Hud(game.batch);

        //Load our map
        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,1/ MyGdxGame.PPM);


        //set came away from 0,0
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0,-5),true);
        b2dr =  new Box2DDebugRenderer();


        //new B2WorldCreator(world, map);

        new B2WorldCreator(this);

        player = new Mario(this); // initialization of  Mario class object





        world.setContactListener(new WorldContactListener());
       // music = MyGdxGame.manager.get("audio/music/mario_music.ogg", Music.class);
        //music.setLooping(true);
        //music.setVolume(0.3f);
        //music.play();

        //place goomba
        goomba = new Goomba(this,.64f, .32f);

        //place items
        items = new Array<item>();
        //itemsToS = new LinkedBlockingDeque<itemDef>();
        itemsToS = new PriorityQueue<itemDef>();

    }

    public void spawnItem(itemDef idef){
        itemsToS.add(idef);
    }
    public void handleSpanwingitems(){
        if(!itemsToS.isEmpty()){
            itemDef idef = itemsToS.poll();
            if(idef.type == Mushroom.class){
                items.add(new Mushroom(this,idef.position.x,idef.position.y));
            }
        }
    }


    public TextureAtlas getAtlas(){
        return atlas;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        gsm.update(Gdx.graphics.getDeltaTime());
//        gsm.render(batch);


        //renders the map
        renderer.render();


        //renders our Box2DDebugLines
        b2dr.render(world,gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        goomba.draw(game.batch);

        for(item item : items){
            item.draw(game.batch);
        }

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


    }

    @Override
    public void resize(int width, int height) {

        gamePort.update(width,height);// helps strech the screen

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
    map.dispose();
    renderer.dispose();
    world.dispose();
    b2dr.dispose();
        hud.dispose();

    }


    public void handleInput(float dt){

        //if touch move right
        if(Gdx.input.isTouched())
            gamecam.position.x += 100* dt;

    if(player.getY()<-.5){
        player.b2body.applyLinearImpulse(new Vector2(0,.185f),player.b2body.getWorldCenter(),true);
        System.out.println("test");

    }

//        //control our player using immediate impulses
//        if(player.currentState != Mario.State.DEAD) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP)&& player.getY()<MyGdxGame.V_HEIGHT/ (1.3*MyGdxGame.PPM))
                player.b2body.applyLinearImpulse(new Vector2(0,.15f),player.b2body.getWorldCenter(),true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);


    }

    public Mario returnPlayer(){return player;}

    @Override
    public void handleInput() {

    }

    public void update(float dt){
        //handle user input first
        handleInput(dt);
        handleSpanwingitems();
        world.step(1/60f,6,2);
        player.update(dt);
        goomba.update(dt);
        hud.update(dt);

        for(item item :items)
            item.update(dt);


        gamecam.position.x = player.b2body.getPosition().x;

        //update our gamecam with correct coordinates after changes
        gamecam.update();
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(gamecam);

    }

    @Override
    public void render(SpriteBatch sb) {

    }


}
