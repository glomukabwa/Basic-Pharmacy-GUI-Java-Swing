package PharmacyGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {//See why we use this below
            new WelcomePage();
            //Or you can create an object of the method then do:
            //object.setVisible(true);
            //but new WelcomePage() already calls the setVisible method which is inside the WelcomePage so this is repetition and there's no point of doing all this typing
            //object.show() also works however it is an old AWT method and we want to be modern in this case
        });

        //The Swing Utilities.invokeLater() is the safest way of calling a method
        //It ensures that the GUI is created on the Event Dispatch Thread
        //Swing is single-threaded and not thread-safe
        //All Swing components (like JButton, JLabel, JFrame) must be created and modified on a single thread called the Event Dispatch Thread (EDT).
        //If you try to update a button or label from another thread, you can get:
        //Random freezes ❄️
        //Inconsistent UI updates 🎭
        //Even outright crashes 💥
        //invokeLater() schedules your code to run on the EDT.
        //If you are currently on the main thread (which starts your program), it makes sure Swing runs your UI setup in the proper place.
        //So your GUI construction and event handling all happen in the same thread → no race conditions.
        //If you just do new WelcomePage() the constructor runs on the main thread, not the EDT.
        //The EDT hasn’t started yet, so Swing will later “jump threads” internally.
        //Sometimes this works fine, sometimes it leads to subtle bugs like:
        //Components don’t repaint correctly
        //Events (like button clicks) behave strangely
        //UI freezes if your main thread is doing heavy work

        //Analogy:
        //Think of Swing’s EDT as a dedicated painter 🎨
        //You hand him tasks like “draw a button” or “change the label text.”
        //If you try to paint the canvas yourself (from the main thread), sometimes the painter and you overwrite each other → messy picture.
        //invokeLater() says: “Hey painter, please do this when you’re ready” → safe and orderly.
    }
}
