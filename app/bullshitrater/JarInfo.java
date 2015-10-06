package bullshitrater;

public class JarInfo {
  public double bullshitIndex;
  private double sumIndex;
  public double greatestIndex = 0;
  public String longestClassName;
  public int classCount = 0;

  public JarInfo() {}

  public JarInfo(double sumIndex, double greatestIndex, String longestClassName, int classCount) {
    this.sumIndex = sumIndex;
    this.greatestIndex = greatestIndex;
    this.longestClassName = longestClassName;
    this.classCount = classCount;
  }

  public void add(String className, double index) {
    classCount++;
    sumIndex += index;
    if (index > greatestIndex) {
      greatestIndex = index;
      longestClassName = className;
    }
  }

  public JarInfo evaluate() {
    bullshitIndex = sumIndex / classCount;
    return this;
  }

  public JarInfo combine(JarInfo js) {
    return new JarInfo(
        sumIndex + js.sumIndex,
        greatestIndex > js.greatestIndex ? greatestIndex : js.greatestIndex,
        greatestIndex > js.greatestIndex ? longestClassName : js.longestClassName,
        classCount + js.classCount);
  }
}
