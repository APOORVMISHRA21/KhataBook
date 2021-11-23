package Models;

public class AssetEntry {

    private String name, description, value;

    public AssetEntry(){}

    public AssetEntry(String name, String description, String worth){
        this.name = name;
        this.description = description;
        this.value = worth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
