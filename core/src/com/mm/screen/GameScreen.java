package com.mm.screen;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mm.DragonDrop;
import com.mm.helpers.Assets;
import com.mm.helpers.AudioManager;
import com.mm.helpers.CollisionHandler;
import com.mm.helpers.Globals;
import com.mm.helpers.UIHelper;
import com.mm.objects.AbstractGameObject;
import com.mm.objects.Attacker;
import com.mm.objects.DroppingDragon;
import com.mm.objects.Dragon;
import com.mm.objects.DragonData;
import com.mm.objects.DragonSlot;
import com.mm.objects.GoldCoin;
import com.mm.objects.Mountain;
import com.mm.screen.input.GameScreenInputAdapter;
import com.mm.screen.input.GameScreenInputHandler;

/**
 * @author cdgira
 *
 */
public class GameScreen extends SizableScreen
{
    RayHandler rayHandler;
    Light light;
    World world;

    public boolean m_paused = false;
    public boolean m_endGame = false;

    public int score = 0;
    private int gold = 100;

    private float VIEWPORT_WIDTH = 5.0f;
    private float VIEWPORT_HEIGHT = 9.0f;

    public float xScale = 0;
    public float yScale = 0;

    public Array<DroppingDragon> m_droppingDragons;
    public Array<Attacker> m_attackers;
    public Array<AbstractGameObject> m_objectsToRemove;
    public Array<GoldCoin> m_goldCoins;

    private Skin uiBuyWinSkin = new Skin(Gdx.files.internal("ui/clean-crispy-ui.json"));
    private Skin uiMainWinSkin = new Skin(Gdx.files.internal("uiskin_copy.json"));
    private Stage m_stage;

    private OrthographicCamera m_gameCam;
    private OrthographicCamera m_uiCam;

    private Texture m_background;
    private Mountain rightMountain;
    private Mountain leftMountain;

    private Label label;
    private Label scoreLabel;
    private Label goldLabel;

    private ImageButton m_quitButton;

    public DragonSlot[] slots = new DragonSlot[5];
    public int activeSlot = -1; // slot buying or selling a dragon for
    public float m_dropThreshold; // Point at where you can drop a dragon

    SpriteBatch batcher;

    private GameScreenInputAdapter inputProcessor;

    private float m_runTime = 0f;

    // Buy Dragon
    public Window winBuyDragon;
    public ImageButton btnBuyGothDragon;
    public ImageButton btnBuyHazyDragon;
    public ImageButton btnBuyBookDragon;
    public ImageButton btnBuyButlerDragon;
    public ImageButton btnBuyBlueDragon;
    public ImageButton btnBuyDraqDragon;
    public ImageButton btnBuyFairyDragon;
    public ImageButton btnBuyMooseDragon;
    public ImageButton btnBuyOrangeDragon;
    public TextButton btnWinOptCancel;

    // For Box2D Debugging
    private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
    private Box2DDebugRenderer b2DebugRenderer;

    public GameScreen()
    {
	// Get Initial Window Setup  
	m_background = Assets.assetManager.get(Assets.GAME_SCREEN, Texture.class);
	UIHelper.addTexture(Assets.BUY_BTN, Assets.assetManager.get(Assets.BUY_BTN, Texture.class));
	UIHelper.addTexture(Assets.SELL_BTN, Assets.assetManager.get(Assets.SELL_BTN, Texture.class));
	preferredWidth = m_background.getWidth();
	preferredHeight = m_background.getHeight();

	xScale = preferredWidth / VIEWPORT_WIDTH;
	yScale = preferredHeight / VIEWPORT_HEIGHT;

	Gdx.graphics.setWindowedMode(preferredWidth, preferredHeight);

	// Setup the Cameras
	m_gameCam = new OrthographicCamera();
	m_gameCam.setToOrtho(true, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
	//m_gameCam.translate(-VIEWPORT_WIDTH/2, -VIEWPORT_HEIGHT/2, 0);
	m_uiCam = new OrthographicCamera();
	m_uiCam.setToOrtho(true, preferredWidth, preferredHeight);
	//m_uiCam.setToOrtho(false);
	m_uiCam.update();
	//m_cam.setToOrtho(true, preferredWidth, preferredHeight);
	// Move Camera to 0,0
	//cam.translate(-cam.position.x, -cam.position.y, 0);

	batcher = new SpriteBatch();

	m_droppingDragons = new Array<DroppingDragon>();
	m_attackers = new Array<Attacker>();
	m_objectsToRemove = new Array<AbstractGameObject>();
	m_goldCoins = new Array<GoldCoin>();

	GameScreenInputHandler.initializeInstance(this);

	initUI();

	if (world != null)
	    world.dispose();
	world = new World(new Vector2(0, 9.81f), true);
	world.setContactListener(new CollisionHandler(this));

	RayHandler.setGammaCorrection(true);
	RayHandler.useDiffuseLight(true);

	rayHandler = new RayHandler(world);
	rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 0.5f);
	rayHandler.setBlurNum(3);

	light = new ConeLight(rayHandler, 1000, Color.WHITE, 250f, 520f, 250f, 90f, 20f);

	b2DebugRenderer = new Box2DDebugRenderer();

    }

