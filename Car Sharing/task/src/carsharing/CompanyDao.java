package carsharing;

public interface CompanyDao {
    void updateCompanies();
    void addCompany(String name);
    int size();
    Company get(int id);
}

