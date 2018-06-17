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
    private static int m_slotNum = 0;
    private Texture m_slotImage;
    private Texture m_dragonImage;
    private Rectangle m_slotButton;
    private String m_slotName;
    public ImageButton m_buyButton;
    public ImageButton m_sellButton;

    public DragonSlot(Vector2 slotPos, Vector2 btnPos)
    {
	m_slotImage = Assets.assetManager.get(Assets.DRAGON_SLOT, Texture.class);
	m_slotName = "SLOT-" + m_slotNum;
	slotPos.set(slotPos.x, slotPos.y);
	m_position = slotPos;
	m_dragonImage = null;

	// Construct Buy Button
	m_buyButton = UIHelper.constructButton(Assets.BUY_BTN, Assets.BUY_BTN + "-" + m_slotNum);
	m_buyButton.setSize(m_buyButton.getWidth() * 0.35f, m_buyButton.getHeight() * 0.35f);
	float y = btnPos.y - m_buyButton.getHeight();
	m_buyButton.setPosition(btnPos.x + 5, y - 5);
	m_buyButton.setVisible(true);
	
	// Construct Sell Button
	m_sellButton = UIHelper.constructButton(Assets.SELL_BTN, Assets.SELL_BTN + "-"+m_slotNum);
	m_sellButton.setSize(m_sellButton.getWidth() * 0.35f,m_sellButton.getHeight() * 0.35f);
	y = btnPos.y - m_sellButton.getHeight();
	m_sellButton.setPosition(slotPos.x + 5, y - 5);
	m_sellButton.setVisible(false);
	    
	m_slotNum++;
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
	    m_sellButton.setVisible(true);
	    m_buyButton.setVisible(false);
	}
	else
	{
	    m_dragonImage = null;
	    m_sellButton.setVisible(false);
	    m_buyButton.setVisible(true);
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
