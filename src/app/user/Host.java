package app.user;

import lombok.Getter;

public class Host extends User{

    public Host(String username, int age, String city) {
        super(username, age, city);
    }

    public boolean isHost() {
        return true;
    }
}
