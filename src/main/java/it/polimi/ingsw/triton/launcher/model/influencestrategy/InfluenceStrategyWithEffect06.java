package it.polimi.ingsw.triton.launcher.model.influencestrategy;

import it.polimi.ingsw.triton.launcher.model.Island;
import it.polimi.ingsw.triton.launcher.model.player.Player;

public class InfluenceStrategyWithEffect06 extends InfluenceStrategy {
    /**
     * This method calculate the influence between two players on an island without considering tower's influence
     * @param player candidate for the dominance
     * @param professors
     * @param dominator player who dominates the island
     * @param island on which to calculate the influence
     * @return influence value
     */
    @Override
    public int execute(Player player, Player[] professors, Player dominator, Island island) {
        int influence = 0;
        for (int i = 0; i < professors.length; i++) {
            if (professors[i].equals(player)) {
                influence += island.getStudents()[i];
            }
        }
        return influence;
    }
}
