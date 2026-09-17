public class Student {
    String name;
    double score;
    boolean passed;

    public Student(String name, double score) {
        this.name = name;
        this.score = score;
        this.passed = false;
    }

    public void checkPassed() {
        if (score >= 70) {
            passed = true;
        } else {
            passed = false;
        }
    }
}