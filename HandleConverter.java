import java.nio.ByteBuffer;


/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 *
 *  @author Josh
 *  @version Nov 16, 2011
 */
public class HandleConverter
{


    public HandleConverter()
    {
    }

    public static Node convertToNode(Handle handle, MemoryManager manager)
    {
        int messageSize = manager.getSize(handle);
        byte[] nodeMessage = new byte[messageSize];
        manager.get(nodeMessage, handle, messageSize);

        ByteBuffer bbuff = ByteBuffer.allocate(4);
        if (nodeMessage[0] == 0) // if the first byte is 0, its a region node
        {
            //the node is internal, get the children Handles
            Handle[] handles = new Handle[4];
            for (int i = 0; i <= 3; i++)
            {
                for(int j = 0; j < 4; j++)
                {
                    bbuff.put(nodeMessage[j+i*4+1]); // 1-4, 5-8, 9-12, 13-16
                }
                handles[i] = new Handle(bbuff.getInt(0)); // pull those four bytes from the bbuff and put it into a handle
                bbuff.clear();
            }
            return new RegionNode(handles[0], handles[1], handles[2], handles[3], manager);
        }
        else // if the first byte is non-zero, its a leaf node
        {
            //construct a leaf node
            bbuff.put(nodeMessage[1]);
            int numRecs = bbuff.getInt(0);
            bbuff.clear();
            Handle handles[] = new Handle[numRecs];
            for (int i = 0; i < numRecs; i ++)
            {
                for(int j = 0; j < 4; j++)
                {
                    bbuff.put(nodeMessage[j+i*4+2]);
                }
                handles[i] = new Handle(bbuff.getInt(0));
                bbuff.clear();
            }
            return new LeafNode(handles, manager)



        }
    }
}
