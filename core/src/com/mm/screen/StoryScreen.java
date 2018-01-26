package com.mm.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mm.helpers.AssetLoader;
import com.mm.screen.input.StoryScreenInputHandler;


public class StoryScreen extends SizableScreen
{
    private Skin buttonSkin = new Skin();
    private Skin uiSkin;
    private Stage m_stage;
    
    private OrthographicCamera m_cam;

    private Texture m_background;
    
    private ImageButton m_nextButton;
    
    private float rotation;
    
    private Label label;
    
    public StoryScreen()
    {
        m_cam = new OrthographicCamera();
        
        // TODO: Turn into Hashtable so don't get screwed if index changes.
        m_background = AssetLoader.backgroundTextures.get(5);
        preferredWidth = m_background.getWidth();
        preferredHeight = m_background.getHeight();
       // Gdx.graphics.setDisplayMode(preferredWidth, preferredHeight, false);
        
        buttonSkin.addRegions(AssetLoader.buttonsAtlas);
        
        StoryScreenInputHandler.initializeInstance(this);
        
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("Game_Font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        BitmapFont bfont = gen.generateFont(parameter);
        gen.dispose();

        m_stage = new Stage(new ScalingViewport(Scaling.none,preferredWidth,preferredHeight));
        uiSkin = new Skin();

        uiSkin.addRegions(new TextureAtlas(Gdx.files.internal("uiskin.atlas")));
        uiSkin.add("default-font", bfont);

        uiSkin.load(Gdx.files.internal("uiskin.json"));
 
        //m_stage.setViewport(preferredWidth,preferredHeight,false);
        
        
        label = new Label("", uiSkin);
        label.setText(StoryScreenInputHandler.getInstance().stories[3]);
        
        label.setAlignment(Align.center);
        label.setPosition(400, 400);
        m_stage.addActor(label);
        
        m_nextButton = constructButton(StoryScreenInputHandler.NEXT_BUTTON);
        m_nextButton.setPosition(200, 200);
        m_stage.addActor(m_nextButton);
        
        
        
        m_cam = new OrthographicCamera();
        m_cam.setToOrtho(true, preferredWidth, preferredHeight);
        // Move Camera to 0,0
        //cam.translate(-cam.position.x, -cam.position.y, 0);


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
        button.addListener(StoryScreenInputHandler.getInstance());
        button.setName(name);
        return button;
    }

    /**
     * TODO: Potential refactor as near duplicate with MainScreen
     */
    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        m_cam.update();
        
        SpriteBatch batcher;
        batcher = new SpriteBatch();
        // Attach batcher to camera
        batcher.setProjectionMatrix(m_cam.combined);
        batcher.begin();
        //batcher.draw(m_background, 0, 0, m_background.getWidth(), m_background.getHeight(),0, 0, m_background.getWidth(), m_background.getHeight(),false,true);
        rotation += delta;
        rotation %= 360;
        batcher.draw(m_background, 0, 0, m_background.getWidth()/2, m_background.getHeight()/2, m_background.getWidth(), m_background.getHeight(), 1.0f, 1.0f, rotation, 0, 0, m_background.getWidth(), m_background.getHeight(), false, true);
       // draw (Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX,
       //         float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY)
        batcher.end();
        
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
        Gdx.app.log("StoryScreen", "resizing");
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
     * Changes the image displayed in the background.
     * @param texture
     */
    public void setBackgroundImage(Texture texture)
    {
        m_background = texture;
        preferredWidth = m_background.getWidth();
        preferredHeight = m_background.getHeight();
        this.resize(preferredWidth,preferredHeight);
        label.setText(StoryScreenInputHandler.getInstance().stories[StoryScreenInputHandler.getInstance().imageNum-2]);
        
    }

}
