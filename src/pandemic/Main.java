package pandemic;

import pandemic.Animator;

/**
 * Launches the simulation with names of simulation views to animate.
 * @author Peter Sander
 */
class Main {

    public static void main(String[] args) {
        // pass along which simulation views to instantiate
        Animator.main("pandemic.GridView");
    }
}
