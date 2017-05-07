package ttp;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by yanshuo on 17/5/4.
 */
public class TTPDynamicItems {
    public ArrayList<ArrayList> dynamicItemStatus;

    public TTPDynamicItems(TTPInstance instance, int instanceIndex){
        String filename = instance.file.getName();
        //generate two dynamicItem benchmarks

            File benchmark = new File(filename + ".dynamicItem" + instanceIndex);
            if(benchmark.exists()){
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(benchmark));
                    String line;
                    ArrayList<ArrayList> dItem = new ArrayList<ArrayList>();
                    while ((line = br.readLine()) != null) {
                        // process the line
                        ArrayList tmplist = new ArrayList();
                        String[] tmpStr = line.split(" ");
                        for(int i = 1; i < tmpStr.length; i++){
                            tmplist.add(Integer.parseInt(tmpStr[i]));
                        }
                        for(int i = 0; i < tmplist.size(); i++){
                            //System.out.println(i+" "+tmplist.get(i));
                        }
                        dItem.add(tmplist);
                    }
                    br.close();
                    this.dynamicItemStatus = dItem;
                }
                catch (IOException e){
                    e.printStackTrace();
                }


            }
            else {
                BufferedWriter bw = null;
                try {
                    benchmark.createNewFile();
                    bw = new BufferedWriter(new FileWriter(benchmark));
                    this.dynamicItemStatus = initialization(instance);

                    for(int i = 0; i < this.dynamicItemStatus.size(); i++){
                        for(int j = 0; j < this .dynamicItemStatus.get(i).size(); j++) {
                            //System.out.println(i+" "+this.dynamicItemStatus.get(i).get(j));
                        }
                    }



                    for(int i = 0; i < this.dynamicItemStatus.size(); i++){
                        for(int j = 0; j < this .dynamicItemStatus.get(i).size(); j++) {
                            bw.write(" ");
                            bw.write(String.valueOf(this.dynamicItemStatus.get(i).get(j)));
                        }
                        bw.newLine();
                    }
                    bw.flush();
                    bw.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
    }


    private static ArrayList<ArrayList> initialization(TTPInstance instance){
        ArrayList<ArrayList> result = new ArrayList<ArrayList>();
        Random rand = new Random();
        for(int i = 0; i < (100000 / 50 + 10); i++){
            ArrayList tmplist = new ArrayList();
            for(int j = 0; j < instance.numberOfItems; j++){
                if(rand.nextInt(instance.numberOfItems) < 5){
                    tmplist.add(j);
                }
            }
            result.add(tmplist);
        }
        return result;
    }


}
