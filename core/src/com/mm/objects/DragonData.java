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
    public final float m_baseHealth;
    public final float m_armorBonus;
    public final float m_weaponBonus;
    public final float m_speedBonus;
    
    public DragonData(Texture img, int goldDropCost, int goldBuyCost, float health, float armorBonus, float weaponBonus, float speedBonus)
    {
	m_image = img;
	m_goldDropCost = goldDropCost;
	m_goldBuyCost = goldBuyCost;
	m_baseHealth = health;
	m_armorBonus = armorBonus;
	m_weaponBonus = weaponBonus;
	m_speedBonus = speedBonus;
    }
    
    public int computeDamage(AttackerData attacker)
    {
	float dmgMod = m_armorBonus/attacker.m_armor + m_weaponBonus/attacker.m_weapon + m_speedBonus/attacker.m_speed;
	int dmg = (int)dmgMod;
	if (dmg == 0)
	    dmg = 1;
        return dmg;	
    }

}
