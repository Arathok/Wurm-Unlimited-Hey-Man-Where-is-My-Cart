package org.arathok.wurmunlimited.mods.hmwimc;

import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
import com.wurmonline.server.behaviours.*;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.endgames.EndGameItems;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.NotOwnedException;
import javafx.fxml.FXMLLoader;
import org.gotti.wurmunlimited.modsupport.actions.ActionEntryBuilder;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.ActionPropagation;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

public class TeleportCartPerformer implements ActionPerformer {


    public ActionEntry actionEntry;
    static HashMap<Long, Long> coolDowns;

    public TeleportCartPerformer() {
        actionEntry = new ActionEntryBuilder((short) ModActions.getNextActionId(), "Teleport Cart", "focusing",
                new int[]{
                        6 /* ACTION_TYPE_NOMOVE */,
                        48 /* ACTION_TYPE_ENEMY_ALWAYS */,
                        36 /* USE SOURCE AND TARGET */,

                }).range(4).build();

        ModActions.registerAction(actionEntry);

    }


    @Override
    public short getActionId() {
        return actionEntry.getNumber();
    }

    public static boolean canUse(Creature performer, Item source) throws NotOwnedException {
        return performer.isPlayer() && source.getOwner() == performer.getWurmId();
    }

    @Override
    public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {


        try {
            if (!canUse(performer, target)) {

                performer.getCommunicator().sendAlertServerMessage("You are not allowed to do that");
                return propagate(action,
                        ActionPropagation.FINISH_ACTION,
                        ActionPropagation.NO_SERVER_PROPAGATION,
                        ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);

            }
        } catch (NotOwnedException e) {
            throw new RuntimeException(e);
        }
        if (counter == 1.0F) {
            if (!(coolDowns.containsKey(performer.getWurmId())||coolDowns.get(performer.getWurmId())>System.currentTimeMillis()))
            {
                performer.getCommunicator().sendAlertServerMessage("you can use this ability only every "+(float)Config.coolDownTime/3600.0F+" hours.");
                return propagate(action,
                        ActionPropagation.FINISH_ACTION,
                        ActionPropagation.NO_SERVER_PROPAGATION,
                        ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            if (source.getBless() == null) {
                performer.getCommunicator().sendSafeServerMessage("You try to focus on your target but somehow you can't see your cart through the veil of reality. You realize that your papersheet must be enchanted");
                return propagate(action,
                        ActionPropagation.FINISH_ACTION,
                        ActionPropagation.NO_SERVER_PROPAGATION,
                        ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }

            if (source.getInscription() == null) {
                performer.getCommunicator().sendSafeServerMessage("As you try to focus on the target, you see that your paper sheet is not inscribed. As you focus to try to find nothing, you realize you will find nothing everywhere.");
                return propagate(action,
                        ActionPropagation.FINISH_ACTION,
                        ActionPropagation.NO_SERVER_PROPAGATION,
                        ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            }
            action.setTimeLeft(Config.actionSeconds*10);
            performer.sendActionControl("focusing", true, Config.actionSeconds*10);
            return propagate(action,
                    ActionPropagation.CONTINUE_ACTION,
                    ActionPropagation.NO_SERVER_PROPAGATION,
                    ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        } else if (action.currentSecond() == Config.actionSeconds) {

            Optional<Item> cart;
            long cartId = 0L;


            cartId = Long.parseLong(source.getInscription().getInscription());


            cart = Items.getItemOptional(cartId);
            Item realcart;
            if (!cart.isPresent()) {
                performer.getCommunicator().sendAlertServerMessage("This cart seems to be non existant! (Inscription was null!)");
                return propagate(action,
                        ActionPropagation.FINISH_ACTION,
                        ActionPropagation.NO_SERVER_PROPAGATION,
                        ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
            } else {
                 realcart = cart.get();
            }
            realcart.setPosXYZ(performer.getPosX(), performer.getPosY(), performer.getPositionZ());
            Vehicle cartVehicle = Vehicles.getVehicle(realcart);
            Set<Creature> draggers = cartVehicle.getDraggers();
            for (Creature oneCreature : draggers) {
                oneCreature.setVisible(false);
                oneCreature.setVisible(true);
            }
            long runOutTime=System.currentTimeMillis()+Config.coolDownTime;

            coolDowns.put (performer.getWurmId(),runOutTime);
            Config.addToDB(performer,runOutTime);

            return propagate(action,
                    ActionPropagation.FINISH_ACTION,
                    ActionPropagation.NO_SERVER_PROPAGATION,
                    ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);

        }
        return propagate(action,
                ActionPropagation.CONTINUE_ACTION,
                ActionPropagation.NO_SERVER_PROPAGATION,
                ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
    }
}











