package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;

public class AttackerData
{
    public final Texture m_image;
    public final int m_goldValue;
    public final float m_impulse;
    public final float m_baseHealth;
    public final int m_goldSteals;
    public final float m_armor;
    public final float m_weapon;
    public final float m_speed;
    
    public AttackerData(Texture img, float impulse, float health, float armor, float weapon, float speed)
    {
	m_image = img;
	m_goldValue = (int)((Math.pow(2, armor-1) + Math.pow(2,weapon-1) + Math.pow(2,speed-1))*health*0.75);
	m_impulse = impulse;
	m_baseHealth = health;
	m_goldSteals = 0;//(m_goldValue*3) / 4;
	m_armor = armor;
	m_weapon = weapon;
	m_speed = speed;
    }
}
