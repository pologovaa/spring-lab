package factory;

import factory.async.Async;
import factory.benchmark.Benchmark;
import factory.injectbytype.InjectByType;
import factory.postproxy.PostProxy;
import factory.scheduled.Scheduled;
import factory.singleton.Singleton;
import lombok.Getter;

import javax.annotation.PostConstruct;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jeka on 24/08/2016.
 */
@Benchmark
@Singleton
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

    @Scheduled(from = "2016-09-01T12:30", to = "2016-09-01T15:56:00", frequency = 2, timeUnit = TimeUnit.SECONDS)
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
