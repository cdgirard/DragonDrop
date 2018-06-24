package com.mm.helpers;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets
{
    // Backgrounds
    public static final String MAIN_SCREEN = "images/backgrounds/dragon_drop_main.png";
    public static final String GAME_SCREEN = "images/backgrounds/dragon_drop_game.png";
    
    // Attackers
    public static final String SOILDER = "images/attackers/soilder.png";
    public static final String CROSSBOW = "images/attackers/crossbow.png";
    public static final String SPEARMAN = "images/attackers/spearman.png";
    public static final String KNIGHT = "images/attackers/knight.png";
    public static final String FARMER = "images/attackers/farmer.png";
    public static final String SPEED_DOT = "images/attackers/speed_dot.png";
    public static final String WEAPON_DOT = "images/attackers/weapon_dot.png";
    public static final String ARMOR_DOT = "images/attackers/armor_dot.png";
    
    // Dragons
    public static final String GOTH_DRAGON = "images/dragons/goth_dragon.png";
    public static final String HAZY_DRAGON = "images/dragons/hazy_dragon.png";
    public static final String BOOK_DRAGON = "images/dragons/book_dragon.png";
    public static final String FAIRY_DRAGON = "images/dragons/fairy_dragon.png";
    public static final String ORANGE_DRAGON = "images/dragons/orange_dragon.png";
    public static final String BLUE_DRAGON = "images/dragons/blue_dragon.png";
    public static final String MOOSE_DRAGON = "images/dragons/moose_dragon.png";
    public static final String DRAQ_DRAGON = "images/dragons/draq_dragon.png";
    public static final String BUTLER_DRAGON = "images/dragons/butler_dragon.png";
    
    // Landscape
    public static final String MOUNTAIN = "images/landscape/mountain.png";
    
    // UI
    public static final String DRAGON_SLOT = "images/ui/dragon_slot.png";
    public static final String BUY_BTN = "images/ui/buy_btn.png";
    public static final String SELL_BTN = "images/ui/sell_btn.png";
    public static final String GOLD_COIN = "images/ui/gold_coin.png";
    public static TextureAtlas buttonsAtlas;
    
    // Sounds                                         
    public static final String DRAGON_COLLISION = "sounds/artillery_explosion.wav";
    
    // Music
    public static final String INTRO_MUSIC = "music/jd_nighthawk.mp3";
    
    public static AssetManager assetManager = new AssetManager();
    

    public static void load()
    {
        loadBackgrounds();
        loadGUIImages();
        loadDragons();
        loadAttackers();
        loadLandscape();
        loadSounds();
        loadMusic();
    }
    
    private static void loadGUIImages()
    {
        buttonsAtlas = new TextureAtlas(Gdx.files.internal("images/packed/buttons.atlas")); //** button atlas image **//
       
        assetManager.load(DRAGON_SLOT, Texture.class);  
        assetManager.load(BUY_BTN, Texture.class);
        assetManager.load(SELL_BTN, Texture.class);
        assetManager.load(GOLD_COIN, Texture.class);
        assetManager.finishLoading();
    }
    
    private static void loadLandscape()
    {
	assetManager.load(MOUNTAIN, Texture.class);
	assetManager.finishLoading();
    }
    
    private static void loadAttackers()
    {
	assetManager.load(SOILDER, Texture.class); 
	assetManager.load(CROSSBOW, Texture.class); 
	assetManager.load(SPEARMAN, Texture.class); 
	assetManager.load(KNIGHT, Texture.class);
	assetManager.load(FARMER, Texture.class);
	assetManager.load(SPEED_DOT, Texture.class);
	assetManager.load(WEAPON_DOT, Texture.class);
	assetManager.load(ARMOR_DOT, Texture.class);
        assetManager.finishLoading();
    }
    
    private static void loadDragons()
    {
	assetManager.load(GOTH_DRAGON, Texture.class); 
	assetManager.load(HAZY_DRAGON, Texture.class);
	assetManager.load(BOOK_DRAGON, Texture.class);
	assetManager.load(BUTLER_DRAGON, Texture.class);
	assetManager.load(BLUE_DRAGON, Texture.class);
	assetManager.load(DRAQ_DRAGON, Texture.class);
	assetManager.load(ORANGE_DRAGON, Texture.class);
	assetManager.load(FAIRY_DRAGON, Texture.class);
	assetManager.load(MOOSE_DRAGON, Texture.class);
        assetManager.finishLoading();
    }
    
    private static void loadBackgrounds()
    {   
        assetManager.load(GAME_SCREEN, Texture.class);             
        assetManager.load(MAIN_SCREEN, Texture.class);     
        assetManager.finishLoading();
        
    }
    
    private static void loadSounds()
    {
	// Load Sounds
	assetManager.load(DRAGON_COLLISION, Sound.class);
	assetManager.finishLoading();
    }
    
    private static void loadMusic()
    {
	assetManager.load(INTRO_MUSIC, Music.class);
	assetManager.finishLoading();
    }
    
    
    public static void dispose()
    {
        buttonsAtlas.dispose();
        assetManager.dispose();
    }

}
