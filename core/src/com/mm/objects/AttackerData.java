package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;

public class AttackerData
{
    public Texture m_image;
    public int m_gold = 10;
    public float m_impulse = -6.0f;
    
    public AttackerData(Texture img, int gold, float impulse)
    {
	m_image = img;
	m_gold = gold;
	m_impulse = impulse;
    }
}
