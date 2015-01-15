import com.lamfire.utils.ObjectPool;
import com.lamfire.utils.RandomUtils;
import com.lamfire.utils.Threads;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: lamfire
 * Date: 15-1-15
 * Time: 下午2:30
 * To change this template use File | Settings | File Templates.
 */
public class ObjectPoolTest implements Runnable{

    public static class Item{
        private int value = 100;
        private String name = "name";

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static ObjectPool<Item> pool = new ObjectPool<Item>(Item.class);

    public void run() {
        Item item = pool.borrowObject();
        Threads.sleep(RandomUtils.nextInt(1000));
        pool.returnObject(item);
    }

    public static void main(String[] args) {
        pool.setMaxInstanceIdle(12);
        pool.setMaxInstanceSize(12);
        ThreadPoolExecutor executor = Threads.newCachedThreadPool();
        while(true){
            if(executor.getTaskCount() - executor.getCompletedTaskCount() < 100){
                executor.submit(new ObjectPoolTest());
                System.out.println(pool);
            }
        }
    }
}
