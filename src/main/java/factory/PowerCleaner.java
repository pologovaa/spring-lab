package factory;

import factory.injectrandomdata.InjectRandomData;
import factory.injectrandomdata.TextType;

import java.time.LocalDate;

/**
 * Created by Jeka on 24/08/2016.
 */
public class PowerCleaner implements Cleaner {
    @InjectRandomData(min=3,max=7)
    private int repeat;

    @InjectRandomData(type = TextType.NAME)
    private String name;

    @InjectRandomData(minDate = "2016-08-31", maxDate = "2016-11-30")
    private LocalDate date;

    @Override
    @Benchmark
    public void clean() {
        System.out.println(LocalDate.now());
        System.out.println(LocalDate.now().plusMonths(3));

        for (int i = 0; i < repeat; i++) {
            System.out.println("VVVVVVVVVVvv   " + name + " " + date);
        }
    }
}
