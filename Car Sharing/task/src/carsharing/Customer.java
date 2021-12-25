package carsharing;

public class Customer {

    private final int id;
    private final String name;
    private final int carId;

    Customer(int id, String name, int carId){
        this.id = id;
        this.name = name;
        this.carId = carId;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getCarId() { return carId; }

    public boolean hasCar() {
        if (carId!=0)
            return true;
        else
            return false;
    }

    public String toString(){
        return name;
    }

}