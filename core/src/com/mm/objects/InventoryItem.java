package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.mm.helpers.Assets;

public class InventoryItem
{
    public final static int RING = 1;
    public final static int PAINTING = 2;
    private int m_type;
    private Texture m_slotImage;
    private Texture m_itemImage;
    
    public InventoryItem()
    {
        m_slotImage = Assets.slotTexture;
        m_itemImage = null;
    }
    
    public Texture getItemImage()
    {
        return m_itemImage;
    }
    
    public Texture getSlotImage()
    {
        return m_slotImage;
    }

    public void setItem(int type, Texture itemTexture)
    {
        m_type = type;
        m_itemImage = itemTexture;
    }

}
