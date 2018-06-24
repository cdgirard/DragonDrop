package com.mm;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.mm.helpers.Assets;
import com.mm.helpers.Globals;
import com.mm.helpers.HighScoreListFileManager;
import com.mm.screen.GameScreen;
import com.mm.screen.MainScreen;
import com.mm.screen.SizableScreen;

public class DragonDrop extends ApplicationAdapter
{
    /**
     * Should only ever get initialized once, but not sure if a way to 
     * set that up.
     */
    public static MainScreen MAIN_SCREEN;
    public static GameScreen GAME_SCREEN;

    public static DragonDrop m_dreamScape;

    private SizableScreen screen;

    /**
     * If libgdx is running correctly this should only ever get called once,
     * so there should only ever be one instance of DreamScape and that instance
     * should not change while the game runs.
     */
    @Override
    public void create()
    {
	m_dreamScape = this;
	Assets.load();
	Globals.highScores = HighScoreListFileManager.loadHighScores();
	MAIN_SCREEN = new MainScreen();
	GAME_SCREEN = new GameScreen();
	Gdx.app.log("DragonDrop", "created");

	

	setScreen(MAIN_SCREEN);
    }

    @Override
    public void dispose()
    {
	if (screen != null)
	    screen.hide();
	// AssetLoader.dispose();
    }

    @Override
    public void pause()
    {
	if (screen != null)
	    screen.pause();
    }

    @Override
    public void resume()
    {
	if (screen != null)
	    screen.resume();
    }

    @Override
    public void render()
    {
	if (screen != null)
	    screen.render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void resize(int width, int height)
    {
	if (screen != null)
	    screen.resize(width, height);
    }

    /**
     * Sets the current screen. {@link Screen#hide()} is called on any old
     * screen, and {@link Screen#show()} is called on the new screen, if any.
     * 
     * @param screen
     *            may be {@code null}
     */
    public void setScreen(SizableScreen screen)
    {
	if (this.screen != null)
	    this.screen.hide();
	this.screen = screen;
	if (this.screen != null)
	{
	    this.screen.show();
	    this.screen.resize();

	}
    }

    /** @return the currently active {@link Screen}. */
    public SizableScreen getScreen()
    {
	return screen;
    }
}
