package modifying.auth.model;

public class Account {
    String userName, password, safeUserName;

    public Account(String userName) {
        this.userName = userName;
        this.safeUserName = properName(userName);
    }

    public String getUserName() {
        return userName;
    }

    public String getSafeUserName() {
        return safeUserName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static final String SAFE_NAME_PATTERN = "[\\\\/:*?\"<>|\\p{Cntrl}]";
    public static String properName(String s){
        String cleaned = s.replaceAll(SAFE_NAME_PATTERN,"_");
        if(cleaned.startsWith(".")||cleaned.startsWith("-")){
            cleaned = "_"+cleaned;
        }
        return cleaned;
    }

}
