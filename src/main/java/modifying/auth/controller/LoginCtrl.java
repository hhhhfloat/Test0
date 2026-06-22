package modifying.auth.controller;

import before.controller.AudioCtrl;
import before.dao.UserDao;
import before.model.entity.Account;
import javafx.application.Platform;
import modifying.shared.model.TOAST_TYPE;



public class LoginCtrl {
    private final UserDao userDao;
    private final AudioCtrl audioCtrl;
    private final AuthSceneCtrl authSceneCtrl;

    private Account account;
    private int loadNumber = 0;
    public Account getAccount() {
        return account;
    }

    public LoginCtrl(UserDao userDao, AudioCtrl audioCtrl, AuthSceneCtrl authSceneCtrl){
        this.userDao = userDao;
        this.audioCtrl = audioCtrl;
        this.authSceneCtrl = authSceneCtrl;
    }

    // Login Scene events
    public void handleLoginQuit(){
        audioCtrl.playButtonSound();
        authSceneCtrl.showConfirmDialog(
                "Sure to quit?",
                "",
                ()->{
                    audioCtrl.playButtonSound();
                    Platform.exit();
                }, ()->{}
        );
    }
    public void handleLoginConfirm(String username, String password){
        audioCtrl.playButtonSound();
        if(!username.trim().isEmpty() && !userDao.existForLogin(username)){
            authSceneCtrl.showToast("Username doesn't exist", TOAST_TYPE.WARNING);
        }
        else if(userDao.validate(username, password)){
            account = userDao.findByUsername(username);
            if(account == null) {
                userDao.deleteAccount(username);
                authSceneCtrl.showToast("Invalid Username", TOAST_TYPE.WARNING);
                return;
            }
            loadNumber = 0;
            authSceneCtrl.showAccountScene(this,"Login Succeeded",TOAST_TYPE.SUCCESS);
        } else{
            authSceneCtrl.showToast("Incorrect password", TOAST_TYPE.WARNING);
        }
    }
    public void handleRegister(){
        audioCtrl.playButtonSound();
        authSceneCtrl.showRegisterScene(this);

    }

    // Register Scene events
    public void handleRegisterCancel()
    {
        authSceneCtrl.showLoginScene(this);
    }
    public void handleRegisterConfirm(String username, String password, String confirmPwd){
        audioCtrl.playButtonSound();
        if(username.isEmpty()){
            authSceneCtrl.showToast("Username cannot be empty",TOAST_TYPE.WARNING);
            return;
        }
        String safeUsername = properName(username);
        if(username.length() > 1000){
            authSceneCtrl.showToast("Please use a shorter name\n2h0rteΠ γ0ur namε w1tμ\n.&❂*…←…鳼№茡洟丗▦©∭", TOAST_TYPE.ERROR);
        } else if (safeUsername.length()>200){
            authSceneCtrl.showToast("Please use a shorter name !#&@\\DEL※",TOAST_TYPE.WARNING);
        }
        else if (userDao.existForRegister(username)){
            authSceneCtrl.showToast("Safe name ["+properName(username)+"] is taken. Please choose another one.",TOAST_TYPE.WARNING);
        }
        else if(password.isEmpty()){
            authSceneCtrl.showToast("Please set up your password",TOAST_TYPE.WARNING);
        }
        else if(!password.equals(confirmPwd)){
            authSceneCtrl.showToast("Passwords do not match", TOAST_TYPE.WARNING);
        }
        else {
            userDao.createUser(username, password);
            account = new Account(username);
            account.setPassword(password);
            loadNumber = 0;
            authSceneCtrl.showAccountScene(this,"Register succeeded",TOAST_TYPE.SUCCESS);
        }
    }

    // Account Scene events

    public void handleStart(){
        audioCtrl.playButtonSound();
        authSceneCtrl.showLoadScene(this);
    }
    public void handleLogout() {
        audioCtrl.playButtonSound();
        authSceneCtrl.showConfirmDialog(
                "Sure to logout?",
                "",
                ()->{
                    audioCtrl.playButtonSound();
                    loadNumber=0;
                    account = null;
                    authSceneCtrl.showLoginScene(this);
                }, audioCtrl::playButtonSound);

    }
    public void handleQuit() {
        audioCtrl.playButtonSound();
        authSceneCtrl.showConfirmDialog(
                "Sure to quit?",
                "",
                ()->{
                    audioCtrl.playButtonSound();
                    Platform.exit();
                }, ()->{}
        );
    }

    // Load Scene events
    public void handleLoad(int k){
        audioCtrl.playButtonSound();
        loadNumber = k;
        // only need to find the place of load file and check whether the hash code matches
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




}
