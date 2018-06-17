package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.mm.helpers.Assets;
import com.mm.helpers.UIHelper;
import com.mm.screen.input.GameScreenInputHandler;

public class DragonSlot extends SimpleAbstractGameObject
{
    private static int m_slotNum = 1;
    private Texture m_slotImage;
    private Texture m_dragonImage;
    private Rectangle m_slotButton;
    private String m_slotName;
    public ImageButton m_buyButton;
    public ImageButton m_sellButton;

    public DragonSlot(Vector2 pos)
    {
	m_slotImage = Assets.assetManager.get(Assets.DRAGON_SLOT, Texture.class);
	m_slotName = "SLOT-" + m_slotNum++;
	pos.set(pos.x * m_slotImage.getWidth(), pos.y);
	m_position = pos;
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

    public Rectangle getSlotButton()
    {
	return m_slotButton;
    }

    public void setDragon(Texture itemTexture)
    {
	m_dragonImage = itemTexture;
	if (m_dragonImage != null)
	{
	    m_slotButton = new Rectangle();
	    m_slotButton.set(m_position.x, m_position.y, (float) m_slotImage.getWidth(), (float) m_slotImage.getHeight());

	    // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
	    // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
	    // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
	    // revert the checked state.
	    //m_slotButton.addListener(GameScreenInputHandler.getInstance());

	}
    }

    @Override
    public void render(SpriteBatch batcher)
    {
	if (m_dragonImage != null)
	{
	    batcher.draw(m_dragonImage, m_position.x, m_position.y, m_slotImage.getWidth(), m_slotImage.getHeight(), 0, 0, m_dragonImage.getWidth(), m_dragonImage.getHeight(), false, true);
	}
	batcher.draw(m_slotImage, m_position.x, m_position.y, m_slotImage.getWidth(), m_slotImage.getHeight(), 0, 0, m_slotImage.getWidth(), m_slotImage.getHeight(), false, true);
    }

}
