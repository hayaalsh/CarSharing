package carsharing;

import java.util.List;

public interface CustomerDao {
    List<Customer> getAll();
    void rentCar(Customer customer, Car car);
    void returnCar(Customer customer);
    void addCustomer(String name);
}

