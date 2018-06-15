package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.mm.helpers.Assets;

/**
 * Used to manage the location and state of the different attackers in the game.
 * TODO: May make a specific class for each if they vary enough.
 * @author cdgira
 *
 */
public class Attacker extends AbstractGameObject
{
    private static AttackerData[] attackerTypes = {
	new AttackerData(Assets.assetManager.get(Assets.FARMER, Texture.class),10),
	new AttackerData(Assets.assetManager.get(Assets.SOILDER, Texture.class),20),
	new AttackerData(Assets.assetManager.get(Assets.CROSSBOW, Texture.class),25),
	new AttackerData(Assets.assetManager.get(Assets.SPEARMAN, Texture.class),15),
	new AttackerData(Assets.assetManager.get(Assets.KNIGHT, Texture.class),50),
    };
    public AttackerData myData;

    public Attacker(int type)
    {
	myData = attackerTypes[type];

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
	Texture image = myData.m_image;
	batcher.draw(image, m_position.x - origin.x, m_position.y - origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, 0, 0, image.getWidth(), image.getHeight(), false, true);
    }

}
