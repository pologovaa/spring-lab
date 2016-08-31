package factory.context;

import factory.Config;
import factory.JavaConfig;
import factory.Singleton;
import lombok.Getter;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by JavaSchoolSdudent on 31.08.2016.
 */
public class ApplicationContext {
    private static ApplicationContext context = new ApplicationContext();

    public static ApplicationContext getInstance() {
        return context;
    }

    private final ObjectFactory objectFactory;
    private Reflections scanner = new Reflections();

    private ApplicationContext() {
        objectFactory = ObjectFactory.getInstance();
        Set<Class<?>> singletons = scanner.getTypesAnnotatedWith(Singleton.class);
        for (Class<?> singleton : singletons) {
            Singleton annotation = singleton.getAnnotation(Singleton.class);
            if (!annotation.lazy() && !objectFactory.getSingletonsMap().containsKey(singleton)) {
                objectFactory.getSingletonsMap().put(singleton, objectFactory.createObject(singleton));
            }
        }
    }

    public <T> T createObject(Class<T> type) {
        return objectFactory.createObject(type);
    }

/*    public <T> T createObject(Class<T> type) {
        type = resolveImpl(type);
        if (type.isAnnotationPresent(Singleton.class) && singletonsMap.containsKey(type)) {
            return (T) singletonsMap.get(type);
        }
        T t = objectFactory.createObject(type);
        singletonsMap.put(type, t);
        return t;
    } */

}
