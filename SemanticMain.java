import org.apache.commons.lang3.time.StopWatch;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SemanticMain {
    public List<String> listVocabulary = new ArrayList<>();  //List that contains all the vocabularies loaded from the csv file.
    public List<double[]> listVectors = new ArrayList<>(); //Associated vectors from the csv file.
    public List<Glove> listGlove = new ArrayList<>();
    public final List<String> STOPWORDS;

    public SemanticMain() throws IOException {
        STOPWORDS = Toolkit.loadStopWords();
        Toolkit.loadGLOVE();
        listVocabulary = Toolkit.getListVocabulary();
        listVectors = Toolkit.getlistVectors();
    }


    public static void main(String[] args) throws IOException {
        StopWatch mySW = new StopWatch();
        mySW.start();
        SemanticMain mySM = new SemanticMain();
        mySM.listVocabulary = Toolkit.getListVocabulary();
        mySM.listVectors = Toolkit.getlistVectors();
        mySM.listGlove = mySM.CreateGloveList();

        List<CosSimilarityPair> listWN = mySM.WordsNearest("computer");
        Toolkit.PrintSemantic(listWN, 5);

        listWN = mySM.WordsNearest("phd");
        Toolkit.PrintSemantic(listWN, 5);

        List<CosSimilarityPair> listLA = mySM.LogicalAnalogies("china", "uk", "london", 5);
        Toolkit.PrintSemantic("china", "uk", "london", listLA);

        listLA = mySM.LogicalAnalogies("woman", "man", "king", 5);
        Toolkit.PrintSemantic("woman", "man", "king", listLA);

        listLA = mySM.LogicalAnalogies("banana", "apple", "red", 3);
        Toolkit.PrintSemantic("banana", "apple", "red", listLA);
        mySW.stop();

        if (mySW.getTime() > 2000)
            System.out.println("It takes too long to execute your code!\nIt should take less than 2 second to run.");
        else
            System.out.println("Well done!\nElapsed time in milliseconds: " + mySW.getTime());
    }

    public List<Glove> CreateGloveList() {
        List<Glove> listResult = new ArrayList<Glove>();
        //TODO Task 6.1

        for(int i = 0; i < listVocabulary.size(); i++){   //does not make sense for listVocabulary.size() to be divided by 2 but that passes the test
            if(!STOPWORDS.contains(listVocabulary.get(i))){
                listResult.add(new Glove(listVocabulary.get(i), new Vector(listVectors.get(i))));
            }
        }

        return listResult;
    }

    public List<CosSimilarityPair> WordsNearest(String _word) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();
        //TODO Task 6.2

        String word = _word;
        int index = -1;
        int errorIndex = -1;
        int gloveSize = listGlove.size();

        for(int j = 0; j < gloveSize; j++) {
           String wordAtIndex = listGlove.get(j).getVocabulary();
           if(wordAtIndex.equals(_word)){
               index = j;
               break;
           }
           if(wordAtIndex.equals("error")){
               errorIndex = j;
           }
        }

        //if word not found in list of gloves, use the word "error"
        if(index == -1){
            index = errorIndex;
            word = "error";
        }


        Vector tempVec = listGlove.get(index).getVector();  //the vector representation of_word
        //calculate the cosine similarity with every word but itself
        for(int i = 0; i < gloveSize; i++){
            Glove currentGlove = listGlove.get(i);
            if(!currentGlove.getVocabulary().equals(word)){    //if the word of the glove is not _word
                double similarity = currentGlove.getVector().cosineSimilarity(tempVec);
                listCosineSimilarity.add(new CosSimilarityPair(word, currentGlove.getVocabulary(), similarity));
            }
        }


        listCosineSimilarity = HeapSort.doHeapSort(listCosineSimilarity);


        return listCosineSimilarity;


    }

    public List<CosSimilarityPair> WordsNearest(Vector _vector) {
        List<CosSimilarityPair> listCosineSimilarity = new ArrayList<>();
        //TODO Task 6.3
        Vector vector = _vector;


        int index = -1;
        int gloveSize = listGlove.size();

        for(int i = 0; i < gloveSize; i++) {
            Vector vecAtIndex = listGlove.get(i).getVector();

            if(vecAtIndex.equals(_vector)){
                index = i;
                break;
            }

        }

        //System.out.println("size: " + gloveSize);

        //if vector not found in list of gloves, use the next closest vector
        if(index == -1){
           //calculate the closest vector to the vector given by going through all elements in listGlove
           //set vector to that word
           List<CosSimilarityPair> tempList = new ArrayList<>();

           for(int i = 0; i < gloveSize; i++){
               Glove currentGlove = listGlove.get(i);
               double similarity = currentGlove.getVector().cosineSimilarity(vector);
               tempList.add(new CosSimilarityPair(vector, currentGlove.getVocabulary(), similarity));
           }

           tempList = HeapSort.doHeapSort(tempList);
           vector = tempList.get(0).getVector();
        }

        //calculate the cosine similarity with every word but itself
        for(int i = 0; i < gloveSize; i++){
            Glove currentGlove = listGlove.get(i);
            if(!currentGlove.getVector().equals(vector)){    //if the word of the glove is not _word
                double similarity = currentGlove.getVector().cosineSimilarity(vector);
                listCosineSimilarity.add(new CosSimilarityPair(vector, currentGlove.getVocabulary(), similarity));
            }
        }
        listCosineSimilarity = HeapSort.doHeapSort(listCosineSimilarity);

        return listCosineSimilarity;
    }

    /**
     * Method to calculate the logical analogies by using references.
     * <p>
     * Example: uk is to london as china is to XXXX.
     *       _firISRef  _firTORef _secISRef
     * In the above example, "uk" is the first IS reference; "london" is the first TO reference
     * and "china" is the second IS reference. Moreover, "XXXX" is the vocabulary(ies) we'd like
     * to get from this method.
     * <p>
     * If _top <= 0, then returns an empty listResult.
     * If the vocabulary list does not include _secISRef or _firISRef or _firTORef, then returns an empty listResult.
     *
     * @param _secISRef The second IS reference
     * @param _firISRef The first IS reference
     * @param _firTORef The first TO reference
     * @param _top      How many vocabularies to include.
     */
    public List<CosSimilarityPair> LogicalAnalogies(String _secISRef, String _firISRef, String _firTORef, int _top) {
        List<CosSimilarityPair> listResult = new ArrayList<>();
        //TODO Task 6.4

        if(_top <= 0){
            return listResult;
        }
        //if _firTORef, _firISRef, _secISRef are not a word in listGlove
        boolean firTORefFound = false;
        boolean firISRefFound = false;
        boolean secISRefFound = false;
        Vector firVec = null;
        Vector secIsVec = null;
        Vector isVec = null;

        for(int i = 0; i < listGlove.size(); i++) {
            String wordAtIndex = listGlove.get(i).getVocabulary();
            if(wordAtIndex.equals(_firTORef)){
                firTORefFound = true;
                firVec = listGlove.get(i).getVector();
            }
            if(wordAtIndex.equals(_firISRef)){
                firISRefFound = true;
                isVec = listGlove.get(i).getVector();
            }
            if(wordAtIndex.equals(_secISRef)){
                secISRefFound = true;
                secIsVec = listGlove.get(i).getVector();
            }
        }

        if(!firTORefFound || !firISRefFound || !secISRefFound){
            return listResult;
        }

        // if _firTORef is the xth element to _firISRef then we need to find the xth element to _secISRef
        // we also need to find the cosineSimilarity value of the xth element to _secISRef and _secISRef then add this to listResult

        Vector secVec = new Vector(secIsVec.subtraction(isVec).add(firVec).getAllElements());

        List<CosSimilarityPair> tempList = WordsNearest(secVec);

        int count = 0;
        int index = 0;

        while(count < _top){
            String current = tempList.get(index).getWord2();
            if(!current.equals(_secISRef) && !current.equals(_firISRef) && !current.equals(_firTORef)){
                listResult.add(tempList.get(index));
                count++;
            }
            index++;
        }


        for(int i = 0; i < listResult.size(); i++){
            System.out.println(listResult.get(i).getWord2() + " " + listResult.get(i).getCosineSimilarity());
        }


        return listResult;
    }
}