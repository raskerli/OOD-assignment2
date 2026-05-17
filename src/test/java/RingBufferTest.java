import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RingBufferTest {

    @Test
    void newBufferShouldHaveCorrectCapacityAndInitialState() {
        RingBuffer buffer = new RingBuffer(5);

        assertEquals(5, buffer.getCapacity());
        assertEquals(0, buffer.getSize());
        assertEquals(0, buffer.getWriteIndex());
    }

    @Test
    void writeShouldIncreaseSizeUntilCapacity() {
        RingBuffer buffer = new RingBuffer(3);

        buffer.write("A");
        buffer.write("B");

        assertEquals(2, buffer.getSize());
        assertEquals("A", buffer.read(0));
        assertEquals("B", buffer.read(1));
    }

    @Test
    void writeShouldNotIncreaseSizeBeyondCapacity() {
        RingBuffer buffer = new RingBuffer(3);

        buffer.write("A");
        buffer.write("B");
        buffer.write("C");
        buffer.write("D");

        assertEquals(3, buffer.getSize());
    }

    @Test
    void bufferShouldOverwriteOldestItemWhenFull() {
        RingBuffer buffer = new RingBuffer(3);

        buffer.write("A");
        buffer.write("B");
        buffer.write("C");
        buffer.write("D");
        buffer.write("E");

        assertEquals(3, buffer.getSize());

        assertEquals("C", buffer.read(0));
        assertEquals("D", buffer.read(1));
        assertEquals("E", buffer.read(2));
    }

    @Test
    void readShouldReturnNullForInvalidIndex() {
        RingBuffer buffer = new RingBuffer(3);

        buffer.write("A");

        assertNull(buffer.read(-1));
        assertNull(buffer.read(1));
        assertNull(buffer.read(100));
    }

    @Test
    void bufferReaderShouldReturnNullWhenNothingIsAvailable() {
        RingBuffer buffer = new RingBuffer(3);
        BufferReader reader = new BufferReader("ReaderA", buffer);

        assertFalse(reader.hasNext());
        assertNull(reader.read());
        assertEquals(0, reader.getReadCount());
    }

    @Test
    void bufferReaderShouldReadItemsInOrder() {
        RingBuffer buffer = new RingBuffer(5);
        BufferWriter writer = new BufferWriter(buffer);
        BufferReader reader = new BufferReader("ReaderA", buffer);

        writer.write("A");
        writer.write("B");
        writer.write("C");

        assertTrue(reader.hasNext());
        assertEquals("A", reader.read());
        assertEquals("B", reader.read());
        assertEquals("C", reader.read());

        assertFalse(reader.hasNext());
        assertNull(reader.read());
        assertEquals(3, reader.getReadCount());
    }

    @Test
    void multipleReadersShouldReadIndependently() {
        RingBuffer buffer = new RingBuffer(5);
        BufferWriter writer = new BufferWriter(buffer);

        BufferReader readerA = new BufferReader("ReaderA", buffer);
        BufferReader readerB = new BufferReader("ReaderB", buffer);

        writer.write("A");
        writer.write("B");

        assertEquals("A", readerA.read());

        writer.write("C");

        assertEquals("A", readerB.read());
        assertEquals("B", readerB.read());
        assertEquals("C", readerB.read());

        assertEquals("B", readerA.read());
        assertEquals("C", readerA.read());

        assertEquals(3, readerA.getReadCount());
        assertEquals(3, readerB.getReadCount());
    }

    @Test
    void bufferWriterShouldTrackNumberOfWrites() {
        RingBuffer buffer = new RingBuffer(2);
        BufferWriter writer = new BufferWriter(buffer);

        assertEquals(0, writer.getWriteCount());

        writer.write("A");
        writer.write("B");
        writer.write("C");

        assertEquals(3, writer.getWriteCount());
        assertEquals(2, buffer.getSize());
    }

    @Test
    void bufferReaderShouldStoreReaderName() {
        RingBuffer buffer = new RingBuffer(3);
        BufferReader reader = new BufferReader("ReaderA", buffer);

        assertEquals("ReaderA", reader.getName());
    }

  @Test
void zeroCapacityBufferShouldThrowWhenWriting() {
    RingBuffer buffer = new RingBuffer(0);

    assertEquals(0, buffer.getCapacity());
    assertThrows(ArrayIndexOutOfBoundsException.class, () -> buffer.write("A"));
}

    @Test
    void negativeCapacityShouldThrowException() {
        assertThrows(NegativeArraySizeException.class, () -> new RingBuffer(-1));
    }
}