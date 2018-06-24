package com.mm.helpers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.mm.objects.AttackerData;
import com.mm.objects.DragonData;

public class Globals
{
    // Gold value equation: (2^(weapon-1)+2^(armor-1)+2^(speed-1))*health
    public static AttackerData[] attackerTypes = {
	new AttackerData(Assets.assetManager.get(Assets.FARMER, Texture.class), -0.035f, 1, 1, 2, 2),
	new AttackerData(Assets.assetManager.get(Assets.SOILDER, Texture.class), -0.042f, 2, 3, 1, 1),
	new AttackerData(Assets.assetManager.get(Assets.CROSSBOW, Texture.class), -0.04f, 1, 2, 4, 3),
	new AttackerData(Assets.assetManager.get(Assets.SPEARMAN, Texture.class), -0.035f, 2, 2, 3, 2),
	new AttackerData(Assets.assetManager.get(Assets.KNIGHT, Texture.class), -0.062f, 4, 4, 1, 4),
    };
    
    public static DragonData[] dragonTypes = {
	new DragonData(Assets.assetManager.get(Assets.GOTH_DRAGON, Texture.class), 1, 2, 3, 1),
	new DragonData(Assets.assetManager.get(Assets.HAZY_DRAGON, Texture.class), 2, 1, 1, 5),
	new DragonData(Assets.assetManager.get(Assets.BOOK_DRAGON, Texture.class), 3, 5, 1, 1),
	new DragonData(Assets.assetManager.get(Assets.BLUE_DRAGON, Texture.class), 1, 1, 5, 1),
	new DragonData(Assets.assetManager.get(Assets.MOOSE_DRAGON, Texture.class), 2, 3, 2, 2),
	new DragonData(Assets.assetManager.get(Assets.FAIRY_DRAGON, Texture.class), 3, 1, 2, 4),
	new DragonData(Assets.assetManager.get(Assets.DRAQ_DRAGON, Texture.class), 1, 4, 2, 1),
	new DragonData(Assets.assetManager.get(Assets.BUTLER_DRAGON, Texture.class), 2, 2, 3, 2)
    };
    
    public static int gold = 100;
    public static int score = 0;
    
    public static Array<HighScoreEntry> highScores = new Array<HighScoreEntry>();
    public static final int MAX_SCORES = 10;

    public static void sortHighScoreList()
    {
	for (int x=0;x<highScores.size-1;x++)
	{
	    int y = x+1;
	    int z = x;
	    while ((y > 0) && (highScores.get(y).score > highScores.get(z).score))
	    {
		HighScoreEntry entry = highScores.removeIndex(y);
		highScores.insert(z, entry);
		y--;
		z--;
	    }
	}
	
	while (highScores.size > MAX_SCORES)
	    highScores.removeIndex(MAX_SCORES);
    }

    public static void updateGold(int value)
    {
	gold += value;
    }

}
