package clustering;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import similarity.ACS;
import similarity.ADS;
import similarity.Pars;


public class FindJumpingPatterns {
	public static void jepA(){
		int n = 216, i, id;
		char tag[] = new char[n+1], cluster;
		BufferedReader br = null;
		String sCurrentLine, extents[], cleanLine, clusters;
		boolean jumping;
		try{
			br = new BufferedReader(new FileReader("cluster_tagging.txt"));
			i = 1;
			while((sCurrentLine = br.readLine()) != null){
				tag[i] = sCurrentLine.charAt(0);
				i++;
			}
			br.close();

			PrintWriter writer = new PrintWriter("jumping patterns A.txt", "UTF-8");
			br = new BufferedReader(new FileReader("conceptsANoCat.txt"));
			i = 1;
			while((sCurrentLine = br.readLine()) != null){
				clusters = "";
				cleanLine = sCurrentLine.replaceAll("\\[", "").replaceAll("]", "");
				cleanLine = cleanLine.replaceAll("V00", "").replaceAll("V0", "").replaceAll("V", "");
				extents = cleanLine.split(",");
				id = Integer.parseInt(extents[0]);
				cluster = tag[id];
				clusters += cluster;
				jumping = true;
				for(int j=1;j<extents.length;j++){
					id = Integer.parseInt(extents[j]);
					clusters += tag[id];
					if(tag[id] != cluster){
						jumping = false;
					}
				}
				writer.print(clusters + " " + (jumping ? "1" : "0")  + '\n');
				i++;
			}
			br.close();
			writer.close();
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}		
	}

	public static void jepV(){
		int n = 216, i, id;
		char tag[] = new char[n+1], cluster;
		BufferedReader br = null;
		String sCurrentLine, extents[], cleanLine, element, clusters;
		boolean jumping;
		try{
			br = new BufferedReader(new FileReader("cluster_tagging.txt"));
			i = 1;
			while((sCurrentLine = br.readLine()) != null){
				tag[i] = sCurrentLine.charAt(0);
				i++;
			}
			br.close();

			PrintWriter writer = new PrintWriter("jumping patterns A.txt", "UTF-8");
			br = new BufferedReader(new FileReader("conceptsANoCat.txt"));
			i = 0;
			while((sCurrentLine = br.readLine()) != null){
				clusters = "";
				element = sCurrentLine.split(":")[2];
				cleanLine = element.replaceAll(" \\[", "").replaceAll("], \'f\'", "");
				cleanLine = cleanLine.replaceAll("V00", "").replaceAll("V0", "").replaceAll("V", "");
				extents = cleanLine.split(", ");
				if(extents[0].length() < 1) continue;
				id = Integer.parseInt(extents[0]) + 1;
				cluster = tag[id];
				clusters += cluster;
				jumping = true;
				for(int j=1;j<extents.length;j++){
					id = Integer.parseInt(extents[j])+1;
					clusters += tag[id];
					if(tag[id] != cluster){
						jumping = false;
					}
				}
				writer.print(clusters + " " + (jumping ? "1" : "0")  + '\n');
				i++;
			}
			br.close();
			writer.close();
			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}		
	}
	
	public static void replaceVByCluster(){
		int n = 216, i;
		char tag[] = new char[n+1], cluster;
		BufferedReader br = null;
		String sCurrentLine, extents[], cleanLine;
		try{
			br = new BufferedReader(new FileReader("cluster_tagging.txt"));
			i = 1;
			while((sCurrentLine = br.readLine()) != null){
				tag[i] = sCurrentLine.charAt(0);
				i++;
			}
			br.close();

			br = new BufferedReader(new FileReader("conceptByStab.txt"));
			while((sCurrentLine = br.readLine()) != null){
				cleanLine = sCurrentLine.replaceAll("\\[", "").replaceAll("]", "");
				cleanLine = cleanLine.replaceAll("V00", "").replaceAll("V0", "").replaceAll("V", "");
				extents = cleanLine.split(",");
				for(int j=0;j<extents.length;j++){
					cluster = tag[Integer.parseInt(extents[j])];
					System.out.print(cluster + " ");
				}
				System.out.println("");
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}		
	}
	
	public static void main(String[] args){
		jepA();
		//jepV();
		//replaceVByCluster();
	}
}
