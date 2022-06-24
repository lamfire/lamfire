package com.lamfire.logger;

public class LoggerFactory {
    public static Logger getLogger(String name){
        try{
            Logger logger = new Log4jLogger(name);
            return logger;
        }catch (Throwable e){
        }
        try{
            Logger logger =  new Slf4jLogger(name);
            return logger;
        }catch (Throwable e){
        }
        return new JdkLogger(name);
    }
    
    public static Logger getLogger(Class<?> claxx){
        return getLogger(claxx.getName());
    }
}
