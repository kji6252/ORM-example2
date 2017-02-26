package net.zzong.ormex.orm;


import net.zzong.ormex.supertypetoken.TypeReference;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.*;

/**
 * Created by 김종인 on 2017-02-21.
 */
public class ORMMaper {

    Connection connection;
    ORMMperFactory ORMMperFactory;

    public ORMMaper(Connection connection,ORMMperFactory ORMMperFactory) {
        this.connection = connection;
        this.ORMMperFactory = ORMMperFactory;
    }


    public <T> T query(String sql, TypeReference<T> tr) {
        T t=null;
        if(tr.type instanceof Class<?>){//TypeReference<String>
            t = classQuery(sql, (Class<T>)tr.type);
            //return ((Class<T>)tr.type).cast(map.get(tr.type));
        } else { //TypeReference<List<String>>
            ParameterizedType pt = (ParameterizedType) tr.type;
            if(List.class.equals(pt.getRawType())) {
                Class<T> cls =(Class<T>)pt.getActualTypeArguments()[0];
                return (T)listQuery(sql, cls);
            } else if(Map.class.equals(pt.getRawType())){
                Class<T> cls =(Class<T>)pt.getActualTypeArguments()[1];
                return (T)mapQuery(sql, cls);
            }
        }
        return t;
    }

    public <T> T classQuery(String sql, Class<T> returnClass){
        BeanInfo beanInfo = null;
        T returnObject = null;
        try {
            beanInfo = Introspector.getBeanInfo(returnClass);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }


        //connection.
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {

                    try {
                        returnObject = returnClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i=1;i<=metaData.getColumnCount();i++){
                        final String columnName = metaData.getColumnName(i).toLowerCase();
                        T finalReturnObject = returnObject;
                        Arrays.stream(beanInfo.getPropertyDescriptors())
                                .filter(propertyDescriptor -> propertyDescriptor.getName().equals(columnName))
                                .forEach(propertyDescriptor -> {
                                    try {
                                        propertyDescriptor.getWriteMethod()
                                                .invoke(finalReturnObject, rs.getObject(columnName));
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnObject;
    }

    public <T> List<T> listQuery(String sql, Class<T> returnClass){
        List<T> mappingObjects = new ArrayList<T>();
        ObjectInformation<T> objectInformation = (ObjectInformation<T>) this.ORMMperFactory.getObjectInformationMap().get(returnClass);
        //connection.
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    T entityObject= objectInformation.getEntityClass().newInstance();
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i=1;i<=metaData.getColumnCount();i++){
                        final String columnName = metaData.getColumnName(i).toLowerCase();
                        Arrays.stream(objectInformation.getBeanInfo().getPropertyDescriptors())
                                .filter(propertyDescriptor -> propertyDescriptor.getName().equals(columnName))
                                .forEach(propertyDescriptor -> {
                                    try {
                                        propertyDescriptor.getWriteMethod()
                                                .invoke(entityObject, rs.getObject(columnName));
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                    mappingObjects.add(entityObject);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mappingObjects;
    }

    public <K,V> Map<K,V> mapQuery(String sql, Class<V> returnClass){
        System.out.println(sql);
        Map<K,V> mappingObjects = new HashMap<K,V>();
        ObjectInformation<V> objectInformation = (ObjectInformation<V>) this.ORMMperFactory.getObjectInformationMap().get(returnClass);
        //connection.
        try (Statement statement = connection.createStatement()) {
            try (ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    V entityObject= objectInformation.getEntityClass().newInstance();
                    ResultSetMetaData metaData = rs.getMetaData();
                    for (int i=1;i<=metaData.getColumnCount();i++){
                        final String columnName = metaData.getColumnName(i).toLowerCase();
                        Arrays.stream(objectInformation.getBeanInfo().getPropertyDescriptors())
                                .filter(propertyDescriptor -> propertyDescriptor.getName().equals(columnName))
                                .forEach(propertyDescriptor -> {
                                    try {
                                        propertyDescriptor.getWriteMethod()
                                                .invoke(entityObject, rs.getObject(columnName));
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                    mappingObjects.put((K) objectInformation.getKeyField().get(entityObject),entityObject);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mappingObjects;
    }

    public void close(){
        try {
            this.connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.ORMMperFactory.getOrmMapers().remove(this);
    }
}
