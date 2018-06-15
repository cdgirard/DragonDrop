package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mm.helpers.Assets;

public class Dragon extends SimpleAbstractGameObject
{
    private static Dragon m_dragon;
    private boolean m_active = false;

    public Texture m_image;

    private Dragon()
    {
	m_image = Assets.assetManager.get(Assets.GOTH_DRAGON, Texture.class);
    }

    public static Dragon getInstance()
    {
	if (m_dragon == null)
	    m_dragon = new Dragon();
	return m_dragon;
    }
    
    /**
     * Set the image of the Dragon to be dragged for dropping.
     * @param image
     */
    public void setImage(Texture dragon)
    {
	m_image = dragon;
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
	    batcher.draw(m_image, m_position.x-dimension.x/2, m_position.y-dimension.y/2, dimension.x, dimension.y, 0, 0, m_image.getWidth(), m_image.getHeight(), false, true);
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
