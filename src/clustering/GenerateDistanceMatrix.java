package clustering; // be careful about the presence of sequence index

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import similarity.ACS;
import similarity.ADS;
import similarity.Pars;

public class GenerateDistanceMatrix {
	public static void main(String[] args){
		BufferedReader br = null;
		List<String> dataset = new ArrayList<String>();
		String sCurrentLine;
		BigDecimal distance, similarity, union, max, intersection;
		List<BigDecimal> allDist = new ArrayList<BigDecimal>();
		ACS acs = new ACS();
		ADS ads = new ADS();
		Pars pars=new Pars();
		int lineNum = 0;
		List<Vector> seq1, seq2;
		try{
			br = new BufferedReader(new FileReader("254hierarchical90secondsC.txt"));
			while((sCurrentLine = br.readLine()) != null){
				//sCurrentLine = sCurrentLine.split(",")[1];
				dataset.add(sCurrentLine);
				seq1=pars.pars_sequence_integer(sCurrentLine);
				allDist.add(ads.Number_Subsequence(seq1));
				//allDist.add(ads.approxNumberOfSequences(seq1,5));
			}
			br.close();
			PrintWriter writer = new PrintWriter("dist_254hierarchical90secondsC.txt", "UTF-8");
			for(int i=1;i<dataset.size();i++){
				System.out.println("i = " + i);
				for(int j=0;j<i;j++){
					seq1=pars.pars_sequence_integer(dataset.get(i));
					seq2=pars.pars_sequence_integer(dataset.get(j));
					intersection = acs.ACS_method(seq1,seq2);
					//intersection = acs.approxNumberOfCommonSequences(seq1, seq2, 2);
					max = allDist.get(i).max(allDist.get(j));
					
					similarity = intersection.divide(max,MathContext.DECIMAL128);
					distance = BigDecimal.ONE.subtract(similarity);//.movePointRight(12);
					writer.print(distance + " ");
				}
				writer.println();
			}
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
