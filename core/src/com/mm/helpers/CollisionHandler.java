package com.mm.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.ObjectMap;
import com.mm.objects.AbstractGameObject;
import com.mm.objects.Attacker;
import com.mm.objects.DroppingDragon;
import com.mm.screen.GameScreen;

public class CollisionHandler implements ContactListener
{
    private ObjectMap<Short, ObjectMap<Short, ContactListener>> listeners;

    private GameScreen m_screen;

    public CollisionHandler(GameScreen w)
    {
	m_screen = w;
	listeners = new ObjectMap<Short, ObjectMap<Short, ContactListener>>();
    }

    public void addListener(short categoryA, short categoryB, ContactListener listener)
    {
	addListenerInternal(categoryA, categoryB, listener);
	addListenerInternal(categoryB, categoryA, listener);
    }

    @Override
    public void beginContact(Contact contact)
    {
	Fixture fixtureA = contact.getFixtureA();
	Fixture fixtureB = contact.getFixtureB();

	//Gdx.app.log("CollisionHandler-begin A", "begin");

	ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
	if (listener != null)
	{
	    listener.beginContact(contact);
	}
    }

    @Override
    public void endContact(Contact contact)
    {
	Fixture fixtureA = contact.getFixtureA();
	Fixture fixtureB = contact.getFixtureB();

	//Gdx.app.log("CollisionHandler-end A", "end");

	// Gdx.app.log("CollisionHandler-end A", fixtureA.getBody().getLinearVelocity().x+" : "+fixtureA.getBody().getLinearVelocity().y);
	// Gdx.app.log("CollisionHandler-end B", fixtureB.getBody().getLinearVelocity().x+" : "+fixtureB.getBody().getLinearVelocity().y);
	ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
	if (listener != null)
	{
	    listener.endContact(contact);
	}
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold)
    {
	//Gdx.app.log("CollisionHandler-preSolve A", "preSolve");
	Fixture fixtureA = contact.getFixtureA();
	Fixture fixtureB = contact.getFixtureB();
	ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
	if (listener != null)
	{
	    listener.preSolve(contact, oldManifold);
	}
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse)
    {
	//Gdx.app.log("CollisionHandler-postSolve A", "postSolve");
	Fixture fixtureA = contact.getFixtureA();
	Fixture fixtureB = contact.getFixtureB();

	processContact(contact);

	ContactListener listener = getListener(fixtureA.getFilterData().categoryBits, fixtureB.getFilterData().categoryBits);
	if (listener != null)
	{
	    listener.postSolve(contact, impulse);
	}
    }

    private void addListenerInternal(short categoryA, short categoryB, ContactListener listener)
    {
	ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
	if (listenerCollection == null)
	{
	    listenerCollection = new ObjectMap<Short, ContactListener>();
	    listeners.put(categoryA, listenerCollection);
	}
	listenerCollection.put(categoryB, listener);
    }

    private ContactListener getListener(short categoryA, short categoryB)
    {
	ObjectMap<Short, ContactListener> listenerCollection = listeners.get(categoryA);
	if (listenerCollection == null)
	{
	    return null;
	}
	return listenerCollection.get(categoryB);
    }

    private void processContact(Contact contact)
    {
	//Gdx.app.log("CollisionHandler-process A", "process");
	Fixture fixtureA = contact.getFixtureA();
	Fixture fixtureB = contact.getFixtureB();
	AbstractGameObject objA = (AbstractGameObject) fixtureA.getBody().getUserData();
	AbstractGameObject objB = (AbstractGameObject) fixtureB.getBody().getUserData();

	if ((objA instanceof DroppingDragon) && (objB instanceof Attacker))
	{
	    processDragonContact(fixtureA, fixtureB);
	}
	else if ((objB instanceof DroppingDragon) && (objA instanceof Attacker))
	{
	    processDragonContact(fixtureB, fixtureA);
	}
    }

    private void processDragonContact(Fixture dragonFixture, Fixture attackerFixture)
    {
	

	AudioManager.instance.play(Assets.assetManager.get(Assets.DRAGON_COLLISION, Sound.class));

	Attacker attacker = (Attacker) attackerFixture.getBody().getUserData();
	DroppingDragon dragon = (DroppingDragon) dragonFixture.getBody().getUserData();
	float damageReduction = dragon.m_data.computeDamageReduction(attacker.myData);
	attacker.health -= 1;
	dragon.health -= 1 * damageReduction;
	if (attacker.health <= 0)
	{
	    Globals.score += attacker.myData.m_goldValue;
	    Globals.updateGold(attacker.myData.m_goldValue);
	    m_screen.controller.flagForRemoval(attacker);
	}

	if (dragon.health <= 0)
	{
	    m_screen.controller.flagForRemoval(dragon);
	}
    }

}