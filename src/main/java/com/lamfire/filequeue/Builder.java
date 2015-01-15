package com.lamfire.filequeue;

import com.lamfire.utils.StringUtils;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 14-11-6
 * Time: 上午10:11
 * To change this template use File | Settings | File Templates.
 */
public abstract class Builder<E> {
    protected String dataDir;
    protected String name;
    protected int indexBufferSize = 4 * 1024 * 1024;
    protected int storeBufferSize = 4 * 1024 * 1024;


    public String dataDir() {
        return dataDir;
    }

    public Builder<E> dataDir(String dataDir) {
        this.dataDir = dataDir;
        return this;
    }

    public String name() {
        return name;
    }

    public Builder<E> name(String name) {
        this.name = name;
        return this;
    }

    public int indexBufferSize() {
        return indexBufferSize;
    }

    public Builder<E> indexBufferSize(int indexBufferSize) {
        this.indexBufferSize = indexBufferSize;
        return this;
    }

    public int storeBufferSize() {
        return storeBufferSize;
    }

    public Builder<E> storeBufferSize(int storeBufferSize) {
        this.storeBufferSize = storeBufferSize;
        return this;
    }

    public E build()throws IOException{
        if(StringUtils.isBlank(dataDir)){
            throw new IllegalArgumentException("Argument 'dataDir' can not be empty.");
        }

        if(StringUtils.isBlank(name)){
            throw new IllegalArgumentException("Argument 'name' can not be empty.");
        }

        return make(dataDir,name,indexBufferSize,storeBufferSize) ;
    }

    abstract E make(String dataDir, String name, int indexBufferSize, int storeBufferSize) throws IOException;
}
