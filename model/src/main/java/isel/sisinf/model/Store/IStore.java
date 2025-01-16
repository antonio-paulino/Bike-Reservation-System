package isel.sisinf.model.Store;

import isel.sisinf.model.Person.Manager;

public interface IStore {
    int getCode();
    void setCode(int code);
    String getEmail();
    void setEmail(String email);
    String getAddress();
    void setAddress(String address);
    String getLocation();
    void setLocation(String location);
    String getPhone();
    void setPhone(String phone);
    Manager getManager();
    void setManager(Manager manager);

    boolean isActive();

    void setActive(boolean active);
}
