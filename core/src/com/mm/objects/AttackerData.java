package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class AttackerData
{
    public final Texture m_image;
    public final float m_impulse;
    private final float m_baseHealth;
    private final float m_baseArmor;
    private final float m_baseWeapon;
    private final float m_baseSpeed;
    
    // Tracks experience for when to level up the Attacker
    public int m_experience;
    
    // Stats that are changed during gameplay
    public int m_goldValue;
    public int m_goldSteals;
    public float m_armor;
    public float m_weapon;
    public float m_speed;
    public float m_health;
    
    public AttackerData(Texture img, float impulse, float health, float armor, float weapon, float speed)
    {
	m_image = img;
	m_goldValue = (int)((Math.pow(2, armor-1) + Math.pow(2,weapon-1) + Math.pow(2,speed-1))*health*0.75);
	m_impulse = impulse;
	m_baseHealth = health;
	m_goldSteals = (m_goldValue*3) / 4;
	m_armor = m_baseArmor = armor;
	m_weapon = m_baseWeapon = weapon;
	m_speed = m_baseSpeed = speed;
    }
    
    public void resetStats()
    {
	m_experience = 0;
	m_armor = m_baseArmor;
	m_weapon = m_baseWeapon;
	m_speed = m_baseSpeed;
	m_health = m_baseHealth;
	m_goldValue = (int)((Math.pow(2, m_armor-1) + Math.pow(2,m_weapon-1) + Math.pow(2,m_speed-1))*m_health*0.75);
	m_goldSteals = (m_goldValue*3) / 4;
    }
    
    public void levelUp()
    {
	int stat = MathUtils.random(1, 3);
	switch (stat)
	{
	    case 1:
		m_armor += 0.2;
		break;
	    case 2:
		m_weapon += 0.2;
		break;
	    case 3:
		m_speed += 0.2;
		break;
	}
	m_goldValue = (int)((Math.pow(2, m_armor-1) + Math.pow(2,m_weapon-1) + Math.pow(2,m_speed-1))*m_health*0.75);
	m_goldSteals = (m_goldValue*3) / 4;
	m_experience -= 10;
    }
}
