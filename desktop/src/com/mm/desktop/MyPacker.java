package com.mm.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

public class MyPacker
{
    public static void main(String[] args) throws Exception
    {
        Settings settings = new Settings();
        settings.pot = false;
        TexturePacker.process(settings,"../core/assets/images/tmp/","../core/assets/images/packed", "buttons");
        //"../android/assets/data/tiles/world/ground"
        //
    }
}
