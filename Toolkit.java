import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Toolkit {
    private static List<String> listVocabulary = new ArrayList<String>();
    private static List<double[]> listVectors = new ArrayList<double[]>();
    private static final String FILENAME_GLOVE = "glove.6B.50d_Reduced.csv";
    private static final String FILENAME_STOPWORDS = "stopwords.csv";

    public static void loadGLOVE() throws IOException {
        //TODO Task 5.1
        BufferedReader myReader = null;
        File filePath = null;

        try{

            //filePath = Paths.get(resource.toURI());
            filePath = Toolkit.getFileFromResource("glove.6B.50d_Reduced.csv");
            File myFile = new File(filePath.toURI());

            myReader = new BufferedReader(new FileReader(myFile));
            String line = myReader.readLine();

            listVocabulary.clear();

            int index = 0;
            while(line != null){
                if(line.charAt(index) == ','){  //if we reach the end of a word
                    listVocabulary.add(line.substring(0, index));   //add the word to the list

                    String numbers = line.substring(index+1);   //create a string with all the numbers
                    String[] vecArr = numbers.split(",");   //split the string into an array of numbers without the commas
                    double[] doubles = new double[vecArr.length];
                    //convert from string to double
                    for(int i = 0; i < vecArr.length; i++){
                        doubles[i] = Double.parseDouble(vecArr[i]);
                    }
                    listVectors.add(doubles);    //add array to the list

                    index = 0;  //reset index so we start the next line at the first letter
                    line = myReader.readLine(); //go to the next line

                }
                else{   //if we have not reached the end of the word
                    index += 1;
                }
            }


        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<String> loadStopWords() throws IOException {
        List<String> listStopWords = new ArrayList<String>();
        BufferedReader myReader = null;
        //TODO Task 5.2
        File filePath = null;

        try{
            ClassLoader classLoader = Toolkit.class.getClassLoader();

            filePath = Toolkit.getFileFromResource("stopwords.csv");
            File myFile = new File(filePath.toURI());

            myReader = new BufferedReader(new FileReader(myFile));
            String line = myReader.readLine();

            while(line != null){
                listStopWords.add(line);
                line = myReader.readLine();
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return listStopWords;
    }

    private static File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = Toolkit.class.getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }

    public static List<String> getListVocabulary() {
        return listVocabulary;
    }

    public static List<double[]> getlistVectors() {
        return listVectors;
    }

    /**
     * DO NOT MODIFY
     * Method to print out the semantic information.
     * <p>
     * Example: uk is to london as china is to XXXX.
     * _firISRef  _firTORef _secISRef
     * In the above example, "uk" is the first IS reference; "london" is the first TO reference
     * and "china" is the second IS reference.
     *
     * @param _secISRef The second IS reference
     * @param _firISRef The first IS reference
     * @param _firTORef The first TO reference
     * @param _list     The CosSimilarityPair list
     */
    public static void PrintSemantic(String _secISRef, String _firISRef, String _firTORef, List<CosSimilarityPair> _list) {
        System.out.println("=============================");
        System.out.printf("Identifying the logical analogies of %s (use %s and %s as a reference).\r\n", _secISRef, _firISRef, _firTORef);
        System.out.printf("%s is to %s as %s is to %s.\r\nOther options include:\r\n", _firISRef, _firTORef, _secISRef, _list.get(0).getWord2());
        for (int i = 1; i < _list.size(); i++) {
            System.out.println(_list.get(i).getWord2() + ", " + _list.get(i).getCosineSimilarity());
        }
        System.out.println("=============================");
    }

    /**
     * DO NOT MODIFY
     *
     * @param _listCosineSimilarity The CosSimilarityPair list.
     * @param _top                  How many vocabularies to print.
     */
    public static void PrintSemantic(List<CosSimilarityPair> _listCosineSimilarity, int _top) {
        if (_listCosineSimilarity.size() > 0) {
            System.out.println("============" + _listCosineSimilarity.get(0).getWord1() + "============");
            System.out.println("The nearest words are:");
            for (int i = 0; i < _top; i++) {
                System.out.printf("%s,%.5f\r\n", _listCosineSimilarity.get(i).getWord2(), _listCosineSimilarity.get(i).getCosineSimilarity());
            }
        } else {
            System.out.println("The specified word doesn't exist in the vocabulary.");
        }
    }
}
