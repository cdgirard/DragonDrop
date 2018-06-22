package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mm.helpers.Assets;

public class GoldCoin extends SimpleAbstractGameObject
{
    Texture m_image;
    
    public GoldCoin(Vector2 pos)
    {
	m_image = Assets.assetManager.get(Assets.GOLD_COIN,Texture.class);
	m_position = pos;
    }

    @Override
    public void render(SpriteBatch batcher)
    {
	batcher.draw(m_image, m_position.x, m_position.y, m_image.getWidth(), m_image.getHeight(), 0, 0, m_image.getWidth(), m_image.getHeight(), false, true);
    }

}
