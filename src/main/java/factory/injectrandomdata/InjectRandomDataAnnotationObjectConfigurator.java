package factory.injectrandomdata;

import factory.ObjectConfigurator;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by Jeka on 24/08/2016.
 */
public class InjectRandomDataAnnotationObjectConfigurator implements ObjectConfigurator {
    private Map<Class, RandomDataGenerator> generatorMap = new HashMap<>();
    private Reflections reflections = new Reflections("factory");

    @SneakyThrows
    public InjectRandomDataAnnotationObjectConfigurator() {
        Set<Class<? extends RandomDataGenerator>> classes = reflections.getSubTypesOf(RandomDataGenerator.class);
        for (Class<? extends RandomDataGenerator> aClass : classes) {
            if (!Modifier.isAbstract(aClass.getModifiers())) {
                RandomDataType[] annotations = aClass.getAnnotationsByType(RandomDataType.class);
                if (annotations.length==0) {
                    throw new
                            IllegalStateException("" +
                            "if you implement" +
                            RandomDataGenerator.class.getSimpleName());
                }
                for (RandomDataType annotation : annotations) {
                    Class dataClass = annotation.value();
                    if (generatorMap.containsKey(dataClass)) {
                        throw new IllegalArgumentException("already in use");
                    }
                    generatorMap.put(dataClass, aClass.newInstance());
                }
            }
        }
    }

    @Override
    public void configure(Object t) throws Exception {
        Class<?> type = t.getClass();
        Set<Field> fields = ReflectionUtils.getAllFields(type);
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectRandomData.class)) {
                Class<?> fieldType = field.getType();
                RandomDataGenerator randomDataGenerator = generatorMap.get(fieldType);
                if (randomDataGenerator == null) {
                    throw new IllegalStateException("Can not generate random value for type " + fieldType);
                }
                Object randomValue = randomDataGenerator.generateRandomValue(field.getAnnotation(InjectRandomData.class));
                field.setAccessible(true);
                field.set(t, randomValue);
            }
        }
    }
}
