package com.mm.screen.input;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
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
	    float xLoc = screenX/m_screen.xScale;
	    float yLoc = screenY/m_screen.yScale;
	    Dragon.getInstance().m_position.set(xLoc, yLoc);
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
	    Dragon.getInstance().setImage(slot.getDragonImage());
	    Dragon.getInstance().setActive(true);
	    float xLoc = screenX/m_screen.xScale;
	    float yLoc = screenY/m_screen.yScale;
	    Dragon.getInstance().m_position.set(xLoc, yLoc);
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
	    m_screen.gold -= 5;
	    m_screen.updateMessageLabel("Dragon Dropped");
	    Texture image = Dragon.getInstance().m_image;
	    Dragon.getInstance().setActive(false);
	    float xLoc = screenX/m_screen.xScale;
	    float yLoc = screenY/m_screen.yScale;
	    m_screen.dropDragon(image, new Vector2(xLoc,yLoc));
	}
	return false;
    }

    @Override
    public void dispose()
    {
	// TODO Auto-generated method stub
	
    }

}
