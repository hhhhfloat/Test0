package before.model.entity;

public class SaveDataWrapper {
    private String dataJson;
    private String hash;

    public SaveDataWrapper(){}

    public SaveDataWrapper(String dataJson, String hash){
        this.dataJson = dataJson;
        this.hash = hash;
    }

    public String getDataJson() { return dataJson; }
    public void setDataJson(String dataJson) { this.dataJson = dataJson; }
    public String getHash() { return hash; }
    public void setHash(String hash) { this.hash = hash; }
}
