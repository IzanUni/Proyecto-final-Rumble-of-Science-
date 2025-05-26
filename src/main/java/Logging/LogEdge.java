package Logging;

import java.time.LocalDateTime;

public class LogEdge {
    public final LogNode from, to;
    public final String action;     // ej. "move", "attack", "spawn"
    public final LocalDateTime when;

    public LogEdge(LogNode from, LogNode to, String action) {
        this.from   = from;
        this.to     = to;
        this.action = action;
        this.when   = LocalDateTime.now();
    }
}
