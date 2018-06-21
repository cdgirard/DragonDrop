package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;

/**
 * Used to store the base data on each type of dragon.  Instance
 * variables set to final because this data should not be changed.
 * If it needs to be for a specific dropping dragon that should be
 * handled by the DroppingDragon or DragonSlot classes.
 * @author cdgira
 *
 */
public class DragonData
{
    public final Texture m_image;
    public final int m_goldDropCost;
    public final int m_goldBuyCost;
    public float m_baseHealth;
    public float m_armorBonus;
    public float m_weaponBonus;
    public float m_speedBonus;
    
    public DragonData(Texture img, int goldDropCost, int goldBuyCost, float health)
    {
	m_image = img;
	m_goldDropCost = goldDropCost;
	m_goldBuyCost = goldBuyCost;
	m_baseHealth = health;
    }
    
    public int computeDamage(AttackerData attacker)
    {
        return -1;	
    }

}
