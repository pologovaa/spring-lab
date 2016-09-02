package db.com.koala.factory.injectrandomdata;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JavaSchoolSdudent on 31.08.2016.
 */
@RandomDataType(int.class)
public class RandomNumberGenerator implements RandomDataGenerator {
    @Override
    public Object generateRandomValue(InjectRandomData annotation) {
        return ThreadLocalRandom.current().nextInt(annotation.min(), annotation.max());
    }
}
