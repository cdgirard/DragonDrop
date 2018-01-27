package com.mm.screen.input;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mm.helpers.Assets;
import com.mm.screen.StoryScreen;

public class StoryScreenInputHandler extends ChangeListener //implements InputProcessor
{
    private static StoryScreenInputHandler theHandler = null;
    
    public String stories[];
    
    public static String NEXT_BUTTON = "nextBtn";
    
    public int imageNum = 5;
    
    private StoryScreen m_screen;
    
    private StoryScreenInputHandler(StoryScreen screen)
    {
        m_screen = screen;
        stories = new String[4];
        fillStories();
    }
    
    private void fillStories()
    {
        stories[3] = "The Roiidynne for the ????? System.  A creature that sees\n"+
                     "all that goes on and sends out warnings or in some rare cases\n"+
                     "people to help where there is distress.";
        stories[0] = "The planet ???, a desert world covered in a stone precious every\n"+
                     "where except on the planet where it is found.  There is a horrible\n"+
                     "disease spreading through the population of the planet.  ???? has\n"+
                     "been sent by the Roiidynne to bring a cure to the people of the planet.";
        stories[1] = "Really cool looking forest world, perhaps a stop to create the cure?";
        stories[2] = "Another cool planet, perhaps another stop to create the cure?\n"+
                     "On this planet the people have realized that the two stars in the\n"+
                     "system are about to explode and are trying to contact the Roiidynne\n"+
                     "for help by creating special airplanes that ride the air currents of\n"+
                     "the world into space.";
    }
    
    public static void initializeInstance(StoryScreen screen)
    {
        theHandler = new StoryScreenInputHandler(screen);
    }
    
    public static StoryScreenInputHandler getInstance()
    {
        return theHandler;
    }
    
    @Override
    public void changed(ChangeEvent event, Actor actor)
    {
        if (actor.getName().equals(NEXT_BUTTON))
        {
            imageNum++;
            if (imageNum > 5)
                imageNum = 2;
            
            m_screen.setBackgroundImage(Assets.backgroundTextures.get(imageNum));

            //DreamScape.m_dreamScape.setScreen(DreamScape.MAIN_SCREEN);
        }

        
        System.out.println("Clicked! Is checked: " + actor.getName());
        // button.setText("Good job!");
        
    }
}
