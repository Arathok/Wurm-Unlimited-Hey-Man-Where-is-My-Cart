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

public class CartBehaviour implements BehaviourProvider{

        private final List<ActionEntry> generateWrit;
        private final CartPerformer generateWritPerformer;

        public CartBehaviour() {
            this.generateWritPerformer = new CartPerformer();
            this.generateWrit = Collections.singletonList(generateWritPerformer.actionEntry);

            ModActions.registerActionPerformer(generateWritPerformer);

        }
        @Override
        public List<ActionEntry> getBehavioursFor(Creature performer, Item source, Item target) {

            if ((source.getTemplateId()== ItemList.papyrusSheet||source.getTemplateId()==ItemList.paperSheet)&&target.getTemplate().isVehicle()) {
                if (CartPerformer.canUse(performer, target)) {
                    return new ArrayList<>(generateWrit);
                }


            }
            return null;
        }


    }






