package carsharing;
import java.util.List;

public interface CarDao {
    List<Car> getAll(Company company);
    List<Car> getAvalible(Company company);
    void addCar(String name, Company company);
    Car get(Customer owner);
}
