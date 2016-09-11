package db.com.koala.factory;

import db.com.koala.factory.cache.Cache;
import db.com.koala.factory.injectspringbean.InjectSpringBean;
import db.com.koala.factory.singleton.Singleton;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
@Singleton
public class MyService {
    @InjectSpringBean
    private MyDao dao;

    @Cache(duration = 500)
    public String getAddress() {
        return dao.getAddress();
    }

    @Cache
    public String getName() {
        return dao.getName();
    }
}
