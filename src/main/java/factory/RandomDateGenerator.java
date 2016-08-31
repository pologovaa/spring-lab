package factory;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by JavaSchoolSdudent on 31.08.2016.
 */
@RandomDataType(LocalDate.class)
public class RandomDateGenerator implements RandomDataGenerator {

    @Override
    public Object generateRandomValue(InjectRandomData annotation) {
        long fromDay = LocalDate.parse(annotation.minDate()).toEpochDay();
        long toDay = LocalDate.parse(annotation.maxDate()).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(fromDay, toDay);
        return LocalDate.ofEpochDay(randomDay);
    }
}
