package pandemic;


import java.util.List;
import java.util.Random;

import static pandemic.Event.INFECTION_DURATION;
import static pandemic.Event.INFECTION_RATE;
import static pandemic.State.*;
import static pandemic.Transition.nextState;

/**
 * A class representing the people in our simulation.
 *
 * @author Sahi Gonsangbeu
 * @version 2021.04.20
 */
class Human {


    // The state of the people (Healthy, infected, recovered or dead)
    private State status;
    // the people's field
    private Field field;
    // the people's location on the field
    private Location location;
    private static final Random rand = Randomizer.getRandom();

    static int MAX_CONTAMINATION = 10;
    int contamination;

    /**
     * Create a new people at location in field.
     *
     * @param field    The field currently occupied.
     * @param location The location within the field.
     */
    Human(Field field, Location location, State state) {
        if(state==INFECTED){
            status=INFECTED;
            contamination=1;
        }
        else if (state == RECOVERED){
            status = RECOVERED;
            contamination=0;
        }
        else {
            status = HEALTHY;
            contamination = 0;
        }
        this.field = field;
        setLocation(location);
    }

    public static void setMaxContamination(int max) {
        MAX_CONTAMINATION = max;
    }


    /**
     * Return the people's location.
     *
     * @return The people's location.
     */
    Location getLocation() {
        return location;
    }

    /**
     * Place the people at the new location in the given field.
     *
     * @param newLocation The people's new location.
     */
    void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the people's field.
     *
     * @return The people's field.
     */
    Field getField() {
        return field;
    }

    /**
     * @return the human's state
     */
    public State getStatus(){
        return status;
    }

    void setStatus(State state){
        this.status=state;
    }


    /**
     * This is what a human does most of time
     */
    void act() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        for (Location where : adjacent) {
            Human human = field.getPeopleAt(where);
            if (human != null) {
                if (human.getStatus() == INFECTED) {
                    if (this.getStatus() == HEALTHY) {
                        State newState = nextState(HEALTHY, INFECTION_RATE);
                        setStatus(newState);
                    } else if (this.getStatus() == RECOVERED) {
                        State newState = nextState(RECOVERED, INFECTION_RATE);
                        setStatus(newState);
                    }
                }

            }

        }
        
    }
    /**
     * this is how an infected person behaves
     */
    void infectedBehaviour() {
        contamination++;
        if (contamination > MAX_CONTAMINATION) {
            State newState;
            if (this.getStatus() == INFECTED){
                newState = nextState(INFECTED, INFECTION_DURATION);
            }
            else {
                newState = nextState(HEALTHY, INFECTION_RATE);
            }
            setStatus(newState);

        }

    }

    /**
     * This is how a person moves from a location to another
     */
    void move(){
        Location newLocation = getField().freeAdjacentLocation(getLocation());
        setLocation(newLocation);
    }

}


