package com.mm.screen;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.mm.objects.AbstractGameObject;
import com.mm.objects.Attacker;
import com.mm.objects.DroppingDragon;

/**
 * Job of this class is to manage all the non-ui objects
 * in the game screen.
 * @author cdgira
 *
 */
public class GameScreenController
{
    public Array<DroppingDragon> m_droppingDragons;
    public Array<Attacker> m_attackers;
    public Array<AbstractGameObject> m_objectsToRemove;
    
    private OrthographicCamera m_gameCam;

}
