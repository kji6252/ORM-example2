package net.zzong.ormex.orm;

import net.zzong.ormex.model.User;
import org.junit.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by 김종인 on 2017-02-26.
 */
public class exTest {
    @Test
    public void 클래스정보가져오기(){
        System.out.println(User.class);
        System.out.println(User.class.getGenericSuperclass());
        Arrays.stream(User.class.getAnnotations()).forEach(System.out::println);
        Arrays.stream(User.class.getDeclaredFields()).forEach(System.out::println);
    }

    @Test
    public void 클래스정보가져오기2() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        System.out.println(beanInfo.getBeanDescriptor());
        Arrays.stream(beanInfo.getPropertyDescriptors()).forEach(System.out::println);
        Arrays.stream(beanInfo.getMethodDescriptors()).forEach(System.out::println);
    }

    @Test
    public void 심플매핑() throws IntrospectionException {
        List<Map<String,Object>> datas = new ArrayList<>();
        Map<String,Object> mapData = new HashMap<>();
        mapData.put("id",0);
        mapData.put("name","철수");
        mapData.put("email","asdf@naver.com");
        mapData.put("regdt",new Date());
        datas.add(mapData);
        mapData = new HashMap<>();
        mapData.put("id",1);
        mapData.put("name","영희");
        mapData.put("email","zxczxc@gmail.com");
        mapData.put("regdt",new Date());
        datas.add(mapData);

        BeanInfo beanInfo = Introspector.getBeanInfo(User.class);
        List<User> users = new ArrayList<>();
        datas.stream().forEach(stringObjectMap -> {
            User u = new User();
            Arrays.stream(beanInfo.getPropertyDescriptors()).forEach(propertyDescriptor -> {
                if(!propertyDescriptor.getName().equals("class")) {
                    try {
                        propertyDescriptor.getWriteMethod().invoke(u, stringObjectMap.get(propertyDescriptor.getName()));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
            users.add(u);
        });
        System.out.println(users);
    }

    @Test
    public void 슈퍼타입토큰() throws IntrospectionException {
        class Sub<T>{}
        class Sup extends Sub<String> {}

        Sup sup = new Sup();
        List<String> list= new ArrayList<String>();
        System.out.println(sup.getClass().getGenericSuperclass());
        System.out.println(list.getClass().getGenericSuperclass());
    }





}
