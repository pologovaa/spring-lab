package factory.context;

import factory.*;
import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by Jeka on 24/08/2016.
 */
public class ObjectFactory {
    private static ObjectFactory ourInstance = new ObjectFactory();
    private List<ObjectConfigurator> objectConfigurators = new ArrayList<>();
    private List<ProxyConfigurator> proxyConfigurators = new ArrayList<>();
    private Config config = new JavaConfig();

    @Getter
    private Map<Class, Object> singletonsMap = new HashMap<>();

    public static ObjectFactory getInstance() {
        return ourInstance;
    }

    private Reflections scanner = new Reflections();

    @SneakyThrows
    private ObjectFactory() {
        Set<Class<? extends ObjectConfigurator>> classes = scanner.getSubTypesOf(ObjectConfigurator.class);
        for (Class<? extends ObjectConfigurator> aClass : classes) {
            if (!Modifier.isAbstract(aClass.getModifiers())) {
                objectConfigurators.add(aClass.newInstance());
            }
        }
        Set<Class<? extends ProxyConfigurator>> classSet = scanner.getSubTypesOf(ProxyConfigurator.class);
        for (Class<? extends ProxyConfigurator> aClass : classSet) {
            if (!Modifier.isAbstract(aClass.getModifiers())) {
                proxyConfigurators.add(aClass.newInstance());
            }
        }
    }

    @SneakyThrows
    public <T> T createObject(Class<T> type) {
        type = resolveImpl(type);
        if (type.isAnnotationPresent(Singleton.class)) {
            if (singletonsMap.containsKey(type)) {
                return (T) singletonsMap.get(type);
            } else {
                T t = instantiate(type);
                singletonsMap.put(type, t);
                return t;
            }
        }
        return instantiate(type);
    }

    private <T> T instantiate(Class<T> type) throws Exception {
        T t = type.newInstance();
        configure(t);
        invokeInitMethods(type, t);
        t = wrapWithProxy(type, t);
        return t;
    }

    private <T> Class<T> resolveImpl(Class<T> type) {
        if (type.isInterface()) {
            Class implClass = config.getImplClass(type);
            if (implClass == null) {
                Set<Class<? extends T>> classes = scanner.getSubTypesOf(type);
                if (classes.size() != 1) {
                    //// TODO: 24/08/2016 сделайте хорошо
                    throw new RuntimeException("0 or more than one impl found for " + type + " bind needed impl in your config");
                }
                type = (Class<T>) classes.iterator().next();
            } else {
                type = implClass;
            }
        }
        return type;
    }


    private <T> T wrapWithProxy(Class<T> type, T t) {
        for (ProxyConfigurator proxyConfigurator : proxyConfigurators) {
            t = (T) proxyConfigurator.wrapWithProxy(t, type);
        }
        return t;
    }


    private <T> void invokeInitMethods(Class<T> type, T t) throws IllegalAccessException, InvocationTargetException {
        Set<Method> methods = ReflectionUtils.getAllMethods(type, method -> method.isAnnotationPresent(PostConstruct.class));
        for (Method method : methods) {
            method.invoke(t);
        }
    }


    private <T> void configure(T t) throws Exception {
        for (ObjectConfigurator objectConfigurator : objectConfigurators) {
            objectConfigurator.configure(t);
        }
    }
}








