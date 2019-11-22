import java.util.ArrayList;
import java.util.List;

class Observable {
    List<Observer> observers = new ArrayList<>();

    protected void notifyObservers() {
        for (Observer o : this.observers){
            o.update();
        }
    }

    public void addObserver(Observer o) {
        this.observers.add(o);
    }

    public void removeObserver(Observer o) {
        this.observers.remove(o);
    }
}