package com.mm.screen.input;

import com.badlogic.gdx.InputAdapter;
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
	    //m_screen.updateMessageLabel("Dragging: " + screenX + " : " + screenY);
	    float xLoc = screenX / m_screen.xScale;
	    float yLoc = screenY / m_screen.yScale;
	    Dragon.getInstance().m_position.set(xLoc, yLoc);
	    if (screenY > m_screen.m_dropThreshold)
	    {
	        Dragon.getInstance().dimension.set(0.5f, 0.5f);
	        m_screen.updateDropThreshold(0.5f);
	    }
	    else
	    {
		Dragon.getInstance().dimension.set(1.0f, 1.0f);
		m_screen.updateDropThreshold(1.0f);
	    }
	}
	return true;
    }

    /**
     * Mouse button pressed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
	DragonSlot slot = m_screen.getSlot(screenX, screenY);
	if (slot != null)
	{
	    //m_screen.updateMessageLabel("Grabbed Dragon: " + screenX + " : " + screenY);
	    Dragon.getInstance().m_data = slot.getDragonData();
	    Dragon.getInstance().setActive(true);
	    float xLoc = screenX / m_screen.xScale;
	    float yLoc = screenY / m_screen.yScale;
	    Dragon.getInstance().m_position.set(xLoc, yLoc);
	}
	return false;
    }

    /**
     * Mouse button released;
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
	if (Dragon.getInstance().getActive())
	{
	    if (screenY > m_screen.m_dropThreshold)
	    {
		
		if (m_screen.getGold() >= 5)
		{
		    m_screen.updateGold(-Dragon.getInstance().m_data.m_goldDropCost);
		    //m_screen.updateMessageLabel("Dragon Dropped");
		    Dragon.getInstance().setActive(false);
		    float xLoc = screenX / m_screen.xScale;
		    float yLoc = screenY / m_screen.yScale;
		    m_screen.controller.dropDragon(Dragon.getInstance().m_data, new Vector2(xLoc, yLoc));
		}
		else
		{
		    Dragon.getInstance().setActive(false);
		    //m_screen.updateMessageLabel("Not Enough Gold!!");
		}
	    }
	    else
	    {
		Dragon.getInstance().setActive(false);
	    }
	}
	return false;
    }

    @Override
    public void dispose()
    {
	// TODO Auto-generated method stub

    }

}
