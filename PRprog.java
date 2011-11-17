//Compiled using Eclipse Java compiler on a Windows 7 64-bit machine.
//Date completed: 10/10/11
//Replicates a record managing system.  A client parses a list of commands for
//inserting, removing, and searching for records.  The client communicates with
//a Binary Search Tree, and a PRQuadtree, both of which store the records using
//specific keys.  The BST uses the city name as the key, while the PRQT uses the
//location as a point.  This allows for easy searching of records by name or region.


import java.io.FileNotFoundException;

/**
 * // -------------------------------------------------------------------------
/**
 *  The main class for a record managing program.  Passes a list of commands to
 *  a client which will store and manage the records using a Binary Search Tree,
 *  and PRQuadtree.
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Oct 7, 2011
 */

//On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class PRprog
{
    /**
     * Initialize a Client to manage city records using a Binary Search Tree
     * and PRQuadtree.  The client will execute a list of commands for inserting,
     * removing, and searching for the records.
     * @param args args[0]- the file name of the text file containing the
     * commands for the client.
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        @SuppressWarnings("unused")
        Client RecordClient = new Client(args[0]);
    }
}
