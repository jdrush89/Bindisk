import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * // -------------------------------------------------------------------------
/**
 *  A class for reading in and executing commands for storing, deleting,
 *  and searching for records using a Binary Search Tree and PRQuadtree.
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Sep 28, 2011
 */
public class Client
{
    //The quadtree the Client uses to manage records using point keys
    private static PRQuadTree<CityRecord> qt = new PRQuadTree<CityRecord>();
    //The binary Search Tree the Client uses to manage records using name keys
    private static BinarySearchTree<String, CityRecord> bst = new BinarySearchTree<String, CityRecord>();

    /**
     * Create a new Client to handle records and execute a list of commands
     * for inserting, removing, and searching those records.
     * @param fileName the text file containing the commands.
     * @throws FileNotFoundException
     */
    public Client(String fileName) throws FileNotFoundException
    {
        executeCommands(fileName);
    }
    /**
     * Executes the commands for storing and managing records
     * specified by an input file.
     * @param fileName the text file containing the commands.
     * @throws FileNotFoundException
     */
    private static void executeCommands(String fileName)
    throws FileNotFoundException
    {
        File file = new File(fileName);
        Scanner lineScanner = new Scanner(file);
        int x = 0;
        //read commands in line by line, parsing and executing them.
        try
        {
            while(lineScanner.hasNext())
            {
                parseCommand(lineScanner.nextLine());
                x++;
            }
        }
        finally
        {
            lineScanner.close();
        }
    }

    /**
     * Strips extra whitespace from a command and determines what it is,
     * whether the command be insert, remove, or print, and its arguments.
     * @param parseString the single command to be stripped and parsed
     */
    private static void parseCommand(String parseString)
    {
        String trimString = parseString.trim();
        StringTokenizer st = new StringTokenizer(trimString, " ");
        //using ' ' as a tokenizer, get the first token and determine what the
        //command is
        if(st.hasMoreTokens()) {
            printTokens(trimString);
            String command = st.nextToken();

            if(command.equalsIgnoreCase("insert"))
                parseInsert(st);
            else if(command.equalsIgnoreCase("remove"))
                parseRemove(st);
            else if(command.equalsIgnoreCase("find"))
                parseFind(st);
            else if(command.equalsIgnoreCase("search"))
                parseSearch(st);
            else if(command.equalsIgnoreCase("debug"))
                debugRequest();
            else if(command.equalsIgnoreCase("makenull"))
                makeNullRequest();
            else
                System.out.println("Invalid command '" + command + "'");
        }
    }
    /**
     * Print the command given by the string in tokens to avoid extra whitespace.
     * @param st the string containing the command to print
     */
    public static void printTokens(String st)
    {
        StringTokenizer printTokenizer = new StringTokenizer(st);
        System.out.println();
        while(printTokenizer.hasMoreTokens()) {
            System.out.print(printTokenizer.nextToken() + " ");
        }
        System.out.println("\n");
    }

    /**
     * Parses the "insert x y name" command. If it is a valid command
     * syntax, the insertion will be processed.
     * @param commandToken the command to process separated in a StringTokenizer
     */
    private static void parseInsert(StringTokenizer commandToken) {
        if(commandToken.countTokens() != 3)
        {
            System.out.println("Insert commands MUST be in the format " +
                    "'insert x y name'");
            while(commandToken.hasMoreTokens()) {
                System.out.print(commandToken.nextToken());
            }
            return;
        }

        int x = Integer.parseInt(commandToken.nextToken());
        int y = Integer.parseInt(commandToken.nextToken());
        String name = commandToken.nextToken();

        insertRequest(x, y, name);

    }

    /**
     * insertRequest tries to insert the given parameters into
     * the PRQuadTree and BST.  Then insertion will fail if the coordinate
     * values are negative or above 2^14 - 1, or if a record at those coordinates
     * already exists.
     * @param x the x coordinate of the Record being inserted
     * @param y the y coordinate of the Record being inserted
     * @param name the name of this city
     */
    private static void insertRequest(int x, int y, String name) {
        if (x > qt.MAX_COORD || y > qt.MAX_COORD || x < 0 || y < 0)
        {
            System.out.println("Insert failed, coordinates are out of bounds");
            return;
        }
        CityRecord rec = new CityRecord(x, y, name);

    	if(qt.insert(rec, x, y))
    	{
    	    bst.insert(name, rec);
    	    System.out.println("Insertion successful");
    	}
    	else
    	    System.out.println("Insert failed, record with those coordinates" +
    	    		" already exists");
    }

