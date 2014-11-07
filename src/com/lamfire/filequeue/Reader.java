package com.lamfire.filequeue;


import java.io.IOException;

interface Reader {

    public boolean hashMore();

    public byte[] peek() throws IOException;

    public byte[] peek(int i) throws IOException;

    public byte[] poll() throws IOException;
}
