package Logging;


public class LogEdge {
    private final LogNode from;
    private final LogNode to;
    private final String action;
    private final int turn;

    public LogEdge(LogNode from, LogNode to, String action, int turn) {
        this.from = from;
        this.to = to;
        this.action = action;
        this.turn = turn;
    }

    public LogNode getFrom() {
        return from;
    }

    public LogNode getTo() {
        return to;
    }

    public String getAction() {
        return action;
    }

    public int getTurn() {
        return turn;
    }
}
