package com.test.filequeue;

import com.lamfire.filequeue.FileBuffer;
import com.lamfire.filequeue.FileQueue;
import com.lamfire.logger.Logger;
import com.lamfire.utils.Asserts;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.StringUtils;
import com.lamfire.utils.Threads;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Tests for QueueFile.
 * 
 * @author Bob Lee (bob@squareup.com)
 */
public class QueuePreformaceTest {
    private static final Logger LOGGER = Logger.getLogger(QueuePreformaceTest.class);
    private final String TEXT = RandomUtils.randomTextWithFixedLength(1000);

    private FileQueue queue;

    AtomicInteger writeCounter = new AtomicInteger();

    AtomicInteger readCounter = new AtomicInteger();

    AtomicInteger iops = new AtomicInteger();

    AtomicInteger errorCounter = new AtomicInteger();

    QueuePreformaceTest() throws Exception{
        queue = new FileQueue("/data/FileQueue/" ,"preformance");
        queue.clear();
    }

    Runnable statusThread = new Runnable() {
        int preReadCount = 0;
        int preWriteCount = 0;
        int preIops = 0 ;

        @Override
        public void run() {
            int readCount = readCounter.get();
            int writeCount = writeCounter.get();
            int iopsCount = iops.get();

            System.out.println("[read/write] = " +readCount +"/" +writeCount +" [error/queues] = "+ errorCounter.get() +"/" + queue.size() +" [read/s] = " + (readCount - preReadCount) +" [write/s] = " + (writeCount - preWriteCount) +" [iops/s] = " +(iopsCount - preIops));

            preReadCount = readCount;
            preWriteCount = writeCount;
            preIops = iopsCount;
        }
    };

    Runnable writeThread = new Runnable() {
        @Override
        public void run() {
            while(true){
                String wTxt = TEXT +":" +writeCounter.getAndIncrement();
                try{
                queue.add(wTxt.getBytes());
                iops.incrementAndGet();
                }catch (Throwable t){
                    LOGGER.error(t.getMessage(),t);
                }
            }
        }
    };

    Runnable readThread = new Runnable() {
        @Override
        public void run() {
            while(true){
                try{
                byte[] bytes = queue.poll();
                if(bytes == null){
                     continue;
                }
                String txt = new String(bytes);
                String [] ds = StringUtils.split(txt,':');
                iops.incrementAndGet();
                if(ds.length != 2){
                    LOGGER.error("[ERROR]:" + txt);
                    errorCounter.incrementAndGet();
                    readCounter.incrementAndGet();
                    continue;
                }


                int val = Integer.parseInt(ds[1]);
                int index = readCounter.get();
                if(val != index)     {
                    LOGGER.error("[ERROR]:" + index + " != " + val + " - " + txt);
                    errorCounter.incrementAndGet();
                }
                readCounter.incrementAndGet();
                }catch (Throwable t){
                    LOGGER.error(t.getMessage(),t);
                }
            }
        }
    };

    public void startup(){
        Threads.startup(writeThread);
        Threads.startup(readThread);
        Threads.scheduleWithFixedDelay(statusThread,1,1,TimeUnit.SECONDS);
    }

	public static void main(String[] args) throws Exception {
        QueuePreformaceTest test = new QueuePreformaceTest();
        test.startup();
	}
}
