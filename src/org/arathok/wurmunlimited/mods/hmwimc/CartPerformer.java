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

public class CartPerformer implements ActionPerformer {




        public ActionEntry actionEntry;


        public CartPerformer() {
            actionEntry = new ActionEntryBuilder((short) ModActions.getNextActionId(), "Generate Writ", "inscribing",
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



            if (source.getInscription()!=null)
            {
                performer.getCommunicator().sendSafeServerMessage("You try to draw up a schematic and the details about your cart, but you notice you already wrote something on this paper sheet.");
                return propagate(action,
                        ActionPropagation.FINISH_ACTION,
                        ActionPropagation.NO_SERVER_PROPAGATION,
                        ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);

            }       long wurmId=target.getWurmId();
                    String inscription = Long.toString(wurmId);
                    source.setInscription(inscription,performer.getName());
                    performer.getCommunicator().sendSafeServerMessage("You draw up the details of your cart including its name on the paper sheet. If you can get a priest to bless it you may always be able to find your cart by focussing on it, channeling it with your body.");
                    source.setName("Vehicle Writ:"+target.getName());





            return propagate(action,
                    ActionPropagation.FINISH_ACTION,
                    ActionPropagation.NO_SERVER_PROPAGATION,
                    ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        }

    }








