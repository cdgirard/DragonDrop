package com.mm.screen;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.mm.helpers.Assets;
import com.mm.helpers.CollisionHandler;
import com.mm.objects.AbstractGameObject;
import com.mm.objects.Attacker;
import com.mm.objects.Dragon;
import com.mm.objects.DragonData;
import com.mm.objects.DroppingDragon;
import com.mm.objects.Mountain;

/**
 * Job of this class is to manage all the non-ui objects
 * in the game screen.
 * @author cdgira
 *
 */
public class GameScreenController
{
    public static final float VIEWPORT_WIDTH = 5.0f;
    public static final float VIEWPORT_HEIGHT = 9.0f;
    
    public World world;
    
    RayHandler rayHandler;
    Light light;
    
    public Array<DroppingDragon> m_droppingDragons;
    public Array<Attacker> m_attackers;
    public Array<AbstractGameObject> m_objectsToRemove;

    public int gold = 100;
    
    private Mountain rightMountain;
    private Mountain leftMountain;
    
    private OrthographicCamera m_gameCam;
    
    private SpriteBatch batcher;
    
    // For Box2D Debugging
    private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
    private Box2DDebugRenderer b2DebugRenderer;
    
    public GameScreenController()
    {
	// Setup the Cameras
	m_gameCam = new OrthographicCamera();
	m_gameCam.setToOrtho(true, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
	
	batcher = new SpriteBatch();
	
	m_droppingDragons = new Array<DroppingDragon>();
	m_attackers = new Array<Attacker>();
	m_objectsToRemove = new Array<AbstractGameObject>();
	
	if (world != null)
	    world.dispose();
	world = new World(new Vector2(0, 9.81f), true);
	
	RayHandler.setGammaCorrection(true);
	RayHandler.useDiffuseLight(true);

	rayHandler = new RayHandler(world);
	rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 0.5f);
	rayHandler.setBlurNum(3);

	light = new ConeLight(rayHandler, 1000, Color.WHITE, 250f, 520f, 250f, 90f, 20f);
	
	addMountains();
	
	b2DebugRenderer = new Box2DDebugRenderer();
    }
    
    public void init()
    {
	if (m_objectsToRemove.size > 0)
	{
	    for (AbstractGameObject obj : m_objectsToRemove)
	    {
		if (obj instanceof DroppingDragon)
		{
		    int index = m_droppingDragons.indexOf((DroppingDragon) obj, true);
		    if (index != -1)
		    {
			m_droppingDragons.removeIndex(index);
			world.destroyBody(obj.body);
		    }
		}
		if (obj instanceof Attacker)
		{
		    int index = m_attackers.indexOf((Attacker) obj, true);
		    if (index != -1)
		    {
			m_attackers.removeIndex(index);
			world.destroyBody(obj.body);
		    }
		}
	    }
	    m_objectsToRemove.removeRange(0, m_objectsToRemove.size - 1);
	}
	
	if (m_droppingDragons.size > 0)
	{
	    for (DroppingDragon obj : m_droppingDragons)
	    {
		world.destroyBody(obj.body);
	    }
	    m_droppingDragons.removeRange(0, m_droppingDragons.size - 1);
	}

	if (m_attackers.size > 0)
	{
	    for (Attacker obj : m_attackers)
	    {
		world.destroyBody(obj.body);
	    }
	    m_attackers.removeRange(0, m_attackers.size - 1);
	}
    }
    
    public void flagForRemoval(AbstractGameObject obj)
    {
	m_objectsToRemove.add(obj);
    }
    
    /**
     * Places a mountain on either side of the map to keep things from going
     * off the sides.
     * @param pos
     */
    public void addMountains()
    {
	rightMountain = new Mountain();
	createMountain(rightMountain, new Vector2(5, 6.25f));

	leftMountain = new Mountain();
	createMountain(leftMountain, new Vector2(0, 6.25f));
    }

    /**
     * TODO: Not sure if should become createAttacker?
     */
    private void spawnAttacker()
    {
	int y = (int) MathUtils.random(0, 4);
	
	Attacker attacker = new Attacker(y);

	float x = (float)(Math.random()*4+0.5);//y + 0.5f;
	Vector2 pos = new Vector2(x, 9.0f);
	attacker.m_position.set(pos);

	BodyDef bodyDef = new BodyDef();
	bodyDef.position.set(pos);
	bodyDef.angle = 0; // rotation;
	Body body = world.createBody(bodyDef);
	body.setType(BodyType.DynamicBody);
	body.setFixedRotation(true);
	body.setUserData(attacker);
	attacker.body = body;

	PolygonShape polygonShape = new PolygonShape();
	float halfWidth = attacker.bounds.width / 2.0f;
	float halfHeight = attacker.bounds.height / 2.0f;
	polygonShape.setAsBox(halfWidth, halfHeight);

	FixtureDef fixtureDef = new FixtureDef();
	fixtureDef.shape = polygonShape;
	fixtureDef.density = 50;
	fixtureDef.restitution = 0f;
	fixtureDef.friction = 0.5f;
	body.createFixture(fixtureDef);
	polygonShape.dispose();
	m_attackers.add(attacker);
    }
    
