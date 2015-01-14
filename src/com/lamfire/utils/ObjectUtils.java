package com.lamfire.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ObjectUtils {

	/**
	 * 设置对象属性
	 * 
	 * @param target
	 * @param propertyValues
	 */
	public static void setProperties(Object target, Map<String, Object> propertyValues) {
		if (target==null || propertyValues == null || propertyValues.isEmpty())
			return;
		try {
			PropertyDescriptor[] pdArray = ClassUtils.getPropertyDescriptorsArray(target.getClass());
			if(pdArray == null)return;
			for (PropertyDescriptor pd : pdArray) {
				String propertyName = pd.getName();
				Object value = propertyValues.get(propertyName);
				if (value != null) {
					setPropertyValue(target, pd, value);
				}
			}
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 设置对象属性
	 * 
	 * @param target
	 * @param name
	 * @param value
	 */
	public static void setProperty(Object target, String name, Object value) {

		PropertyDescriptor pd = ClassUtils.getPropertyDescriptor(target.getClass(), name);
		if (pd == null)
			return;
		setPropertyValue(target, pd, value);

	}

	/**
	 * 获得属性值
	 * 
	 * @param target
	 * @param propertyName
	 * @return
	 */
	public static Object getPropertyValue(Object target, String propertyName) {
		PropertyDescriptor pd = ClassUtils.getPropertyDescriptor(target.getClass(), propertyName);
		if (pd == null){
			throw new RuntimeException("the property '" + propertyName + "' not found");
		}
		return readPropertyValue(target, pd);

	}

    public static Field getField(Object target, String propertyName) {
          return ClassUtils.getField(target.getClass(),propertyName);
    }

    public static Object getFieldValue(Object target, String propertyName)  {
        Field field = getField(target,propertyName);
        boolean accessible = field.isAccessible();
        if(!accessible){
            field.setAccessible(true);
        }
        try {
            Object result = field.get(target);
            return result;
        } catch (IllegalAccessException e) {

        } finally {
            field.setAccessible(accessible);
        }
        return null;
    }

    public static void setFieldValue(Object target, String propertyName,Object value)  {
        Field field = getField(target,propertyName);
        boolean accessible = field.isAccessible();
        if(!accessible){
            field.setAccessible(true);
        }
        try {
            field.set(target,value);
        } catch (IllegalAccessException e) {

        } finally {
            field.setAccessible(accessible);
        }
    }

    protected static Object readPropertyValue(Object objInstance , PropertyDescriptor pd){
		if (pd == null){
			return null;
        }

		Method getter = pd.getReadMethod();
		if (getter == null) {
			return getFieldValue(objInstance,pd.getName());
		}
		return invokeMethod(objInstance, getter);
	}
	
	protected static void writePropertyValue(Object objInstance , PropertyDescriptor pd,Object value){
		if (pd == null){
			return ;
		}
		Method setter = pd.getWriteMethod();
		if (setter == null) {
			setFieldValue(objInstance,pd.getName(),value);
            return;
		}
		invokeMethod(objInstance, setter,value);
	}

	/**
	 * 设置对象属性
	 * 
	 * @param target
	 * @param pd
	 * @param value
	 */
	protected static void setPropertyValue(Object target, PropertyDescriptor pd, Object value) {
		
		if (target==null || pd == null){
			return;
		}
		
		if(value == null){
			//调用写入属性方法进行附值
            writePropertyValue(target,pd,value);
			return ;
		}
		
		Class propType = pd.getPropertyType();
		Object desValue = value;
		//如果是复杂对象则先转换复杂对象，再附值
		if(!value.getClass().isAssignableFrom(propType) && value instanceof Map){
			desValue = convertToObject((Map)value,propType);
            writePropertyValue(target,pd,desValue);
			return ;
		}
		
		//如果不是目标类型的子类，则进行类型转换
		if(!value.getClass().isAssignableFrom(propType)){
			desValue = TypeConvertUtils.convertValue(desValue, propType);
		}
		
		//调用写入属性方法进行附值
        writePropertyValue(target,pd,desValue);

	}
	
	public static <T>T convertToObject(Map<String, Object> map,Class<T> claxx){
		try {
			T desObject = claxx.newInstance();
			setProperties(desObject,map);
			return desObject;
		} catch (Exception e) {
			throw new RuntimeException("the class '" +claxx.getName() + "' not cerate new instance," + e.getMessage());
		}
	}
	
	
	public static Object invokeMethod(Object obj, Method invokeMethod, Object... args) {
		boolean hasChangeAccessibleFlag = false;
		if (!invokeMethod.isAccessible()) {
			invokeMethod.setAccessible(true);
			hasChangeAccessibleFlag = true;
		}
		try {
			return invokeMethod.invoke(obj, args);
		} catch (Exception e) {
			throw new RuntimeException("cannot invoke " + invokeMethod.getName() + " for " + obj.getClass().getName(), e);
		} finally {
			if (hasChangeAccessibleFlag) {
				invokeMethod.setAccessible(false);
			}
		}
	}
	
	public static Method getMethod(Object obj,String name,Object[] args) throws SecurityException, NoSuchMethodException{
		Class<?>[] types = null;
		if(args != null){
			types = new Class<?>[args.length];
			for(int i=0;i<args.length;i++){
				types[i] = args[i].getClass();
			}
		}
		return obj.getClass().getMethod(name, types);
	}

	/**
	 * 拷贝相同的属性值到目标对象
	 * 
	 * @param source
	 * @param dest
	 * @throws IntrospectionException 
	 */

	public static void copyProperties(Object source, Object dest) {
		
		PropertyDescriptor[] pds = null;
		try {
			pds = ClassUtils.getPropertyDescriptorsArray(source.getClass());
		} catch (IntrospectionException e) {
			
		}
		
		if(pds == null)return ;
		
		for (PropertyDescriptor pd : pds) {
			try {
				Object value = getPropertyValue(source, pd.getName());
				setProperty(dest, pd.getName(), value);
			} catch (Exception e) {
			}
		}

	}

}
