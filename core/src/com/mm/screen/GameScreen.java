package com.mm.screen;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.mm.helpers.Assets;
import com.mm.objects.Hero;
import com.mm.objects.InventoryItem;
import com.mm.objects.Plane;
import com.mm.screen.input.GameScreenInputAdapter;
import com.mm.screen.input.GameScreenInputHandler;


public class GameScreen extends SizableScreen
{
    RayHandler rayHandler;
    Light light;
    World world;
    
    private Skin buttonSkin = new Skin();
    private Skin uiSkin = new Skin(Gdx.files.internal("uiskin_copy.json"));
    private Stage m_stage;
    
    private OrthographicCamera m_cam;

    private Texture m_background;
    
    private Hero m_hero;
    
    private Animation m_activeAnimation;
    
    private Label label;
    
    private Plane m_plane;
    
    private ImageButton m_quitButton, m_doorButton;
    private ImageButton m_paintingButton;
    private ImageButton m_leftPurpleTreeButton;
    private ImageButton m_planeButton;
    
    private GameScreenInputAdapter inputProcessor;
    
    private float m_runTime = 0f;
    
    public GameScreen()
    {
        m_cam = new OrthographicCamera();
        
        m_background = Assets.assetManager.get(Assets.LOUNGE,Texture.class);
        m_hero = Hero.getInstance(); 
        m_hero.setNextRelativeSize(0.5f);
        m_hero.setPosition(30,70);
        m_plane = new Plane();
        
        preferredWidth = m_background.getWidth();
        preferredHeight = m_background.getHeight();

        Gdx.graphics.setWindowedMode(preferredWidth, preferredHeight);
        
        buttonSkin.addRegions(Assets.buttonsAtlas);
        
        buildPlaneAnimation();
        
        GameScreenInputHandler.initializeInstance(this);
        
        m_stage = new Stage();
        
        m_quitButton = constructButton(GameScreenInputHandler.QUIT_BUTTON);
        Texture slotImage = m_hero.getInventory()[0].getSlotImage();
        int x = m_hero.getInventory().length*slotImage.getWidth();
        m_quitButton.setPosition(x, preferredHeight-m_quitButton.getHeight());
        m_stage.addActor(m_quitButton);
        
        m_doorButton = constructBlankButton(GameScreenInputHandler.DOOR_BUTTON,105,205);
        m_doorButton.setPosition(495, 0);
        m_stage.addActor(m_doorButton);
         
        m_paintingButton = constructBlankButton(GameScreenInputHandler.PAINTING_BUTTON,120,125);
        m_paintingButton.setPosition(255, 140);
        m_stage.addActor(m_paintingButton);
        
        m_leftPurpleTreeButton = constructBlankButton(GameScreenInputHandler.LEFT_TREE_BUTTON,50,100);
        m_leftPurpleTreeButton.setPosition(50, 180);
        m_stage.addActor(m_leftPurpleTreeButton);
        
        m_planeButton = constructBlankButton(GameScreenInputHandler.PLANE_BUTTON,120,120);
        m_planeButton.setPosition(330, 330);
        m_stage.addActor(m_planeButton);
        
        label = new Label("Messages appear here.", uiSkin);
        
        label.setAlignment(Align.center);
        label.setPosition(m_hero.getX(), m_hero.getY()+60);
        m_stage.addActor(label);
        
        m_cam = new OrthographicCamera();
        m_cam.setToOrtho(true, preferredWidth, preferredHeight);
        // Move Camera to 0,0
        //cam.translate(-cam.position.x, -cam.position.y, 0);
        
        world = new World(new Vector2(0,0),true);
        
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);
        
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.5f, 0.5f, 0.5f, 0.5f);
        rayHandler.setBlurNum(3);
        
        light = new ConeLight(rayHandler, 1000, Color.WHITE, 250f, 520f, 250f,90f,20f);
        
        
    }
    
    private void buildPlaneAnimation()
    {
        TextureRegion[] planeFrames = new TextureRegion[4];
        
        for (int x=1;x<5;x++)
        {
            planeFrames[x-1] = Assets.planesAtlas.findRegion("plane"+x);
            planeFrames[x-1].flip(false, true);
            
        }
      //  planeFrames[0] = AssetLoader.planesAtlas.findRegion("plane"+1);
      //  planeFrames[0].flip(false, true);
      //  planeFrames[1] = AssetLoader.planesAtlas.findRegion("plane"+2);
      //  planeFrames[1].flip(false, true);
        m_activeAnimation = new Animation(2.0f, planeFrames);
        m_activeAnimation.setPlayMode(Animation.PlayMode.LOOP);
        System.out.println("Animation: "+m_activeAnimation.getKeyFrames().length);
    }
    
    private ImageButton constructBlankButton(String name, int width, int height)
    {
        //Texture blankTexture = new Texture(width,height,Pixmap.Format.RGBA8888);
        Texture blankTexture = new Texture(width,height,Pixmap.Format.RGB888);  // Uncomment to see the buttons
        buttonSkin.add(name,blankTexture);
        return constructButton(name);
    }
    
    /**
     * TODO: Refactor as near duplicate with MainScreen.
     * @param name
     * @return
     */
    private ImageButton constructButton(String name)
    {
        ImageButtonStyle imgButtonStyle = new ImageButtonStyle();
        imgButtonStyle.up = buttonSkin.newDrawable(name);
        imgButtonStyle.down = buttonSkin.newDrawable(name);
        imgButtonStyle.checked = buttonSkin.newDrawable(name);
        imgButtonStyle.over = buttonSkin.newDrawable(name);
        ImageButton button = new ImageButton(imgButtonStyle);
        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
        button.addListener(GameScreenInputHandler.getInstance());
        button.setName(name);
        return button;
    }

    /**
     * TODO: Potential refactor as near duplicate with MainScreen
     */
    @Override
    public void render(float delta)
    {
        m_runTime += delta;
        Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        m_cam.update();
        
        SpriteBatch batcher;
        batcher = new SpriteBatch();
        // Attach batcher to camera
        batcher.setProjectionMatrix(m_cam.combined);
        batcher.begin();
        batcher.draw(m_background, 0, 0, m_background.getWidth(), m_background.getHeight(),0, 0, m_background.getWidth(), m_background.getHeight(),false,true);
        m_hero.render(batcher,delta);
        m_plane.render(batcher,delta);
        // TextureRegion plane = m_activeAnimation.getKeyFrame(m_runTime);
        // batcher.draw(plane.getTexture(), 0, 0, plane.getRegionWidth(), plane.getRegionHeight(),plane.getRegionX(), plane.getRegionY()-plane.getRegionHeight(), plane.getRegionWidth(), plane.getRegionHeight(),false,true);
        //System.out.println("Plane: "+plane.getRegionX()+" : "+plane.getRegionY()+" , "+plane.getRegionHeight()+" : "+plane.getRegionWidth());
 
        int x = 0;
        
        for (InventoryItem item : m_hero.getInventory())
        {
            Texture slotImage = item.getSlotImage();
            int y = 0;//m_background.getHeight() - slotImage.getHeight();
            Texture itemImage = item.getItemImage();
            if (itemImage != null)
            {
                batcher.draw(itemImage, x, y, itemImage.getWidth(), itemImage.getHeight(),0, 0, itemImage.getWidth(), itemImage.getHeight(),false,true);
            }
            
            batcher.draw(slotImage, x, y, slotImage.getWidth(), slotImage.getHeight(),0, 0, slotImage.getWidth(), slotImage.getHeight(),false,true);
            x = x + slotImage.getWidth();
        }
        
        batcher.end();
        
        rayHandler.setCombinedMatrix(m_cam);
        rayHandler.update();
        rayHandler.render();
        
        label.setPosition(m_hero.getX(), m_hero.getY()+60);
        
        m_stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        m_stage.draw();
     //   Gdx.app.log("MainScreen", "Stage: "+m_stage.getViewport().getScreenHeight()+" , "+m_stage.getViewport().getScreenWidth());

    }

    @Override
    public void resize(int width, int height)
    {
        Gdx.graphics.setWindowedMode(width, height);
        
        
        m_cam.setToOrtho(true, width, height);
        m_stage.getViewport().update(width, height,true);
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

    public void updateUI()
    {
        m_hero.addItem(InventoryItem.RING,Assets.ringItemTexture);
    }
}
