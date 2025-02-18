package com.dat3m.dartagnan.program.processing;

import com.dat3m.dartagnan.program.Program;
import com.dat3m.dartagnan.program.processing.compilation.Compilation;
import org.sosy_lab.common.configuration.Configuration;
import org.sosy_lab.common.configuration.InvalidConfigurationException;
import org.sosy_lab.common.configuration.Option;
import org.sosy_lab.common.configuration.Options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.dat3m.dartagnan.configuration.OptionNames.*;

@Options
public class ProcessingManager implements ProgramProcessor {

    private final List<ProgramProcessor> programProcessors = new ArrayList<>();

    // =========================== Configurables ===========================

    @Option(name = REDUCE_SYMMETRY,
            description = "Reduces the symmetry of the program (unsound in general).",
            secure = true)
    private boolean reduceSymmetry = false;

    @Option(name = CONSTANT_PROPAGATION,
            description = "Performs constant propagation.",
            secure = true)
    private boolean constantPropagation = true;

    @Option(name = DEAD_ASSIGNMENT_ELIMINATION,
            description = "Performs dead code elimination.",
            secure = true)
    private boolean dce = true;

    @Option(name = DYNAMIC_PURE_LOOP_CUTTING,
            description = "Instruments loops to terminate early when spinning.",
            secure = true)
    private boolean dynamicPureLoopCutting = true;

    // =================== Debugging options ===================

    @Option(name = PRINT_PROGRAM_BEFORE_PROCESSING,
            description = "Prints the program before any processing.",
            secure = true)
    private boolean printBeforeProcessing = false;

    @Option(name = PRINT_PROGRAM_AFTER_SIMPLIFICATION,
            description = "Prints the program after simplification.",
            secure = true)
    private boolean printAfterSimplification = false;

    @Option(name = PRINT_PROGRAM_AFTER_COMPILATION,
            description = "Prints the program after compilation.",
            secure = true)
    private boolean printAfterCompilation = false;

    @Option(name = PRINT_PROGRAM_AFTER_UNROLLING,
            description = "Prints the program after unrolling.",
            secure = true)
    private boolean printAfterUnrolling = false;

    @Option(name = PRINT_PROGRAM_AFTER_PROCESSING,
            description = "Prints the program after all processing.",
            secure = true)
    private boolean printAfterProcessing = false;


// ======================================================================

    private ProcessingManager(Configuration config) throws InvalidConfigurationException {
        config.inject(this);

        programProcessors.addAll(Arrays.asList(
                printBeforeProcessing ? DebugPrint.withHeader("Before processing") : null,
                UnreachableCodeElimination.fromConfig(config),
                ComplexBlockSplitting.newInstance(),
                BranchReordering.fromConfig(config),
                LoopFormVerification.fromConfig(config),
                Simplifier.fromConfig(config),
                printAfterSimplification ? DebugPrint.withHeader("After simplification") : null,
                Compilation.fromConfig(config),
                printAfterCompilation ? DebugPrint.withHeader("After compilation") : null,
                SimpleSpinLoopDetection.fromConfig(config),
                LoopUnrolling.fromConfig(config),
                printAfterUnrolling ? DebugPrint.withHeader("After loop unrolling") : null,
                dynamicPureLoopCutting ? DynamicPureLoopCutting.fromConfig(config) : null,
                constantPropagation ? SparseConditionalConstantPropagation.fromConfig(config) : null,
                dce ? DeadAssignmentElimination.fromConfig(config) : null,
                RemoveDeadCondJumps.fromConfig(config),
                reduceSymmetry ? SymmetryReduction.fromConfig(config) : null,
                MemoryAllocation.newInstance(),
                EventIdReassignment.newInstance(), // Normalize used Ids (remove any gaps)
                printAfterProcessing ? DebugPrint.withHeader("After processing") : null,
                LogProgramStatistics.newInstance()
        ));
        programProcessors.removeIf(Objects::isNull);
    }

    public static ProcessingManager fromConfig(Configuration config) throws InvalidConfigurationException {
        return new ProcessingManager(config);
    }

    // ==================================================

    public void run(Program program) {
        programProcessors.forEach(p -> p.run(program));
    }


}