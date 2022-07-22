package org.example;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestCounter {
    AtomicInteger allRequestCounter = new AtomicInteger();
    AtomicInteger emailRequestCounter = new AtomicInteger();

    RequestCounter() {
        allRequestCounter.set(0);
        emailRequestCounter.set(0);
    }

    public void increaseAllRequestCounter() {
        allRequestCounter.incrementAndGet();
    }

    public void increaseEmailRequestCounter() {
        emailRequestCounter.incrementAndGet();
    }

    public void increaseAll() {
        increaseAllRequestCounter();
        increaseEmailRequestCounter();
    }

    public AtomicInteger getAllRequestCounter() {
        return allRequestCounter;
    }

    public AtomicInteger getEmailRequestCounter() {
        return emailRequestCounter;
    }

    public String statToString() {
        return "All request: " + allRequestCounter.toString() + " Email request: " + emailRequestCounter.toString();
    }

}
