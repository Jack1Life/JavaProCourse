package flats;

import javax.persistence.*;

@Entity
@Table(name="Flats")
public class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String city;
    private String district;
    private String street;
    private String houseNum;
    private int houseBuildYear;
    private int floor;
    private double square;
    private int roomsNum;
    private long price;

    public Flat() {}

    public Flat(String city, String district, String street, String houseNum, int houseBuildYear, int floor, double square, int roomsNum, long price) {
        this.city = city;
        this.district = district;
        this.street = street;
        this.houseNum = houseNum;
        this.houseBuildYear = houseBuildYear;
        this.floor = floor;
        this.square = square;
        this.roomsNum = roomsNum;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }

    public int getHouseBuildYear() {
        return houseBuildYear;
    }

    public void setHouseBuildYear(int houseBuildYear) {
        this.houseBuildYear = houseBuildYear;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public int getRoomsNum() {
        return roomsNum;
    }

    public void setRoomsNum(int roomsNum) {
        this.roomsNum = roomsNum;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "---- Квартира " + this.id + System.lineSeparator()
                + "\tРозташування: " + this.city + ", " + this.district + " район, " + this.street + " " + this.houseNum + System.lineSeparator()
                + "\tПоверх: " + this.floor + System.lineSeparator()
                + "\tКількість кімнат: " + this.roomsNum + System.lineSeparator()
                + "\tПлоща: " + this.square + System.lineSeparator()
                + "\tРік побудови: " + this.houseBuildYear + System.lineSeparator()
                + "\tЦіна за місяць: " + this.price;
    }
}
