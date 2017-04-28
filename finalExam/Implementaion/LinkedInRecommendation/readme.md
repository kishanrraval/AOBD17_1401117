# Using the code:
* Extract all files from data.rar file to LinkedInRecommendation folder
* These files contains intermediate data and skills, company and  for whole training data train
* Download similarity matrix generated for training data from [here](https://drive.google.com/open?id=0B0w2LDAuXPPqa3hXcTJqMDhUN0k) and place it in the same folder.
* To run the algorithm on your own data, pass your JSON file to recommend.java/generateLSVSupport function, which cleans the data according to our need. Do the same for training data. 


------------------
Classes

* LinkedInRecommendation.java - Main program which contains all steps from cleaning the data to generating the output. Uncomment necessary steps to generate intermediate files.
* Recommend.java - Functions implemented for all the tasks such as cleaning data, generating matrix, reading and storing data, serialization and deserialization
* Utils.java - Contains utility functions for LSA
* Value.java - A helper class to create a new datatype which contains two String.
