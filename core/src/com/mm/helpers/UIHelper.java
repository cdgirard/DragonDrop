package com.mm.helpers;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;

public class UIHelper
{
    private static Skin buttonSkin = new Skin();
    
    public static ImageButton constructBlankButton(String name, int width, int height)
    {
        //Texture blankTexture = new Texture(width,height,Pixmap.Format.RGBA8888);
        Texture blankTexture = new Texture(width,height,Pixmap.Format.RGB888);  // Uncomment to see the buttons
        buttonSkin.add(name,blankTexture);
        return constructButton(name);
    }
    
    /**
     * Constructs a button but does not attach it to any listeners or
     * stages.
     * 
     * @param name
     * @return
     */
    public static ImageButton constructButton(String name)
    {
        ImageButtonStyle imgButtonStyle = new ImageButtonStyle();
        imgButtonStyle.up = buttonSkin.newDrawable(name);
        imgButtonStyle.down = buttonSkin.newDrawable(name);
        imgButtonStyle.checked = buttonSkin.newDrawable(name);
        imgButtonStyle.over = buttonSkin.newDrawable(name);
        ImageButton button = new ImageButton(imgButtonStyle);
        button.setName(name);
        return button;
    }
    
    public static void addRegions(TextureAtlas atlas)
    {
	buttonSkin.addRegions(atlas);
    }
    
    /**
     * Used to reset the buttonSkin so it doesn't get filled up with
     * skins from screens no longer active.
     */
    public static void resetSkin()
    {
	buttonSkin.dispose();
	buttonSkin = new Skin();
    }

}
