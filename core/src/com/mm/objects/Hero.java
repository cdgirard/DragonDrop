package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mm.helpers.Assets;

public class Hero
{
    private static Hero m_hero;
    
    private InventoryItem[] items;
    
    private float xLoc, yLoc;
    private Texture m_image;
    private float nextXLoc, nextYLoc;
    private float m_presentSize, m_nextSize;
    private float m_sizeChangeRate;
    
    private Hero()
    {
        m_image = Assets.heroTexture;
        xLoc = 200;
        yLoc = 200;
        m_presentSize = 1.0f;
        m_nextSize = m_presentSize;
        nextXLoc = xLoc;
        nextYLoc = yLoc;
        
        items = new InventoryItem[8];
        for (int x=0;x<items.length;x++)
            items[x] = new InventoryItem();
    }
    
    public static Hero getInstance()
    {
        if (m_hero == null)
            m_hero = new Hero();
        return m_hero;
    }

    public void render(SpriteBatch batcher, float delta)
    {
        float maxXChange = delta * 15;
        float maxYChange = delta * 15;
        
        if (m_presentSize > m_nextSize)
        {
            m_presentSize -= delta*m_sizeChangeRate;
            if (m_presentSize < m_nextSize)
                m_presentSize = m_nextSize;
        }
        else if (m_presentSize < m_nextSize)
        {
            m_presentSize += delta*m_sizeChangeRate;
            if (m_presentSize > m_nextSize)
                m_presentSize = m_nextSize;
        }
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

        float modYLoc = yLoc +m_image.getHeight()-m_image.getHeight()*m_presentSize;
        batcher.draw(m_image, xLoc, modYLoc, m_image.getWidth()*m_presentSize, m_image.getHeight()*m_presentSize, 0, 0, m_image.getWidth(), m_image.getHeight(), false, true);

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

    /**
     * Sets what to change the Hero's size to over time, usually during a move.  Size
     * is usually a value between 0f to 1.0f, where 1.0f means max size.
     * @param percentSize
     */
    public void setNextRelativeSize(float percentSize)
    {
        m_nextSize = percentSize;
        
        float totalDistance = Math.abs(yLoc-nextYLoc)/5.0f;
        float totalSizeChange = Math.abs(m_presentSize - m_nextSize);
        if (totalDistance != 0)
            m_sizeChangeRate = totalSizeChange/totalDistance;
        else
            m_sizeChangeRate = 5;
    }

    public int getY()
    {
        return Math.round(yLoc);
    }
    public int getX()
    {
        return Math.round(xLoc);
    }
    
    /**
     * Returns the Hero's inventory.
     * @return
     */
    public InventoryItem[] getInventory()
    {
        return items;
    }
    
    /**
     * Add an item to the player's inventory.
     * @param type
     * @param image
     */
    public void addItem(int type, Texture image)
    {
        items[0].setItem(type, image);
    }

}
