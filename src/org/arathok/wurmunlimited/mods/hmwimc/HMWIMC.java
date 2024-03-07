package org.arathok.wurmunlimited.mods.hmwimc;

import com.wurmonline.server.creatures.Communicator;
import org.gotti.wurmunlimited.modloader.interfaces.*;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HMWIMC implements WurmServerMod, Initable, PreInitable, Configurable, ItemTemplatesCreatedListener, ServerStartedListener, ServerPollListener, PlayerMessageListener
{
 public static final Logger logger=Logger.getLogger("HMWIMC");
    private boolean readDbs=false;
    private Connection dbConn;

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
        if (!readDbs) {
            dbConn = ModSupportDb.getModSupportDb();
            logger.log(Level.INFO, "Creating Databases");
            try {
                if (!ModSupportDb.hasTable(dbConn, "HMWIMCCooldowns")) {
                    // table create
                    try (PreparedStatement ps = dbConn.prepareStatement("CREATE TABLE HMWIMCCooldowns (playerId LONG PRIMARY KEY NOT NULL DEFAULT 0,LONG canUseAgainAt)")) {
                        ps.execute();
                    } catch (SQLException e) {
                        logger.log(Level.WARNING, "Could not create Table HMWIMCCooldowns!");
                        throw new RuntimeException(e);
                    }

                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Config.readFromDb(dbConn);
            readDbs = true;

        }
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
