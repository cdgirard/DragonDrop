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
    
    public DragonData(Texture img, float health, float armorBonus, float weaponBonus, float speedBonus)
    {
	m_image = img;
	m_goldBuyCost = (int)((armorBonus+weaponBonus+speedBonus)*5*health);
	m_goldDropCost = m_goldBuyCost/6;
	m_baseHealth = health;
	m_armorBonus = armorBonus;
	m_weaponBonus = weaponBonus;
	m_speedBonus = speedBonus;
    }
    
    public float computeDamageReduction(AttackerData attacker)
    {
	float dmgMod = 1;
	if (m_armorBonus > attacker.m_armor)
	    dmgMod++;
	else if (m_armorBonus < (attacker.m_armor - m_armorBonus/2))
	    dmgMod--;
	if (m_weaponBonus > attacker.m_weapon)
	    dmgMod++;
	else if (m_weaponBonus < (attacker.m_weapon - m_armorBonus/2))
	    dmgMod--;
	if (m_speedBonus > attacker.m_speed)
	    dmgMod++;
	else if (m_speedBonus < (attacker.m_speed - m_speedBonus/2))
	    dmgMod--;

	if (dmgMod > 0)
	    dmgMod = 1/dmgMod;
	else
	    dmgMod = -dmgMod + 2;
	
        return dmgMod;	
    }

}
