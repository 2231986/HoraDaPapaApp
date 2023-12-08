package pt.ipleiria.estg.dei.horadapapa.models;

public class Plate {
    private int id, capa, price;
    private String title, description;

    public Plate(int id, int capa, int price, String title, String description) {
        this.id = id;
        this.capa = capa;
        this.price = price;
        this.title = title;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapa() {
        return capa;
    }

    public void setCapa(int capa) {
        this.capa = capa;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
