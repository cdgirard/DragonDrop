package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mm.helpers.Assets;

public class Mountain extends AbstractGameObject
{
    private Texture m_image;
    
    public Mountain()
    {
	m_image = Assets.assetManager.get(Assets.MOUNTAIN,Texture.class);
	init();
    }
    
    private void init()
    {
	dimension.set(0.5f, 9.0f);
	bounds.set(0, 0, dimension.x, dimension.y);
	origin.set(dimension.x / 2, dimension.y / 2);
    }

    @Override
    public void render(SpriteBatch batcher)
    {
	batcher.draw(m_image, m_position.x - origin.x, m_position.y - origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, 0, 0, m_image.getWidth(), m_image.getHeight(), false, true);
    }

}
