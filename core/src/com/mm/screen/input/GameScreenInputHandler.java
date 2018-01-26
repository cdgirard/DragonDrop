package com.mm.screen.input;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mm.MurderMystery;
import com.mm.objects.Hero;
import com.mm.screen.GameScreen;

public class GameScreenInputHandler extends ChangeListener //implements InputProcessor
{
    private static GameScreenInputHandler theHandler = null;
    private GameScreen m_screen;
    
    public static String CANCEL_BUTTON = "newGameBtn";
    public static String BLANK_BUTTON = "blankButton";
    public static String TREE_BUTTON = "Tree";
    public static String GREEN_BALL_BUTTON = "greenBall";
    public static String LEFT_TREE_BUTTON = "leftTree";
    public static String PLANE_BUTTON = "planeButton";
    
    private GameScreenInputHandler(GameScreen screen)
    {
        m_screen = screen;
    }
    
    public static void initializeInstance(GameScreen screen)
    {
        theHandler = new GameScreenInputHandler(screen);
    }
    
    public static GameScreenInputHandler getInstance()
    {
        return theHandler;
    }
    
    @Override
    public void changed(ChangeEvent event, Actor actor)
    {
        if (actor.getName().equals(CANCEL_BUTTON))
        {
            MurderMystery.m_dreamScape.setScreen(MurderMystery.MAIN_SCREEN);
        }
        if (actor.getName().equals(TREE_BUTTON))
        {
           Hero hero = Hero.getInstance();
           hero.setNextPosition(350,100);
           hero.setNextRelativeSize(0.60f);
           
        }
        if (actor.getName().equals(GREEN_BALL_BUTTON))
        {
           m_screen.updateMessageLabel("Cannot reach there - Maybe if I could fly.");
        }
        if (actor.getName().equals(LEFT_TREE_BUTTON))
        {
            Hero hero = Hero.getInstance();
            if ((hero.getX() == 100) &&  (hero.getY() == 100))
            {
                m_screen.updateUI();
            }
            else
            {
                hero.setNextPosition(100,100);
                hero.setNextRelativeSize(0.30f);
            }
        }
        if (actor.getName().equals(PLANE_BUTTON))
        {
            
        }
        
        System.out.println("Clicked! Is checked: " + actor.getName());
        // button.setText("Good job!");
        
    }
}
