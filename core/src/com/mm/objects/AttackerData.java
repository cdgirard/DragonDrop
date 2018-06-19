package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;

public class AttackerData
{
    public final Texture m_image;
    public final int m_gold;
    public final float m_impulse;
    public final float m_baseHealth;
    
    public AttackerData(Texture img, int gold, float impulse, float health)
    {
	m_image = img;
	m_gold = gold;
	m_impulse = impulse;
	m_baseHealth = health;
    }
}
