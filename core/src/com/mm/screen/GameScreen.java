package com.mm.screen;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
import com.mm.objects.Dragon;
import com.mm.objects.DragonSlot;
import com.mm.objects.GoldCoin;
import com.mm.screen.input.GameScreenInputAdapter;
import com.mm.screen.input.GameScreenInputHandler;

/**
 * @author cdgira
 *
 */
public class GameScreen extends SizableScreen
{
    public boolean m_paused = false;
    public boolean m_endGame = false;

    public int score = 0;

    private Texture m_background;

    public float xScale = 0;
    public float yScale = 0;

    public GameScreenController controller;
    
    public Array<GoldCoin> m_goldCoins;

    private Skin uiBuyWinSkin = new Skin(Gdx.files.internal("ui/clean-crispy-ui.json"));
    private Skin uiMainWinSkin = new Skin(Gdx.files.internal("uiskin_copy.json"));
    private Stage m_stage;

    private OrthographicCamera m_uiCam;

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

    public GameScreen()
    {
	// Get Initial Window Setup  
	m_background = Assets.assetManager.get(Assets.GAME_SCREEN, Texture.class);
	UIHelper.addTexture(Assets.BUY_BTN, Assets.assetManager.get(Assets.BUY_BTN, Texture.class));
	UIHelper.addTexture(Assets.SELL_BTN, Assets.assetManager.get(Assets.SELL_BTN, Texture.class));
	preferredWidth = m_background.getWidth();
	preferredHeight = m_background.getHeight();

	xScale = preferredWidth / GameScreenController.VIEWPORT_WIDTH;
	yScale = preferredHeight / GameScreenController.VIEWPORT_HEIGHT;

	Gdx.graphics.setWindowedMode(preferredWidth, preferredHeight);

	// Setup the Cameras
	m_uiCam = new OrthographicCamera();
	m_uiCam.setToOrtho(true, preferredWidth, preferredHeight);
	m_uiCam.update();

	batcher = new SpriteBatch();

	controller = new GameScreenController();
	controller.world.setContactListener(new CollisionHandler(this));
	
	m_goldCoins = new Array<GoldCoin>();

	GameScreenInputHandler.initializeInstance(this);
	
	initUI();
    }

    /**
     * Initializes all the key data values for the start of a new game.
     */
    public void init()
    {
	controller.init();
	
	slots[0].setDragon(0);
	slots[1].setDragon(1);
	slots[2].setDragon(2);
	slots[3].setDragon(-1);
	slots[4].setDragon(-1);

	score = 0;
	controller.gold = 100;
	m_runTime = 0f;
	m_endGame = false;
	
	// Create Gold Coins
	// Is here instead of initUI, because it needs to be
	// done every time the window is made active again.
	for (int i = 0; i < controller.gold; i = i + 10)
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
	int x = (int) MathUtils.random(25, preferredWidth - 45);
	int y = (int) MathUtils.random(200, 300);
	Vector2 pos = new Vector2(x, y);
	return new GoldCoin(pos);
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

	goldLabel = new Label("Gold: " + controller.gold, uiMainWinSkin);
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
	m_dropThreshold = m_quitButton.getHeight() + slots[0].getSlotImage().getHeight() * (0.5f + 0.25f * modifier);
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
	{
	    controller.update(delta);
	    updateGui(delta);
	    m_runTime += delta;
	}

	Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
	Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
	renderBackground();
	
	controller.render(delta);

	renderGui();

	m_stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
	m_stage.draw();
	//   Gdx.app.log("MainScreen", "Stage: "+m_stage.getViewport().getScreenHeight()+" , "+m_stage.getViewport().getScreenWidth());


    }

    public void endGame()
    {
	if (m_goldCoins.size > 0)
	    m_goldCoins.removeRange(0, m_goldCoins.size - 1);
	Dragon.getInstance().setActive(false);
	AudioManager.instance.play(Assets.assetManager.get(Assets.INTRO_MUSIC, Music.class));
	DragonDrop.m_dreamScape.setScreen(DragonDrop.MAIN_SCREEN);
	
    }

    private void updateGui(float delta)
    {
	scoreLabel.setText("Score: " + score);
	goldLabel.setText("Gold: " + controller.gold);
	
	if ((controller.gold <= 0) && (controller.m_droppingDragons.size == 0))
	{
	    m_endGame = true;
	}

	int horde = m_goldCoins.size * 10;

	if (horde/10 < controller.gold/10)
	{
	    for (int i = horde; i < controller.gold; i = i + 10)
	    {
                GoldCoin coin = createGoldCoin();
                m_goldCoins.add(coin);
	    }
	}
	else if (horde/10 > controller.gold/10)
	{
	    for (int i = horde; i > controller.gold; i = i - 10)
	    {
		if (m_goldCoins.size > 0)
                    m_goldCoins.removeIndex(m_goldCoins.size-1);
	    }
	}
    }

    private void renderBackground()
    {
	batcher.setProjectionMatrix(m_uiCam.combined);
	batcher.begin();
	
	batcher.draw(m_background, 0, 0, m_background.getWidth(), m_background.getHeight(), 0, 0, m_background.getWidth(), m_background.getHeight(), false, true);
	batcher.end();
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

	xScale = preferredWidth / GameScreenController.VIEWPORT_WIDTH;
	yScale = preferredHeight / GameScreenController.VIEWPORT_HEIGHT;

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
	if (controller.gold >= Globals.dragonTypes[type].m_goldBuyCost)
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
	controller.updateGold(value);

    }
    

    public int getGold()
    {
	return controller.gold;
    }
}
