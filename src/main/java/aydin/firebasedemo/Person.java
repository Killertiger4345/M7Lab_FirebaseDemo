package aydin.firebasedemo;

public class Person {
    private String name;
    private int age;
    private String phoneNumber;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
        this.phoneNumber = "-1";
    }

    public Person(String name, int age, String phoneNumber) {
        this.name = name;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public String getPhoneNumber() { return phoneNumber; }

    public void setName(String name) {
        this.name = name;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

}