    /**
     * Parses the "remove x y name" and "remove name" command. If it is a valid command
     * syntax, the removal will be processed.
     * @param commandToken the command to process separated in a StringTokenizer
     */
    private static void parseRemove(StringTokenizer commandToken) {
        int numTokens = commandToken.countTokens();

      	if(numTokens != 1 && numTokens != 2)
        {
            System.out.println("Remove commands MUST be in the format " +
                    "'remove x y' or 'remove name'");
            while(commandToken.hasMoreTokens()) {
                System.out.print(commandToken.nextToken());
            }
            return;
        }

    	if(numTokens == 2) {

          int x = Integer.parseInt(commandToken.nextToken());
          int y = Integer.parseInt(commandToken.nextToken());

    	    removeRequest(x, y);
      }
    	else {

    	    String name = commandToken.nextToken();
    	    removeRequest(name);
    	}
    }
    /**
     * removeRequest tries to remove a record based on given coordinates.
     * Removal fails if the coordinates are negative, or above 2^14 - 1, or
     * if the record at that point doesn't exist.
     * @param x the x coordinate of the record to remove
     * @param y the y coordinate of the record to remove
     */
    private static void removeRequest(int x, int y) {
        if (x > qt.MAX_COORD || y > qt.MAX_COORD || x < 0 || y < 0)
        {
            System.out.println("Remove failed, coordinates are out of bounds");
            return;
        }
      	CityRecord rec = qt.remove(x, y);
      	if(rec != null) {
      	    bst.remove(rec.getName());
      	    System.out.println("Removed the city record of "+rec);
      	}
      	else
      	    System.out.println("Remove failed, record wasn't found");
    }

   /**
     * removeRequest tries to remove a record given the record's name.  The
     * removal fails if a record with that name doesn't exist.  If multiple
     * records with the name exist, only one will be deleted.
     * @param name the name of the city to be removed
     */

    private static void removeRequest(String name) {
        CityRecord rec = bst.remove(name);
    	if (rec != null)
	    {
	        qt.remove(rec.getX(), rec.getY());
	        System.out.println("Removed the record of "+rec);
	    }
	    else
	        System.out.println("Remove failed, record wasn't found");
    }



   /**
     * Parses the "find name" command. If it is a valid command
     * syntax, the find will be processed.
     * @param commandToken the command to process separated in a StringTokenizer
     */
    private static void parseFind(StringTokenizer commandToken) {
        int numTokens = commandToken.countTokens();
    	if(numTokens != 1)
        {
            System.out.println("Find commands MUST be in the format " +
                "'find name'");
            while(commandToken.hasMoreTokens()) {
                System.out.print(commandToken.nextToken());
            }
            return;
        }

        String name = commandToken.nextToken();
        findRequest(name);
    }
    /**
     * findRequest tries to find a record based on the given name
     * @param name the name to search by
     */

    private static void findRequest(String name) {
    	List<CityRecord> recList = bst.find(name);
    	if(recList.size() <= 0) {
    	    System.out.println("Could not find any cities with the name '"+name+"'.");
    	    return;
    	}

    	for(CityRecord rec : recList) {
    	    System.out.println("Found the city "+rec.getName() + " located at ("+rec.getX()+", "+rec.getY()+").");
    	}
    }

    /**
     * Parses the "search x y radius" command. If it is a valid command
     * syntax, the search will be processed.
     * @param commandToken the command to process separated in a StringTokenizer
     */
    private static void parseSearch(StringTokenizer commandToken) {
        int numTokens = commandToken.countTokens();

        if(numTokens != 3)
        {
            System.out.println("Search commands MUST be in the format " +
                    "'search x y radius'");
            while(commandToken.hasMoreTokens()) {
                System.out.print(commandToken.nextToken());
            }
            return;
        }

        int x = Integer.valueOf(commandToken.nextToken());
        int y = Integer.valueOf(commandToken.nextToken());
        int radius = Integer.valueOf(commandToken.nextToken());

        searchRequest(x,y,radius);
    }

    /**
     * searchRequest finds all cities in a given radius from the x,y coordinate
     * the absolute value of the x and y must be less than 2^14, and the radius
     * must be a positive integer less than 2^14.
     * @param x the x coordinate of the search area center
     * @param y the y coordinate of the search area center
     * @param radius the radius of the search area
     */
    private static void searchRequest(int x, int y, int radius) {
        if (Math.abs(x) > qt.MAX_COORD || Math.abs(y) > qt.MAX_COORD)
        {
            System.out.println("Coordinates are out of bounds");
            return;
        }
        if (radius < 0 || radius >= qt.MAX_COORD)
        {
            System.out.println("Radius value is invalid");
            return;
        }
    	ArrayList<CityRecord> recList = qt.search(x, y, radius);
    	// the QuadTree should print how how many nodes it looked at when searching

    	if(recList.size() <= 0) {
    	    System.out.println("Could not find any cities within "+radius+" units of ("+x+", "+y+").");
    	    return;
    	}

    	for(CityRecord rec : recList) {
    	    System.out.println("Found the city " + rec.getName() +
    	        " located at ("+rec.getX()+", "+rec.getY()+").");
    	}

    }

    /**
     * Handles the "debug" command.
     */
    private static void debugRequest() {
    	//the QuadTree should print out debug information now
        qt.debug();
        System.out.println();
    }

    /**
     * Handles the "makenull" command.
     */
    private static void makeNullRequest() {
    	//the QuadTree should print out debug information now
        qt.clear();
        bst.clear();
        System.out.println("Storage is now empty");
    }


}
