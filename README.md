# Introduction-to-NLP
You can think of this project as a mathematical thesaurus. 
Given a word, the code outputs other words with the closest meaning to this word using mathematical calculations. 
This is done through calculating cosine similarity between pairs of words.

Disclaimer: I did not write all of this code. 
This was a university assignment therefore I was given rough guidlines on how to solve the problem. 
Taking this into consideration, a significant portion of the code is still my own.

To run:
Create an 'src' folder containing a 'main' folder. In the 'main' folder create a 'java' folder and a 'resources' folder.
Put all the .java files into the 'java' folder and all of the .csv files into the 'resources' folder.
In any Java compatible IDE, open the src folder and run any of the Java files.

How it works (briefly):
Each word in the csv file called glove is assigned a vector value.
We enter a word in the code which will have it's similarity to every other word calculated by use of the vector values.
The formula used for this is cosine similarity.
We then use a heap sort to display the most similar words to the inputted word in the terminal. 
