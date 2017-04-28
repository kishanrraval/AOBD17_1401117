/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package linkedinrecommendation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jblas.DoubleMatrix;
import org.jblas.Singular;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Recommend
{
    public String readFile(String FilePath)
    {
        BufferedReader br = null;
	FileReader fr = null;
        String out = "";
        try 
        {

            fr = new FileReader(FilePath);
            br = new BufferedReader(fr);

            
            String sCurrentLine;

            br = new BufferedReader(new FileReader(FilePath));

            while ((sCurrentLine = br.readLine()) != null) {
                out += sCurrentLine;
            }

	} 
        catch (IOException e) 
        {
            e.printStackTrace();

        }
        finally 
        {
            try 
            {
                if (br != null)
                    br.close();

		if (fr != null)
                    fr.close();

            } catch (IOException ex) 
            {

                ex.printStackTrace();

            }

        }
        return out;
    }
    public void parse(String str, String FileName)
    {
        
        /*int size = jsonArray.length();
        
        JSONObject object = jsonArray.getJSONObject(0);
        String name = object.getString("Additional-Info");
        System.out.println(name);
        object.put("file", FileName);*/

        
    }
    
    public String[] getFileName(String path)
    {
        File folder = new File(path);
        return folder.list();
    }
    
    public void composeFile(String path)
    {
        String data;
        String[] file = getFileName(path);
        List<JSONObject> objs = new ArrayList<>();
        JSONArray myArray = new JSONArray();
        
        for(int i = 0 ; i < file.length ; i++)
        {
            data = readFile(path + file[i]);
            //System.out.println(data);
            //cln.parse(data, file[i]);
            
            JSONObject jsonObj = new JSONObject(data);
            JSONArray jsonArray = jsonObj.getJSONArray("Candidates");
            int size =  jsonArray.length();
            for (int j = 0 ; j < size ; j++)
            {
                JSONObject candidate = jsonArray.getJSONObject(j);
                candidate.put("file", file[i]);
                objs.add(candidate);
                myArray.put(candidate);
            }
        }
        
        JSONObject myCan = new JSONObject();
        myCan.put("Candidates", myArray);
        
        writeObj(myCan, "CandidatesAll.txt");
    }
    
    public void writeObj(JSONObject obj, String path)
    {
        FileWriter fl = null;
        try {
            fl = new FileWriter(path);
        } catch (IOException ex) {
            Logger.getLogger(LinkedInRecommendation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try
        {
            
            fl.write(obj.toString());
            
        } catch (IOException ex) {
            Logger.getLogger(LinkedInRecommendation.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            try 
            {
                
                fl.flush();
                fl.close();
            } 
            catch (IOException ex) {
                Logger.getLogger(LinkedInRecommendation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void refactor(String path)    
    {
        String data = readFile(path);
        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = jsonObject.getJSONArray("Candidates");
        
        for(int i = 0 ; i < jsonArray.length() ; i++)
        {
            JSONObject object = jsonArray.getJSONObject(i);
            
            JSONObject education = object.getJSONObject("Education");
            String qual = education.getString("Qualification");
            
            JSONObject work = object.getJSONObject("Work-Experience");
            String Company = work.getString("Company");
            String jobDuration = work.getString("Job-Duration");
            String jobDesc = work.getString("Job-Description");
            String jobTitle = work.getString("Job Title");
            
            
            object.remove("Education");
            object.remove("Work-Experience");
            object.put("Qualification", qual);
            object.put("Company", Company);
            object.put("Job-Duration", jobDuration);
            object.put("Job-Description", jobDesc);
            object.put("Job-Title", jobTitle);
            
        }
        
        this.writeObj(jsonObject, "single_Can.txt");
    }
    
    public int getSize(JSONObject obj)
    {
        JSONArray arr = obj.getJSONArray("Candidates");
        return arr.length();
    }
    
    public String getData(JSONObject obj, String key, int index)
    {
        JSONArray arr = obj.getJSONArray("Candidates");
        JSONObject myCan = arr.getJSONObject(index);
        return myCan.getString(key);
    }
    
    public ArrayList<String> generateSeperator(String attr, JSONObject json)
    {
        String[] spt = new String[]{"&&", "," , "|", ";", ".", "\n", "\u2022", "\uf0d8", "\u25a1", "\u002a", "\u27a2"};
        String data, data1="";
        ArrayList<String> coll = new ArrayList<>();
        for (int j = 0 ; j < this.getSize(json) ; j++)
        {
            data = getData(json, attr, j);
            for (int i = 0 ; i < spt.length ; i++ )
            {
                data = data.replace(spt[i], ",");
            }
            String[] parts = data.split(",");
            //System.out.print(parts[0]);
            for (int i = 0 ; i < parts.length; i++)
            {
                //System.out.println("from parts " + i + "  " + parts[i]);
                if(!coll.contains(parts[i].trim()))
                {
                    coll.add(parts[i].trim());
                }
            }
            
        }
        return coll;
    }
    
    public void saveFile(String data, String path)
    {
        try {
                File file = new File(path);
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(data);
                fileWriter.flush();
                fileWriter.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public void generateCSV(String[] files, JSONObject can)
    {
        for(String file: files)
        {
            String out = "";
            //ArrayList<String> job = obj.generateSeperator("Job-Title", can);
            ArrayList<String> job = generateSeperator(file, can);
            //String[] idList = job.toString();
            for(int i = 0 ; i < job.size() ; i++)
            {
                out += job.get(i) + "\n";
            }
            saveFile(out, file+".csv");

        }
    }
    
    public boolean[][] generateMatrix(ArrayList<String> atr, String name, JSONObject obj)
    {
        int userSize = getSize(obj), atrSize = atr.size();
        String data;
        boolean [][]out = new boolean[userSize][atrSize];
        for(int i = 0 ; i < userSize ; i++)
        {
            data = getData(obj, name, i);
            data = " " + data + " ";
            for (int j = 0 ; j < atrSize ; j++)
            {
                if(data.contains(" "+ atr.get(j) +" "))
                {
                    out[i][j] = true;
                }
            }
        }
        return out;
    }
    
    public void generateCSVfrom2d(boolean[][] arr, String path)
    {
        /*String out = "";
        int temp = arr.length;
        int temp1 = arr[0].length;
        boolean[][] myArr = arr;
        for(int i = 0 ; i < temp ; i++)
        {
            System.out.println("User" + i);
            for(int j = 0 ; j < temp-1 ; j++)
            {
                out += myArr[i][j] + ",";
            }
            out+= myArr[i][temp-1] + "\n";
        }
        return out;*/
        try {
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file);
            
            
            int temp = arr.length;
            int temp1 = arr[0].length;
            boolean[][] myArr = arr;
            for(int i = 0 ; i < temp ; i++)
            {
                System.out.println("User" + i);
                for(int j = 0 ; j < temp1-1 ; j++)
                {
                    fileWriter.write((myArr[i][j]?"1":"0") + ",");
                }
                fileWriter.write(myArr[i][temp1-1]?"1":"0" + "\n");
            }
            
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        
    }
    
    public void generateLSVSupport(JSONObject obj, String path)
    {
        int size = this.getSize(obj);
        String[] key = new String[]{"Job-Title","Additional-Info","Qualification"
               ,"Company","file","Job-Description","Skills",
               "Resume-Summary","Location"};
        String append[]= new String[]{"qa","qb","qc","qd","qe","qf","qg","qh","qi"};
        String temp;
        Utils utl = new Utils();
        String[] t;
        
        try {
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file);
            
            
            for(int i = 0 ; i < size ; i++)
            {
                System.out.println("User "  + i);
                for(int j = 0 ; j < key.length ; j++)
                {
                    temp = this.getData(obj, key[j], i);
                    temp = utl.removeSpecialChars(temp);
                    temp = utl.removeStopWords(temp);
                    t = temp.split(" ");
                    for(int k = 0 ; k < t.length ; k++)
                    {
                        fileWriter.write(t[k] + append[j] + " ");
                    }
                }
                fileWriter.write((System.getProperty( "line.separator" )));
                fileWriter.write((System.getProperty( "line.separator" )));
                
            }
            
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public Map<String, DoubleMatrix> applyLSA(String path)
    {
        Utils utl = new Utils();
        Map<String, Integer> termFrequencyMap = new HashMap<>();
        List<String> docs = utl.getFileLines(path, true);
        List<String[]> tokenizedDocs =  new ArrayList<>();
        // get unique terms
        for (String doc : docs) {
            doc = doc.toLowerCase();
            doc = utl.removeSpecialChars(doc);
            doc = utl.removeStopWords(doc);
            String terms[] = doc.split(" ");
            tokenizedDocs.add(terms);
            for (String term : terms) {
                if (term.isEmpty() || term.length() < 2) continue;
                if (termFrequencyMap.containsKey(term)) {
                    termFrequencyMap.put(term, termFrequencyMap.get(term) + 1);
                } else {
                    termFrequencyMap.put(term, 1);
                }
            }
        }
        //keeps only frequent terms
        List<String> frequentTermsList = new ArrayList<>();
        Set<String> termsSet = termFrequencyMap.keySet();
        termsSet.stream().filter(s -> termFrequencyMap.get(s) > 1.0).forEach(frequentTermsList::add);
        String[] sortedTermArray = frequentTermsList.toArray(new String[frequentTermsList.size()]);
        Arrays.sort(sortedTermArray);

        // generate terms x document matrix, document is the column vector
        int numOfTerms = sortedTermArray.length;
        int numOfDocs = docs.size();
        System.out.println("Number of Docs: "+numOfDocs+" Number of Terms: "+ numOfTerms);
        double[][] docTermMatrix = new double[numOfTerms][numOfDocs];
        for (int d = 0; d < numOfDocs; d++) {
            String[] tokens = tokenizedDocs.get(d);
            for (String token : tokens) {
                int pos = Arrays.binarySearch(sortedTermArray,token);
                if (pos>-1)
                docTermMatrix[pos][d] = docTermMatrix[pos][d] + 1.0;
            }
        }

        /*double s[][] = new double[8][5];
        s[0][0] = 1.0;
        s[1][0] = 1.0;
        s[1][1] = 1.0;
        s[2][1] = 1.0;
        s[3][1] = 1.0;
        s[3][2] = 1.0;
        s[4][3] = 1.0;
        s[5][2] = 1.0;
        s[5][3] = 1.0;
        s[6][3] = 1.0;
        s[7][3] = 1.0;
        s[7][4] = 1.0;
        DoubleMatrix doubleMatrix[] = Singular.fullSVD(new DoubleMatrix(s));*/
        DoubleMatrix[] doubleMatrix = Singular.sparseSVD(new DoubleMatrix(docTermMatrix));
        DoubleMatrix S = doubleMatrix[0];
        DoubleMatrix sigma = doubleMatrix[1];
        //DoubleMatrix U = doubleMatrix[2];
        // reducing sigma to a size k
        List<Double> nonZero = new ArrayList<>();
        for (int i = 0; i < sigma.length; i++) {
            if (sigma.get(i) > 0.0) {
                nonZero.add(i, sigma.get(i));
            } else {
                break;
            }
        }
        int k = nonZero.size();
        DoubleMatrix reducedSigma = new DoubleMatrix(nonZero.size());
        for (int i = 0; i < k; i++) {
            reducedSigma.put(i, 0, nonZero.get(i));
        }
        DoubleMatrix reducedS = S.getRange(0,S.rows,0,k);
        //DoubleMatrix reducedU = U.getRange(0,k,0,U.columns);
        Map<String, DoubleMatrix> finalTermVector = new HashMap<>();
        for (int i = 0; i < numOfTerms; i++) {
            finalTermVector.put(sortedTermArray[i], reducedS.getRow(i).mul(reducedSigma));
        }
        return finalTermVector;
        
    }
    
    public Map<String, Double> getVector(Map<String, DoubleMatrix> map, String query)
    {
        Utils utl = new Utils();
        Map<String, Double> similarityMap = new HashMap<>();
        for (String term : map.keySet()) {
            DoubleMatrix main = map.get(query), inQ = map.get(term);
            double magni = utl.getMagnitude(inQ.data) * utl.getMagnitude(main.data);
            if (magni > 0.001) {
                similarityMap.put(term, main.dot(inQ) / magni);
            }
            
        }
               
        return (similarityMap);
    }
    
    
    public String[] getSimilar(Map<String, Double> map)
    {
        
        String out = map.toString();
        String[] spl = out.split(" ");
        String[] str1, str2= new String[spl.length];
        String s1,temp,s2 ;
        Value[] vals = new Value[spl.length];
        for (int i = 0 ; i < spl.length ; i++)
        {
            str1 = spl[i].split("=");
            s1 = str1[0].trim();
            s1 = s1.replace("[{", "");
            str2[i] = s1;
           
        }
        return str2;
    }
    
    public String removeExtra(String in)
    {
        return in.substring(0,in.length()-2);
    }
    
    public String getFirstSimilar(Map<String, Double> map)
    {
        Utils utl = new Utils();
        String out = utl.entriesSortedByValues(map).toString();
        String[] spl = out.split(" ");
        String[] str1;
        String s1 ;
        str1 = spl[1].split("=");
        s1 = str1[0].trim();
        s1 = s1.replace("[{", "");
           
        
        return s1;
    }
    
    public void generateProbFile(Map<String, DoubleMatrix> map)
    {
        //Map<String, DoubleMatrix> map = null;
        for(String term : map.keySet())
        {
            DoubleMatrix main = map.get(term);
            
        }
    }
    
    public String[] getKeyArray(Value v[])
    {
        String out[] = new String[v.length];
        for(int i = 0 ; i < out.length ; i++)
        {
            out[i] = v[i].key;
        }
        return out;
    }
    
    public String[] getProperValues(String[] key, Value[] val)
    {
        int valLength = val.length;
        ArrayList<String> ls = new ArrayList<>(Arrays.asList(key));
        int keyLength = key.length;
        String[] out = new String[key.length];
        String temp; int index;
        for(int i = 0 ; i < valLength ; i++)
        {
            out[ls.indexOf(val[i].key)] = val[i].value;
        }
        return out;
    }
    
    public void generateCSVfromDouble(Value[] vals, String path)
    {
        try {
            File file = new File(path);
            FileWriter fileWriter = new FileWriter(file);
            
            String[] allKey = this.getKeyArray(vals);
            String[] myMat = new String[allKey.length];
            System.out.println("allkey length  "+allKey.length);
            int temp = myMat.length;
            for(int i = 0 ; i < allKey.length ;i++)
            {
                System.out.println("User" + i);
                myMat = this.getProperValues(allKey, vals);
                
                for(int j = 0 ; j < temp-1 ; j++)
                {
                    fileWriter.write(myMat[i] + ",");
                }
                fileWriter.write(myMat[temp-1] + "\n");
            }
            
            
            
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    
    public JSONArray shuffleJsonArray (JSONArray array) throws JSONException 
    {
    // Implementing Fisher–Yates shuffle
        Random rnd = new Random();
        for (int i = array.length() - 1; i >= 0; i--)
        {
          int j = rnd.nextInt(i + 1);
          // Simple swap
          Object object = array.get(j);
          array.put(j, array.get(i));
          array.put(i, object);
        }
        return array;
    }
    
    public JSONArray generateTrain(JSONArray array)
    {
        JSONArray out = new JSONArray();
        for(int i = 0 ; i < 650 ; i++)
        {
            out.put(array.get(i));
        }
        return out;
    }
    
    public JSONArray generateTest(JSONArray array)
    {
        System.out.println("hoo");
        JSONArray out = new JSONArray();
        int size = array.length();
        for(int i = 650 ; i < size ; i++)
        {
            System.out.println("hi");
            out.put(array.get(i));
        }
        return out;
    }
    
    public void storeMap(Map<String, DoubleMatrix> map, String path)
    {
        try
           {
                  FileOutputStream fos =
                     new FileOutputStream(path);
                  ObjectOutputStream oos = new ObjectOutputStream(fos);
                  oos.writeObject(map);
                  oos.close();
                  fos.close();
                  System.out.printf("Serialized HashMap data is saved in hashmap.ser");
           }catch(IOException ioe)
            {
                  ioe.printStackTrace();
            }
    }
    
    public Map<String, DoubleMatrix> getMap(String path)
    {
        Map<String, DoubleMatrix> map = null;
        
        try
      {
         FileInputStream fis = new FileInputStream(path);
         ObjectInputStream ois = new ObjectInputStream(fis);
         map = (Map) ois.readObject();
         ois.close();
         fis.close();
      }catch(IOException ioe)
      {
         ioe.printStackTrace();
         return null;
      }catch(ClassNotFoundException c)
      {
         System.out.println("Class not found");
         c.printStackTrace();
         return null;
      }
        finally{
            return map;
        }
    }
}
