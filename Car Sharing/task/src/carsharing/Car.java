package carsharing;

public class Car {

    private final int id;
    private final String name;
    private final int companyId;

    Car(int id, String name, int companyId){
        this.id = id;
        this.name = name;
        this.companyId = companyId;
    }

    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getCompanyId(){
        return companyId;
    }

    public String toString(){
        return name;
    }

}