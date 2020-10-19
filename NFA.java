public class NFA {
    public static void main(String[] args) {
        FA fa1 = new FA("nfa2");
        fa1.printFA();
        FA fa2 = fa1.toDFA();
        fa2.printFA();
    }
}
