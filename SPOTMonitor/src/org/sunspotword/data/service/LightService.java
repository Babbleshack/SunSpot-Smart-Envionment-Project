/**
 *
 * @author Dominic Lindsay
 */
package org.sunspotword.data.service;
import org.sunspotworld.homePatterns.TaskObservable;
import org.sunspotworld.homePatterns.TaskObserver;
import org.sunspotworld.spotMonitors.IMonitor;
public class LightService implements IService, TaskObserver {
    IMonitor monitor;
    public LightService(IMonitor monitor) {
        this.monitor = monitor;
    }
    public void startService() {
        this.monitor.startMonitor();
    }
    public void stopService() {
        this.monitor.stopMonitor();
    }

    public void update(TaskObservable o, Object arg) {
        //send data on radio
    }

    public void update(TaskObservable o) {
        //sends data on radio
    }
}
