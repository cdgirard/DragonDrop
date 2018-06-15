package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;

public class AttackerData
{
    public Texture m_image;
    public int m_gold = 10;
    
    public AttackerData(Texture img, int gold)
    {
	m_image = img;
	m_gold = gold;
    }
}
