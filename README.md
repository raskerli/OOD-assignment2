# Ring Buffer – Multiple Readers, Single Writer

## Overview

This project implements a **Ring Buffer** (circular buffer) in Java that supports:
- **One writer** that writes items into a fixed-size buffer
- **Multiple readers**, each reading independently from the same buffer
- When the buffer is full, the writer **overwrites the oldest data**
- Slow readers may **miss items** if the writer laps them

---

## Project Structure
```
RingBuffer-MultiReader/
├── RingBuffer.java      # The core circular buffer (data + write logic)
├── BufferWriter.java    # Wraps the write operation for the single writer
├── BufferReader.java    # Each reader has its own reading position
├── Main.java            # Demo with 1 writer and 3 readers
└── README.md
```

---

## Design Explanation

### `RingBuffer`
Holds the fixed-size array, the write pointer, and the current size. It knows nothing about who is reading — it just stores data and lets readers access items by index.

### `BufferWriter`
A simple wrapper around `RingBuffer.write()`. Only one `BufferWriter` should exist per buffer. Tracks how many items have been written.

### `BufferReader`
Each reader has its own `readCount` that tracks how far it has read. Reading does not remove data — it just advances the reader's own pointer. If the buffer has been overwritten past a reader's position, that reader may miss items.

---

## UML Class Diagram
```mermaid
classDiagram
    class RingBuffer {
        -Object[] data
        -int capacity
        -int writeIndex
        -int size
        +write(item Object) void
        +read(index int) Object
        +getSize() int
        +getCapacity() int
        +getWriteIndex() int
    }

    class BufferWriter {
        -RingBuffer buffer
        -int writeCount
        +write(item Object) void
        +getWriteCount() int
    }

    class BufferReader {
        -String name
        -RingBuffer buffer
        -int readCount
        +read() Object
        +hasNext() boolean
        +getReadCount() int
        +getName() String
    }

    BufferWriter --> RingBuffer : writes to
    BufferReader --> RingBuffer : reads from
```

---

## UML Sequence Diagram – write()
```mermaid
sequenceDiagram
    participant Client
    participant BufferWriter
    participant RingBuffer

    Client->>BufferWriter: write("Hello")
    BufferWriter->>RingBuffer: write("Hello")
    RingBuffer->>RingBuffer: store at data[writeIndex]
    RingBuffer->>RingBuffer: writeIndex = (writeIndex + 1) % capacity
    RingBuffer->>RingBuffer: if size < capacity, size++
    RingBuffer-->>BufferWriter: done
    BufferWriter->>BufferWriter: writeCount++
    BufferWriter-->>Client: done
```

---

## UML Sequence Diagram – read()
```mermaid
sequenceDiagram
    participant Client
    participant BufferReader
    participant RingBuffer

    Client->>BufferReader: read()
    BufferReader->>RingBuffer: getSize()
    RingBuffer-->>BufferReader: size
    alt readCount < size
        BufferReader->>RingBuffer: read(readCount)
        RingBuffer->>RingBuffer: calculate actual array index
        RingBuffer-->>BufferReader: item
        BufferReader->>BufferReader: readCount++
        BufferReader-->>Client: item
    else nothing new
        BufferReader-->>Client: null
    end
```

---

## How to Run

### Requirements
- Java 8 or higher

### Steps

1. Clone or download the repository:
```bash
   git clone 
   cd 
```

2. Compile all Java files:
```bash
   javac *.java
```

3. Run the demo:
```bash
   java Main
```
