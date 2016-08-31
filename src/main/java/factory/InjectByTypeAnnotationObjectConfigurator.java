package factory;

import factory.context.ApplicationContext;
import factory.context.ObjectFactory;
import org.reflections.ReflectionUtils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by Jeka on 24/08/2016.
 */
public class InjectByTypeAnnotationObjectConfigurator implements ObjectConfigurator {
    @Override
    public void configure(Object t) throws Exception {
        Set<Field> fields = ReflectionUtils.getAllFields(t.getClass());
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectByType.class)) {
                Class<?> type = field.getType();
                Object value;
                if (type.equals(WeakReference.class)) {
                    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                    Type[] actualTypeArguments = genericType.getActualTypeArguments();
                    if (actualTypeArguments.length == 0) {
                        throw new IllegalStateException("No generic parameters");
                    }
                    type = (Class<?>) actualTypeArguments[0];
                    value = ObjectFactory.getInstance().createObject(type);
                    value = new WeakReference<>(value);
                    } else {
                        value = ObjectFactory.getInstance().createObject(type);
                    }

                field.setAccessible(true);
                field.set(t,value);
            }
        }
    }
}
