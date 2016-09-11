package db.com.koala.factory.injectspringbean;

import db.com.koala.factory.context.ObjectConfigurator;
import db.com.koala.factory.context.ObjectFactory;
import org.reflections.ReflectionUtils;
import org.springframework.context.ApplicationContext;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by Jeka on 24/08/2016.
 */
public class InjectSpringBeanAnnotationObjectConfigurator implements ObjectConfigurator {

    @Override
    public void configure(Object t) throws Exception {
        Set<Field> fields = ReflectionUtils.getAllFields(t.getClass());
        for (Field field : fields) {
            if (field.isAnnotationPresent(InjectSpringBean.class)) {
                ApplicationContext springContext = ObjectFactory.getInstance().getSpringContext();
                if (springContext == null) {
                    throw new IllegalStateException("Spring application context must be provided to " + db.com.koala.factory.context.ApplicationContext.class +
                    "if you want to use " + InjectSpringBean.class.getName() + " annotation");
                }
                Class<?> type = field.getType();
                Object value;
                if (type.equals(WeakReference.class)) {
                    ParameterizedType genericType = (ParameterizedType) field.getGenericType();
                    Type[] actualTypeArguments = genericType.getActualTypeArguments();
                    if (actualTypeArguments.length == 0) {
                        throw new IllegalStateException("No generic parameters");
                    }
                    type = (Class<?>) actualTypeArguments[0];
                    value = springContext.getBean(type);
                    if (value == null) {
                        throw new IllegalStateException("There is no bean for " + type + " created by Spring");
                    }
                    value = new WeakReference<>(value);
                } else {
                    value = springContext.getBean(type);
                    if (value == null) {
                        throw new IllegalStateException("There is no bean for " + type + " created by Spring");
                    }
                }
                field.setAccessible(true);
                field.set(t, value);
            }
        }
    }
}
;