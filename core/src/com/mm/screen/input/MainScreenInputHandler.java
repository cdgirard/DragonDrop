package com.mm.screen.input;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mm.DragonDrop;
import com.mm.helpers.Assets;
import com.mm.helpers.AudioManager;

/**
 * All user input should get routed into this class.  Setup as a Singleton.
 * @author cdgira
 *
 */
public class MainScreenInputHandler extends ChangeListener //implements InputProcessor
{
    private static MainScreenInputHandler theHandler = null;
    
    public static String START_NEW_GAME_BUTTON = "newGameBtn";
    public static String LOAD_GAME_BUTTON = "loadGameBtn";
    
    
    private MainScreenInputHandler()
    {
    }
    
    public static void initializeInstance()
    {
        theHandler = new MainScreenInputHandler();
    }
    
    public static MainScreenInputHandler getInstance()
    {
        return theHandler;
    }
    
    @Override
    public void changed(ChangeEvent event, Actor actor)
    {
        if (actor.getName().equals(START_NEW_GAME_BUTTON))
        {
            AudioManager.instance.stopMusic();
            DragonDrop.GAME_SCREEN.init();
            DragonDrop.m_dreamScape.setScreen(DragonDrop.GAME_SCREEN);
        }
        
        System.out.println("Clicked! Is checked: " + actor.getName());
        // button.setText("Good job!");
        
    }

}
