package net.zzong.ormex.orm;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by 김종인 on 2017-02-21.
 */
@Getter
public class ORMMperFactory {

    private Queue<ORMMaper> ormMapers;
    private BasicDataSource dataSource;
    private Map<Class, ObjectInformation<Class>> objectInformationMap;

    public ORMMperFactory(BasicDataSource dataSource, List<Class> objectList) {
        this.ormMapers = new ConcurrentLinkedDeque<>();
        this.dataSource = dataSource;
        this.objectInformationMap = objectInformationMap(objectList);
    }

    private Map<Class,ObjectInformation<Class>> objectInformationMap (List<Class> objectList){
        Map<Class,ObjectInformation<Class>> entityInfomationMap = new ConcurrentHashMap<>();
        if(objectList==null || objectList.size() <= 0) throw new RuntimeException("entytiList 값이 없다");
        for (Class aClass : objectList) {
                entityInfomationMap.put(aClass, new ObjectInformation(aClass));
        }
        return entityInfomationMap;
    }

    public ORMMaper getORMMaper() {
        ORMMaper entityManager = null;
        try {
            entityManager = new ORMMaper(this.dataSource.getConnection(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.ormMapers.add(entityManager);
        return entityManager;
    }

    public void close(){
        this.ormMapers.stream().forEach(ORMMaper::close);
    }
}
