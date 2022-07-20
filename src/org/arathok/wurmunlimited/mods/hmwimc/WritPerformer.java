package org.arathok.wurmunlimited.mods.hmwimc;

import com.wurmonline.server.Items;
import com.wurmonline.server.NoSuchItemException;
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

public class WritPerformer implements ActionPerformer {


    public ActionEntry actionEntry;


    public WritPerformer() {
        actionEntry = new ActionEntryBuilder((short) ModActions.getNextActionId(), "Search Cart", "searching",
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
        return performer.isPlayer() && !target.getTemplate().getName().contains("body");
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
        Item cart = null;
        long cartId = 0L;


        cartId = Long.parseLong(source.getInscription().getInscription());


        try {
            cart = Items.getItem(cartId);

            int xDistance = Math.abs(performer.getTileX() - cart.getDataX());
            int yDistance = Math.abs(performer.getTileY() - cart.getDataY());
            int distance = (int) Math.sqrt(xDistance * xDistance + yDistance * yDistance);
            int direction = MethodsCreatures.getDir(performer, cart.getDataX(), cart.getDataY());

            performer.getCommunicator().sendNormalServerMessage(
                    EndGameItems.getDistanceString(
                            distance,
                            cart.getName(),
                            MethodsCreatures.getLocationStringFor(performer.getStatus().getRotation(), direction, "you"),
                            true));
            float damage = Math.min(100f, Math.max(0.15f,
                    new java.util.Random().nextFloat()));

            // rare, supreme, and fantastic maps take 10%, 20%, and
            // 30% less damage respectively.
            damage = damage * (1f - target.getRarity() / 10f);

            performer.getCommunicator().sendNormalServerMessage("Seeing through the veil of reality wears out your vehicles Writ a bit.");

            if (source.setDamage(source.getDamage() + damage, true)) {
                Items.destroyItem(target.getWurmId());
                performer.getCommunicator().sendNormalServerMessage("The vehicle writ is in such a bad shape, that you cant even see the drawing and its magical power is lost. The writ crumbles to dust.");

            }
            return propagate(action,
                    ActionPropagation.FINISH_ACTION,
                    ActionPropagation.NO_SERVER_PROPAGATION,
                    ActionPropagation.NO_ACTION_PERFORMER_PROPAGATION);
        } catch (NoSuchItemException e) {
            throw new RuntimeException(e);
        }

    }
}




