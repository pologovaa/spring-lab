package db.com.koala.factory.context;

/**
 * Created by Jeka on 24/08/2016.
 */
public interface ProxyConfigurator {
    Object wrapWithProxy(Object t, Class type);
}
