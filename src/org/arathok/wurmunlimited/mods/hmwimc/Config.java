package org.arathok.wurmunlimited.mods.hmwimc;

import com.wurmonline.server.creatures.Creature;
import org.gotti.wurmunlimited.modsupport.ModSupportDb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class Config {
    public static int actionSeconds = 120;
    public static long coolDownTime = 86400;

    public static void addToDB(Creature performer, long runOutTime) {
        Connection dbConn= ModSupportDb.getModSupportDb();
        PreparedStatement ps = null;
        try {
            ps = dbConn.prepareStatement("insert or update into HMWIMCCooldowns (playerId,canUseAgainAt) values (?,?)");

        ps.setLong(1,performer.getWurmId());
        ps.setLong(2,runOutTime);
        } catch (SQLException e) {
            HMWIMC.logger.log(Level.WARNING,"could not write to the db!",e);
            throw new RuntimeException(e);
        }

    }

    public static void readFromDb(Connection dbConn) {

        PreparedStatement ps;
        try {
            ps = dbConn.prepareStatement("SELECT * FROM HMWIMCCooldowns");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long playerId = rs.getLong("playerId");
                long coolDownEnd = rs.getLong("canUseAgainAt"); // liest quasi den Wert von der Spalte
                TeleportCartPerformer.coolDowns.put(playerId,coolDownEnd);

            }
        } catch (SQLException e) {
            HMWIMC.logger.log(Level.WARNING,"could not read from the db!",e);
            throw new RuntimeException(e);
        }

    }

}

