package com.lamfire.filequeue;

import java.io.IOException;

interface Writer {

    public void write(byte[] bytes) throws IOException;

}
