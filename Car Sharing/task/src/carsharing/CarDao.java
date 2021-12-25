package carsharing;

public interface CarDao {
    void updateCars(Company company);
    void addCar(String name);
    int size();
    Car get(int id);
    void updateCars();
}
