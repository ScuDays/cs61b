package capers;

import jdk.jshell.execution.Util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import static capers.Utils.*;

/** A repository for Capers 
 * @author TODO
 * The structure of a Capers Repository is as follows:
 *
 * .capers/ -- top level folder for all persistent data in your lab12 folder
 *    - dogs/ -- folder containing all of the persistent data for dogs
 *    - story -- file containing the current story
 *
 * TODO: change the above structure if you do something different.
 */
public class CapersRepository {
    /** Current Working Directory. */
    static final File CWD = new File(System.getProperty("user.dir"));

    /** Main metadata folder. */
    static final File CAPERS_FOLDER = new File("D:\\cs61B\\lab6\\capers"); // TODO Hint: look at the `join`
                                            //      function in Utils
    static File StORY_FIle = new File("D:\\cs61B\\lab6\\capers\\story.txt");

    /**
     * Does required filesystem operations to allow for persistence.
     * (creates any necessary folders or files)
     * Remember: recommended structure (you do not have to follow):
     *
     * .capers/ -- top level folder for all persistent data in your lab12 folder
     *    - dogs/ -- folder containing all of the persistent data for dogs
     *    - story -- file containing the current story
     */
    public static void setupPersistence(String folder1) throws IOException {
        folder1 = "D:\\cs61B\\lab6\\capers\\" + folder1 + ".txt";
        File a =new File(folder1);
        if(a.exists()){
            return;
        }
        else {
            a.createNewFile();
        }
        // TODO
    }

    /**
     * Appends the first non-command argument in args
     * to a file called `story` in the .capers directory.
     * @param text String of the text to be appended to the story
     */
    public static void writeStory(String text) throws IOException {
        if(!StORY_FIle.exists()){
            StORY_FIle.createNewFile();
        }
        String input = Utils.readContentsAsString(StORY_FIle);
        input += text;
        input += "\n";
        Utils.writeContents(StORY_FIle,input);

        // TODO
        String output = Utils.readContentsAsString(StORY_FIle);
        System.out.println(output);
    }

    /**
     * Creates and persistently saves a dog using the first
     * three non-command arguments of args (name, breed, age).
     * Also prints out the dog's information using toString().
     */
    public static void makeDog(String name, String breed, int age) throws IOException {
        // TODO
        Dog adog = new Dog(name,breed,age);
        setupPersistence(name);

        String folder  = "D:\\cs61B\\lab6\\capers\\" + name + ".txt";
        File a = new File(folder);
        Utils.writeObject(a,adog);
        System.out.println(adog.toString());
    }

    /**
     * Advances a dog's age persistently and prints out a celebratory message.
     * Also prints out the dog's information using toString().
     * Chooses dog to advance based on the first non-command argument of args.
     * @param name String name of the Dog whose birthday we're celebrating.
     */
    public static void celebrateBirthday(String name) {
        // TODO
         String folder  = "D:\\cs61B\\lab6\\capers\\" + name + ".txt";
         File input = new File(folder);
         Dog dog = Utils.readObject(input, Dog.class);
          dog.haveBirthday();

        File output = new File(folder);
        Utils.writeObject(output,dog);
    }
}
