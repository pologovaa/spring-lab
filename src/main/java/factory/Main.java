package factory;

import factory.context.ApplicationContext;

/**
 * Created by Jeka on 24/08/2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        while (true) {
            IRobot iRobot = ApplicationContext.getInstance().createObject(IRobot.class);
            System.out.println(iRobot);
            iRobot.cleanRoom();
            Thread.sleep(2000);
        }
      /*  MyService<Integer> service = ObjectFactory.getInstance().createObject(MyService.class);
        service.a(12);
        service.b();*/
    }
}
