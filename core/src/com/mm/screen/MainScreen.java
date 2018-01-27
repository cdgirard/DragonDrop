package com.mm.screen;

import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.mm.helpers.Assets;
import com.mm.screen.input.MainScreenInputHandler;

public class MainScreen extends SizableScreen
{
    RayHandler rayHandler;
    Light light;
    World world;
    
    private Skin buttonSkin = new Skin();
    
    private OrthographicCamera m_cam;
    
    private Stage m_stage;// = new Stage();
    
    private ImageButton m_startNewGameButton, m_loadGameButton;
    
    private TextureRegion m_background;
      
    public MainScreen()
    {
        
        // TODO: Turn into Hashtable so don't get screwed if index changes.
        m_background = new TextureRegion(Assets.backgroundTextures.get(0));
        m_background.flip(false, true);
        preferredWidth = m_background.getTexture().getWidth();
        preferredHeight = m_background.getTexture().getHeight();
              
        Gdx.graphics.setWindowedMode(preferredWidth, preferredHeight);
        buttonSkin.addRegions(Assets.buttonsAtlas);
        
        MainScreenInputHandler.initializeInstance();
        m_stage = new Stage();
      //  m_stage.getViewport().update(preferredWidth, preferredHeight);

        m_startNewGameButton = constructButton(MainScreenInputHandler.START_NEW_GAME_BUTTON);
        m_startNewGameButton.setPosition(700, 200);
        m_stage.addActor(m_startNewGameButton);
        
        m_loadGameButton = constructButton(MainScreenInputHandler.LOAD_GAME_BUTTON);
        m_loadGameButton.setPosition(700, 75);
        m_stage.addActor(m_loadGameButton);
        
        

        
        m_cam = new OrthographicCamera(preferredWidth,preferredHeight);
        m_cam.update();
       // m_cam.setToOrtho(true, preferredWidth, preferredHeight);
        world = new World(new Vector2(0,0),true);
        
        RayHandler.setGammaCorrection(true);
        RayHandler.useDiffuseLight(true);
        
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.5f);
        rayHandler.setBlurNum(3);
        
        //light = new PointLight(rayHandler, 1000, null,50f, 625f, 400f);
       // light.setColor( Color.WHITE);
        //light.setColor(Color.PURPLE);
        
         light = new ConeLight(rayHandler, 1000, Color.WHITE, 1000f, 640f, 75f,90f,30f);
       // conelight.setStaticLight(true);
        
        // Move Camera to 0,0
        //cam.translate(-cam.position.x, -cam.position.y, 0);
        

    }
    
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
        button.addListener(MainScreenInputHandler.getInstance());
        button.setName(name);
        return button;
    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(1.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        m_cam.update();
        
        SpriteBatch batcher;
        batcher = new SpriteBatch();
        // Attach batcher to camera
        batcher.setProjectionMatrix(m_cam.combined);
        batcher.disableBlending();
        batcher.begin();
        batcher.draw(m_background, 0, 0, m_background.getTexture().getWidth(), m_background.getTexture().getHeight());
        batcher.enableBlending();
        batcher.end();
        
        rayHandler.setCombinedMatrix(m_cam);
        rayHandler.update();
        rayHandler.render();
        
        m_stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        m_stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        Gdx.graphics.setWindowedMode(width, height);
        
        
        m_cam.setToOrtho(true, width, height);
        m_stage.getViewport().update(width, height,true);
        
        Gdx.app.log("MainScreen", "resizing:"+width+" , "+height);
    }

    @Override
    public void show()
    {
        // InputProcessor inputProcessorOne = MainScreenInputHandler.getInstance();
        InputProcessor inputProcessorTwo = m_stage;
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
      //  inputMultiplexer.addProcessor(inputProcessorOne);
        inputMultiplexer.addProcessor(inputProcessorTwo);
        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.app.log("MainScreen", "show called");
    }

    @Override
    public void hide()
    {
        Gdx.input.setInputProcessor(null);
        Gdx.app.log("MainScreen", "hide called");
    }

    @Override
    public void pause()
    {
        Gdx.app.log("MainScreen", "pause called");
    }

    @Override
    public void resume()
    {
        Gdx.app.log("MainScreen", "resume called");
    }

    @Override
    public void dispose()
    {
        // Leave blank
    }

}
