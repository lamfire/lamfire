package com.lamfire.filequeue;

import java.io.IOException;

public interface QueueWriter {

    public void write(byte[] bytes) throws IOException;

}
