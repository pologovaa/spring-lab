package factory.context;

import factory.singleton.Singleton;
import lombok.Getter;
import lombok.Setter;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Created by JavaSchoolSdudent on 31.08.2016.
 */
public class ApplicationContext {
//    @Setter
//    @Getter
//    private org.springframework.context.ApplicationContext springContext;
//    private static ApplicationContext context = new ApplicationContext();

//    public static ApplicationContext getInstance() {
//        return context;
//    }

    private final ObjectFactory objectFactory;
    private Reflections scanner = new Reflections();

    public ApplicationContext(org.springframework.context.ApplicationContext springContext) {
        objectFactory = ObjectFactory.getInstance();
        objectFactory.setSpringContext(springContext);
        createEagerSingletons();
    }

    public ApplicationContext() {
        objectFactory = ObjectFactory.getInstance();
        createEagerSingletons();
    }

    private void createEagerSingletons() {
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

}
