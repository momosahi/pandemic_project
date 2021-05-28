package pandemic;


/**
 * The different status of a person in the simulation
 * @author Sahi Gonsangbeu
 */
enum State {
    // each state is associated to a number between 1 and 4
    // I replaced SUSCEPTIBLE by HEALTHY
    HEALTHY(1),
    INFECTED(2),
    RECOVERED(3),
    DEAD(4);

    protected int number;

    State(int number) {
        this.number = number;
    }

    String getSimpleName(){
        String chaine = "";
        switch (number){
            case 1:
                chaine = "healthy (h)";
                break;
            case 2:
                chaine = "infected (i)";
                break;
            case 3:
                chaine = "recovered (r)";
                break;
            default:
                chaine = "dead (rip)";
                break;
        }
        return chaine;

    }
}
