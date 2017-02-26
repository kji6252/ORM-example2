package net.zzong.ormex.supertypetoken;

import net.zzong.ormex.orm.ORMMperFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by 김종인 on 2017-02-25.
 */
public class TypeSafeMap{
    Map<Type,Object> map;

    <T> void put(TypeReference<T> tr, T value){
        map.put(tr.type,value);
    }

    <T> T get(TypeReference<T> tr){
        if(tr.type instanceof Class<?>)
            return ((Class<T>)tr.type).cast(map.get(tr.type)); //TypeReference<String>
        else
            return ((Class<T>)((ParameterizedType)tr.type).getRawType()).cast(map.get(tr.type)); //TypeReference<List<String>>
    }
}
