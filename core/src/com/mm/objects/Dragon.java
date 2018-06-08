package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mm.helpers.Assets;

public class Dragon
{
    private static Dragon m_dragon;
    
    private float xLoc, yLoc;
    private Texture m_image;
    private float nextXLoc, nextYLoc;
    
    private Dragon()
    {
        m_image = Assets.assetManager.get(Assets.GOTH_DRAGON,Texture.class);
        xLoc = 200;
        yLoc = 200;
        nextXLoc = xLoc;
        nextYLoc = yLoc;
    }
    
    public static Dragon getInstance()
    {
        if (m_dragon == null)
            m_dragon = new Dragon();
        return m_dragon;
    }

    public void render(SpriteBatch batcher, float delta)
    {
        float maxXChange = delta * 15;
        float maxYChange = delta * 15;
        
       // System.out.println("Present Size: " + m_presentSize);
        if (xLoc < nextXLoc)
        {
            xLoc = xLoc + maxXChange;
            if (xLoc > nextXLoc)
                xLoc = nextXLoc;
        }
        else if (xLoc > nextXLoc)
        {
            xLoc = xLoc - maxXChange;
            if (xLoc < nextXLoc)
                xLoc = nextXLoc;
        }

        if (yLoc < nextYLoc)
        {
            yLoc = yLoc + maxYChange;
            if (yLoc > nextYLoc)
                yLoc = nextYLoc;
        }
        else if (yLoc > nextYLoc)
        {
            yLoc = yLoc - maxYChange;
            if (yLoc < nextYLoc)
                yLoc = nextYLoc;
        }

        float modYLoc = yLoc +m_image.getHeight()-m_image.getHeight();
        batcher.draw(m_image, xLoc, modYLoc, m_image.getWidth(), m_image.getHeight(), 0, 0, m_image.getWidth(), m_image.getHeight(), false, true);

    }
    
    /**
     * Sets the immediate position of the Hero and kills the nextXLoc, nextYLoc as well.
     * Doesn't cancel out any resizing though.
     * @param x
     * @param y
     */
    public void setPosition(int x, int y)
    {
	xLoc = nextXLoc = x;
	yLoc = nextYLoc = y;
    }

    /**
     * Sets where the Hero should be moved to.
     * @param xLoc
     * @param yLoc
     */
    public void setNextPosition(int xLoc, int yLoc)
    {
        nextXLoc = xLoc;
        nextYLoc = yLoc;
    }

    public int getY()
    {
        return Math.round(yLoc);
    }
    public int getX()
    {
        return Math.round(xLoc);
    }

}
