package com.mm.screen.input;

import com.badlogic.gdx.graphics.Texture;
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
	if (actor == m_screen.btnWinOptCancel)
	{
	    m_screen.m_paused = false;
	    m_screen.winBuyDragon.setVisible(false);
	}
	else if (actor == m_screen.btnBuyGothDragon)
	{
	    m_screen.slots[m_screen.activeSlot].setDragon(0);
	    
	    m_screen.gold -= m_screen.slots[m_screen.activeSlot].getDragonData().m_goldBuyCost;
	    m_screen.m_paused = false;
	    m_screen.winBuyDragon.setVisible(false);
	    m_screen.activeSlot = -1;
	}
	else if (actor == m_screen.btnBuyHazyDragon)
	{
	    m_screen.slots[m_screen.activeSlot].setDragon(1);
	    
	    m_screen.gold -= m_screen.slots[m_screen.activeSlot].getDragonData().m_goldBuyCost;
	    m_screen.m_paused = false;
	    m_screen.winBuyDragon.setVisible(false);
	    m_screen.activeSlot = -1;
	}
	else if (actor == m_screen.btnBuyBookDragon)
	{
	    m_screen.slots[m_screen.activeSlot].setDragon(2);
	    
	    m_screen.gold -= m_screen.slots[m_screen.activeSlot].getDragonData().m_goldBuyCost;
	    m_screen.m_paused = false;
	    m_screen.winBuyDragon.setVisible(false);
	    m_screen.activeSlot = -1;
	}
	else if (actor.getName().equals(QUIT_BUTTON))
	{
	    DragonDrop.m_dreamScape.setScreen(DragonDrop.MAIN_SCREEN);
	}
	else if (actor.getName().contains(Assets.BUY_BTN))
	{
	    m_screen.updateMessageLabel("Bought a Dragon.");
	    m_screen.onBuyDragonClicked(actor.getName());
	}

	//System.out.println("Clicked! Is checked: " + actor.getName());


    }
}
