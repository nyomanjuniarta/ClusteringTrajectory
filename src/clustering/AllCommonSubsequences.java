package clustering;

import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;

import similarity.ACS;
import similarity.Pars;
import weka.core.Instance;
import weka.core.NormalizableDistance;

public class AllCommonSubsequences extends NormalizableDistance {
	
	@Override
	public double distance(Instance first, Instance second) {
		//System.out.println("hitung " + first.stringValue(0) + " " + second.stringValue(0));
		Pars pars=new Pars();
		String s1 = first.stringValue(1);
		String s2 = second.stringValue(1);
		List<Vector> seq1 = pars.pars_sequence_integer(s1);
		List<Vector> seq2 = pars.pars_sequence_integer(s2);
		ACS acs = new ACS();
		double value=acs.ACS_method(seq1, seq2);
		return -value;
	}

	@Override
	public String getRevision() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String globalInfo() {
		return "All Common Subsequences";
	}

	@Override
	protected double updateDistance(double arg0, double arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

}
