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
import com.mm.helpers.Assets;
import com.mm.helpers.AudioManager;
import com.mm.helpers.CollisionHandler;
import com.mm.helpers.UIHelper;
import com.mm.objects.AbstractGameObject;
import com.mm.objects.Attacker;
import com.mm.objects.DroppingDragon;
import com.mm.objects.Dragon;
import com.mm.objects.DragonSlot;
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
    
    public int score = 0;
    public int gold = 100;

    private float VIEWPORT_WIDTH = 5.0f;
    private float VIEWPORT_HEIGHT = 9.0f;

    public float xScale = 0;
    public float yScale = 0;

    public Array<AbstractGameObject> m_droppingDragons;
    public Array<Attacker> m_attackers;
    public Array<AbstractGameObject> m_objectsToRemove;

    private Skin uiSkin = new Skin(Gdx.files.internal("uiskin_copy.json"));
    private Stage m_stage;

    private OrthographicCamera m_gameCam;
    private OrthographicCamera m_uiCam;

    private Texture m_background;

    private Label label;
    private Label scoreLabel;
    private Label goldLabel;

    private ImageButton m_quitButton;
    

    private DragonSlot[] slots = new DragonSlot[5];

    SpriteBatch batcher;

    private GameScreenInputAdapter inputProcessor;

    private float m_runTime = 0f;
    
	// Buy Dragon
	private Window winBuyDragon;
	private ImageButton btnBuyGothDragon;
	private ImageButton btnBuyHazyDragon;
	private TextButton btnWinOptCancel;

    private ImageButton btnBuyBookDragon;
    // For Box2D Debugging
    private static final boolean DEBUG_DRAW_BOX2D_WORLD = true;
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
	m_uiCam = new OrthographicCamera(preferredWidth, preferredHeight);
	m_uiCam.setToOrtho(true);
	m_uiCam.update();
	//m_cam.setToOrtho(true, preferredWidth, preferredHeight);
	// Move Camera to 0,0
	//cam.translate(-cam.position.x, -cam.position.y, 0);

	batcher = new SpriteBatch();

	m_droppingDragons = new Array<AbstractGameObject>();
	m_attackers = new Array<Attacker>();
	m_objectsToRemove = new Array<AbstractGameObject>();

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
     * Setup the UI components for the GameScreen.
     */
    private void initUI()
    {
	UIHelper.addRegions(Assets.buttonsAtlas);
	UIHelper.addTexture(Assets.BUY_BTN, Assets.assetManager.get(Assets.BUY_BTN, Texture.class));
	m_stage = new Stage();

	m_quitButton = UIHelper.constructButton(GameScreenInputHandler.QUIT_BUTTON, GameScreenInputHandler.QUIT_BUTTON);
	int x = 100;
	m_quitButton.setPosition(x, preferredHeight - m_quitButton.getHeight());
	m_quitButton.addListener(GameScreenInputHandler.getInstance());
	m_stage.addActor(m_quitButton);

	label = new Label("Messages appear here.", uiSkin);
	label.setAlignment(Align.center);
	label.setPosition(200, 300);
	m_stage.addActor(label);

	scoreLabel = new Label("Score: " + score, uiSkin);
	scoreLabel.setAlignment(Align.left);
	scoreLabel.setPosition(25, preferredHeight - 25);
	m_stage.addActor(scoreLabel);

	goldLabel = new Label("Gold: " + gold, uiSkin);
	goldLabel.setAlignment(Align.left);
	goldLabel.setPosition(25, preferredHeight - 50);
	m_stage.addActor(goldLabel);

	// Buy window for dragons.
	Table layerOptionsWindow = buildBuyDragonsWindowLayer();
	m_stage.addActor(layerOptionsWindow);

	for (int index = 0; index < slots.length; index++)
	{
	    slots[index] = new DragonSlot(new Vector2(index, m_quitButton.getHeight()));
	    slots[index].m_buyButton = UIHelper.constructButton(Assets.BUY_BTN, Assets.BUY_BTN + index);
	    ImageButton buyBtn = slots[index].m_buyButton;
	    buyBtn.setSize(buyBtn.getWidth() * 0.35f, buyBtn.getHeight() * 0.35f);
	    float y = preferredHeight - m_quitButton.getHeight() - buyBtn.getHeight();
	    buyBtn.setPosition(slots[index].m_position.x + 5, y - 5);
	    buyBtn.addListener(GameScreenInputHandler.getInstance());
	    m_stage.addActor(buyBtn);
	    
	    slots[index].m_sellButton = UIHelper.constructButton(Assets.SELL_BTN, Assets.SELL_BTN + index);
	    ImageButton sellBtn = slots[index].m_sellButton;
	    sellBtn.setSize(sellBtn.getWidth() * 0.35f,sellBtn.getHeight() * 0.35f);
	    y = preferredHeight - m_quitButton.getHeight() - sellBtn.getHeight();
	    sellBtn.setPosition(slots[index].m_position.x + 5, y - 5);
	    sellBtn.addListener(GameScreenInputHandler.getInstance());
	    m_stage.addActor(sellBtn);
	    sellBtn.setVisible(false);
	}

	slots[0].setDragon(Assets.assetManager.get(Assets.GOTH_DRAGON, Texture.class));
	slots[0].m_buyButton.setVisible(false);
	slots[0].m_sellButton.setVisible(true);
	
	slots[1].setDragon(Assets.assetManager.get(Assets.BOOK_DRAGON, Texture.class));
	slots[1].m_buyButton.setVisible(false);
	slots[1].m_sellButton.setVisible(true);
	
	slots[2].setDragon(Assets.assetManager.get(Assets.HAZY_DRAGON, Texture.class));
	slots[2].m_buyButton.setVisible(false);
	slots[2].m_sellButton.setVisible(true);
    }

    private Table buildBuyDragonsWindowLayer()
    {
	winBuyDragon = new Window("Buy Dragon", uiSkin);
	winBuyDragon.add(buildBuyDragonsRow()).row();
	winBuyDragon.add(buildBuyDragonsWinButtons()).pad(10, 0, 10, 0);
	// Making the whole window transparent.
	winBuyDragon.setColor(1, 1, 1, 0.8f);
	winBuyDragon.setVisible(false);
	// winBuyDragon.debug();
	winBuyDragon.pack();
	// Not doing anything
	winBuyDragon.setPosition(preferredWidth - winBuyDragon.getWidth() - 50, 50);
	winBuyDragon.setMovable(false);
	return winBuyDragon;
    }

    private Table buildBuyDragonsRow()
    {
	Table tbl = new Table();
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	tbl.add(new Label("Goth Dragon", uiSkin, "default-font", Color.ORANGE));
	tbl.add(new Label("Hazy Dragon", uiSkin, "default-font", Color.ORANGE));
	tbl.add(new Label("Book Dragon", uiSkin, "default-font", Color.ORANGE));
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
	
	tbl.row();
	tbl.columnDefaults(0).padRight(10);
	tbl.columnDefaults(1).padRight(10);
	tbl.add(new Label("30 gold", uiSkin, "default-font", Color.ORANGE));
	tbl.add(new Label("50 gold", uiSkin, "default-font", Color.ORANGE));
	tbl.add(new Label("100 gold", uiSkin, "default-font", Color.ORANGE));
	return tbl;
    }

    private Table buildBuyDragonsWinButtons()
    {
	Table tbl = new Table();
	tbl.pad(10, 10, 0, 10);
	btnWinOptCancel = new TextButton("Cancel", uiSkin);
	tbl.add(btnWinOptCancel);
	btnWinOptCancel.addListener(GameScreenInputHandler.getInstance());
	return tbl;
    }

    public void onBuyDragonClicked()
    {
	m_paused = true;
	winBuyDragon.setVisible(true);
    }
    
    private Table buildBuyDragonsWindowLayer()
    {
	winBuyDragon = new Window("Options", uiSkin);
	winBuyDragon.add(buildBuyDragonsRow()).row();
	winBuyDragon.add(buildOptWinSkinSelection()).row();
	winBuyDragon.add(buildOptWinDebug()).row();
	winBuyDragon.add(buildOptWinButtons()).pad(10, 0, 10, 0);
	// Making the whole window transparent.
	winBuyDragon.setColor(1, 1, 1, 0.8f);
	winBuyDragon.setVisible(false);
	// winBuyDragon.debug();
	winBuyDragon.pack();
	// Not doing anything
	winBuyDragon.setPosition(preferredWidth - winBuyDragon.getWidth() - 50, 50);
	winBuyDragon.setMovable(false);
	return winBuyDragon;
    }
	
	private Table buildBuyDragonsRow()
	{
		Table tbl = new Table();
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Goth Dragon", uiSkin, "default-font", Color.ORANGE)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		UIHelper.addTexture(Assets.GOTH_DRAGON, Assets.assetManager.get(Assets.GOTH_DRAGON, Texture.class));
		btnBuyGothDragon = UIHelper.constructButton(Assets.GOTH_DRAGON,Assets.GOTH_DRAGON);
		tbl.add(btnBuyGothDragon);
		UIHelper.addTexture(Assets.HAZY_DRAGON, Assets.assetManager.get(Assets.HAZY_DRAGON, Texture.class));
		btnBuyHazyDragon = UIHelper.constructButton(Assets.HAZY_DRAGON,Assets.HAZY_DRAGON);
		tbl.add(btnBuyHazyDragon);
//		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
//		tbl.add(sldSound);
//		tbl.row();
//		chkMusic = new CheckBox("", skinLibgdx);
//		tbl.add(chkMusic);
//		tbl.add(new Label("Music", skinLibgdx));
//		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
//		tbl.add(sldMusic);
//		tbl.row();
		return tbl;
	}
	
	private Table buildOptWinSkinSelection()
	{
		Table tbl = new Table();
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Character Skin", uiSkin, "default-font", Color.ORANGE)).colspan(2);
//		tbl.row();
//		selCharSkin = new SelectBox<CharacterSkin>(skinLibgdx);
//		selCharSkin.setItems(CharacterSkin.values());
//		selCharSkin.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) 
//			{
//				onCharSkinSelected(((SelectBox<CharacterSkin>)actor).getSelectedIndex());
//			}
//		});
//		tbl.add(selCharSkin).width(120).padRight(20);
//		imgCharSkin = new Image(Assets.instance.block.block);
//		tbl.add(imgCharSkin).width(80).height(50);
		return tbl;
	}
	
	private Table buildOptWinDebug()
	{
		Table tbl = new Table();
		tbl.pad(10, 10, 0, 10);
		tbl.add(new Label("Debug", uiSkin, "default-font", Color.RED)).colspan(3);
		tbl.row();
		tbl.columnDefaults(0).padRight(10);
		tbl.columnDefaults(1).padRight(10);
		tbl.add(new Label("Show FPS Counter", uiSkin));
		tbl.row();
		return tbl;
	}
	
	private Table buildOptWinButtons()
	{
		Table tbl = new Table();
		Label lbl = null;
		lbl = new Label("", uiSkin);
//		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
//		lbl.setStyle(new LabelStyle(lbl.getStyle()));
//		lbl.getStyle().background = skinLibgdx.newDrawable("white");
//		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
//		tbl.row();
//		lbl = new Label("", skinLibgdx);
//		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
//		lbl.setStyle(new LabelStyle(lbl.getStyle()));
//		lbl.getStyle().background = skinLibgdx.newDrawable("white");
//		tbl.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
//		tbl.row();
//		btnWinOptSave = new TextButton("Save", skinLibgdx);
//		tbl.add(btnWinOptSave).padRight(30);
//		btnWinOptSave.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				onSaveClicked();
//			}
//		});
//		btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
//		tbl.add(btnWinOptCancel);
//		btnWinOptCancel.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				onCancelClicked();
//			}
//		});
		return tbl;
	}

	
	public void onOptionsClicked()
	{
		winBuyDragon.setVisible(true);
	}


    public DragonSlot getSlot(int x, int y)
    {
	for (DragonSlot s : slots)
	{
	    if (s.getSlotButton() != null)
	    {
		updateMessageLabel("X: " + x + " Y: " + y);
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
     * Causes a new dragon to start falling from the specified location.
     * @param pos
     */
    public void dropDragon(Texture image, Vector2 pos)
    {
	DroppingDragon droppedDragon = new DroppingDragon(image);

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
	fixtureDef.density = 50;
	fixtureDef.restitution = 0.5f;
	fixtureDef.friction = 0.5f;
	body.createFixture(fixtureDef);
	polygonShape.dispose();
	m_droppingDragons.add(droppedDragon);
    }

    private void spawnAttacker()
    {
	int y = (int) MathUtils.random(0, 4);
	Attacker attacker = new Attacker(y);

	float x = y + 0.5f;
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
	fixtureDef.restitution = 0.5f;
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
	// Create 3 slots for dragons to be dragged and dropped

	for (AbstractGameObject obj : m_droppingDragons)
	{
	    obj.render(batcher);
	    updateMessageLabel("Dragon Dropping:" + obj.m_position.x + " : " + obj.m_position.y);
	}
	for (AbstractGameObject obj : m_attackers)
	{
	    obj.render(batcher);
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
		    int index = m_droppingDragons.indexOf(obj, true);
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
	for (AbstractGameObject obj : m_droppingDragons)
	{
	    obj.update(delta);
	}
	for (Attacker obj : m_attackers)
	{
	    float scaledImpulse = obj.myData.m_impulse / delta;
	    //obj.body.linVelLoc
	    obj.body.applyLinearImpulse(new Vector2(0, scaledImpulse), obj.m_position, true);
	    obj.update(delta);
	    if (obj.m_position.y < 2.5f)
	    {
		gold -= 10;
		flagForRemoval(obj);
	    }
	}
	Dragon.getInstance().update(delta);

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

	int x = 0;
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
}
