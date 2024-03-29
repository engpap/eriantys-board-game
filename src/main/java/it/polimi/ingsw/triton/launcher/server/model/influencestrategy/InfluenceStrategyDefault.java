package it.polimi.ingsw.triton.launcher.server.model.influencestrategy;

import it.polimi.ingsw.triton.launcher.server.model.islands.Island;
import it.polimi.ingsw.triton.launcher.server.model.player.Player;

/**
 * This class contains code that is used by Default and Effect08.
 * This class is the superClass of Default and Effect08 in order to remove duplicated code.
 */
public class InfluenceStrategyDefault implements InfluenceStrategy {

    /**
     * This method calculates the default influence between two players on an island.
     *
     * @param player     candidate for the dominance.
     * @param professors the array with professors associated to each player.
     * @param dominator  player who dominates the island.
     * @param island     on which to calculate the influence.
     * @return the influence value.
     */
    @Override
    public int execute(Player player, Player[] professors, Player dominator, Island island) {
        int influence = 0;
        if (dominator != null && dominator.equals(player))
            influence += island.getDim();
        for (int i = 0; i < professors.length; i++) {
            if (professors[i] != null && professors[i].equals(player)) {
                influence += island.getStudents()[i];
            }
        }
        return influence;
    }
}
