package net.zzong.ormex;

import net.zzong.ormex.model.User;
import net.zzong.ormex.orm.ORMMaper;
import net.zzong.ormex.orm.ORMMperFactory;
import net.zzong.ormex.setting.SettingLoad;
import net.zzong.ormex.setting.SettingVO;
import net.zzong.ormex.supertypetoken.TypeReference;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by 김종인 on 2017-02-24.
 */
public class Main {
    public static void main(String... args){
        try {
            //셋팅 정보 로드
            SettingVO settingVO = SettingLoad.loadProperties(new FileInputStream(ClassLoader.getSystemResource("db.properties").getFile()));
            //DataSource 생성
            BasicDataSource basicDataSource = SettingLoad.loadBasicDataSource(settingVO);
            //orm매퍼를 생성한 팩토리 생성
            ORMMperFactory ormMperFactory = new ORMMperFactory(basicDataSource, Arrays.asList(User.class));
            //orm매퍼 생성
            ORMMaper ormMaper = ormMperFactory.getORMMaper();


            //필드값이 이메일만 있는경우
            System.out.println(ormMaper.query("select email from users",new TypeReference<User>(){}));
            //타입에 유저만 있을경우 한오브젝트만 매핑
            System.out.println(ormMaper.query("select * from users",new TypeReference<User>(){}));
            //필드값에 이름만 있고 리스트타입에 유저가 있을경우
            System.out.println(ormMaper.query("select name from users",new TypeReference<List<User>>(){}));
            //리스트타입에 유저가 있을경우
            System.out.println(ormMaper.query("select * from users",new TypeReference<List<User>>(){}));
            //맵타입에 유저가 있을경우
            System.out.println(ormMaper.query("select * from users",new TypeReference<Map<Long,User>>(){}));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
