public class MyCharCounter extends PApplet implements CharCounter 
{  
  String testFile = "currentTestFile.txt";

  public void setup() {
    size(200, 200);
    processFile(testFile);

    // ...  print data to console    
  }

  public void draw() {
    background(100);
    // ...
  }

  public void processFile(String fileName) {
    // ...
  }

  public int getFrequency(String character) {
    // ...
    return 0;
  }

  public float getProbability(String character) {
    // ...
    return 0;
  }

}// end class



