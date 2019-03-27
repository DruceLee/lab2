import java.util.Date;

public class Task {
    private String title;
    private Date time = null;
    private Date start = null;
    private Date end = null;
    private int interval;
    private boolean active;

    public Task() {
    }

    public Task(String title) {
        this.title = title;
    }

    public Task(String title, Date start, Date end, int interval, boolean active) {
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
        this.active = active;
    }

    public Task(String title, Date time, boolean active) {
        //
        this.title = title;
        this.time = time;
        this.active = active;
    }

    public String getTitle() {
        return title;
    }

    public Date getTime() {
        return time;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public int getInterval() {
        return interval;
    }

    public boolean isActive() {
        return active;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
