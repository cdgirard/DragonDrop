package com.mm.objects;

import com.badlogic.gdx.graphics.Texture;

public class DragonData
{
    public Texture m_image;
    public int m_goldDropCost = 10;
    public int m_goldBuyCost = 10;
    
    public DragonData(Texture img, int goldDropCost, int goldBuyCost)
    {
	m_image = img;
	m_goldDropCost = goldDropCost;
	m_goldBuyCost = goldBuyCost;
    }

}
