
package com.lamfire.logger;

import java.util.logging.Level;
import java.util.logging.Logger;

class JdkLogger extends com.lamfire.logger.Logger {

    private final Logger logger;
    private final String loggerName;

    public JdkLogger(Logger logger, String loggerName) {
        this.logger = logger;
        this.loggerName = loggerName;
    }
    
    public JdkLogger(Class<?> cls){
    	this.loggerName = cls.getName();
    	this.logger = Logger.getLogger(this.loggerName);
    }
    
    public JdkLogger(String name){
    	this.loggerName = name;
    	logger = Logger.getLogger(name);
    }

    public Logger getLogger() {
        return logger;
    }

    public void debug(String msg) {
        logger.logp(Level.FINE, loggerName, null, msg);
    }

    public void debug(String msg, Throwable cause) {
        logger.logp(Level.FINE, loggerName, null, msg, cause);
    }

    public void error(String msg) {
        logger.logp(Level.SEVERE, loggerName, null, msg);
    }

    public void error(String msg, Throwable cause) {
        logger.logp(Level.SEVERE, loggerName, null, msg, cause);
    }

    public void info(String msg) {
        logger.logp(Level.INFO, loggerName, null, msg);
    }

    public void info(String msg, Throwable cause) {
        logger.logp(Level.INFO, loggerName, null, msg, cause);
    }

    public boolean isDebugEnabled() {
        return logger.isLoggable(Level.FINE);
    }

    public boolean isErrorEnabled() {
        return logger.isLoggable(Level.SEVERE);
    }

    public boolean isInfoEnabled() {
        return logger.isLoggable(Level.INFO);
    }

    public boolean isWarnEnabled() {
        return logger.isLoggable(Level.WARNING);
    }

    public void warn(String msg) {
        logger.logp(Level.WARNING, loggerName, null, msg);
    }

    public void warn(String msg, Throwable cause) {
        logger.logp(Level.WARNING, loggerName, null, msg, cause);
    }

    @Override
    public String toString() {
        return loggerName;
    }
    
	@Override
	public void debug(Throwable cause) {
		debug(cause.getMessage(), cause);
	}

	@Override
	public void error(Throwable cause) {
		error(cause.getMessage(), cause);
		
	}

	@Override
	public void info(Throwable cause) {
		info(cause.getMessage(), cause);
	}

	@Override
	public void warn(Throwable cause) {
		warn(cause.getMessage(), cause);
	}
}
