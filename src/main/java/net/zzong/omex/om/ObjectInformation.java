package net.zzong.omex.om;

import lombok.Data;

import javax.persistence.Id;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 김종인 on 2017-02-21.
 */
@Data
public class ObjectInformation<T> {
    Class<T> objectClass;
    String nativeTableName;
    BeanInfo beanInfo;
    List<PropertyDescriptor> propertyDescriptor;
    Field keyField;

    public ObjectInformation(Class<T> objectClass) {
        this.objectClass = objectClass;
        try {
            nativeTableName = objectClass.getAnnotation(javax.persistence.Table.class).name();
            beanInfo = Introspector.getBeanInfo(objectClass);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        propertyDescriptor = Arrays.stream(beanInfo.getPropertyDescriptors())
                .filter(propertyDescriptor1 -> !propertyDescriptor1.getPropertyType().getName().equals("java.lang.Class"))
                .collect(Collectors.toList());
        /*
        keyPropertyDescriptor = propertyDescriptor.stream()
                .filter(propertyDescriptor1 -> propertyDescriptor1.getPropertyType().getAnnotation(Id.class) instanceof Id)
                .findFirst()
                .orElse(null);
        */
        keyField = Arrays.stream(objectClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(Id.class) instanceof Id)
                .findFirst()
                .orElse(null);
        keyField.setAccessible(true);

        System.out.println("keyField = "+keyField);
    }
}
