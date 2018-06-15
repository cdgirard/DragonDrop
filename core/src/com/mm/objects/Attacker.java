package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mm.helpers.Assets;

/**
 * Used to manage the location and state of the different attackers in the game.
 * TODO: May make a specific class for each if they vary enough.
 * @author cdgira
 *
 */
public class Attacker extends AbstractGameObject
{
    private Texture image;
    public int gold = 10;

    public Attacker(int type)
    {
	if (type == 0)
	    image = Assets.assetManager.get(Assets.FARMER, Texture.class);
	else if (type == 1)
	    image = Assets.assetManager.get(Assets.SOILDER, Texture.class);
	else if (type == 2)
	    image = Assets.assetManager.get(Assets.CROSSBOW, Texture.class);
	else if (type == 3)
	    image = Assets.assetManager.get(Assets.SPEARMAN, Texture.class);
	else if (type == 4)
	    image = Assets.assetManager.get(Assets.KNIGHT, Texture.class);
	if (type != 4)
            dimension.set(0.75f,0.75f);
	else
	    dimension.set(0.75f,1.25f);
	bounds.set(0, 0, dimension.x, dimension.y);
	origin.set(dimension.x / 2, dimension.y / 2);
    }

    @Override
    public void render(SpriteBatch batcher)
    {
	batcher.draw(image, m_position.x - origin.x, m_position.y - origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, 0, 0, image.getWidth(), image.getHeight(), false, true);
    }

}
