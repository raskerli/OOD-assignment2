// RingBuffer.java
// The core buffer that holds the data array and write position.
// Only one writer should use this.

public class RingBuffer {

    private final Object[] data;   // fixed-size array to store items
    private final int capacity;    // max number of items the buffer holds
    private int writeIndex;        // where the next write will go
    private int size;              // how many items are currently stored

    public RingBuffer(int capacity) {
        this.capacity = capacity;
        this.data = new Object[capacity];
        this.writeIndex = 0;
        this.size = 0;
    }

    // Writer calls this to add a new item.
    // If the buffer is full, the oldest item is overwritten.
    public synchronized void write(Object item) {
        data[writeIndex] = item;
        writeIndex = (writeIndex + 1) % capacity;

        // track how many valid items are in the buffer (max = capacity)
        if (size < capacity) {
            size++;
        }
    }

    // Readers use this to get an item at a specific index position.
    public synchronized Object read(int index) {
        if (index < 0 || index >= size) {
            return null; // nothing to read at this position
        }
        // calculate the actual array slot for this logical index
        int start = (writeIndex - size + capacity) % capacity;
        int actual = (start + index) % capacity;
        return data[actual];
    }

    // Returns how many items are currently in the buffer.
    public synchronized int getSize() {
        return size;
    }

    // Returns the total capacity of the buffer.
    public int getCapacity() {
        return capacity;
    }

    // Returns the current write position (used by readers to detect overwrites).
    public synchronized int getWriteIndex() {
        return writeIndex;
    }
}
