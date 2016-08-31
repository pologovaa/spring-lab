package factory;

/**
 * Created by JavaSchoolSdudent on 31.08.2016.
 */
@RandomDataType(String.class)
public class RandomStringGenerator implements RandomDataGenerator {
    @Override
    public Object generateRandomValue(InjectRandomData annotation) {
        return annotation.type().getRandomValue();
    }
}
