package org.arathok.wurmunlimited.mods.hmwimc;

import com.wurmonline.server.behaviours.ActionEntry;
import com.wurmonline.server.creatures.Creature;
import com.wurmonline.server.items.Item;
import com.wurmonline.server.items.ItemList;
import org.gotti.wurmunlimited.modsupport.actions.BehaviourProvider;
import org.gotti.wurmunlimited.modsupport.actions.ModActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Behaviour

       implements BehaviourProvider {

        private final List<ActionEntry> searchCart;
        private final Performer searchPerformer;

        public Behaviour() {
            this.searchPerformer = new Performer();
            this.searchCart = Collections.singletonList(searchPerformer.actionEntry);

            ModActions.registerActionPerformer(searchPerformer);

        }
        @Override
        public List<ActionEntry> getBehavioursFor(Creature performer, Item source,Item target) {

            if ((source.getTemplateId()==ItemList.papyrusSheet||source.getTemplateId()==ItemList.paperSheet)&&target.getTemplateId() == ItemList.bodyBody) {
                if (Performer.canUse(performer, target)) {
                    return new ArrayList<>(searchCart);
                }


            }
            return null;
        }


    }



