package factory;

import factory.context.ApplicationContext;

import java.time.LocalDateTime;

/**
 * Created by Jeka on 24/08/2016.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
//        while (true) {
//            IRobot robot = ApplicationContext.getInstance().createObject(IRobot.class);
//            System.out.println(robot);
//            robot.cleanRoom();
//            Thread.sleep(2000);
//        }
        System.out.println(LocalDateTime.now());
        ApplicationContext.getInstance();//.createObject(IRobot.class);
    }
}
