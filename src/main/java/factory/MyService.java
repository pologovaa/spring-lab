package factory;

import factory.cache.Cache;
import factory.injectspringbean.InjectSpringBean;
import factory.singleton.Singleton;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
@Singleton
public class MyService {
    @InjectSpringBean
    private MyDao dao;// = new MyDao();

    @Cache(duration = 500)
    public String getAddress() {
        return dao.getAddress();
    }

    @Cache
    public String getName() {
        return dao.getName();
    }
}
