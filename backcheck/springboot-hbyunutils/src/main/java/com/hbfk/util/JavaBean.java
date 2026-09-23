package com.hbfk.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

public class JavaBean {
	public static void transformationClass(Object source, Object dest) throws Exception {
		BeanInfo sourceBean = Introspector.getBeanInfo(source.getClass(),java.lang.Object.class);
		PropertyDescriptor[] sourceProperty = sourceBean.getPropertyDescriptors();
		
		BeanInfo destBean = Introspector.getBeanInfo(dest.getClass(),java.lang.Object.class);
		PropertyDescriptor[] destProperty = destBean.getPropertyDescriptors();
		
		for (int i = 0; i < sourceProperty.length; i++) {
			for (int j = 0; j < destProperty.length; j++) {
				System.out.println(sourceProperty[i].getName());
				if(sourceProperty[i].getName().equals(destProperty[j].getName())) {
					destProperty[j].getWriteMethod().invoke(dest, sourceProperty[i].getReadMethod().invoke(source));
					break;
				}
			}
		}
		
	}
	
}
