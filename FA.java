import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

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
        isNondeterministic = true;
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
     * Prints the FA based on whether or not it is deterministic.
     */
    public void printFA() {
        if (isNondeterministic) {
            
        } else {
            
        }
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

        char[] alpha = scan.nextLine().replaceAll(" ", "").toCharArray();
        for (char a : alpha) alphabets.add(a);

        String curLine;
        String[] curSets;
        String[] curSet;
        for (int i = 0; i < numStates; i++) {
            curLine = scan.nextLine().trim();
            curLine = curLine.substring(curLine.indexOf("{"));
            curLine = curLine.replaceAll("\\{", "");
            curLine = curLine.replaceAll("\\}", "");
            curSets = curLine.split(" ");
            ArrayList<ArrayList<Integer>> trans = new ArrayList<>();
            for (String sets : curSets) {
                curSet = sets.split(",");
                ArrayList<Integer> s = new ArrayList<>();
                for (String state : curSet) {
                    if (!state.equals("")) {
                        s.add(Integer.valueOf(state));
                    }
                }
                trans.add(s);
            }
            transitions.add(trans);
        }

        initial = Integer.parseInt(scan.nextLine());

        String accept = scan.nextLine().trim();
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