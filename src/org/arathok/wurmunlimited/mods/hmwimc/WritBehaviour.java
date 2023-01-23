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

public class WritBehaviour

       implements BehaviourProvider {

        private final List<ActionEntry> searchCart;
        private final WritPerformer searchWritPerformer;

        public WritBehaviour() {
            this.searchWritPerformer = new WritPerformer();
            this.searchCart = Collections.singletonList(searchWritPerformer.actionEntry);

            ModActions.registerActionPerformer(searchWritPerformer);

        }
        @Override
        public List<ActionEntry> getBehavioursFor(Creature performer, Item source,Item target) {

            if (((source.getTemplateId()==ItemList.papyrusSheet||source.getTemplateId()==ItemList.paperSheet))&&(target.getTemplateId() == ItemList.bodyBody||target.getTemplateId()==ItemList.bodyHand)) {
                if (WritPerformer.canUse(performer, target)) {
                    return new ArrayList<>(searchCart);
                }


            }
            return null;
        }


    }



