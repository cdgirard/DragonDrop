package com.mm.screen.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;
import com.mm.objects.Dragon;
import com.mm.objects.DragonSlot;
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
	if (Dragon.getInstance().getActive())
	{
	    m_screen.updateMessageLabel("Dragging");
	    Dragon.getInstance().setPosition(screenX-50, screenY-50);
	}
	return true;
    }
    
    /**
     * Mouse button pressed
     */
    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button)
    {
	DragonSlot slot = m_screen.getSlot(screenX, screenY);
	if (slot != null)
	{
	    m_screen.updateMessageLabel("Grabbed Dragon");
	    Dragon.getInstance().setActive(true);
	    Dragon.getInstance().setPosition(screenX-50, screenY-50);
	}
	return false;
    }
    
    /**
     * Mouse button released;
     */
    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button)
    {
	if (Dragon.getInstance().getActive())
	{
	    m_screen.updateMessageLabel("Dragon Dropped");
	    Dragon.getInstance().setActive(false);
	}
	return false;
    }

    @Override
    public void dispose()
    {
	// TODO Auto-generated method stub
	
    }

}
