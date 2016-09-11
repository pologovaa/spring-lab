package db.com.koala.factory;

import db.com.koala.factory.benchmark.Benchmark;
import db.com.koala.factory.injectbytype.InjectByType;
import db.com.koala.factory.postproxy.PostProxy;
import db.com.koala.factory.scheduled.Scheduled;
import db.com.koala.factory.singleton.Singleton;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * Created by Jeka on 24/08/2016.
 */
@Benchmark
@Singleton(lazy = true)
@Getter
public class IRobot {


    public void setAge(String age) {
        System.out.println("String");
    }

    public void setAge(int age) {
        System.out.println("int");
    }

    @InjectByType
    private Speaker speaker;
    @InjectByType
    private Cleaner cleaner;

    @PostProxy
    @Benchmark
    public void init() {
        System.out.println("Init method");
        System.out.println(cleaner.getClass().getName());
    }

    @Scheduled(from = "2016-09-01T12:30", frequency = 2, timeUnit = TimeUnit.SECONDS)
    //@Async
    @Benchmark
    public void cleanRoom(){
        speaker.speak("I started");
        Cleaner cleaner = this.cleaner;
        if (cleaner != null) {
            cleaner.clean();
        }
        speaker.speak("I finished");
    }
}
