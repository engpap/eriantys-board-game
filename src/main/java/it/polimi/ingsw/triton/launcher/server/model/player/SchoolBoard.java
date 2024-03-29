package it.polimi.ingsw.triton.launcher.server.model.player;

import it.polimi.ingsw.triton.launcher.server.model.enums.Color;
import it.polimi.ingsw.triton.launcher.server.model.enums.TowerColor;
import it.polimi.ingsw.triton.launcher.utils.Utility;
import it.polimi.ingsw.triton.launcher.utils.exceptions.EndGameException;
import it.polimi.ingsw.triton.launcher.utils.exceptions.IllegalClientInputException;
import it.polimi.ingsw.triton.launcher.utils.message.ErrorTypeID;

import java.io.Serializable;
import java.util.Arrays;

public class SchoolBoard implements Serializable {
    private final TowerColor towerColor;
    private final int[] entrance;
    private final int[] diningRoom;
    private int numTowers;

    private final boolean[][] availableCoins;

    /**
     * @param towerColor the color of towers in the school board.
     */
    public SchoolBoard(TowerColor towerColor, int numPlayers) {
        this.towerColor = towerColor;
        if (numPlayers == 2)
            this.numTowers = 8;
        else
            this.numTowers = 6;
        this.entrance = new int[5];
        this.diningRoom = new int[5];
        this.availableCoins = new boolean[Color.numOfColors()][10];
        setupAvailableCoins();
    }

    public int getNumTowers() {
        return numTowers;
    }

    public int[] getEntrance() {
        return entrance;
    }

    public int[] getDiningRoom() {
        return diningRoom;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }

    public boolean[][] getAvailableCoins() {
        return availableCoins;
    }

    private void setupAvailableCoins() {
        for (int i = 0; i < Color.numOfColors(); i++) {
            for (int j = 0; j < 10; j++) {
                availableCoins[i][j] = ((j + 1) % 3) == 0;
            }
        }
    }

    /**
     * Moves the towers from the school board to the island.
     *
     * @param numOfTowerToMove is equal to the dimension of the island.
     * @throws EndGameException if the player has not other towers on the school board so the game is ended.
     */
    public void moveTowerOntoIsland(int numOfTowerToMove) throws EndGameException {
        numTowers -= numOfTowerToMove;
        if (numTowers <= 0) {
            numTowers = 0;
            throw new EndGameException();
        }
    }

    /**
     * Removes the towers from the island and put them into the school board.
     *
     * @param numOfTowerToMove is equal to the dimension of the island.
     */
    public void moveTowerOntoSchoolBoard(int numOfTowerToMove) {
        numTowers += numOfTowerToMove;
    }

    /**
     * Adds a student in the dining room of the school board.
     *
     * @param color the color of the student to insert.
     * @throws IllegalClientInputException if the dining room corresponding to that color is already full.
     */
    public void addStudentIntoDiningRoom(Color color) throws IllegalClientInputException {
        if (diningRoom[color.ordinal()] < 10)
            diningRoom[color.ordinal()]++;
        else
            throw new IllegalClientInputException(ErrorTypeID.DINING_ROOM_OF_COLOR_IS_FULL);
    }

    /**
     * Adds a student at the entrance of the school board.
     *
     * @param color the color of the student to add at the entrance.
     */
    public void addStudentIntoEntrance(Color color) {
        entrance[color.ordinal()]++;
    }

    /**
     * @param color the color of the students.
     * @return the number of students in the dining room with
     * the color passed in parameter.
     */
    public int getStudentsNumber(Color color) {
        return diningRoom[color.ordinal()];
    }

    /**
     * Removes a student of a specified color from entrance.
     *
     * @param student the color of the student to remove.
     * @throws IllegalArgumentException when there are no students of the specified color in the entrance.
     */
    public void removeStudentFromEntrance(Color student) throws IllegalArgumentException {
        if (entrance[student.ordinal()] > 0)
            entrance[student.ordinal()]--;
        else
            throw new IllegalArgumentException("No students of color " + student.name() + " at entrance");
    }

    public boolean isEntranceEmpty() {
        return Arrays.stream(entrance).sum() == 0;
    }

    public boolean isDiningRoomEmpty() {
        return Arrays.stream(diningRoom).sum() == 0;
    }

    @Override
    public String toString() {
        return "{" +
                "Tower color: " + towerColor +
                ", Number of Towers: " + numTowers +
                ", Entrance -> " + Utility.printColoredStudents(entrance) +
                ", Dining Room -> " + Utility.printColoredStudents(diningRoom) +
                "}\n\t";
    }

}
