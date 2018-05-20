package clustering;

import java.io.BufferedReader;
import java.io.FileReader;

public class FindCluster {
	public static void main(String[] args){
		String fileName = "254VisitorsMerging.txt", sCurrentLine, splits[];
		String fileData = "254Visitors.txt";
		String extractedDatas[] = new String[1000];
		int ex=0;
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\" + fileData));
			while ((sCurrentLine = br.readLine()) != null) {
				//splits = sCurrentLine.split(" ");
				extractedDatas[ex] = sCurrentLine.split(" ")[1].split("\\.")[0];
				ex++;
			}
			br = new BufferedReader(new FileReader(System.getProperty("user.dir") + "\\" + fileName));
			while ((sCurrentLine = br.readLine()) != null) {
				splits = sCurrentLine.split(" ");
				System.out.println(splits[1]+" "+splits[2] + " = " + extractedDatas[Integer.parseInt(splits[1])] + " " + extractedDatas[Integer.parseInt(splits[2])]);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
