package com.mm.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.mm.objects.AttackerData;
import com.mm.objects.DragonData;

public class Globals
{
    public static AttackerData[] attackerTypes = {
	new AttackerData(Assets.assetManager.get(Assets.FARMER, Texture.class),10, -0.035f, 1, 5, 1, 2, 2),
	new AttackerData(Assets.assetManager.get(Assets.SOILDER, Texture.class),20, -0.042f, 2, 10, 3, 1, 1),
	new AttackerData(Assets.assetManager.get(Assets.CROSSBOW, Texture.class),25, -0.04f, 1, 15, 2, 4, 3),
	new AttackerData(Assets.assetManager.get(Assets.SPEARMAN, Texture.class),15, -0.035f, 2, 10, 2, 3, 2),
	new AttackerData(Assets.assetManager.get(Assets.KNIGHT, Texture.class),50, -0.062f, 4, 30, 4, 1, 4),
    };
    
    public static DragonData[] dragonTypes = {
	new DragonData(Assets.assetManager.get(Assets.GOTH_DRAGON, Texture.class),10, 30, 1, 2, 2, 2),
	new DragonData(Assets.assetManager.get(Assets.HAZY_DRAGON, Texture.class),10, 50, 2, 2, 2, 5),
	new DragonData(Assets.assetManager.get(Assets.BOOK_DRAGON, Texture.class),10, 100, 3, 5, 2, 2),
	new DragonData(Assets.assetManager.get(Assets.BLUE_DRAGON, Texture.class),10, 50, 2, 2, 2, 5),
	new DragonData(Assets.assetManager.get(Assets.MOOSE_DRAGON, Texture.class),10, 50, 2, 2, 2, 5),
	new DragonData(Assets.assetManager.get(Assets.FAIRY_DRAGON, Texture.class),10, 50, 2, 2, 2, 5),
	new DragonData(Assets.assetManager.get(Assets.DRAQ_DRAGON, Texture.class),10, 50, 2, 2, 2, 5),
	new DragonData(Assets.assetManager.get(Assets.BUTLER_DRAGON, Texture.class),10, 100, 3, 5, 2, 2)
    };

}
