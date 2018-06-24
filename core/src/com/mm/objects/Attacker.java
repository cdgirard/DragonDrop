package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mm.helpers.Globals;

/**
 * Used to manage the location and state of the different attackers in the game.
 * TODO: May make a specific class for each if they vary enough.
 * @author cdgira
 *
 */
public class Attacker extends AbstractGameObject
{
    public AttackerData myData;
    public float health;

    public Attacker(int type)
    {
	myData = Globals.attackerTypes[type];
	myData.m_experience++;
	health = myData.m_health;

	if (type != 4)
            dimension.set(0.5f,0.5f);
	else
	    dimension.set(0.5f,0.85f);
	
	bounds.set(0, 0, dimension.x, dimension.y);
	origin.set(dimension.x / 2, dimension.y / 2);
    }
    
    @Override
    public void update(float deltaTime)
    {
	float velocityThreshold = -0.5f*myData.m_speed;
	if (body.getLinearVelocity().y > velocityThreshold)
	{
	    float scaledImpulse = myData.m_impulse / deltaTime;
	    body.applyLinearImpulse(new Vector2(0, scaledImpulse), m_position, true);
	}
	else if (body.getLinearVelocity().y < velocityThreshold - .5f)
	{
	    body.setLinearVelocity(body.getLinearVelocity().x, velocityThreshold);
	}
	super.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batcher)
    {
	Texture image = myData.m_image;
	Vector2 dotLoc = new Vector2(m_position.x - origin.x,m_position.y - origin.y);
	batcher.draw(image, m_position.x - origin.x, m_position.y - origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, 0, 0, image.getWidth(), image.getHeight(), false, true);
	// Draw Weapon Level
	for (int i=0;i<myData.m_weapon;i++)
	    batcher.draw(AttackerData.m_weaponDot, dotLoc.x+0.05f*i, dotLoc.y, 0, 0, 0.04f, 0.04f, 1, 1, rotation, 0, 0, AttackerData.m_weaponDot.getWidth(), AttackerData.m_weaponDot.getHeight(), false, true);
	   // batcher.draw(AttackerData.m_weaponDot, weaponLoc.x+0.1f, weaponLoc.y, 0, 0, 0.04f, 0.04f, 1, 1, rotation, 0, 0, AttackerData.m_weaponDot.getWidth(), AttackerData.m_weaponDot.getHeight(), false, true);
	for (int i=0;i<myData.m_speed;i++)
	    batcher.draw(AttackerData.m_speedDot, dotLoc.x+0.05f*i, dotLoc.y+dimension.y-0.04f, 0, 0, 0.04f, 0.04f, 1, 1, rotation, 0, 0, AttackerData.m_speedDot.getWidth(), AttackerData.m_speedDot.getHeight(), false, true);
	for (int i=0;i<myData.m_armor;i++)
	    batcher.draw(AttackerData.m_armorDot, dotLoc.x+dimension.x-0.04f, dotLoc.y+0.05f*i, 0, 0, 0.04f, 0.04f, 1, 1, rotation, 0, 0, AttackerData.m_armorDot.getWidth(), AttackerData.m_armorDot.getHeight(), false, true);
    }

}
