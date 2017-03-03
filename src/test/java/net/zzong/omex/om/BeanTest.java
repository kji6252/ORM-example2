package net.zzong.omex.om;

import net.zzong.omex.model.User;
import net.zzong.omex.supertypetoken.TypeReference;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by 김종인 on 2017-02-25.
 */
public class BeanTest {
    @Test
    public void 빈정보확인() throws NoSuchFieldException {
        query(new TypeReference<List<List<List<List<User>>>>>() {});
        //query(new TypeReference<Map<String, User>>() {});
        //query(new TypeReference<String>() {});
    }


    <T> void query(TypeReference<T> tr) {
        if (tr.type instanceof Class<?>) {
            Class<T> clazz= (Class<T>) tr.type;
            System.out.println(String.class.equals(clazz));

        } else { //ParameterizedType
            ParameterizedType t = (ParameterizedType) tr.type;
            //System.out.println(List.class.equals((Class<T>)t.getRawType()));
            Arrays.stream(t.getActualTypeArguments()).forEach(type -> {
                //System.out.println(type);
                if(type instanceof ParameterizedType) {
                    Arrays.stream(((ParameterizedType) type).getActualTypeArguments()).forEach(type1 -> {
                        System.out.println("부모임");
                        System.out.println(type1);
                        query((ParameterizedType)type1);
                    });

                }
            });
        }

    }

    void query(ParameterizedType pt) {
        Arrays.stream(pt.getActualTypeArguments()).forEach((Type type) -> {
            if(type instanceof ParameterizedType) {
                System.out.println("자식을 태움");
                System.out.println(type);
                query((ParameterizedType)type);
            }
        });
    }
}


