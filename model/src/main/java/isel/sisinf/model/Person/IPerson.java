package isel.sisinf.model.Person;

public interface IPerson {
    int getId();
    void setId(int id);
    String getName();
    void setName(String name);
    String getAddress();
    void setAddress(String address);
    String getEmail();
    void setEmail(String email);
    String getPhone();
    void setPhone(String phone);
    String getIdentification();
    void setIdentification(String identification);
    String getNationality();
    void setNationality(String nationality);

    boolean isActive();

    void setActive(boolean active);

}
