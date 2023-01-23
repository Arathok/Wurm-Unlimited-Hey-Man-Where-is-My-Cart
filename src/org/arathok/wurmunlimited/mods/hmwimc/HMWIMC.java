package org.arathok.wurmunlimited.mods.hmwimc;

import com.wurmonline.server.creatures.Communicator;
import org.gotti.wurmunlimited.modloader.interfaces.*;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HMWIMC implements WurmServerMod, Initable, PreInitable, Configurable, ItemTemplatesCreatedListener, ServerStartedListener, ServerPollListener, PlayerMessageListener
{
 public static Logger logger;
    @Override
    public void configure(Properties properties) {

    }

    @Override
    public void onItemTemplatesCreated() {

    }

    @Override
    public boolean onPlayerMessage(Communicator communicator, String s) {
        return false;
    }

    @Override
    public void onServerPoll() {

    }

    @Override
    public void onServerStarted() {
        ModActions.registerBehaviourProvider(new WritBehaviour());
        ModActions.registerBehaviourProvider(new CartBehaviour());
        logger.log(Level.INFO,"Hey man, where is my cart? Is done Loading! Many thanks to the entire Wurm Coding Community!");
    }

    @Override
    public void init() {
        WurmServerMod.super.init();
    }

    @Override
    public void preInit() {
        WurmServerMod.super.preInit();
    }
}