    /**
     * Initializes all the key data values for the start of a new game.
     */
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

	slots[0].setDragon(0);

	slots[1].setDragon(1);

	slots[2].setDragon(2);

	slots[3].setDragon(-1);

	slots[4].setDragon(-1);

	addMountains();

	score = 0;
	gold = 100;
	m_runTime = 0f;
	m_endGame = false;
	
	for (int i = 0; i < gold; i = i + 10)
	{
	    GoldCoin coin = createGoldCoin();
	    m_goldCoins.add(coin);
	}
    }

    /**
     * Creates a single gold coin in the horde area.
     * @return
     */
    private GoldCoin createGoldCoin()
    {
	int x = (int) MathUtils.random(10, preferredWidth - 10);
	int y = (int) MathUtils.random(200, 300);
	Vector2 pos = new Vector2(x, y);
	return new GoldCoin(pos);
    }

    /**
     * Setup the UI components for the GameScreen.
     */
    private void initUI()
    {
	// Create Gold Coins
	for (int i = 0; i < gold; i = i + 10)
	{
	    GoldCoin coin = createGoldCoin();
	    m_goldCoins.add(coin);
	}

	UIHelper.addRegions(Assets.buttonsAtlas);
	UIHelper.addTexture(Assets.BUY_BTN, Assets.assetManager.get(Assets.BUY_BTN, Texture.class));
	m_stage = new Stage();

	m_quitButton = UIHelper.constructButton(GameScreenInputHandler.QUIT_BUTTON, GameScreenInputHandler.QUIT_BUTTON);
	m_quitButton.setPosition(100, preferredHeight - m_quitButton.getHeight());
	m_quitButton.addListener(GameScreenInputHandler.getInstance());
	m_stage.addActor(m_quitButton);

	label = new Label("Messages appear here.", uiMainWinSkin);
	label.setAlignment(Align.left);
	label.setPosition(300, preferredHeight - 50);
	m_stage.addActor(label);

	scoreLabel = new Label("Score: " + score, uiMainWinSkin);
	scoreLabel.setAlignment(Align.left);
	scoreLabel.setPosition(25, preferredHeight - 25);
	m_stage.addActor(scoreLabel);

	goldLabel = new Label("Gold: " + gold, uiMainWinSkin);
	goldLabel.setAlignment(Align.left);
	goldLabel.setPosition(25, preferredHeight - 50);
	m_stage.addActor(goldLabel);

	// Buy window for dragons.
	Table layerOptionsWindow = buildBuyDragonsWindowLayer();
	m_stage.addActor(layerOptionsWindow);

	for (int index = 0; index < slots.length; index++)
	{
	    float xLoc = index * Assets.assetManager.get(Assets.DRAGON_SLOT, Texture.class).getWidth();
	    float yLoc = m_quitButton.getHeight();
	    slots[index] = new DragonSlot(new Vector2(xLoc, yLoc), new Vector2(xLoc, preferredHeight - yLoc));
	    ImageButton buyBtn = slots[index].m_buyButton;
	    buyBtn.addListener(GameScreenInputHandler.getInstance());
	    m_stage.addActor(buyBtn);

	    ImageButton sellBtn = slots[index].m_sellButton;
	    sellBtn.addListener(GameScreenInputHandler.getInstance());
	    m_stage.addActor(sellBtn);
	}
	slots[0].setDragon(0);

	slots[1].setDragon(1);

	slots[2].setDragon(2);

	m_dropThreshold = m_quitButton.getHeight() + slots[0].getSlotImage().getHeight() * 1.5f;
    }

    public void updateDropThreshold(float modifier)
    {
	m_dropThreshold = m_quitButton.getHeight() + slots[0].getSlotImage().getHeight() * (1 + 0.5f * modifier);
    }

    private Table buildBuyDragonsWindowLayer()
    {
	winBuyDragon = new Window("Buy Dragon", uiBuyWinSkin);
	winBuyDragon.add(buildBuyDragonsRow1()).row();
	winBuyDragon.add(buildBuyDragonsRow2()).row();
	winBuyDragon.add(buildBuyDragonsWinButtons()).pad(10, 0, 10, 0);
	// Making the whole window transparent.
	winBuyDragon.setColor(1f, 1f, 1f, 1f);
	winBuyDragon.setVisible(false);
	// winBuyDragon.debug();
	winBuyDragon.pack();
	// Not doing anything
	winBuyDragon.setPosition(preferredWidth - winBuyDragon.getWidth() - 20, 325);
	winBuyDragon.setMovable(false);
	return winBuyDragon;
    }

    private Table buildBuyDragonsRow1()
    {
	Table tbl = new Table();
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	String font = "font";
	tbl.add(new Label("Goth Dragon", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label("Hazy Dragon", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label("Book Dragon", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label("Blue Dragon", uiBuyWinSkin, font, Color.ORANGE));
	tbl.row();
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	UIHelper.addTexture(Assets.GOTH_DRAGON, Assets.assetManager.get(Assets.GOTH_DRAGON, Texture.class));
	btnBuyGothDragon = UIHelper.constructButton(Assets.GOTH_DRAGON, Assets.GOTH_DRAGON);
	btnBuyGothDragon.addListener(GameScreenInputHandler.getInstance());
	tbl.add(btnBuyGothDragon);

	UIHelper.addTexture(Assets.HAZY_DRAGON, Assets.assetManager.get(Assets.HAZY_DRAGON, Texture.class));
	btnBuyHazyDragon = UIHelper.constructButton(Assets.HAZY_DRAGON, Assets.HAZY_DRAGON);
	btnBuyHazyDragon.addListener(GameScreenInputHandler.getInstance());
	tbl.add(btnBuyHazyDragon);

	UIHelper.addTexture(Assets.BOOK_DRAGON, Assets.assetManager.get(Assets.BOOK_DRAGON, Texture.class));
	btnBuyBookDragon = UIHelper.constructButton(Assets.BOOK_DRAGON, Assets.BOOK_DRAGON);
	btnBuyBookDragon.addListener(GameScreenInputHandler.getInstance());
	tbl.add(btnBuyBookDragon);

	UIHelper.addTexture(Assets.BLUE_DRAGON, Assets.assetManager.get(Assets.BLUE_DRAGON, Texture.class));
	btnBuyBlueDragon = UIHelper.constructButton(Assets.BLUE_DRAGON, Assets.BLUE_DRAGON);
	btnBuyBlueDragon.addListener(GameScreenInputHandler.getInstance());
	tbl.add(btnBuyBlueDragon);

	tbl.row();
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	// TODO: Change this to gather data from Dragon Data.
	tbl.add(new Label(Globals.dragonTypes[0].m_goldBuyCost + " gold", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label(Globals.dragonTypes[1].m_goldBuyCost + " gold", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label(Globals.dragonTypes[2].m_goldBuyCost + " gold", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label(Globals.dragonTypes[3].m_goldBuyCost + " gold", uiBuyWinSkin, font, Color.ORANGE));
	return tbl;
    }

    private Table buildBuyDragonsRow2()
    {
	Table tbl = new Table();
	String font = "font";
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	tbl.add(new Label("Moose Dragon", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label("Fairy Dragon", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label("Draq Dragon", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label("Butler Dragon", uiBuyWinSkin, font, Color.ORANGE));
	tbl.row();
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	UIHelper.addTexture(Assets.MOOSE_DRAGON, Assets.assetManager.get(Assets.MOOSE_DRAGON, Texture.class));
	btnBuyMooseDragon = UIHelper.constructButton(Assets.MOOSE_DRAGON, Assets.MOOSE_DRAGON);
	btnBuyMooseDragon.addListener(GameScreenInputHandler.getInstance());
	tbl.add(btnBuyMooseDragon);

	UIHelper.addTexture(Assets.FAIRY_DRAGON, Assets.assetManager.get(Assets.FAIRY_DRAGON, Texture.class));
	btnBuyFairyDragon = UIHelper.constructButton(Assets.FAIRY_DRAGON, Assets.FAIRY_DRAGON);
	btnBuyFairyDragon.addListener(GameScreenInputHandler.getInstance());
	tbl.add(btnBuyFairyDragon);

	UIHelper.addTexture(Assets.DRAQ_DRAGON, Assets.assetManager.get(Assets.DRAQ_DRAGON, Texture.class));
	btnBuyDraqDragon = UIHelper.constructButton(Assets.DRAQ_DRAGON, Assets.DRAQ_DRAGON);
	btnBuyDraqDragon.addListener(GameScreenInputHandler.getInstance());
	tbl.add(btnBuyDraqDragon);

	UIHelper.addTexture(Assets.BUTLER_DRAGON, Assets.assetManager.get(Assets.BUTLER_DRAGON, Texture.class));
	btnBuyButlerDragon = UIHelper.constructButton(Assets.BUTLER_DRAGON, Assets.BUTLER_DRAGON);
	btnBuyButlerDragon.addListener(GameScreenInputHandler.getInstance());
	tbl.add(btnBuyButlerDragon);

	tbl.row();
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	// TODO: Change this to gather data from Dragon Data.
	tbl.add(new Label(Globals.dragonTypes[4].m_goldBuyCost + " gold", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label(Globals.dragonTypes[5].m_goldBuyCost + " gold", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label(Globals.dragonTypes[6].m_goldBuyCost + " gold", uiBuyWinSkin, font, Color.ORANGE));
	tbl.add(new Label(Globals.dragonTypes[7].m_goldBuyCost + " gold", uiBuyWinSkin, font, Color.ORANGE));
	return tbl;
    }

    private Table buildBuyDragonsWinButtons()
    {
	Table tbl = new Table();
	tbl.pad(10, 10, 0, 10);
	btnWinOptCancel = new TextButton("Cancel", uiBuyWinSkin);
	tbl.add(btnWinOptCancel);
	btnWinOptCancel.addListener(GameScreenInputHandler.getInstance());
	return tbl;
    }

    public void onBuyDragonClicked(String button)
    {
	activeSlot = Integer.parseInt(button.split("-")[1]);
	m_paused = true;
	winBuyDragon.setVisible(true);
    }

    public void onSellDragonClicked(String button)
    {
	int slot = Integer.parseInt(button.split("-")[1]);
	updateGold(slots[slot].getDragonData().m_goldBuyCost / 2);
	slots[slot].setDragon(-1);
    }

    public DragonSlot getSlot(int x, int y)
    {
	for (DragonSlot s : slots)
	{
	    if (s.getSlotButton() != null)
	    {
		//updateMessageLabel("X: " + x + " Y: " + y);
		s.getSlotButton().contains(x, y);
	    }

	    if (s.getSlotButton() != null && s.getSlotButton().contains(x, y))
		return s;
	}
	return null;
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
	createMountain(rightMountain, new Vector2(5, 6.5f));

	leftMountain = new Mountain();
	createMountain(leftMountain, new Vector2(0, 6.5f));
    }

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

    /**
     * Causes a new dragon to start falling from the specified location.
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
     * TODO: Potential refactor as near duplicate with MainScreen
     */
    @Override
    public void render(float delta)
    {
	if (m_endGame)
	{
	    endGame();

	}
	if (!m_paused)
	    update(delta);

	Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
	Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	m_gameCam.update();

	// Attach batcher to camera
	batcher.setProjectionMatrix(m_gameCam.combined);
	batcher.begin();
	batcher.draw(m_background, 0, 0, 5.0f, 9.0f, 0, 0, m_background.getWidth(), m_background.getHeight(), false, true);
	batcher.end();

	renderGui();

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
	    //updateMessageLabel("Attacker:" + obj.health);
	}

	batcher.end();

	rayHandler.setCombinedMatrix(m_gameCam);
	rayHandler.update();
	rayHandler.render();

	m_stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	m_stage.draw();
	//   Gdx.app.log("MainScreen", "Stage: "+m_stage.getViewport().getScreenHeight()+" , "+m_stage.getViewport().getScreenWidth());

	if (DEBUG_DRAW_BOX2D_WORLD)
	{
	    b2DebugRenderer.render(world, m_gameCam.combined);
	}
    }

    public void endGame()
    {
	if (m_goldCoins.size > 0)
	    m_goldCoins.removeRange(0, m_goldCoins.size - 1);
	Dragon.getInstance().setActive(false);
	AudioManager.instance.play(Assets.assetManager.get(Assets.INTRO_MUSIC, Music.class));
	DragonDrop.m_dreamScape.setScreen(DragonDrop.MAIN_SCREEN);
	
    }

    /**
     * Update the present state of all objects in the game before 
     * rendering everything.
     * @param delta
     */
    private void update(float delta)
    {
	scoreLabel.setText("Score: " + score);
	goldLabel.setText("Gold: " + gold);
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

	if ((gold <= 0) && (m_droppingDragons.size == 0))
	{
	    m_endGame = true;
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
	    float scaledImpulse = obj.myData.m_impulse / delta;
	    //obj.body.linVelLoc
	    obj.body.applyLinearImpulse(new Vector2(0, scaledImpulse), obj.m_position, true);
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

	m_runTime += delta;
    }

    /**
     * Draw the GUI layer.
     */
    private void renderGui()
    {
	batcher.setProjectionMatrix(m_uiCam.combined);
	batcher.begin();
	
	int i = 0;
	for (GoldCoin coin : m_goldCoins)
	{
	    coin.render(batcher);
	    //this.updateMessageLabel("Draw Coin: "+i);
	    i++;
	}

	for (DragonSlot slot : slots)
	{
	    slot.render(batcher);
	}

	batcher.end();
    }

    @Override
    public void resize(int width, int height)
    {
	Gdx.graphics.setWindowedMode(width, height);

	xScale = preferredWidth / VIEWPORT_WIDTH;
	yScale = preferredHeight / VIEWPORT_HEIGHT;

	m_uiCam.setToOrtho(true, width, height);
	m_stage.getViewport().update(width, height, true);
	Gdx.app.log("GameScreen", "resizing");
    }

    @Override
    public void show()
    {
	// InputProcessor inputProcessorOne = MainScreenInputHandler.getInstance();
	InputProcessor inputProcessorTwo = m_stage;
	InputMultiplexer inputMultiplexer = new InputMultiplexer();
	//  inputMultiplexer.addProcessor(inputProcessorOne);
	inputMultiplexer.addProcessor(inputProcessorTwo);
	inputProcessor = new GameScreenInputAdapter(this);
	inputMultiplexer.addProcessor(inputProcessor);
	Gdx.input.setInputProcessor(inputMultiplexer);
	Gdx.app.log("GameScreen", "show called");
    }

    @Override
    public void hide()
    {
	Gdx.input.setInputProcessor(null);
	Gdx.app.log("GameScreen", "hide called");
    }

    @Override
    public void pause()
    {
	Gdx.app.log("GameScreen", "pause called");
    }

    @Override
    public void resume()
    {
	Gdx.app.log("GameScreen", "resume called");
    }

    @Override
    public void dispose()
    {
	// Leave blank
    }

    /**
     * Updates the message area on the screen with a new message.
     * @param text
     */
    public void updateMessageLabel(String text)
    {
	label.setText(text);

    }

    /**
     * Buys a dragon and places it in the slot purchased for.
     * @param type
     */
    public void boughtDragonForSlot(int type)
    {
	if (gold >= Globals.dragonTypes[type].m_goldBuyCost)
	{
	    slots[activeSlot].setDragon(type);

	    updateGold(-slots[activeSlot].getDragonData().m_goldBuyCost);
	    m_paused = false;
	    winBuyDragon.setVisible(false);
	    activeSlot = -1;
	}
	else
	{
	    //updateMessageLabel("Not enough gold!");
	}

    }

    /**
     * Update the amount of gold to show in the horde.
     * @param value
     */
    public void updateGold(int value)
    {
	gold += value;
	int horde = m_goldCoins.size * 10;
	//this.updateMessageLabel("Update Gold: "+gold+" : "+horde);
	if (horde < gold)
	{
	    for (int i = horde; i < gold; i = i + 10)
	    {
                GoldCoin coin = createGoldCoin();
                m_goldCoins.add(coin);
	    }
	}
	else if (horde > gold)
	{
	    for (int i = horde; i > gold; i = i - 10)
	    {
		if (m_goldCoins.size > 0)
                    m_goldCoins.removeIndex(0);
	    }
	}
    }

    public int getGold()
    {
	return gold;
    }
}
