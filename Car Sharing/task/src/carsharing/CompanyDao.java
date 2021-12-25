package carsharing;

import java.util.List;

public interface CompanyDao {
    List<Company> getAll();
    void addCompany(String name);
    Company get(Car car);
}

