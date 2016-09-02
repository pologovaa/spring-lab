package db.com.koala.factory.injectrandomdata;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectRandomData {
    int min() default 0;
    int max() default 0;
    String minDate() default "";
    String maxDate() default "";
    TextType type() default TextType.DEFAULT;
}
