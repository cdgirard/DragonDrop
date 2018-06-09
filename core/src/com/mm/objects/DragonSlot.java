package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.mm.helpers.Assets;

public class DragonSlot
{
    private Texture m_slotImage;
    private Texture m_dragonImage;
    
    public DragonSlot()
    {
        m_slotImage = Assets.assetManager.get(Assets.DRAGON_SLOT,Texture.class);
        m_dragonImage = null;
    }
    
    public Texture getDragonImage()
    {
        return m_dragonImage;
    }
    
    public Texture getSlotImage()
    {
        return m_slotImage;
    }

    public void setDragon(Texture itemTexture)
    {
        m_dragonImage = itemTexture;
    }

}
