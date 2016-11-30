package com.yhsjedu.datacloud.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;



public class ReflectUitls {

    //获取泛型上的具体类型（第一个）
    public static Class<?> getGenericType(Class<?> clazz) {
        return getGenericType(clazz, 0);
    }

    //获取泛型上的具体类型（指定哪个类型）
    public static Class<?> getGenericType(Class<?> clazz, int i) {
        Type type = clazz.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            return (Class<?>) types[i];
        }
        return null;
    }


    /**
	 * 
	 * java反射bean的get方法
	 * 
	 * @param clazz javaBean对象类型
	 * @param fieldName 字段名称
	 * @return get方法
	 */
	public static Method getGetter(Class<?> clazz, String fieldName) throws NoSuchMethodException {
		// get+字段名第一个字母小写，得到get方法名

        // 拿到拷贝源上的属性器数组
        try {
            PropertyDescriptor[] objPds = Introspector.getBeanInfo(
                    clazz).getPropertyDescriptors();

            for (int i = 0; objPds.length > 1 && i < objPds.length; i++) {
                //跳出从object继承的class属性,源上必须有get方法
                if (Class.class == objPds[i].getPropertyType()
                        || objPds[i].getReadMethod() == null) {
                    continue;
                }

                if (objPds[i].getName().equals(fieldName)){
                    return objPds[i].getReadMethod();
                }
            }

        } catch (IntrospectionException e) {
            throw new NoSuchMethodException(e.getMessage());
        }

        return null;

	}

	

}
