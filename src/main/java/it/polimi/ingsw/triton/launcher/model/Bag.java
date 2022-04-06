package it.polimi.ingsw.triton.launcher.model;

import java.util.NoSuchElementException;
import java.util.Random;

public class Bag {
    private int numPlayer;
    private int[] students;
    private static Bag bagInstance;

    // some some useful final variables
    private final int NUM_OF_STUDENTS_COLORS = Color.values().length;
    private final int NUM_OF_STUDENTS_FOREACH_COLOR = 26;

    private Bag(int numPlayers) {
        this.students=new int[5];
        this.numPlayer=numPlayers;
    }

    public static Bag instance(int numPlayers){
        if(bagInstance == null)
            bagInstance = new Bag(numPlayers);
        return bagInstance;
    }

    /**
     * Draw a random student from the bag
     * @return a random student
     */
    public Color drawStudent() throws NoSuchElementException{
        if(isEmpty())
            throw new NoSuchElementException("The bag is empty; a student cannot be drawn!");
        else{
            // generates a random number until it finds a student's color that has at least one pawn
            Random random = new Random();
            int randomIndex = random.nextInt(NUM_OF_STUDENTS_COLORS);
            int result=students[randomIndex];
            // if result == 0, there's no students of this color; thus it's necessary to generate another color
            while(result==0) {
                randomIndex = random.nextInt(NUM_OF_STUDENTS_COLORS);
                result = students[randomIndex];
            }
            // decrements the number of the drawn student
            students[randomIndex]--;
            return Color.values()[randomIndex];
        }
    }

    /**
     * Fill the cloud tile with a number of students,
     * the number of students depend by the number of player
     * @param cloudTile the cloudTile to fill
     */
    public void fillCloudTile(CloudTile cloudTile) {
        if (numPlayer == 2){
            cloudTile.setStudents(bagInstance.drawStudent(),bagInstance.drawStudent(),bagInstance.drawStudent());
        }
        if (numPlayer > 2){
            cloudTile.setStudents(bagInstance.drawStudent(),bagInstance.drawStudent(),bagInstance.drawStudent(),bagInstance.drawStudent());
        }
    }


    /**
     * Add to the bag 24 students for each color
     */
    public void fillBag() {
        for (int i=0; i<NUM_OF_STUDENTS_COLORS; i++){
            students[i] += NUM_OF_STUDENTS_FOREACH_COLOR-2;
        }
    }

    /**
     * Add a student to the bag
     * @param color the student to add to the bag
     */
    public void addStudent(Color color) {
        students[color.ordinal()]++;
    }



    /**
     * check if the bag is empty
     * @return true if the bag is empty, else return false
     */
    public boolean isEmpty(){
        for(int i=0;i<NUM_OF_STUDENTS_COLORS;i++){
            if(students[i]>0){
                return false;
            }
        }
        return true;
    }
    /**
     * @return the array of students of the bag
     */
    public int[] getStudents() {
        return students;
    }


}
