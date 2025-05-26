package Logging;

import Material.ListaSE;
import Material.IteradorSE;

public class LogGraph {
    private final ListaSE<LogNode> nodes = new ListaSE<>();
    private final ListaSE<LogEdge> edges = new ListaSE<>();

    public LogNode getOrCreateNode(String id, String label) {
        IteradorSE<LogNode> it = nodes.getIterador();
        while (it.hasNext()) {
            LogNode n = it.next();
            if (n.getId().equals(id)) {
                return n;
            }
        }
        LogNode n = new LogNode(id, label);
        nodes.add(n);
        return n;
    }

    public void addEdge(String fromId, String fromLabel,
                        String toId,   String toLabel,
                        String action, int turn) {
        LogNode f = getOrCreateNode(fromId, fromLabel);
        LogNode t = getOrCreateNode(toId,   toLabel);
        LogEdge e = new LogEdge(f, t, action, turn);
        edges.add(e);
        f.addEdge(e);
    }

    public IteradorSE<LogEdge> getEdges() {
        return edges.getIterador();
    }
}
