package modifying.shared.model;

public enum TOAST_TYPE{
    SUCCESS("toast-success"),
    ERROR("toast-error"),
    WARNING("toast-warning");

    private final String cssClass;

    TOAST_TYPE(String cssClass){
        this.cssClass = cssClass;
    }
    public String getCssClass(){
        return cssClass;
    }
}
