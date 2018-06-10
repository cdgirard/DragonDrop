package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.mm.helpers.Assets;
import com.mm.helpers.UIHelper;
import com.mm.screen.input.GameScreenInputHandler;

public class DragonSlot
{
    private Texture m_slotImage;
    private Texture m_dragonImage;
    private ImageButton m_planeButton;
    
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
    
    public ImageButton getSlotButton()
    {
	return m_planeButton;
    }

    public void setDragon(Texture itemTexture)
    {
        m_dragonImage = itemTexture;
        if (m_dragonImage != null)
        {
            m_planeButton = UIHelper.constructBlankButton(GameScreenInputHandler.PLANE_BUTTON,120,120);
            
            // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
            // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
            // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
            // revert the checked state.
            m_planeButton.addListener(GameScreenInputHandler.getInstance());
            
        }
    }

}
