package net.zzong.omex;

import net.zzong.omex.model.User;
import net.zzong.omex.om.OMMaper;
import net.zzong.omex.om.OMMperFactory;
import net.zzong.omex.setting.SettingLoad;
import net.zzong.omex.setting.SettingVO;
import net.zzong.omex.supertypetoken.TypeReference;
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
            OMMperFactory omMperFactory = new OMMperFactory(basicDataSource, Arrays.asList(User.class));
            //orm매퍼 생성
            OMMaper omMaper = omMperFactory.getOMMaper();

            //필드값이 이메일만 있는경우
            User user = omMaper.query("select email from users",new TypeReference<User>(){}.type);
            System.out.println(user);
            //타입에 유저만 있을경우 한오브젝트만 매핑
            User user2 = omMaper.query("select * from users",new TypeReference<User>(){}.type);
            System.out.println(user2);
            //필드값에 이름만 있고 리스트타입에 유저가 있을경우
            List<User> users = omMaper.query("select name from users",new TypeReference<List<User>>(){}.type);
            System.out.println(users);
            //리스트타입에 유저가 있을경우
            List<User> users2 = omMaper.query("select * from users",new TypeReference<List<User>>(){}.type);
            System.out.println(users2);
            //맵타입에 유저가 있을경우
            Map<Long,User> userMap = omMaper.query("select * from users",new TypeReference<Map<Long,User>>(){}.type);
            System.out.println(userMap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
