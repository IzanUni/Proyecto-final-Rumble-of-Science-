package Logging;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LogNode {
    public final String id;
    public final String label;
    public final LocalDateTime createdAt;
    public final List<LogEdge> edges = new ArrayList<>();

    public LogNode(String id, String label) {
        this.id        = id;
        this.label     = label;
        this.createdAt = LocalDateTime.now();
    }
}
