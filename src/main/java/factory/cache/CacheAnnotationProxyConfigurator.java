package factory.cache;

import com.google.common.cache.CacheBuilder;
import factory.context.ProxyConfigurator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.reflections.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
public class CacheAnnotationProxyConfigurator implements ProxyConfigurator {

    private Map<Method, com.google.common.cache.Cache<MethodInvocation, Object>> cacheForMethod = new HashMap<>();

    @Override
    public Object wrapWithProxy(Object t, Class type) {
        Set<Method> methods = ReflectionUtils.getAllMethods(type, method -> method.isAnnotationPresent(Cache.class));
        if (!methods.isEmpty()) {
            if (type.getInterfaces().length == 0) {
                return Enhancer.create(type, (InvocationHandler) (proxy, method, args) -> invocationHandlerInvoke(t, type, method, args));
            }else {
                return Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(), (proxy, method, args) -> invocationHandlerInvoke(t, type, method, args));
            }
        }
        return t;
    }

    @SneakyThrows
    private Object invocationHandlerInvoke(Object t, Class type, Method method, Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method originalClassMethod = type.getMethod(method.getName(), method.getParameterTypes());
        if (originalClassMethod.isAnnotationPresent(Cache.class)) {
            if (!cacheForMethod.containsKey(originalClassMethod)) {
                Cache annotation = originalClassMethod.getAnnotation(Cache.class);
                int duration = annotation.duration();
                TimeUnit timeUnit = annotation.timeUnit();
                cacheForMethod.put(originalClassMethod, CacheBuilder.newBuilder().expireAfterAccess(duration, timeUnit).build());
            }

            com.google.common.cache.Cache<MethodInvocation, Object> methodCache = cacheForMethod.get(originalClassMethod);
            MethodInvocation invocation = new MethodInvocation(t, method, args);
            Object retVal = methodCache.getIfPresent(invocation);
            if (retVal == null) {
                retVal = method.invoke(t, args);
                methodCache.put(invocation, retVal);
            }
            return retVal;
        } else {
            return method.invoke(t, args);
        }
    }

    @Data
    @AllArgsConstructor
    @EqualsAndHashCode
    private class MethodInvocation {
        private Object object;
        private Method method;
        private Object[] args;
    }
}
