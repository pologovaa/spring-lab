package factory.async;

import factory.context.ProxyConfigurator;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.reflections.ReflectionUtils;
//import org.springframework.cglib.proxy.Enhancer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by JavaSchoolSdudent on 01.09.2016.
 */
public class AsyncAnnotationProxyConfigurator implements ProxyConfigurator {
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
            System.out.println("Executing asyncronously");
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future<Object> future = executorService.submit(() -> method.invoke(t, args));
            return future; //TODO future?
//            return method.invoke(t, args);
        } else {
            return method.invoke(t, args);
        }
    }
}
