package factory;

import factory.cache.Cache;
import factory.singleton.Singleton;
import org.fluttercode.datafactory.impl.DataFactory;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
@Singleton
public class MyService {
    private DataFactory dataFactory = new DataFactory();


    @Cache(duration = 500)
    public String getAddress() {
        return dataFactory.getAddress();
    }

//    @Cache
    public String getName() {
        return dataFactory.getName();
    }
}
