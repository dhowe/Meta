// do not modify this file
interface CharCounter 
{
  // loads and processes the file       
  public void processFile(String fileName);

  // returns the # of occurrences of 'character'      
  public int getFrequency(String character);

  /*
     returns the probability of 'character', defined as 
     getFrequency(character) / totalCount. probability must 
     be between 0 and 1, returning 0 if the char does not 
     occur in the file and 1 if it is the only character.
  */
  public float getProbability(String character);
}
