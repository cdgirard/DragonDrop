package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;

public class AttackerData
{
	public enum AttackerArmor {NO_ARMOR, LIGHT_ARMOR, MEDIUM_ARMOR, HEAVY_ARMOR};
	public enum AttackerWeapon {SHORT_RANGE, MEDIUM_RANGE, LONG_RANGE};
	public enum AttackerSpeed {SLOW, MEDIUM, FAST};
    public final Texture m_image;
    public final int m_goldValue;
    public final float m_impulse;
    public final float m_baseHealth;
    public final int m_goldSteals;
    public final AttackerArmor m_armor;
    public final AttackerWeapon m_weapon;
    public final AttackerSpeed m_speed;
    
    public AttackerData(Texture img, int gold, float impulse, float health, int goldSteals, AttackerArmor armorType, AttackerWeapon weaponType, AttackerSpeed speed)
    {
	m_image = img;
	m_goldValue = gold;
	m_impulse = impulse;
	m_baseHealth = health;
	m_goldSteals = goldSteals;
	m_armor = armorType;
	m_weapon = weaponType;
	m_speed = speed;
    }
}
