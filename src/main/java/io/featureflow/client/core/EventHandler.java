package io.featureflow.client.core;

import io.featureflow.client.FeatureFlowConfig;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by oliver.oldfieldhodge on 3/1/17.
 */
public class EventHandler implements Closeable {
    private final BlockingQueue<Event> eventsQueue;

    public EventHandler(FeatureFlowConfig config) {
        eventsQueue = new ArrayBlockingQueue<Event>(10000);

    }

    public boolean doEvent(Event event){

        return true;

    }

    @Override
    public void close() throws IOException {

    }
}