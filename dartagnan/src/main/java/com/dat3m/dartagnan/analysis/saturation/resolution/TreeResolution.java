package com.dat3m.dartagnan.analysis.saturation.resolution;

import com.dat3m.dartagnan.analysis.saturation.logic.Conjunction;
import com.dat3m.dartagnan.analysis.saturation.logic.SortedCubeSet;
import com.dat3m.dartagnan.analysis.saturation.reasoning.CoLiteral;
import com.dat3m.dartagnan.analysis.saturation.reasoning.CoreLiteral;
import com.dat3m.dartagnan.analysis.saturation.searchTree.DecisionNode;
import com.dat3m.dartagnan.analysis.saturation.searchTree.LeafNode;
import com.dat3m.dartagnan.analysis.saturation.searchTree.SearchNode;
import com.dat3m.dartagnan.analysis.saturation.searchTree.SearchTree;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/*
    Tree-based resolution works takes a SearchTree generated by the SaturationSolver and performs
    a resolution backwards starting from the leaf nodes which contain sets of violations.
    Upon reaching the root node, every decision (coherence) should be resolved
    and the remaining resolvents are coherence-less.
 */
public class TreeResolution {

    private final SearchTree tree;

    public TreeResolution(SearchTree tree) {
        this.tree = tree;
    }

    public SortedCubeSet<CoreLiteral> computeReasons() {
        reduceTree();

        List<Conjunction<CoreLiteral>> coreReasons = computeReasons(tree.getRoot());
        SortedCubeSet<CoreLiteral> result = new SortedCubeSet<>(coreReasons.size());
        result.addAll(coreReasons);
        result.simplify();
        return result;
    }


    private List<Conjunction<CoreLiteral>> computeReasons(SearchNode node) {
        if (node.isEmptyNode()) {
            return Collections.emptyList();
        } else if (node.isLeaf()) {
            return ((LeafNode)node).getInconsistencyReasons();
        } else {
            DecisionNode decNode = (DecisionNode) node;
            CoreLiteral resLit = new CoLiteral(decNode.getEdge());
            List<Conjunction<CoreLiteral>> positive = computeReasons(decNode.getPositive());
            List<Conjunction<CoreLiteral>> negative = computeReasons(decNode.getNegative());

            // A reason is non-resolvable if it does not contain the current decision literal
            Predicate<Conjunction<CoreLiteral>> isNotResolvable = (reason -> !reason.getLiterals().contains(resLit.getOpposite()));
            // From the negative branch, we remove all non-resolvable reasons and store them in <resolvents>.
            // The remaining ones will need to get resolved.
            List<Conjunction<CoreLiteral>> resolvents = negative.stream().filter(isNotResolvable).collect(Collectors.toList());
            negative.removeIf(isNotResolvable);

            for (Conjunction<CoreLiteral> c1 : positive) {
                if (!c1.getLiterals().contains(resLit)) {
                    // ... move all non-resolvable reasons to <resolvents>
                    resolvents.add(c1);
                } else {
                    // ... else resolve the violations
                    for (Conjunction<CoreLiteral> c2 : negative) {
                        Conjunction<CoreLiteral> resolvent = c1.resolveOn(c2, resLit);
                        if (!resolvent.isFalse()) {
                            resolvents.add(resolvent);
                        }
                    }
                }
            }

            // ==== TEST CODE =====
            //TODO: Remove the ugly conversion to clauseSet for minimization
            // Remark: SortedCubeSet.simplify does in general not give as good reduction as the reduction performed
            // by DNF.reduce. However, right now it seems that they are equivalently strong for 1-SAT at least
            SortedCubeSet<CoreLiteral> cubeSet = new SortedCubeSet<>(resolvents.size());
            cubeSet.addAll(resolvents);
            cubeSet.simplify();
            resolvents.clear();
            resolvents.addAll(cubeSet.getCubes());
            return resolvents;

        }
    }

    // ============= Preprocessing ================

    private void reduceTree() {
        removeUnproductiveNodes();
        // Are there any other reductions one can do?
    }

    /*
        This removes all unproductive reasons and decision nodes.
        - A literal co(w1, w2) is unproductive, if there is no reason containing the opposite literal co(w2, w1)
        - A reason is unproductive, if any of its co-literals is unproductive.
        - A leaf node is unproductive, if all its reasons are unproductive
        - A decision node is unproductive, if it has some empty leaf node.
        This method iteratively eliminates unproductive reasons until a fixed point is reached.
         -> Whenever a reason is eliminated, the set of unproductive literals may increase.
        Then it removes all unproductive decision nodes.
     */
    private void removeUnproductiveNodes() {
        List<LeafNode> leaves = (List<LeafNode>)tree.getRoot().findNodes(SearchNode::isLeaf);

        // Remove reasons that are unproductive until a fixed point is reached
        boolean progress;
        do {
            progress = false;
            // Find all resolvable literals (this set may change each iteration as reasons get eliminated!)
            Set<CoreLiteral> resolvableLits = leaves.stream()
                    .flatMap(leaf -> leaf.getInconsistencyReasons().stream())
                    .flatMap(reason -> reason.getResolvableLiterals().stream())
                    .collect(Collectors.toSet());

            for (LeafNode leaf : leaves) {
                // From each leaf, we remove all reasons that contain some unproductive/unresolvable literal
                progress |= leaf.getInconsistencyReasons().removeIf(reason -> reason.getResolvableLiterals().stream()
                        .anyMatch(lit -> !resolvableLits.contains(lit.getOpposite())));
            }
        } while (progress);


        // Remove decision nodes with some empty leaf and replace them by their non-empty subbranch
        for (LeafNode leaf : leaves) {
            if (!leaf.getInconsistencyReasons().isEmpty()) {
                continue;
            }

            DecisionNode decNode = leaf.getParent();
            SearchNode otherBranch = decNode.getPositive() == leaf ? decNode.getNegative() : decNode.getPositive();
            if (otherBranch != null) {
                decNode.replaceBy(otherBranch);
            } else {
                throw new IllegalStateException("Empty branches in TreeResolution. Probably some reason computation bug (again).");
            }
        }
    }
}
