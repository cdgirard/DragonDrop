package com.mm.screen.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.Disposable;
import com.mm.objects.Hero;
import com.mm.screen.GameScreen;

public class GameScreenInputAdapter extends InputAdapter implements Disposable
{
    GameScreen m_screen;
    
    public GameScreenInputAdapter(GameScreen gs)
    {
	m_screen = gs;
	//InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
	//im.addProcessor(this);
	//Gdx.input.setInputProcessor(this);
    }
    
    /**
     * Mouse button pressed
     */
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
	m_screen.updateMessageLabel("Why would I move there?");
	Hero hero = Hero.getInstance();
	hero.setPosition(hero.getX(), hero.getY());  // Stop the hero moving.
	return false;
    }
    
    /**
     * Mouse button released;
     */
    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button)
    {
	m_screen.updateMessageLabel("Why would I move there?");
	return false;
    }

    @Override
    public void dispose()
    {
	// TODO Auto-generated method stub
	
    }

}
