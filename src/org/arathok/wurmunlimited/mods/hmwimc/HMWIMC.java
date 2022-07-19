package org.arathok.wurmunlimited.mods.hmwimc;

import com.wurmonline.server.creatures.Communicator;
import org.gotti.wurmunlimited.modloader.interfaces.*;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.Properties;

public class HMWIMC implements WurmServerMod, Initable, PreInitable, Configurable, ItemTemplatesCreatedListener, ServerStartedListener, ServerPollListener, PlayerMessageListener
{

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
        ModActions.registerBehaviourProvider(new Behaviour());
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
