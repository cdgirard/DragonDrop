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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.utils.Align;
import com.mm.helpers.Assets;
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
    
    private Skin buttonSkin = new Skin();
    private Skin uiSkin = new Skin(Gdx.files.internal("uiskin_copy.json"));
    private Stage m_stage;
    
    private OrthographicCamera m_cam;

    private Texture m_background;
    
    private Label label;
    
    private ImageButton m_quitButton;
    
    private DragonSlot[] slots = new DragonSlot[5];
    
    private GameScreenInputAdapter inputProcessor;
    
    private float m_runTime = 0f;
    
    public GameScreen()
    {
        m_cam = new OrthographicCamera();
        
        m_background = Assets.assetManager.get(Assets.GAME_SCREEN,Texture.class);
        
        preferredWidth = m_background.getWidth();
        preferredHeight = m_background.getHeight();

        Gdx.graphics.setWindowedMode(preferredWidth, preferredHeight);
        
        buttonSkin.addRegions(Assets.buttonsAtlas);
        
        GameScreenInputHandler.initializeInstance(this);
        
        m_stage = new Stage();
        
        m_quitButton = constructButton(GameScreenInputHandler.QUIT_BUTTON);
        int x = 100;
        m_quitButton.setPosition(x, preferredHeight-m_quitButton.getHeight());
        m_stage.addActor(m_quitButton);
        
        label = new Label("Messages appear here.", uiSkin);
        
        label.setAlignment(Align.center);
        label.setPosition(200, 300);
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
        
        for (int index=0;index<slots.length;index++)
        {
            slots[index] = new DragonSlot();
        }
        slots[0].setDragon(Assets.assetManager.get(Assets.GOTH_DRAGON,Texture.class));
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
        
        // Create 3 slots for dragons to be dragged and dropped
        
        int x = 0;
        for (DragonSlot slot : slots)
        {
            int y = (int)m_quitButton.getHeight();
            Texture slotImage = slot.getSlotImage();
            Texture itemImage = slot.getDragonImage();
            if (itemImage != null)
            {
                batcher.draw(itemImage, x, y, slotImage.getWidth(), slotImage.getHeight(),0, 0, itemImage.getWidth(), itemImage.getHeight(),false,true);
            }
            
            batcher.draw(slotImage, x, y, slotImage.getWidth(), slotImage.getHeight(),0, 0, slotImage.getWidth(), slotImage.getHeight(),false,true);
            x = x + slotImage.getWidth();
        }
        
        batcher.end();
        
        rayHandler.setCombinedMatrix(m_cam);
        rayHandler.update();
        rayHandler.render();
        
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
}
