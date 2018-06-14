package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mm.helpers.Assets;

public class DroppingDragon extends AbstractGameObject
{
    private Texture image;

    public DroppingDragon()
    {
	init();
    }

    private void init()
    {
	image = Assets.assetManager.get(Assets.GOTH_DRAGON, Texture.class);

	bounds.set(0, 0, dimension.x, dimension.y);
	origin.set(dimension.x / 2, dimension.y / 2);

    }

    @Override
    public void render(SpriteBatch batch)
    {
	batch.draw(image, m_position.x - origin.x, m_position.y - origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, 0, 0, image.getWidth(), image.getHeight(), false, true);
    }

}
