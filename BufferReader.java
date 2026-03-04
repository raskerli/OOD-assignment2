// BufferReader.java
// Each reader has its own reading position.
// Reading does NOT remove items from the buffer.
// If the writer overwrites old data, the reader may miss items.

public class BufferReader {

    private final String name;       // name to identify this reader
    private final RingBuffer buffer; // shared buffer this reader reads from
    private int readCount;           // how many items this reader has read so far

    public BufferReader(String name, RingBuffer buffer) {
        this.name = name;
        this.buffer = buffer;
        this.readCount = 0;
    }

    // Reads the next unread item for this reader.
    // Returns null if there is nothing new to read.
    public Object read() {
        int available = buffer.getSize();

        // if this reader has already read everything currently in the buffer
        if (readCount >= available) {
            return null;
        }

        Object item = buffer.read(readCount);
        readCount++;
        return item;
    }

    // Returns true if there are more items this reader has not yet read.
    public boolean hasNext() {
        return readCount < buffer.getSize();
    }

    // Returns how many items this reader has read so far.
    public int getReadCount() {
        return readCount;
    }

    // Returns the name of this reader.
    public String getName() {
        return name;
    }
}
