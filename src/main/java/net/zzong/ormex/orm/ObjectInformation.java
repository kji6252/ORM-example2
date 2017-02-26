package net.zzong.ormex.orm;

import lombok.Data;
import net.zzong.ormex.supertypetoken.TypeReference;

import javax.persistence.Id;
import javax.security.auth.kerberos.KerberosTicket;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by 김종인 on 2017-02-21.
 */
@Data
public class ObjectInformation<T> {
    Class<T> entityClass;
    String nativeTableName;
    BeanInfo beanInfo;
    List<PropertyDescriptor> propertyDescriptor;
    Field keyField;

    public ObjectInformation(Class<T> entityClass) {
        this.entityClass = entityClass;
        try {
            nativeTableName = entityClass.getAnnotation(javax.persistence.Table.class).name();
            beanInfo = Introspector.getBeanInfo(entityClass);
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
        keyField = Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(Id.class) instanceof Id)
                .findFirst()
                .orElse(null);
        keyField.setAccessible(true);

        System.out.println("keyField = "+keyField);
    }
}
