// Main.java
// Demo: one writer, three readers, all reading independently.

public class Main {

    public static void main(String[] args) {

        // create a ring buffer with capacity 5
        RingBuffer buffer = new RingBuffer(5);

        // one writer
        BufferWriter writer = new BufferWriter(buffer);

        // three independent readers
        BufferReader readerA = new BufferReader("ReaderA", buffer);
        BufferReader readerB = new BufferReader("ReaderB", buffer);
        BufferReader readerC = new BufferReader("ReaderC", buffer);

        // --- writer writes 3 items ---
        System.out.println("=== Writer writes: A, B, C ===");
        writer.write("A");
        writer.write("B");
        writer.write("C");

        // readerA reads all 3
        System.out.println("\n--- ReaderA reads ---");
        while (readerA.hasNext()) {
            System.out.println("ReaderA got: " + readerA.read());
        }

        // writer writes 2 more items
        System.out.println("\n=== Writer writes: D, E ===");
        writer.write("D");
        writer.write("E");

        // readerB reads everything (A, B, C, D, E)
        System.out.println("\n--- ReaderB reads ---");
        while (readerB.hasNext()) {
            System.out.println("ReaderB got: " + readerB.read());
        }

        // readerA continues from where it left off (D, E)
        System.out.println("\n--- ReaderA continues ---");
        while (readerA.hasNext()) {
            System.out.println("ReaderA got: " + readerA.read());
        }

        // writer fills buffer past capacity to show overwrite behavior
        System.out.println("\n=== Writer writes 5 more (overflow): F, G, H, I, J ===");
        writer.write("F");
        writer.write("G");
        writer.write("H");
        writer.write("I");
        writer.write("J");

        // readerC starts reading now — may have missed early items
        System.out.println("\n--- ReaderC reads (may have missed some) ---");
        while (readerC.hasNext()) {
            System.out.println("ReaderC got: " + readerC.read());
        }

        System.out.println("\n=== Done. Writer wrote " + writer.getWriteCount() + " items total. ===");
    }
}
