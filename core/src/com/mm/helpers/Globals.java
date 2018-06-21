package com.mm.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.mm.objects.AttackerData;
import com.mm.objects.AttackerData.AttackerArmor;
import com.mm.objects.AttackerData.AttackerSpeed;
import com.mm.objects.AttackerData.AttackerWeapon;
import com.mm.objects.DragonData;

public class Globals
{
    public static AttackerData[] attackerTypes = {
	new AttackerData(Assets.assetManager.get(Assets.FARMER, Texture.class),10, -0.035f, 1, 5,AttackerArmor.NO_ARMOR, AttackerWeapon.MEDIUM_RANGE, AttackerSpeed.SLOW),
	new AttackerData(Assets.assetManager.get(Assets.SOILDER, Texture.class),20, -0.042f, 2, 10,AttackerArmor.MEDIUM_ARMOR, AttackerWeapon.SHORT_RANGE, AttackerSpeed.SLOW),
	new AttackerData(Assets.assetManager.get(Assets.CROSSBOW, Texture.class),25, -0.04f, 1, 15,AttackerArmor.LIGHT_ARMOR, AttackerWeapon.MEDIUM_RANGE, AttackerSpeed.MEDIUM),
	new AttackerData(Assets.assetManager.get(Assets.SPEARMAN, Texture.class),15, -0.035f, 2, 10,AttackerArmor.LIGHT_ARMOR, AttackerWeapon.LONG_RANGE, AttackerSpeed.MEDIUM),
	new AttackerData(Assets.assetManager.get(Assets.KNIGHT, Texture.class),50, -0.062f, 4, 30,AttackerArmor.HEAVY_ARMOR, AttackerWeapon.MEDIUM_RANGE, AttackerSpeed.FAST),
    };
    
    public static DragonData[] dragonTypes = {
	new DragonData(Assets.assetManager.get(Assets.GOTH_DRAGON, Texture.class),10, 30, 1),
	new DragonData(Assets.assetManager.get(Assets.HAZY_DRAGON, Texture.class),10, 50, 2),
	new DragonData(Assets.assetManager.get(Assets.BOOK_DRAGON, Texture.class),10, 100, 3)
    };

}
