package Logging;

import Material.ListaSE;
import Material.IteradorSE;

public class LogNode {
    private final String id;
    private final String label;
    private final ListaSE<LogEdge> edges = new ListaSE<>();

    public LogNode(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void addEdge(LogEdge edge) {
        edges.add(edge);
    }

    public IteradorSE<LogEdge> getEdges() {
        return edges.getIterador();
    }
}
