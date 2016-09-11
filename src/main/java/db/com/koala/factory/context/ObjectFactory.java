package db.com.koala.factory.context;

import db.com.koala.factory.postproxy.PostProxy;
import db.com.koala.factory.singleton.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
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
    private Config config;
    @Setter
    @Getter
    private org.springframework.context.ApplicationContext springContext;

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
        Set<Class<? extends Config>> configs = scanner.getSubTypesOf(Config.class);
        if (configs.size() == 1) {
            config = configs.iterator().next().newInstance();
        }
        if (configs.size() > 1) {
            throw new IllegalStateException("There can not be more than one Koala config in one project");
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

    @SneakyThrows
    private <T> T instantiate(Class<T> type) {
        T t = type.newInstance();
        configure(t);
        invokeInitMethods(type, t);
        t = wrapWithProxy(type, t);
        invokePostProxyMethods(type, t);
        return t;
    }

    private <T> Class<T> resolveImpl(Class<T> type) {
        if (type.isInterface()) {
            Class implClass = null;
            if (config != null) {
                implClass = config.getImplClass(type);
            }
            if (implClass == null) {
                Set<Class<? extends T>> classes = scanner.getSubTypesOf(type);
                List<Class<? extends T>> notAbstract = new ArrayList<>();
                for (Class<? extends T> aClass : classes) {
                    if (!Modifier.isAbstract(aClass.getModifiers())) {
                        notAbstract.add(aClass);
                    }
                }
                if (notAbstract.size() == 0) {
                    throw new IllegalStateException("No implementations were found for " + type + ". Bind needed in your config.");
                }
                if (notAbstract.size() > 1) {
                    throw new IllegalStateException("More than one implementations were found for " + type + ": "
                            + notAbstract + ". Bind needed in your config");
                }
                type = (Class<T>) notAbstract.get(0);
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

    private <T> void invokeInitMethods(Class<T> type, T t) {
        invokeMethodAnnotatedWith(type, t, PostConstruct.class);
    }

    private <T> void invokePostProxyMethods(Class<T> type, T t) {
        invokeMethodAnnotatedWith(type, t, PostProxy.class);
    }

    @SneakyThrows
    private <T> void invokeMethodAnnotatedWith(Class<T> type, T t, Class<? extends Annotation> annotationClass) {
        Set<Method> methods = ReflectionUtils.getAllMethods(type, method -> method.isAnnotationPresent(annotationClass));
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








