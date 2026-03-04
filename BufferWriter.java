// BufferWriter.java
// Wraps the write operation.
// Only one writer should exist per buffer (single writer rule).

public class BufferWriter {

    private final RingBuffer buffer; // the buffer this writer writes to
    private int writeCount;          // how many items have been written

    public BufferWriter(RingBuffer buffer) {
        this.buffer = buffer;
        this.writeCount = 0;
    }

    // Writes an item into the buffer.
    // If the buffer is full, the oldest item is overwritten.
    public void write(Object item) {
        buffer.write(item);
        writeCount++;
    }

    // Returns how many items this writer has written.
    public int getWriteCount() {
        return writeCount;
    }
}
