package net.zzong.omex.om;

import net.zzong.omex.model.User;
import net.zzong.omex.supertypetoken.TypeReference;
import org.junit.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by 김종인 on 2017-02-26.
 */
public class exTest {
    @Test
    public void 클래스정보가져오기(){
        //System.out.println(User.class);
        //System.out.println(User.class.getGenericSuperclass());
        //Arrays.stream(User.class.getAnnotations()).forEach(System.out::println);
        Arrays.stream(User.class.getDeclaredFields()).forEach(System.out::println);
    }

    @Test
    public void 빈디스크립터() throws IntrospectionException {
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class);
        System.out.println(userBeanInfo.getBeanDescriptor());
    }

    @Test
    public void 프로퍼티디스크립터() throws IntrospectionException {
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class);
        Arrays.stream(userBeanInfo.getPropertyDescriptors()).forEach(System.out::println);
    }
    @Test
    public void 메서드디스크립터() throws IntrospectionException {
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class);
        Arrays.stream(userBeanInfo.getMethodDescriptors()).forEach(System.out::println);
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
    public void 타입토큰() throws IntrospectionException {
        System.out.println(Integer.class);
        System.out.println(String.class);
        System.out.println(List.class);
    }


    @Test
    public void 슈퍼타입토큰() throws IntrospectionException {
        class Sup<T>{}
        class Sub extends Sup<String> {}

        Sub sub = new Sub();
        System.out.println(sub.getClass().getGenericSuperclass());
    }

    @Test
    public void 슈퍼타입토큰2() throws IntrospectionException {
        class Sub<T>{}
        class Sup extends Sub<List<String>> {}

        ParameterizedType pt = (ParameterizedType) new Sup().getClass().getGenericSuperclass();

        System.out.println(pt.getActualTypeArguments()[0]);

        System.out.println(new TypeReference<List<String>>(){}.type);
    }

    @Test
    public void 타입토큰과슈퍼타입토큰비교() throws IntrospectionException {
        System.out.println(User.class);
        System.out.println(List.class);
        System.out.println(Map.class);

        System.out.println(new TypeReference<User>(){}.type);
        System.out.println(new TypeReference<List<User>>(){}.type);
        System.out.println(new TypeReference<Map<Long,User>>(){}.type);
        System.out.println(new TypeReference<List<List<User>>>(){}.type);
        System.out.println(new TypeReference<List<Map<Long,User>>>(){}.type);
    }

    @Test
    public void 노아규먼트생성자() throws IntrospectionException, IllegalAccessException, InstantiationException {
        class ExampleUser{
            public ExampleUser() {}
        }
        //Object o = ExampleUser.class.newInstance();
        System.out.println(ExampleUser.class);
        //ExampleUser eu = ;
        Arrays.stream(ExampleUser.class.getConstructors()).forEach(System.out::println);
        System.out.println();
    }









}
