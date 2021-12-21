package com.dat3m.dartagnan.witness;

import com.dat3m.dartagnan.parsers.cat.ParserCat;
import com.dat3m.dartagnan.parsers.program.ProgramParser;
import com.dat3m.dartagnan.program.Program;
import com.dat3m.dartagnan.utils.ResourceHelper;
import com.dat3m.dartagnan.utils.Result;
import com.dat3m.dartagnan.utils.Settings;
import com.dat3m.dartagnan.utils.TestHelper;
import com.dat3m.dartagnan.utils.options.DartagnanOptions;
import com.dat3m.dartagnan.verification.VerificationTask;
import com.dat3m.dartagnan.wmm.Wmm;
import com.dat3m.dartagnan.wmm.utils.Arch;
import com.dat3m.dartagnan.wmm.utils.alias.Alias;

import org.junit.Test;
import org.sosy_lab.java_smt.api.ProverEnvironment;
import org.sosy_lab.java_smt.api.SolverContext;
import org.sosy_lab.java_smt.api.SolverContext.ProverOptions;

import static com.dat3m.dartagnan.analysis.Base.runAnalysisIncrementalSolver;

import java.io.File;

public class BuildWitnessTest {

    @Test
    public void BuildWriteEncode() throws Exception {
    	Program p = new ProgramParser().parse(new File(ResourceHelper.TEST_RESOURCE_PATH + "witness/lazy01-O0.bpl"));
    	Wmm wmm = new ParserCat().parse(new File(ResourceHelper.CAT_RESOURCE_PATH + "cat/svcomp.cat"));
    	VerificationTask task = new VerificationTask(p, wmm, Arch.NONE, new Settings(Alias.CFIS, 0, 0));
    	
    	SolverContext ctx = TestHelper.createContext();
    	ProverEnvironment prover = ctx.newProverEnvironment(ProverOptions.GENERATE_MODELS);
    	
		Result res = runAnalysisIncrementalSolver(ctx, prover, task);
	    String[] sOptions = new String[8];
	    sOptions[0] = "-i";
	    sOptions[1] = "ignore.bpl";
	    sOptions[2] = "-cat";
	    sOptions[3] = "ignore";
	    sOptions[4] = "-unroll";
	    sOptions[5] = "1";
	    sOptions[6] = "-create_witness";
	    sOptions[7] = ResourceHelper.TEST_RESOURCE_PATH + "witness/lazy01-O0.bpl";
	    DartagnanOptions option = new DartagnanOptions();
	    option.parse(sOptions);
	    
		WitnessGraph graph = new WitnessBuilder(p, ctx, prover, res).buildGraph(option);
		// Write to file
		graph.write();
		// Create encoding
		graph.encode(p, ctx);
    }
}
