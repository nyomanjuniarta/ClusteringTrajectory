package clustering;

import java.awt.Container;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.HierarchicalClusterer;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.AddCluster;
import weka.gui.hierarchyvisualizer.HierarchyVisualizer;

public class MainWeka {
	public static Instances readFile(String path, boolean hasId){
		ArrayList<Attribute> atts = new ArrayList<Attribute>();
		atts.add(new Attribute("ID",(List) null));
		atts.add(new Attribute("Sequence",(List) null));
		Instances instances = new Instances("SequenceDatabase",atts,10);
		Instance instance = new DenseInstance(2);
		
		BufferedReader br = null;
		String sCurrentLine, lines[];
		int id=0;
		try{
			br = new BufferedReader(new FileReader(path));
			while ((sCurrentLine = br.readLine()) != null) {
				if(hasId){
					lines = sCurrentLine.split(",");
					instance.setValue(atts.get(0), lines[0]);
					instance.setValue(atts.get(1), lines[1]);
				}
				else{
					instance.setValue(atts.get(0), String.valueOf(id));
					instance.setValue(atts.get(1), sCurrentLine);
					id++;
				}
				instances.add(instance);
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		return instances;
	}
	
	public static void main(String[] args){
		Instances instances = readFile(System.getProperty("user.dir")+"\\254VisitorsWithDuration.txt",false);

		for(int i=0;i<instances.size();i++){
			System.out.println(instances.get(i).stringValue(0) + " " + instances.get(i).stringValue(1));
		}
		DistanceFunction acs = new AllCommonSubsequences();
		HierarchicalClusterer hierarchicalClusterer = new HierarchicalClusterer();
		try {
			String[] options = new String[2];
			options[0] = "-L";
			options[1] = "SINGLE";
			hierarchicalClusterer.setOptions(options);
			hierarchicalClusterer.setDistanceFunction(acs);
			hierarchicalClusterer.setNumClusters(4);
			hierarchicalClusterer.setDistanceIsBranchLength(false);
			hierarchicalClusterer.setDebug(true);
			hierarchicalClusterer.buildClusterer(instances);
			
			String[] opsi = hierarchicalClusterer.getOptions();
			for(int y=0;y<opsi.length;y++){
				System.out.print(opsi[y] + " ");
			}
			System.out.println("");
			System.out.println("end of clustering");
			System.out.println(hierarchicalClusterer.getNumClusters());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
