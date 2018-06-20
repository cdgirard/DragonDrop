package com.mm.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.mm.objects.AttackerData;
import com.mm.objects.DragonData;

public class Globals
{
    public static AttackerData[] attackerTypes = {
	new AttackerData(Assets.assetManager.get(Assets.FARMER, Texture.class),10, -0.08f, 1, 5),
	new AttackerData(Assets.assetManager.get(Assets.SOILDER, Texture.class),20, -0.095f, 2, 10),
	new AttackerData(Assets.assetManager.get(Assets.CROSSBOW, Texture.class),25, -0.09f, 1, 15),
	new AttackerData(Assets.assetManager.get(Assets.SPEARMAN, Texture.class),15, -0.08f, 2, 10),
	new AttackerData(Assets.assetManager.get(Assets.KNIGHT, Texture.class),50, -0.14f, 4, 30),
    };
    
    public static DragonData[] dragonTypes = {
	new DragonData(Assets.assetManager.get(Assets.GOTH_DRAGON, Texture.class),10, 30, 1),
	new DragonData(Assets.assetManager.get(Assets.HAZY_DRAGON, Texture.class),10, 50, 2),
	new DragonData(Assets.assetManager.get(Assets.BOOK_DRAGON, Texture.class),10, 100, 3)
    };

}
