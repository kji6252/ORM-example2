package net.zzong.omex.om;

import lombok.Getter;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Created by 김종인 on 2017-02-21.
 */
@Getter
public class OMMperFactory {

    private Queue<OMMaper> OMMapers;
    private BasicDataSource dataSource;
    private Map<Class, ObjectInformation<Class>> objectInformationMap;

    public OMMperFactory(BasicDataSource dataSource, List<Class> objectList) {
        this.OMMapers = new ConcurrentLinkedDeque<>();
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

    public OMMaper getOMMaper() {
        OMMaper omMaper = null;
        try {
            omMaper = new OMMaper(this.dataSource.getConnection(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.OMMapers.add(omMaper);
        return omMaper;
    }

    public void close(){
        this.OMMapers.stream().forEach(OMMaper::close);
    }
}
