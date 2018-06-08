package com.mm.screen.input;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.mm.DragonDrop;
import com.mm.helpers.Assets;
import com.mm.objects.Dragon;
import com.mm.screen.GameScreen;

/**
 * Gets first dibs on any input events that are to be processed,
 * if nothing affects one if it's items then it is passed on to the
 * InputAdapter (if any).  If it does process the event then nothing
 * is passed on to the InputAdapter.
 * @author cdgira
 *
 */
public class GameScreenInputHandler extends ChangeListener //implements InputProcessor
{
    private static GameScreenInputHandler theHandler = null;
    private GameScreen m_screen;

    public static String CANCEL_BUTTON = "newGameBtn";
    public static String QUIT_BUTTON = "quitBtn";
    public static String BLANK_BUTTON = "blankButton";
    public static String DOOR_BUTTON = "Door";
    public static String PAINTING_BUTTON = "greenBall";
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
	if (actor.getName().equals(QUIT_BUTTON))
	{
	    DragonDrop.m_dreamScape.setScreen(DragonDrop.MAIN_SCREEN);
	}
	if (actor.getName().equals(DOOR_BUTTON))
	{
	    Dragon hero = Dragon.getInstance();
	    hero.setNextPosition(500, 70);
	    m_screen.updateMessageLabel("Ooooo let's go look at the door.");
	}
	if (actor.getName().equals(LEFT_TREE_BUTTON))
	{
	    m_screen.updateMessageLabel("Cannot reach there - Maybe if I could fly.");

	}
	if (actor.getName().equals(PLANE_BUTTON))
	{

	}

	//System.out.println("Clicked! Is checked: " + actor.getName());


    }
}
