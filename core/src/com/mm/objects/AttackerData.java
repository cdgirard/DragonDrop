package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;

public class AttackerData
{
    public final Texture m_image;
    public final int m_goldValue;
    public final float m_impulse;
    public final float m_baseHealth;
    public final int m_goldSteals;
    
    public AttackerData(Texture img, int gold, float impulse, float health, int goldSteals)
    {
	m_image = img;
	m_goldValue = gold;
	m_impulse = impulse;
	m_baseHealth = health;
	m_goldSteals = goldSteals;
    }
}
