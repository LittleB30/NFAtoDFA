/**
 * This is the driver class used to run the FA class.
 * @author Alex Smith (alsmi14@ilstu.edu)
 */
public class NFA {
    public static void main(String[] args) {
        // if (args.length < 2) {
        //     return;
        // }
        // String faFile = args[0];
        // String stringsFile = args[1];
        String faFile = "nfa2";
        String stringsFile = "strings.txt";
        FA nfa = new FA(faFile);
        nfa.printFA();
        FA dfa = nfa.toDFA();
        dfa.printFA();
        dfa.areSentences(stringsFile, 30);
    }
}
