package com.lamfire.filequeue;


import java.io.IOException;

public interface QueueReader {

    public boolean hashMore();

    public byte[] peek() throws IOException;

    public byte[] poll() throws IOException;
}
