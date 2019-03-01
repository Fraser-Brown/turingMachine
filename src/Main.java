import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException {
        ArrayList<Character> tape = new ArrayList<>();
        HashMap<String, State> states = new HashMap<>();
        ArrayList<Character> alphabet = new ArrayList<>();
        alphabet.add('_');

        State start = null;
        State reject = null;

        String tm = args[0];
        String input = null;
        if(args.length == 2){
            input = args[1];
        }

        FileReader fr = new FileReader(tm);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();

        if(line.matches("states [1-9]*")){
            boolean alphabetSeen = false;

            while ((line = br.readLine()) != null){
                String[] a = line.split(" ");

               if(line.matches("alphabet.*")){

                    for(int i = 2; i < a.length; i++){
                        alphabet.add(a[i].charAt(0));
                    }
                    alphabetSeen = true;
               }

               else if(alphabetSeen){
                   if(a.length != 5){
                       System.out.println("input error");
                       System.exit(2);
                   }
                   else {
                       State working = states.get(a[0]);
                       State transition = states.get(a[2]);
                       boolean r = false;
                       if (a[4].equals("R")) {
                           r = true;
                       }

                       working.addTransition(a[1].charAt(0), transition, a[3].charAt(0), r);
                   }
               }
               else{
                   State x = new State(a[0]);
                   states.put(a[0], x);
                   if(a.length == 2){
                        if(a[1].equals("+")){
                            x.setAccept(true);
                        }
                        else if(a[1].equals("-")){
                            x.setReject(true);
                            reject = x;
                        }
                   }
                   if(start == null){
                       start = x;
                   }
               }

            }
        }

        else{
            System.out.println("input error");
            System.exit(2);
        }


        if(!input.equals(null)){
            fr = new FileReader(input);
            br = new BufferedReader(fr);
            while ((line = br.readLine()) != null){
                line = line.replaceAll(" ", "");
                char[] addToTape = line.toCharArray();
                for (char x: addToTape) {
                    if(alphabet.contains(x)) {
                        tape.add(x);
                    }
                    else{
                        System.out.print("input error");
                        System.exit(2);
                    }
                }
            }

        }

        br.close();
        fr.close();


        runMachine(start, tape,reject);

    }

    public static void runMachine(State start, ArrayList<Character> tape, State reject){
        int headPointer = 0;
        int counter = 0;
        boolean running = true;
        State current  = start;

        while(running){
            if(current.isAccept()){
                running = false;
                System.out.println("accepted");
                System.out.println(counter - 1);
                printTape(tape);
                System.exit(0);
            }

            if(current.isReject()){
                running = false;
                System.out.println("rejected");
                System.out.println(counter - 1);
                printTape(tape);
                System.exit(1);
            }

            char input = '_';
            if(headPointer < tape.size()) {
                input = tape.get(headPointer);
            }

            State next = current.transitionState(input);
            if(next == null){
                current = reject;
            }
            else{
                char replace = current.outToTape(input);
                if(headPointer < tape.size()) {
                    tape.set(headPointer, replace);
                }
                else{
                    tape.add(replace);
                }


                boolean lr = current.transitionLR(input);
                if(lr){
                    headPointer++;
                }
                else if(headPointer > 0){
                    headPointer--;
                }

                current = next;
            }

            counter++;

        }

    }

    public static void printTape(ArrayList<Character> tape){
        boolean blankRemover = false;
        tape.trimToSize();
        int check = 0;

        String printMe = "";

        for (char x: tape) {
            if(blankRemover) {
                if(!(check == tape.size()-1 && x == '_')) {
                    printMe = printMe + x;
                }
            }
            else if(x != '_'){
                blankRemover = true;
                printMe = printMe + x;

            }
            check++;
        }

        if(!blankRemover){
            printMe = "_";
        }

        System.out.print(printMe);
    }
}

