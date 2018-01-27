package com.mm.helpers;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets
{
    public static final String LOUNGE = "images/backgrounds/lounge.png";
    public static final String PAINTING = "images/items/painting.png";
    public static ArrayList<Texture> backgroundTextures = new ArrayList<Texture>();
    public static Texture heroTexture;
    
    public static TextureAtlas buttonsAtlas;
    public static TextureAtlas planesAtlas;
    public static Texture slotTexture;
    public static Texture ringItemTexture;
    
    public static AssetManager assetManager = new AssetManager();

    public static void load()
    {
        loadBackgrounds();
        loadGUIImages();
        loadHero();
        loadPlanes();
        
    }
    
    private static void loadHero()
    {
        heroTexture = new Texture(Gdx.files.internal("images/people/Hero.png"));
        heroTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
    }
    
    private static void loadGUIImages()
    {
        buttonsAtlas = new TextureAtlas(Gdx.files.internal("images/packed/buttons.atlas")); //** button atlas image **//
        
        slotTexture = new Texture(Gdx.files.internal("images/ui/item_slot.png"));
        slotTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        ringItemTexture = new Texture(Gdx.files.internal("images/items/ring.png"));
        ringItemTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        
        assetManager.load(PAINTING, Texture.class);     
        assetManager.finishLoading();
    }
    
    private static void loadPlanes()
    {
        planesAtlas = new TextureAtlas(Gdx.files.internal("images/packed/airplanes.atlas"));
    }
    
    private static void loadBackgrounds()
    {
        Texture texture = new Texture(Gdx.files.internal("images/backgrounds/DreamCrystal.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        backgroundTextures.add(texture);
        
        texture = new Texture(Gdx.files.internal("images/backgrounds/TheHero.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        backgroundTextures.add(texture);
        
        texture = new Texture(Gdx.files.internal("images/backgrounds/desertPlanet.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        backgroundTextures.add(texture);
        
        texture = new Texture(Gdx.files.internal("images/backgrounds/forest.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        backgroundTextures.add(texture);
        
        texture = new Texture(Gdx.files.internal("images/backgrounds/grassland.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        backgroundTextures.add(texture);
        
        texture = new Texture(Gdx.files.internal("images/backgrounds/roiidynne.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        backgroundTextures.add(texture);
        
        assetManager.load(LOUNGE, Texture.class);     
        assetManager.finishLoading();
        
    }
    
    
    public static void dispose()
    {
        // We must dispose of the texture when we are finished.
        for (Texture texture : backgroundTextures)
        {
            if (texture != null)
                texture.dispose();
        }
        assetManager.dispose();
    }

}
