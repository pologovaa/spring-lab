package factory.async;

import factory.context.ProxyConfigurator;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.reflections.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
public class AsyncAnnotationProxyConfigurator implements ProxyConfigurator {

    private ExecutorService executorService = Executors.newCachedThreadPool();

    @Override
    public Object wrapWithProxy(Object t, Class type) {
        Set<Method> methods = ReflectionUtils.getAllMethods(type, method -> method.isAnnotationPresent(Async.class));
        if (!methods.isEmpty()) {
            if (type.getInterfaces().length == 0) {
                return Enhancer.create(type, (InvocationHandler) (proxy, method, args) -> invocationHandlerInvoke(t, type, method, args));
            }else {
                return Proxy.newProxyInstance(type.getClassLoader(), type.getInterfaces(), (proxy, method, args) -> invocationHandlerInvoke(t, type, method, args));
            }
        }
        return t;
    }

    private Object invocationHandlerInvoke(Object t, Class type, Method method, Object[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method originalClassMethod = type.getMethod(method.getName(), method.getParameterTypes());
        if (originalClassMethod.isAnnotationPresent(Async.class)) {
            return executorService.submit(() -> method.invoke(t, args));
        } else {
            return method.invoke(t, args);
        }
    }
}