    /**
     * Causes a new dragon to start falling from the specified location.
     * TODO: Rename this to createDroppingDragon
     * @param pos
     */
    public void dropDragon(DragonData data, Vector2 pos)
    {
	DroppingDragon droppedDragon = new DroppingDragon(data);

	BodyDef bodyDef = new BodyDef();
	bodyDef.position.set(pos);
	bodyDef.angle = 0; // rotation;
	Body body = world.createBody(bodyDef);
	body.setType(BodyType.DynamicBody);
	body.setUserData(droppedDragon);
	droppedDragon.body = body;

	PolygonShape polygonShape = new PolygonShape();
	float halfWidth = droppedDragon.bounds.width / 2.0f;
	float halfHeight = droppedDragon.bounds.height / 2.0f;
	polygonShape.setAsBox(halfWidth, halfHeight);

	FixtureDef fixtureDef = new FixtureDef();
	fixtureDef.shape = polygonShape;
	fixtureDef.density = 15;
	fixtureDef.restitution = 0.25f;
	fixtureDef.friction = 0.5f;
	body.createFixture(fixtureDef);
	polygonShape.dispose();
	m_droppingDragons.add(droppedDragon);
    }
    
    /**
     * Creates a mountain that should be on one of the sides of the game board.
     * Keeps the attackers and dragons in the playing area.
     * 
     * @param mt
     * @param pos
     */
    public void createMountain(Mountain mt, Vector2 pos)
    {
	mt.m_position = pos;

	BodyDef bodyDef = new BodyDef();
	bodyDef.position.set(mt.m_position);
	bodyDef.angle = 0; // rotation;
	Body body = world.createBody(bodyDef);
	body.setType(BodyType.StaticBody);
	body.setUserData(mt);
	mt.body = body;

	PolygonShape polygonShape = new PolygonShape();
	float halfWidth = mt.bounds.width / 2.0f;
	float halfHeight = mt.bounds.height / 2.0f;
	polygonShape.setAsBox(halfWidth, halfHeight);

	FixtureDef fixtureDef = new FixtureDef();
	fixtureDef.shape = polygonShape;
	fixtureDef.density = 15;
	fixtureDef.restitution = 0.25f;
	fixtureDef.friction = 0.5f;
	body.createFixture(fixtureDef);
	polygonShape.dispose();
    }
    
    public void render(float delta)
    {
	m_gameCam.update();
	
	batcher.setProjectionMatrix(m_gameCam.combined);
	batcher.begin();
	Dragon.getInstance().render(batcher);
	rightMountain.render(batcher);
	leftMountain.render(batcher);
	// Create 3 slots for dragons to be dragged and dropped

	for (DroppingDragon obj : m_droppingDragons)
	{
	    obj.render(batcher);

	}
	for (Attacker obj : m_attackers)
	{
	    obj.render(batcher);
	}

	batcher.end();

	rayHandler.setCombinedMatrix(m_gameCam);
	rayHandler.update();
	rayHandler.render();
	
	if (DEBUG_DRAW_BOX2D_WORLD)
	{
	    b2DebugRenderer.render(world, m_gameCam.combined);
	}
    }
    
    /**
     * Update the present state of all objects in the game before 
     * rendering everything.
     * @param delta
     */
    public void update(float delta)
    {
	if (m_objectsToRemove.size > 0)
	{
	    for (AbstractGameObject obj : m_objectsToRemove)
	    {
		if (obj instanceof DroppingDragon)
		{
		    int index = m_droppingDragons.indexOf((DroppingDragon) obj, true);
		    if (index != -1)
		    {
			m_droppingDragons.removeIndex(index);
			world.destroyBody(obj.body);
		    }
		}
		if (obj instanceof Attacker)
		{
		    int index = m_attackers.indexOf((Attacker) obj, true);
		    if (index != -1)
		    {
			m_attackers.removeIndex(index);
			world.destroyBody(obj.body);
		    }
		}
	    }
	    m_objectsToRemove.removeRange(0, m_objectsToRemove.size - 1);
	}

	world.step(delta, 8, 3);
	for (DroppingDragon obj : m_droppingDragons)
	{
	    obj.update(delta);
	    if (obj.m_position.y > 10.0f)
	    {
		flagForRemoval(obj);
	    }
	}
	for (Attacker obj : m_attackers)
	{
	    obj.update(delta);
	    if (obj.m_position.y < 2.5f)
	    {
		updateGold(-obj.myData.m_goldSteals);
		flagForRemoval(obj);
	    }
	}
	if (Dragon.getInstance().getActive())
	{
	    Dragon.getInstance().update(delta);
	}

	if (Math.random() > 0.98)
	    spawnAttacker();

    }
    
    public void updateGold(int value)
    {
	gold += value;
    }


}
