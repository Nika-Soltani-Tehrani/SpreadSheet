import java.util.*;

public class Spreadsheet {

    private final Map<Coordinate, Cell> spreadsheet = new HashMap<>();
    private Map<Coordinate, Set<Coordinate>> dependents = new HashMap<>();

    public Map<Coordinate, Cell> getSpreadsheet() {
        return spreadsheet;
    }

    public void setCellContent(String coordStr, String rawContent) {
        Coordinate coord = Coordinate.fromString(coordStr);
        CellContent content;
        if (rawContent.startsWith("=")) {
            content = new FormulaContent(rawContent);
        } else if (isNumeric(rawContent)) {
            content = new NumericContent(Double.parseDouble(rawContent));
        } else {
            content = new TextContent(rawContent);
        }

        // Build a simulated dependents graph reflecting removal of old incoming edges to `coord`
        Map<Coordinate, Set<Coordinate>> simulated = copyDependents();
        // Remove coord from all sets in the simulated graph
        for (Set<Coordinate> s : simulated.values()) {
            s.remove(coord);
        }
        // Also remove empty keys optionally (not necessary for correctness)

        // If new content is a formula, get its dependencies and check for cycles in simulated graph
        if (content instanceof FormulaContent) {
            FormulaContent fc = (FormulaContent) content;
            Set<Coordinate> newDeps = fc.getDependencies();

            for (Coordinate dep : newDeps) {
                // If coord can already reach dep in simulated graph, adding dep->coord closes a cycle
                boolean creates = pathExists(simulated, coord, dep);

                System.out.println("checking cycle for " + coord + " depending on " + dep + " => " + creates);
                if (creates) {
                    throw new IllegalStateException("Cyclic dependency detected: " + coord + " <-> " + dep);
                }
                // Add dep->coord in simulated to catch cycles across multiple newDeps
                simulated.computeIfAbsent(dep, k -> new HashSet<>()).add(coord);
            }
        }

        // Everything OK -> now apply the real changes, not in simulation
        // remove old incoming edges of coord from actual graph
        removeDependenciesOf(coord);

        spreadsheet.put(coord, new Cell(coord, content));

        // add new edges in real graph
        addDependencies(coord, content);

        // invalidate dependents
        invalidate(coord);
    }

    private Map<Coordinate, Set<Coordinate>> copyDependents() {
        Map<Coordinate, Set<Coordinate>> copy = new HashMap<>();
        for (Map.Entry<Coordinate, Set<Coordinate>> e : dependents.entrySet()) {
            copy.put(e.getKey(), new HashSet<>(e.getValue()));
        }
        return copy;
    }

    private void removeDependenciesOf(Coordinate coord) {
        for (Set<Coordinate> deps : dependents.values()) {
            deps.remove(coord);
        }
    }

    private void invalidate(Coordinate coord) {
        invalidateRecursive(coord, new HashSet<>());
    }

    private void invalidateRecursive(Coordinate coord, Set<Coordinate> visited) {
        if (!visited.add(coord)) return;

        Set<Coordinate> deps = dependents.get(coord);
        if (deps == null || deps.isEmpty()) return;

        for (Coordinate c : deps) {
            Cell cell = spreadsheet.get(c);
            if (cell != null && cell.getContent() instanceof FormulaContent) {
                FormulaContent fc = (FormulaContent) cell.getContent();
                fc.invalidate();
            }
            invalidateRecursive(c, visited);
        }
    }

    private void addDependencies(Coordinate coord, CellContent content) {
        if (content instanceof FormulaContent) {
            FormulaContent fc = (FormulaContent) content;
            for (Coordinate dep : fc.getDependencies()) {
                dependents.computeIfAbsent(dep, k -> new HashSet<>()).add(coord);
            }
        }
    }

    public Double getCellValue(String coordStr) {
        Coordinate coord = Coordinate.fromString(coordStr);
        Cell cell = spreadsheet.get(coord);
        if (cell == null) {
            throw new IllegalArgumentException("Cell not found: " + coordStr);
        }
        return cell.getValue(this);
    }

    public boolean isNumeric(String s) {
        if (s == null || s.isEmpty()) return false;
        try { Double.parseDouble(s); return true; }
        catch (NumberFormatException e) { return false; }
    }

    public static int toIndex(String col) {
        int result = 0;
        for (char ch : col.toCharArray()) {
            result = result * 26 + (ch - 'A' + 1);
        }
        return result;
    }

    public static String fromIndex(int index) {
        StringBuilder sb = new StringBuilder();
        while (index > 0) {
            index--;
            sb.insert(0, (char)('A' + (index % 26)));
            index /= 26;
        }
        return sb.toString();
    }

    public void rebuildDependencies() {
        dependents.clear();

        for (Map.Entry<Coordinate, Cell> entry : spreadsheet.entrySet()) {
            Coordinate coord = entry.getKey();
            CellContent content = entry.getValue().getContent();

            if (content instanceof FormulaContent) {
                FormulaContent fc = (FormulaContent) content;

                for (Coordinate dep : fc.getDependencies()) {
                    dependents.computeIfAbsent(dep, k -> new HashSet<>())
                            .add(coord);
                }
            }
        }
    }

    /**
     * Check if there is a path from `start` to `target` in the provided dependents graph.
     * Uses iterative DFS on the provided graph (so we can test prospective graphs safely).
     */
    private boolean pathExists(Map<Coordinate, Set<Coordinate>> graph, Coordinate start, Coordinate target) {
        if (start.equals(target)) return true;
        Set<Coordinate> visited = new HashSet<>();
        Deque<Coordinate> stack = new ArrayDeque<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            Coordinate curr = stack.pop();
            if (!visited.add(curr)) continue;

            Set<Coordinate> nexts = graph.get(curr);
            if (nexts == null) continue;
            for (Coordinate nxt : nexts) {
                if (nxt.equals(target)) return true;
                if (!visited.contains(nxt)) stack.push(nxt);
            }
        }
        return false;
    }

}
