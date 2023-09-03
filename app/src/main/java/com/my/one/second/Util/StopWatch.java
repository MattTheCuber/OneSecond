/*
Copyright (c) 2005, Corey Goldberg

StopWatch.java is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

Modified: Bilal Rabbani bilalrabbani1@live.com (Nov 2013)
*/

package com.my.one.second.Util;

public class StopWatch {
    private long startTime = 0;
    private boolean running = false;
    private boolean paused = false;
    private boolean hasRun = false;
    private long currentTime = 0;

    public void start() {
        this.currentTime = 0;
        this.startTime = System.currentTimeMillis();
        this.running = true;
        this.paused = false;
        this.hasRun = true;
    }

    public void stop() {
        this.running = false;
        this.paused = false;
    }

    public void pause() {
        this.running = false;
        this.paused = true;
        currentTime = System.currentTimeMillis() - startTime;
    }
    public void resume() {
        this.running = true;
        this.paused = false;
        this.startTime = System.currentTimeMillis() - currentTime;
    }

    public long getElapsedTimeMili() {
        long elapsed = 0;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime));
        }
        return elapsed;
    }

    public long getElapsedTimeSecs() {
        long elapsed = 0;
        if (running) {
            elapsed = ((System.currentTimeMillis() - startTime) / 1000) % 60;
        }
        return elapsed;
    }

    public String getSecondsAndMili() {
        String mili = Long.toString(getElapsedTimeMili());
        if (mili.length() == 1) {
            return "00.00";
        } else if (mili.length() == 2) {
            return "00.0" + mili.substring(0,1);
        } else if (mili.length() == 3) {
            return "00." + mili.substring(0,2);
        } else if (mili.length() == 4) {
            return "0" + mili.substring(0, 1) + "." + mili.substring(1,3);
        } else if (mili.length() == 5) {
            return mili.substring(0, 2) + "." + mili.substring(2, 4);
        } else if (mili.length() > 5) {
            return "00.00";
        }
        return "Error: 3x73-85";
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    public boolean hasRun() {
        return hasRun;
    }
}
