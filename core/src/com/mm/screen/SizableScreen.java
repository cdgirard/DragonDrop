package com.mm.screen;

import com.badlogic.gdx.Screen;

public abstract class SizableScreen implements Screen
{
    protected int preferredWidth, preferredHeight;
    
    public void resize()
    {
        resize(preferredWidth,preferredHeight);
    }
    

}
