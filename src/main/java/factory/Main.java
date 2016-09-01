package factory;

import factory.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Jeka on 24/08/2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext springContext = new AnnotationConfigApplicationContext("factory");
        ApplicationContext context = new ApplicationContext(springContext);

        MyService myService = context.createObject(MyService.class);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.println(myService.getAddress() + " " + myService.getName());
            }
            Thread.sleep(1000);
        }
    }
}
