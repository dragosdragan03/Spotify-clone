package fileio.input;

public final class UserInput {
    private String username;
    private int age;
    private String city;

    public UserInput() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "UserInput{" +
                "username='" + username + '\'' +
                ", age=" + age +
                ", city='" + city + '\'' +
                '}';
    }
}
