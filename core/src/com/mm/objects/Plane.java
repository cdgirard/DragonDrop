package com.mm.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mm.helpers.AssetLoader;

public class Plane
{
    TextureRegion m_image;
    int xLoc, yLoc;
    float m_runTime;
    
    public Plane()
    {
        m_image = AssetLoader.planesAtlas.findRegion("plane1");
        xLoc = 200;
        yLoc = 200;
        m_runTime = 0;
    }
    
    public void render(SpriteBatch batcher, float delta)
    {
        m_runTime += delta;
        if (m_runTime > 360)
        {
            m_runTime -= 360;
        }
        int xOffset = (int)(100*Math.sin(m_runTime/(2*Math.PI)));
        int yOffset = (int)(100*Math.cos(m_runTime/(2*Math.PI)));
        //batcher.draw(m_image, xLoc+xOffset, yLoc+yOffset);
        batcher.draw(m_image, xLoc+xOffset, yLoc+yOffset, m_image.getRegionWidth()/2, m_image.getRegionHeight()/2, 
                m_image.getRegionWidth(), m_image.getRegionHeight(), 1.0f, 1.0f, m_runTime);
       // batcher.draw(m_image.getTexture(),xLoc+xOffset, yLoc+yOffset, m_image.getRegionWidth()/2, m_image.getRegionHeight()/2, m_image.getRegionWidth(), m_image.getRegionHeight(),
       //         1.0f, 1.0f, 0.0f, xLoc, yLoc, m_image.getRegionWidth(), m_image.getRegionWidth(), false, true);
    }

}
