import java.util.HashMap;

class State {

    String name;

    public State(String name) {
        this.name = name;
    }

    HashMap<Character, Boolean> leftOrRight = new HashMap<>(); //Right will be true
    HashMap<Character, State> nextState = new HashMap<>();
    HashMap<Character, Character> tapeOutput = new HashMap<>();

    boolean accept;
    boolean reject;

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public State transitionState(char input){
        return nextState.getOrDefault(input, null);
    }

    public boolean transitionLR(char input){
        return leftOrRight.getOrDefault(input, null);
    }

    public char outToTape(char input){
        return tapeOutput.getOrDefault(input, null);
    }


    public void addTransition(char input, State next, char output, boolean LR){
        leftOrRight.put(input,LR);
        nextState.put(input, next);
        tapeOutput.put(input, output);
    }
}
