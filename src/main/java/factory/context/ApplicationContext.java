package factory.context;

import factory.singleton.Singleton;
import org.reflections.Reflections;

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
        System.out.println("Application context created");
    }

    public <T> T createObject(Class<T> type) {
        return objectFactory.createObject(type);
    }

}
