package databox.task;

import java.io.File;

public abstract class AbstractTaskChecker {
    private File file;

    public abstract boolean check();
}
