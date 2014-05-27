package com.lamfire.filequeue;

import java.io.IOException;

interface QueueWriter {

    public void write(byte[] bytes) throws IOException;

}
