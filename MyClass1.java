
public class MyClass1 {
    public static void main(String[] args) {
        MyClass2 mc = new MyClass2(4);
        System.out.println(mc.getA());
        int i = 1;
        String a = "Hello";
    }
}

class MyClass2 {
    private int a;

    public MyClass2(int a) {
        this.a = a;
    }

    public int getA() {
        return a;
    }
}