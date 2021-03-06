package db.com.koala.factory;

import org.fluttercode.datafactory.impl.DataFactory;
import org.springframework.stereotype.Component;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
@Component
public class MyDaoImpl implements MyDao {
    private DataFactory dataFactory = new DataFactory();

    public String getAddress() {
        return dataFactory.getAddress();
    }

    public String getName() {
        return dataFactory.getName();
    }
}
