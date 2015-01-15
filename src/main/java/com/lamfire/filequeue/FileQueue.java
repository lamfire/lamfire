package com.lamfire.filequeue;

/**
 * 一个高性能持久化对列,支持先进先出
 * 
 * @author lamfire
 * 
 */
public interface FileQueue {
	public boolean add(byte[] bytes);
    public  byte[] peek();
    public  byte[] peek(int i);
    public  byte[] poll();
	public  long size();
	public void clear();
	public void close();
    public void delete();
}
