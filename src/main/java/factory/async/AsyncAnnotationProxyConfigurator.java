package factory.async;

import factory.context.ProxyConfigurator;
import org.reflections.ReflectionUtils;
import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
public class AsyncAnnotationProxyConfigurator implements ProxyConfigurator {
    @Override
    public Object wrapWithProxy(Object t, Class type) {
        Set<Method> methods = ReflectionUtils.getAllMethods(type, method -> method.isAnnotationPresent(Async.class));
        if (!methods.isEmpty()) {
            if (type.getInterfaces().length == 0) {
                return Enhancer.create(type, (org.springframework.cglib.proxy.InvocationHandler) (proxy, method, args) -> invocationHandlerInvoke(t, type, method, args));
            }else {
                return Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(), (proxy, method, args) -> invocationHandlerInvoke(t, type, method, args));
            }
        }
        return t;
    }

    private Object invocationHandlerInvoke(Object t, Class type, Method method, Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method originalClassMethod = type.getMethod(method.getName(), method.getParameterTypes());
        if (originalClassMethod.isAnnotationPresent(Async.class)) {
            //TODO: execute in another thread
            System.out.println("Executing asyncronously");
            return method.invoke(t, args);
        } else {
            return method.invoke(t, args);
        }
    }
}
