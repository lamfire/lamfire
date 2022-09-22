package com.lamfire.utils;

import com.lamfire.logger.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.ByteBuffer;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class MultiCaster {
    public static final String DEFAULT_MULTI_CAST_ADDRESS = "224.0.0.224";
    public static final int DEFAULT_MULTI_CAST_PORT = 2222;
    public static final int DATA_MAX_LENGTH = 8192;
    private static final Logger LOGGER = Logger.getLogger(MultiCaster.class);
    private ThreadPoolExecutor executor;
    private InetAddress address;
    private int port = DEFAULT_MULTI_CAST_PORT;
    private boolean shutdown = false;
    private MulticastSocket multicastSocket;
    private final byte[] buffer = new byte[DATA_MAX_LENGTH];


    public MultiCaster() {
        this.port = DEFAULT_MULTI_CAST_PORT;
        try {
            this.address = InetAddress.getByName(DEFAULT_MULTI_CAST_ADDRESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public MultiCaster(int port) {
        this.port = port;
        try {
            this.address = InetAddress.getByName(DEFAULT_MULTI_CAST_ADDRESS);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public MultiCaster(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }


    private void open() throws IOException {
        if (multicastSocket == null) {
            this.multicastSocket = new MulticastSocket(port);
            this.multicastSocket.joinGroup(address);
        }
    }

    void receive(DatagramPacket packet) throws IOException {
        if (multicastSocket == null) {
            throw new IOException("Not opened MultiCaster.");
        }
        this.multicastSocket.receive(packet);
    }

    public void startup() throws IOException {
        shutdown = false;
        open();
        if (executor == null) {
            executor = Threads.newSingleThreadScheduledExecutor(Threads.makeThreadFactory("MultiCaster"));
        }
        executor.submit(new HandTask());
        LOGGER.info("startup on " + address + ":" + port);
    }

    public void shutdown() {
        shutdown = true;
        close();
        if (executor != null) {
            this.executor.shutdown();
            this.executor = null;
        }
    }

    private void close() {
        if (multicastSocket != null) {
            try {
                multicastSocket.leaveGroup(this.address);
            } catch (IOException e) {
                LOGGER.warn(e.getMessage(), e);
            }
            multicastSocket.close();
            multicastSocket = null;
        }
    }

    public void send(byte[] bytes) throws IOException {
        if (this.multicastSocket == null) {
            throw new IOException("Not opened MultiCaster.");
        }
        ByteBuffer buffer = ByteBuffer.allocate(4 + bytes.length);
        buffer.putInt(bytes.length);
        buffer.put(bytes);
        buffer.flip();

        bytes = buffer.array();
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
        this.multicastSocket.send(packet);
    }

    public abstract void onMessage(byte[] message);



    private void handNext() {
        try {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            this.receive(packet);

            int len = Bytes.toInt(buffer);
            byte[] data = Bytes.subBytes(buffer, 4, len);
            onMessage(data);
        } catch (Throwable t) {
            LOGGER.error(t.getMessage(), t);
        }
    }

    private class HandTask implements Runnable{
        @Override
        public void run() {
            while(!shutdown){
                handNext();
            }
        }
    }
}
