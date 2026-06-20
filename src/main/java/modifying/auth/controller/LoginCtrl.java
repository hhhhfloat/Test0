package modifying.auth.controller;

import before.controller.AudioCtrl;
import before.dao.UserDao;
import before.model.entity.Account;


public class LoginCtrl {
    private final UserDao userDao;
    private final AudioCtrl audioCtrl;
    private final AuthSceneCtrl authSceneCtrl;

    private Account account;
    private int loadNumber = 0;

    public LoginCtrl(UserDao userDao, AudioCtrl audioCtrl, AuthSceneCtrl authSceneCtrl){
        this.userDao = userDao;
        this.audioCtrl = audioCtrl;
        this.authSceneCtrl = authSceneCtrl;
    }

    public void handleLoginQuit(){
        audioCtrl.playButtonSound();
        // show the warning
        // if yes, quit; if no, disappear
    }
    public void handleLoginConfirm(String username, String password){
        audioCtrl.playButtonSound();
        if (username.trim().isEmpty()){
            // warning : username can't be null
        } else if(!userDao.existForLogin(username)){
            // warning : username doesn't exist
        }
        else if(userDao.validate(username, password)){
            account = userDao.findByUsername(username);
            if(account == null) {
                userDao.deleteAccount(username);
                // warning : INVALID username
                return;
            }
            // warning : "Login succeeded"
            loadNumber = 0;
            authSceneCtrl.showAccountScene(this);
        } else{
            // warning : wrong password
        }
    }
    public void handleRegister(){
        audioCtrl.playButtonSound();
        authSceneCtrl.showRegisterScene(this);
    }

    public void handleRegisterCancel()
    {
        authSceneCtrl.showLoginScene(this);
    }

    public void handleRegisterConfirm(String username, String password, String confirmPwd){
        audioCtrl.playButtonSound();
        if(username.isEmpty()){
            // warning : username can't be empty
            return;
        }
        String safeUsername = properName(username);
        if(username.length() > 1000){
            // warning : Please use a shorter name \n Shorten your name with .&❂*…←…鳼№茡洟丗▦©∭
        } else if (safeUsername.length()>200){
            // warning : Please use a shorter name
        }
        else if (userDao.existForRegister(username)){
            // warning : username already exists
        }
        else if(password.isEmpty()){
            // warning : Please set up your password
        }
        else if(!password.equals(confirmPwd)){
            // warning : password do not match
        }
        else {
            userDao.createUser(username, password);
            account = new Account(username);
            account.setPassword(password);
            // info : register succeeded
            loadNumber = 0;
            authSceneCtrl.showAccountScene(this);
        }
    }



    /// Util methods

    private static final String SAFE_NAME_PATTERN = "[\\\\/:*?\"<>|\\p{Cntrl}]";
    public static String properName(String s){
        String cleaned = s.replaceAll(SAFE_NAME_PATTERN,"_");
        if(cleaned.startsWith(".")||cleaned.startsWith("-")){
            cleaned = "_"+cleaned;
        }
        return cleaned;
    }


    public Account getAccount() {
        return account;
    }

    public void handleLogout() {
        audioCtrl.playButtonSound();
        // warning : sure?
        if(true){
            loadNumber = 0;
            account = null;
            authSceneCtrl.showLoginScene(this);
        }

    }

    public void handleQuit() {
        // warning : sure?
    }
}
