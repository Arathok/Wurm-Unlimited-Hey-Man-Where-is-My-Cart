package org.arathok.wurmunlimited.mods.hmwimc;

import com.wurmonline.server.Items;
import com.wurmonline.server.behaviours.Action;
import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.behaviours.MethodsCreatures;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.endgames.EndGameItems;
import com.wurmonline.server.items.Item;
import org.gotti.wurmunlimited.modsupport.actions.ActionEntryBuilder;
import org.gotti.wurmunlimited.modsupport.actions.ActionPerformer;
import org.gotti.wurmunlimited.modsupport.actions.ActionPropagation;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;
import com.wurmonline.server.questions.Question;
import java.util.Properties;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.logging.Level;

import static org.gotti.wurmunlimited.modsupport.questions.ModQuestions.getBmlHeader;

public class Performer implements ActionPerformer {


        public ActionEntry actionEntry;


        public Performer() {
            actionEntry = new ActionEntryBuilder((short) ModActions.getNextActionId(), "Search Cart", "searching",
                    new int[]{
                            6 /* ACTION_TYPE_NOMOVE */,
                            48 /* ACTION_TYPE_ENEMY_ALWAYS */,
                            36 /* DONT CARE WHETHER SOURCE OR TARGET */,

                    }).range(4).build();

            ModActions.registerAction(actionEntry);

        }



        @Override
        public short getActionId() {
            return actionEntry.getNumber();
        }

        public static boolean canUse(Creature performer, Item target) {
            return performer.isPlayer() && !target.getTemplate().getName().contains ("body");
        }

        @Override
        public boolean action(Action action, Creature performer, Item source, Item target, short num, float counter) {


            if (!canUse(performer, target)) {
                performer.getCommunicator().sendAlertServerMessage("You are not allowed to do that");
                return propagate(action,
                        ActionPropagation.FINISH_ACTION,
                        ActionPropagation.NO_SERVER_PROPAGATION,
                        ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);

            }

            if (source.getBless()==null)
            {
                performer.getCommunicator().sendSafeServerMessage("You try to focus on your target but somehow you can't see your cart through the veil of reality. You realize that your papersheet must be enchanted");

            }

            if (source.getInscription()==null)
            {
                performer.getCommunicator().sendSafeServerMessage("As you try to focus on the target, you see that your paper sheet is not inscribed. As you focus to try to find nothing, you realize you will find nothing everywhere.");

            }
            Item cart = null;
            String cartName="";
            Boolean ok=false;
            if (source.getInscription()!=null) {
                cartName = source.getInscription().getInscription();
            }
            for (Item oneItem : Items.getAllItems())
                if (oneItem.isVehicle())
                {
                    if (oneItem.getName().equals(cartName)&&oneItem.getOwnerId()==performer.getWurmId())
                    {
                        ok = true;
                        cart = oneItem;
                        break;

                    }
                }

            try {
                if (ok) {
                    int xDistance = Math.abs(performer.getTileX() - cart.getDataX());
                    int yDistance = Math.abs(performer.getTileY() - cart.getDataY());
                    int distance = (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
                    int direction = MethodsCreatures.getDir(performer, cart.getDataX(), cart.getDataY());

                    performer.getCommunicator().sendNormalServerMessage(
                            EndGameItems.getDistanceString(
                                    distance,
                                    cartName,
                                    MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), direction, "you"),
                                    true));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return propagate(action,
                    ActionPropagation.FINISH_ACTION,
                    ActionPropagation.NO_SERVER_PROPAGATION,
                    ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }

    }




