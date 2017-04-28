package linkedinrecommendation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.jblas.DoubleMatrix;
import org.json.JSONArray;
import org.json.JSONObject;


public class LinkedInRecommendation {
    public static void main(String[] args) {
        Recommend obj = new Recommend();
        String path = "CandidateProfileData/";
        //obj.composeFile(path);
        //obj.refactor("CandidatesAll.txt");
        String data = obj.readFile("randomCandidates.txt");
        JSONObject can = new JSONObject(data);
        
        //Comments contain work that is done for data cleaning
        
        /*//--GENERATING TEST AND TRAIN DATA
        JSONArray arr = can.getJSONArray("Candidates");
        arr = obj.generateTrain(arr);
        JSONObject myCan = new JSONObject();
        myCan.put("Candidates", arr);
        obj.writeObj(myCan, "train.txt");
        
        arr = can.getJSONArray("Candidates");
        arr = obj.generateTest(arr);
        myCan = new JSONObject();
        myCan.put("Candidates", arr);
        obj.writeObj(myCan, "test.txt");*/
        
        
        /*arr = obj.shuffleJsonArray(arr);
        JSONObject myCan = new JSONObject();
        myCan.put("Candidates", arr);
        obj.writeObj(myCan, "randomCandidates.txt");
        */
        //String job = obj.getData(can, "Job-Title", 0);
        //System.out.println(job);
        
        /*String files[] = new String[]{"Job-Title","Company","Skills"};
        obj.generateCSV(files, can);
        ArrayList<String> job = obj.generateSeperator("Job-Title", can);
        ArrayList<String> company = obj.generateSeperator("Company", can);
        ArrayList<String> skill = obj.generateSeperator("Skills", can);
        boolean[][] jobTitleMat = obj.generateMatrix(job, files[0], can);
        boolean[][] companyMat = obj.generateMatrix(company, files[1], can);
        boolean[][] skillMat = obj.generateMatrix(skill, files[2], can);
        
        System.out.println("temp");
        obj.generateCSVfrom2d(jobTitleMat, "jobTitleMat.casv");
        obj.generateCSVfrom2d(companyMat, "CompanyMat.csv");
        obj.generateCSVfrom2d(skillMat, "skillsMat.csv");
        */
        
        /*String jobTitleCSV = 
        obj.saveFile(jobTitleCSV, "jobTitleMat.csv");
        System.out.println("job done");
        String companyCSV = obj.generateCSVfrom2d(companyMat);
        obj.saveFile(companyCSV, "companyMat.csv");
        System.out.println("company done");
        String skillCSV = obj.generateCSVfrom2d(skillMat);
        obj.saveFile(skillCSV, "skills.csv");
        System.out.println("skills done");*/
        
        /*--------
        obj.generateLSVSupport(can, "train.txt");
        
        
        Map<String, DoubleMatrix> map = obj.applyLSA("temp.txt");
        //System.out.println("Map Ready");

        System.out.println("I start");
        Map<String, Double> similarityMap = obj.getVector(map, "gitqb");
        obj.storeMap(map, "map.ser");
        System.out.println("I end");
        ----------*/
        
        //obj.generateCSVfromDouble(vals, "relation.csv");
        //System.out.println(vector);
        
        Map<String, DoubleMatrix> similarityMap = obj.getMap("map.ser");
        
        Map<String, Double> map = obj.getVector(similarityMap, "gitqb");
        String[] all = obj.getSimilar(map);
        ArrayList<String> allArr =  new ArrayList<>(Arrays.asList(all));
        int count[] = new int[all.length];
        //Reading test data
        /*String testdata = obj.readFile("test.txt");
        JSONObject testcan = new JSONObject(testdata);
        
        obj.generateLSVSupport(testcan, "testLSV.txt");
        */
        
        Utils utl = new Utils();
        List<String> testdocs = utl.getFileLines("testLSV.txt", true);
        String[] words;
        String selWord;
        int ind;
        
        
        for(int i = 0 ; i < 1 ; i++)
        {
            words = testdocs.get(i).split(" ");
            
            for(int j = 0 ; j < words.length ; j++)
            {
                System.out.println("Word " + j + " of " + words.length +"  for user "+ i+1);
                try{
                    map = obj.getVector(similarityMap, words[j]);
                    selWord = obj.getFirstSimilar(map);
                    ind = allArr.indexOf(selWord);
                    if(ind != -1)
                        count[ind]++;
                }
                catch(Exception ex){}
            }
            System.out.println("\n\n\n\n\n\n\n\n---------------------------------Suggestions Are ------------------------------");
            
            for(int j = 0 ; j < 30 ;j++)
            {
                int max=count[0];
                int index = 0;

                for (int k = 0; k < count.length; k++) 
                {
                    if (count[j] > max) 
                    {
                        max = count[j];
                        index = j;
                    }
                        
             
                    count[index] = -1;

                }
                String t1 = allArr.get(index);
                System.out.println(obj.removeExtra(t1));
                
                /*
                //Chnage here and add paramenters for module 2 in the String
                //String module2[]= new String[]{"qa","qb","qc","qd","qe","qf","qg","qh","qi"};
                //qa --> "Job-Title",
                //qb --> "Additional-Info",
                //qc --> "Qualification",
                //qd --> "Company",
                //qe --> "file",
                //qf --> "Job-Description",
                //qg --> "Skills",
                //qh --> "Resume-Summary",
                //qi --> "Location
                
                if(t1.substring(t1.length()-2).equals("qf");
                {
                    String t1 = allArr.get(index);
                    System.out.println(obj.removeExtra(t1));
                }
                else{
                    j--;
                }
                */
            }
        }
        
    }
    
}
