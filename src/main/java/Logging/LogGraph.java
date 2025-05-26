package Logging;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LogGraph {
    private final Map<String, LogNode> nodes = new HashMap<>();
    private final List<LogEdge> edges = new java.util.ArrayList<>();

    public LogNode getOrCreateNode(String id, String label) {
        return nodes.computeIfAbsent(id, k -> new LogNode(id, label));
    }

    public void addEdge(String fromId, String fromLabel,
                        String toId,   String toLabel,
                        String action) {
        LogNode f = getOrCreateNode(fromId, fromLabel);
        LogNode t = getOrCreateNode(toId,   toLabel);
        LogEdge e = new LogEdge(f, t, action);
        edges.add(e);
        f.edges.add(e);
    }
    public List<LogEdge> getEdges() {
        return edges.stream()
                .sorted((a,b) -> a.when.compareTo(b.when))
                .collect(Collectors.toList());
    }
}
