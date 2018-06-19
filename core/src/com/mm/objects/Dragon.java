package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mm.helpers.Assets;
import com.mm.helpers.Globals;

public class Dragon extends SimpleAbstractGameObject
{
    private static Dragon m_dragon;
    private boolean m_active = false;

    public DragonData m_data;

    private Dragon()
    {
	m_data = Globals.dragonTypes[0];
    }

    public static Dragon getInstance()
    {
	if (m_dragon == null)
	    m_dragon = new Dragon();
	return m_dragon;
    }
    

    /**
     * If the Dragon is active then draw it.
     * @param batcher
     * @param delta
     */
    @Override
    public void render(SpriteBatch batcher)
    {
	if (m_active)
	{
		Texture image = m_data.m_image;
	    batcher.draw(image, m_position.x-dimension.x/2, m_position.y-dimension.y/2, dimension.x, dimension.y, 0, 0, image.getWidth(), image.getHeight(), false, true);
	}
    }

    public boolean getActive()
    {
	return m_active;
    }

    public void setActive(boolean value)
    {
	m_active = value;
    }

}
