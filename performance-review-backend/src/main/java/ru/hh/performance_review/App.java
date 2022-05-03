package ru.hh.performance_review;

import ru.hh.nab.starter.NabApplication;
import ru.hh.performance_review.config.ProdConfig;

public class App {

    public static void main(String[] args) {
        NabApplication
                .builder()
                .configureJersey()
                .bindToRoot()
                .build()
                .run(ProdConfig.class);
    }
}
