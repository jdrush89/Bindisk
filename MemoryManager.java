
// -------------------------------------------------------------------------
/**
 *  A class that manages records stored in a memory pool.  Keeps track of the
 *  records stored as bytes, and the free space that exists in memory.  Can
 *  access, insert and remove records.
 *
 *  @author Joshua Rush
 *  @author Benjamin Roble
 *  @version Aug 28, 2011
 */
public class MemoryManager
{
    //pool is a large byte array forholding data about records.
    private byte[] pool;
    //freeList is a doubly linked list of free blocks in the memory pool.
    private FreeBlockList freeList;

    //reference to the BufferPool in order to read and write
    private BufferPool bufferpool;

    /**
     * Creates a memory manager with a memory pool of the
     * specified size.
     * @param poolsize the size of the pool of free memory
     */
    public MemoryManager(int poolsize, BufferPool pBufferPool)
    {
        pool = new byte[poolsize];
        freeList = new FreeBlockList(poolsize);
        bufferPool = pBufferPool;
    }

    /**
     * Inserts a record and returns its position Handle.
     * @param space the record to be inserted
     * @param size the size of the record being inserted
     * @return the position Handle of the inserted record
     */
    public Handle insert(byte[] space, int size)
    {
        //find out the position to make the insertion from the freeblock list.
        int position = freeList.use(size);
        if(position < 0) {
            // THEN ADD A NEW BLOCK TO EXPAND THE MEMORY;
        }
        //copy into the pool the record information.
        bufferPool.write(space, position, size);
        return new Handle(position);
    }

    /**
     * Frees a block at the indicated posHandle.
     * Merge adjacent blocks if necessary.
     * @param theHandle the position Handle of the record
     * to be removed
     */
    public void remove(Handle theHandle)
    {
        int position = theHandle.getPosition();
        int size = getSize(theHandle);
        //clears the pool of the record
        /*for (int i = position; i <= position + sizeInt; i++)
        {
            pool[i] = 0;
        }*/
        bufferPool.remove(position, size+1);
        freeList.free(size+1, position); // have to add one byte because there is
        // one byte in front of the record which indicates size
    }

    /**
     * Return the record with the indicated position Handle,
     * up to size bytes.
     * @param space the byte array to place the record into
     * @param theHandle the posHandle of the record to be returned
     * @param size the number of bytes to copy
     * @return the amount of bytes actually copied into the space array
     */
    public int get(byte[] space, Handle theHandle, int size)
    {
        //System.arraycopy(freeList, theHandle.getPosition(), space, 0, size);
        int recordSize = pool[theHandle.getPosition()];
        int copySize = (size < recordSize) ? size: recordSize;
        System.arraycopy(pool, theHandle.getPosition() + 1, space, 0, copySize);
        return copySize;
    }

    // ----------------------------------------------------------
    /**
     * Return the size of a message on disk.
     * @param theHandle the Handle with the message's position
     * @return the size of the message.
     */
    public int getSize(Handle theHandle)
    {
        int recordSize = pool[theHandle.getPosition()];
        return recordSize;
    }

    /**
     * Prints out a representation of the nodes in the freeblock list.
     */
    public void dump()
    {
        System.out.println(freeList.toString());
    }
}
