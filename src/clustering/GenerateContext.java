package clustering;

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

public class GenerateContext {

	public static void generateDuration(){
		int numberOfItems = 52, c;
		int[] durationArray = new int[numberOfItems];
		String path = System.getProperty("user.dir")+"\\visitorPaths\\";
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		Date time1, time2;
		long duration;
		BufferedReader br = null;
		HashMap<String, String> idMap = new HashMap<String, String>();
		HashMap<String, Integer> intMap = new HashMap<String, Integer>(); 
		String sCurrentLine, lines[], itemId;
		
		try{
			br = new BufferedReader(new FileReader("categories.txt"));
			c = 0;
			PrintWriter writerCsv = new PrintWriter("haifa_duration.csv", "UTF-8");
			while((sCurrentLine = br.readLine()) != null){
				lines = sCurrentLine.split(";");
				idMap.put(lines[0], lines[1]);
				intMap.put(lines[0], c);
				writerCsv.print("," + lines[0]);
				c++;
			}
			writerCsv.println();
			br.close();
			PrintWriter writer = new PrintWriter("haifa_duration.txt", "UTF-8");
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					writerCsv.print(listOfFiles[i].getName());
					for(int k1 = 0; k1 < numberOfItems; k1++){
						durationArray[k1] = 0;
					}
					br = new BufferedReader(new FileReader(listOfFiles[i].getPath()));
					while ((sCurrentLine = br.readLine()) != null) {
						if(sCurrentLine.length()==0){
							break;
						}
						lines = sCurrentLine.split(",");
						if(lines[2].equals("49")) continue;
						time1 = sdf.parse(lines[0]);
						time2 = sdf.parse(lines[1]);
						duration = (time2.getTime() - time1.getTime()) / 1000; // divided by 1000 to convert from milliseconds
						//itemId = idMap.get(lines[2]);
						durationArray[intMap.get(lines[2])] += duration;
					}
					br.close();

					for(int k2 = 0; k2 < numberOfItems; k2++){
						if(durationArray[k2] <= 0){
							writer.print("? ");
							writerCsv.print(",?");
						}
						else if(durationArray[k2] > 900){
							writer.print("100 ");
							writerCsv.print(",100");
						}
						else{
							writer.print((durationArray[k2] / 100) + " ");
							writerCsv.print("," + (durationArray[k2] / 100));
						}
					}
					writer.println();
					writerCsv.println();
				}
			}
		    writer.close();
		    writerCsv.close();
		} catch(IOException e){
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		generateDuration();
	}

}
