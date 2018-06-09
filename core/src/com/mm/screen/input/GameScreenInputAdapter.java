package com.mm.screen.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.mm.objects.Dragon;
import com.mm.screen.GameScreen;

public class GameScreenInputAdapter extends InputAdapter implements Disposable
{
    GameScreen m_screen;
    
    public GameScreenInputAdapter(GameScreen gs)
    {
	m_screen = gs;
    }
    
    /**
     * 
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
	return true;
    }
    
    /**
     * Mouse button pressed
     */
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
	return false;
    }
    
    /**
     * Mouse button released;
     */
    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button)
    {
	return false;
    }

    @Override
    public void dispose()
    {
	// TODO Auto-generated method stub
	
    }

}
