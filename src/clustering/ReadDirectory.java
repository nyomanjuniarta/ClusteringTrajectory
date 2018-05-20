package clustering; // ada yg lebih baru

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class ReadDirectory {
	public static double distance(String a, String b){
		String splitRes[] = a.split(",");
		double a1 = Double.parseDouble(splitRes[0]);
		double a2 = Double.parseDouble(splitRes[1]);
		splitRes = b.split(",");
		double b1 = Double.parseDouble(splitRes[0]);
		double b2 = Double.parseDouble(splitRes[1]);
		
		return Math.sqrt((b2-a2)*(b2-a2) + (b1-a1)*(b1-a1));
	}
	
	public static void generateSPMF(){
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
			
		    PrintWriter writer = new PrintWriter("254Duration.txt", "UTF-8");
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
							//sequence += (duration <= 20000 ? "short " : "long ") + lines[2] + " -1 ";
							sequence += (duration <= 20000 ? "short " : "long ") + "-1 ";
							coordBefore = coordinate.get(lines[2]);
						}
						else if(coordinate.get(lines[2]) != null){
							distWithBefore = distance(coordBefore,coordinate.get(lines[2]));
							//sequence += (duration <= 20000 ? "short " : "long ") + lines[2] + " " + (distWithBefore <= 5 ? "near" : "far") + " -1 ";
							sequence += (duration <= 20000 ? "short " : "long ") + (distWithBefore <= 5 ? "near" : "far") + " -1 ";
							coordBefore = coordinate.get(lines[2]);
						}
						//sequence += lines[2] + " -1 ";
						
					}
					sequence += "-2";
					result = listOfFiles[i].getName().replaceAll(" ", "") + "," + sequence;
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
	
	public static void generateHierarchy(int minimumTime){  // minimumTime in seconds
		String path = System.getProperty("user.dir")+"\\visitorPaths\\";
		
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date time1, time2;
		long duration;
		BufferedReader br = null;
		HashMap<String, String> categories = new HashMap<String, String>();
		String sCurrentLine, lines[], sequence, result, itemId;
		
		try{
			br = new BufferedReader(new FileReader("categories.txt"));
			while((sCurrentLine = br.readLine()) != null){
				lines = sCurrentLine.split(";");
				categories.put(lines[0], lines[1]);
			}
			br.close();
			
		    PrintWriter writer = new PrintWriter("254hierarchical" + String.valueOf(minimumTime) + "seconds.txt", "UTF-8");
			for (int i = 0; i < listOfFiles.length; i++) {
				sequence = "";
				if (listOfFiles[i].isFile()) {
					br = new BufferedReader(new FileReader(listOfFiles[i].getPath()));
					while ((sCurrentLine = br.readLine()) != null) {
						if(sCurrentLine.length()==0){
							break;
						}
						lines = sCurrentLine.split(",");
						time1 = sdf.parse(lines[0]);
						time2 = sdf.parse(lines[1]);
						duration = time2.getTime() - time1.getTime();
						itemId = categories.get(lines[2]);
						/*if(itemId == null || duration < minimumTime*1000){
							continue;
						}*/
						sequence += itemId + " " + duration + " -1 ";
					}
					sequence += "-2";
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

	public static void main(String[] args){
		generateHierarchy(0);
	}
}
