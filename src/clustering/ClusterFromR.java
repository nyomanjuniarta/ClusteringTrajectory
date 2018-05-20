package clustering;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ClusterFromR {
	public static void getListOfItems(){
		String path = System.getProperty("user.dir")+"\\visitorPaths\\", sCurrentLine, item;
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		BufferedReader br = null;
		Set<String> set = new HashSet<String>();
		try{
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					br = new BufferedReader(new FileReader(listOfFiles[i].getPath()));
					while ((sCurrentLine = br.readLine()) != null) {
						if(sCurrentLine.length()==0){
							break;
						}
						item = sCurrentLine.split(",")[2];
						if(!set.contains(item)){
							set.add(item);
						}
					}
					br.close();
				}
			}
			TreeSet<String> sortedSet = new TreeSet<String>(set);
			PrintWriter writer = new PrintWriter("listOfItems.txt", "UTF-8");
			Iterator<String> sortedSetIterator = sortedSet.iterator();
			while(sortedSetIterator.hasNext()){
				writer.println(sortedSetIterator.next());
				sortedSetIterator.remove();
			}
			writer.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void convertToNumber(){
		BufferedReader br = null;
		String sCurrentLine, lines[], res="", item;
		int i = 0;
		HashMap<String, String> coordinate = new HashMap<String, String>();
		try{
			br = new BufferedReader(new FileReader("listOfItems.txt"));
			while((sCurrentLine = br.readLine()) != null){
				coordinate.put(sCurrentLine,String.valueOf(i));
				i++;
			}
			br.close();
			
			br = new BufferedReader(new FileReader("254Visitors.txt"));
			PrintWriter writer = new PrintWriter("254VisitorsInteger.txt", "UTF-8");
			while((sCurrentLine = br.readLine()) != null){
				sCurrentLine = sCurrentLine.split(",")[1];
				lines = sCurrentLine.split(" ");
				res = "";
				for(i=0;i<lines.length;i++){
					item = lines[i];
					if(item.equals("-1")){
						res += item + " ";
					}
					else if(item.equals("-2")){
						res += item;
					}
					else{
						res += coordinate.get(item) + " ";
					}
				}
				writer.println(res);
			}
			br.close();
			writer.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void getTrajectories(String dataset, String clusterFile, int minMember){
		BufferedReader br = null;
		PrintWriter writer = null;
		String sCurrentLine, trajIndices[];
		List<String> allTrajectories = new ArrayList<String>();
		List<String> smallCluster = new ArrayList<String>();
		List<String> membership = new ArrayList<String>();
		int i;
		try{
			br = new BufferedReader(new FileReader(dataset));
			while((sCurrentLine = br.readLine()) != null){
				allTrajectories.add(sCurrentLine);
			}
			br.close();
			
			br = new BufferedReader(new FileReader(clusterFile));
			while((sCurrentLine = br.readLine()) != null){
				membership.add(sCurrentLine);
			}
			br.close();
			
			for(int j=0;j<membership.size();j++){
				trajIndices = membership.get(j).split(" ");
				if(trajIndices.length > minMember){
					writer = new PrintWriter("cluster" + String.valueOf(j+1) + ".txt", "UTF-8");
					for(i=0; i<trajIndices.length; i++){
						writer.println(allTrajectories.get(Integer.parseInt(trajIndices[i]) - 1));
					}
					writer.close();
				}
				else{
					for(i=0; i<trajIndices.length;i++){
						smallCluster.add(allTrajectories.get(Integer.parseInt(trajIndices[i]) - 1));
					}
				}
			}
			
			writer = new PrintWriter("clusterSmall.txt", "UTF-8");
			for(i=0; i<smallCluster.size(); i++){
				writer.println(smallCluster.get(i));
			}
			writer.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static double distance(String a, String b){
		String splitRes[] = a.split(",");
		double a1 = Double.parseDouble(splitRes[0]);
		double a2 = Double.parseDouble(splitRes[1]);
		splitRes = b.split(",");
		double b1 = Double.parseDouble(splitRes[0]);
		double b2 = Double.parseDouble(splitRes[1]);
		
		return Math.sqrt((b2-a2)*(b2-a2) + (b1-a1)*(b1-a1));
	}
	
	public static void getListOfDistanceAndDuration(){
		String path = System.getProperty("user.dir")+"\\visitorPaths\\";
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		BufferedReader br = null;
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date time1, time2;
		HashMap<String, String> coordinate = new HashMap<String, String>();
		long duration = 0;
		String sCurrentLine, lines[], sequence, result, coordBefore;
		double distWithBefore = 0.0;
		
		try{
			br = new BufferedReader(new FileReader("coordinate.txt"));
			while((sCurrentLine = br.readLine()) != null){
				lines = sCurrentLine.split(";");
				coordinate.put(lines[0], lines[1]);
			}
			br.close();
			
		    PrintWriter writer = new PrintWriter("listOfDurationsDistances.csv", "UTF-8");
		    writer.println("distance,duration");
			for (int i = 0; i < listOfFiles.length; i++) {
				sequence = "";
				if (listOfFiles[i].isFile()) {
					br = new BufferedReader(new FileReader(listOfFiles[i].getPath()));
					coordBefore = null;
					distWithBefore = 0.0;
					while ((sCurrentLine = br.readLine()) != null) {
						if(sCurrentLine.length()==0){
							break;
						}
						lines = sCurrentLine.split(",");
						time1 = sdf.parse(lines[0]);
						time2 = sdf.parse(lines[1]);
						duration = time2.getTime() - time1.getTime();
						if(coordBefore == null){
							coordBefore = coordinate.get(lines[2]);
						}
						else if(coordinate.get(lines[2]) != null){
							distWithBefore = distance(coordBefore,coordinate.get(lines[2]));
							coordBefore = coordinate.get(lines[2]);
						}
						if(duration < 0){
							//System.out.println(listOfFiles[i].getName() + " " + sCurrentLine + " " + duration);
							duration = duration * -1;
						}
						result = String.valueOf(distWithBefore) + "," + String.valueOf(duration);
						writer.println(result);
						
					}
					br.close();
				}
			}
		    writer.close();
		} catch(IOException e){
			e.printStackTrace();
		} catch(ParseException e){
			e.printStackTrace();
		}
	}
	
	public static String durationToStr(long duration){
		String res = "";
		if(duration < 14000) res = "0";
		else res = "1";
		return res;
	}
	
	public static String distanceToStr(double distance){
		String res = "";
		if(distance < 2.4) res = "2";
		else res = "3";
		return res;
	}
	
	public static void readDistancesAndDurations(){
		String path = System.getProperty("user.dir")+"\\visitorPaths\\";
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		BufferedReader br = null;
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date time1, time2;
		HashMap<String, String> coordinate = new HashMap<String, String>();
		long duration;
		String sCurrentLine, lines[], sequence, result, coordBefore;
		double distWithBefore;
		
		try{
			br = new BufferedReader(new FileReader("coordinate.txt"));
			while((sCurrentLine = br.readLine()) != null){
				lines = sCurrentLine.split(";");
				coordinate.put(lines[0], lines[1]);
			}
			br.close();
			
		    PrintWriter writer = new PrintWriter("254DurationDistanceMedian.txt", "UTF-8");
			for (int i = 0; i < listOfFiles.length; i++) {
				sequence = "";
				if (listOfFiles[i].isFile()) {
					br = new BufferedReader(new FileReader(listOfFiles[i].getPath()));
					coordBefore = null;
					while ((sCurrentLine = br.readLine()) != null) {
						if(sCurrentLine.length()==0){
							break;
						}
						lines = sCurrentLine.split(",");
						time1 = sdf.parse(lines[0]);
						time2 = sdf.parse(lines[1]);
						duration = time2.getTime() - time1.getTime();
						
						if(coordBefore == null){
							sequence += durationToStr(duration) + " -1 ";
							coordBefore = coordinate.get(lines[2]);
						}
						else if(coordinate.get(lines[2]) != null){
							distWithBefore = distance(coordBefore,coordinate.get(lines[2]));
							sequence += durationToStr(duration) + " " + distanceToStr(distWithBefore) + " -1 ";
							coordBefore = coordinate.get(lines[2]);
						}
						
					}
					sequence += "-2";
					//result = listOfFiles[i].getName().replaceAll(" ", "") + "," + sequence;
					result = sequence;
					br.close();

					writer.println(result);
				}
			}
		    writer.close();
		} catch(IOException e){
			e.printStackTrace();
		} catch(ParseException e){
			e.printStackTrace();
		}
	}
	
	public static void countItemset(){
		BufferedReader br = null;
		String sCurrentLine, itemset[];
		int a,b,c,d;
		try{
			br = new BufferedReader(new FileReader("254DurationDistanceMedian.txt"));
			PrintWriter writer = new PrintWriter("254DurationDistanceMedian.csv", "UTF-8");
			writer.println("0 2,0 3,1 2,1 3,total");
			while((sCurrentLine = br.readLine()) != null){
				a = b = c = d = 0;
				itemset = sCurrentLine.split(" -1 ");
				for(int i=1;i<itemset.length;i++){
					if(itemset[i].equals("0 2")) a++;
					else if(itemset[i].equals("0 3")) b++;
					else if(itemset[i].equals("1 2")) c++;
					else if(itemset[i].equals("1 3")) d++;
				}
				writer.println(a + "," + b + "," + c + "," + d + "," + (itemset.length-2));
			}
			writer.close();
			br.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	
	}
	
	public static void assignCluster(){
		BufferedReader br = null;
		String sCurrentLine, seqs[];
		int ind[] = new int[254];
		int row=1;
		try{
			br = new BufferedReader(new FileReader("visitorClustersByDurDistMedian_15.txt"));
			PrintWriter writer = new PrintWriter("assignClustersByDurDistMedian_15.txt", "UTF-8");
			while((sCurrentLine = br.readLine()) != null){
				seqs = sCurrentLine.split(" ");
				if(seqs.length < 15){
					for(int i=0;i<seqs.length;i++){
						ind[Integer.parseInt(seqs[i])-1] = 0;
					}
				}
				else{
					for(int i=0;i<seqs.length;i++){
						ind[Integer.parseInt(seqs[i])-1] = row;
					}
				}
				row++;
			}
			
			for(int j=0;j<ind.length;j++){
				writer.println(ind[j]);
			}
			writer.close();
			br.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void allSize(){
		BufferedReader br = null;
		String sCurrentLine, seqs[];
		try{
			br = new BufferedReader(new FileReader("254VisitorsInteger.txt"));
			PrintWriter writer = new PrintWriter("listOfSizes.txt", "UTF-8");
			while((sCurrentLine = br.readLine()) != null){
				seqs = sCurrentLine.split("-1");
				writer.println(seqs.length -1);
			}
			writer.close();
			br.close();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		//getListOfItems(); // obtain list of distinct items
		allSize(); // obtain size of each sequence
		//convertToNumber(); // convert items to numbers
		//getListOfDistanceAndDuration(); // to know the distribution of distances and durations
		//readDistancesAndDurations(); // obtain sequences of distance and duration
		//countItemset(); // count all distinct itemsets in each sequence
		//getTrajectories("254DurationDistanceMedian.txt","visitorClustersByDurDistMedian_15.txt",15); // divide visitors according to clusters obtained from R
		//assignCluster(); // create file that displays cluster index for each sequence
	}

}
