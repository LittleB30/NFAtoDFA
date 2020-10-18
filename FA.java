import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class FA {
	private int numStates;
	private ArrayList<Character> alphabets;
	private ArrayList<ArrayList<ArrayList<Integer>>> transitions;
	private int initial;
	private ArrayList<Integer> accepting;
	private boolean isNondeterministic;

    /***********CONSTRUCTORS***********/
    /**
     * Constructs an empty finite automata which is assumed to be nondeterministic.
     */
    public FA() {
        numStates = 0;
        alphabets = new ArrayList<>();
        transitions = new ArrayList<>();
        initial = -1;
        accepting = new ArrayList<>();
        isNondeterministic = false;
    }

    /**
     * Constructs a finite atomata from information stored in a file.
     * @param fileName file to be read from
     */
    public FA(String fileName) {
        numStates = 0;
        alphabets = new ArrayList<>();
        transitions = new ArrayList<>();
        accepting = new ArrayList<>();
        readFA(fileName);
    }

    /**
     * Constructs a finite atomata with given arguments.
     * @param num number of states
     * @param alpha finite list of alphabets
     * @param t transition function
     * @param i initial state
     * @param accept list of accepting states
     */
    public FA(int num, ArrayList<Character> alpha, ArrayList<ArrayList<ArrayList<Integer>>> t, int i, ArrayList<Integer> accept) {
        numStates = num;
        alphabets = alpha;
        transitions = t;
        initial = i;
        accepting = accept;
        if (alphabets.size() == transitions.get(0).size()) {//if there is not a lambda closure then it is a DFA.
            isNondeterministic = false;
        } else {
            isNondeterministic = true;
        }
    }

    /***********PUBLIC METHODS***********/
    /**
     * Converts the current nondeterministic FA to an equivalent deterministic FA.
     * @return a DFA equivalent to the current FA if it nondeterministic, otherwise null
     */
    public FA toDFA() {
        FA dfa = null;
        if (isNondeterministic) {
            dfa = new FA();
            Map<Set<Integer>,ArrayList<Set<Integer>>> transitonTable = new HashMap<>(); 

            ArrayList<Set<Integer>> trans = new ArrayList<>();
            Set<Integer> alphSet = getLambdaClosure(initial);
            boolean foundAllStates = false;
            while (!foundAllStates) {
                for (int i = 0; i < alphabets.size()-1; i++) { //for each nonlambda transition
                    //trans.add(defineTransition(i));
                }
                foundAllStates = true;
                for (Set<Integer> a : trans) {
                    if (!transitonTable.containsKey(a)) {
                        foundAllStates = false;
                    }
                }
            }

            dfa.isNondeterministic = false;
        }

        return dfa;
    }

    /**
     * Prints the FA based on whether or not it is deterministic.
     */
    public void printFA() {
        if (isNondeterministic) {
            System.out.print("Sigma: ");
            for (Character c : alphabets) System.out.print(c + " ");
            System.out.println("\n------");
            for (int i = 0; i < numStates; i++) {
                System.out.print(i + ": ");
                for (int j = 0; j < transitions.get(0).size()-1; j++) {
                    System.out.print("(" + alphabets.get(j) + "," + transitionToString(i, j) + ") ");
                }
                System.out.println("( " + "," + transitionToString(i, transitions.get(0).size()-1) + ")");
            }
            System.out.println("------");
        } else {
            System.out.print("Sigma:\t");
            for (Character c : alphabets) System.out.print(c + "\t");
            System.out.print("\n------");
            for (int i = 0; i < alphabets.size(); i++) System.out.print("----");
            for (int i = 0; i < numStates; i++) {
                System.out.print((i < 10000?" ":"") + (i < 1000?" ":"") + (i < 100?" ":"") + (i < 10?" ":"") + i + ":\t");
                for (ArrayList<Integer> transition : transitions.get(i)) {
                    for (int t : transition) {
                        System.out.print(t);
                        if (!transition.equals(getLambdaTranstions(i))) {
                            System.out.print("\t");
                        }
                    }
                }
                System.out.println();
            }
            System.out.print("\n------");
            for (int i = 0; i < alphabets.size(); i++) System.out.print("----");
        }
        System.out.println(initial + ": Initial State");
        System.out.println(acceptingToString() + ": Accepting State" + (accepting.size() > 1 ? "s":""));
    }

    public String toString() {
        return "M:(Q=" + numStates + 
                ", Σ=" + alphabets.size() + 
                ", δ=" + transitions.get(0).size() + 
                ", q=\"" + initial + "\"" +
                ", A=" + accepting.size() + ")";
    }

    /***********PRIVATE METHODS***********/
    /**
     * Reads an FA from a properly formated file.
     */
    private void readFA(String fileName) {
        Scanner scan = null;
		try {
			scan = new Scanner(new File(fileName));
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            return;
        }
        
        numStates = Integer.parseInt(scan.nextLine());

        char[] alpha = scan.nextLine().replaceAll("\\s", "").toCharArray();
        for (char a : alpha) alphabets.add(a);

        String curLine;
        for (int i = 0; i < numStates; i++) { //for each state
            curLine = scan.nextLine().trim();
            int leftIndex = -1;
            int rightIndex = -1;
            ArrayList<ArrayList<Integer>> trans = new ArrayList<>();
            for (int j = 0; j < curLine.length(); j++) { //find each set of "{}"
                if (curLine.charAt(j) == '{') {
                    leftIndex = j;
                }
                if (curLine.charAt(j) == '}') {
                    rightIndex = j;
                }
                if (leftIndex != -1 && rightIndex != -1) { //then turn that set into an ArrayList<Integer>
                    String curSet = curLine.substring(leftIndex+1, rightIndex);
                    curSet = curSet.replaceAll("\\s", "");
                    String[] states = curSet.split(",");
                    ArrayList<Integer> s = new ArrayList<>();
                    for (String state : states) {
                        if (!state.equals("")) {
                            s.add(Integer.valueOf(state));
                        }
                    }
                    trans.add(s); //add that set to this state's transitions
                    leftIndex = -1;
                    rightIndex = -1;
                }
            }
            transitions.add(trans); //add this state's transitions to the total transition function
        }

        initial = Integer.parseInt(scan.nextLine());

        String accept = scan.nextLine().replaceAll("\\s", "");
        accept = accept.substring(1, accept.length()-1);
        String[] a = accept.split(",");
        for (String state : a) {
            accepting.add(Integer.valueOf(state));
        }

        if (alphabets.size() == transitions.get(0).size()) {//if there is not a lambda closure then it is a DFA.
            isNondeterministic = false;
        } else {
            isNondeterministic = true;
        }
        scan.close();
    }

    /**
     * Adds a transition to the transitions ArrayList and increases the number of states.
     */
    private void addTransition(ArrayList<ArrayList<Integer>> trans) {
        transitions.add(trans);
        numStates++;
    }

    /** 
     * @param state the state of which to get the lambda transition
     * @return the lambda transition of a given state if nondeterministic, otherwise returns null
     */
    private ArrayList<Integer> getLambdaTranstions(int state) {
        ArrayList<Integer> lambda = null;
        if (isNondeterministic) {
            lambda = transitions.get(state).get(transitions.get(state).size()-1);
        }

        return lambda;
    }

    /**
     * @param state the state of which to get the lambda closure
     * @return the lambda closure of a given state if nondeterministic, otherwise returns null
     */
    private Set<Integer> getLambdaClosure(int state) {
        Set<Integer> lambda = null;
        if (isNondeterministic) {
            lambda = new TreeSet<>();
            lambda.add(state);
            for (int lTrans : getLambdaTranstions(state)) {
                closureHelper(lTrans, lambda);
            }
        }

        return lambda;
    }

    private void closureHelper(int state, Set<Integer> lambda) {
        if (lambda.add(state)) {
            for (int t : getLambdaTranstions(state)) {
                closureHelper(t, lambda);
            }
        }
    }

    private String transitionToString(int state, int transition) {
        String temp = "{";
        if (transitions.get(state).get(transition).size() > 0) {
            for (int num : transitions.get(state).get(transition)) {
                temp += num + ",";
            }
            temp += "\b";
        }
        temp += "}";
        return temp;
    }

    private String acceptingToString() {
        String temp = "";
        for (int accept : accepting) {
            temp += accept + ",";
        }
        temp += "\b";
        return temp;
    }

    /***********GETTERS***********/
    public int getNumStates() {
        return numStates;
    }

    public ArrayList<Character> getAlphabets() {
        return alphabets;
    }

    public ArrayList<ArrayList<ArrayList<Integer>>> getTransitions() {
        return transitions;
    }

    public int getInitial() {
        return initial;
    }

    public ArrayList<Integer> getAccepting() {
        return accepting;
    }

    public boolean isNondeterministic() {
        return isNondeterministic;
    }
}