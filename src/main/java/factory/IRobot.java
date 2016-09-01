package factory;

import factory.async.Async;
import factory.benchmark.Benchmark;
import factory.injectbytype.InjectByType;
import lombok.Getter;

import javax.annotation.PostConstruct;
import java.lang.ref.WeakReference;

/**
 * Created by Jeka on 24/08/2016.
 */
@Benchmark
//@Singleton
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
    private WeakReference<Cleaner> cleaner;

    @PostConstruct
    public void init() {
        System.out.println(cleaner.getClass().getName());
    }

    //@Scheduled(from = "", to = "", frequency = 10, timeUnit = TimeUnit.SECONDS)
    @Async
    @Benchmark
    public void cleanRoom(){
        speaker.speak("I started");
        Cleaner cleaner = this.cleaner.get();
        if (cleaner != null) {
            cleaner.clean();
        }
        speaker.speak("I finished");
    }
}
