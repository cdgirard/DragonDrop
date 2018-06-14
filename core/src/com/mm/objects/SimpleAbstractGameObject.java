package com.mm.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public abstract class SimpleAbstractGameObject
{
    public Vector2 m_position;
    public Vector2 dimension;
    public Vector2 origin;
    public Vector2 scale;
    public float rotation;
    
    public SimpleAbstractGameObject()
    {
	m_position = new Vector2();
	dimension = new Vector2(1, 1);
	origin = new Vector2();
	scale = new Vector2(1, 1);
	rotation = 0;
    }
    
    public abstract void render(SpriteBatch batcher);

}
