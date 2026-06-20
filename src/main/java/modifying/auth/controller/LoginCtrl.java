package modifying.auth.controller;

import before.controller.AudioCtrl;
import before.dao.UserDao;
import before.model.entity.Account;


public class LoginCtrl {
    private final UserDao userDao;
    private final AudioCtrl audioCtrl;
    private final AuthSceneCtrl authSceneCtrl;

    private Account account;
    private int loadNumber = 1;

    public LoginCtrl(UserDao userDao, AudioCtrl audioCtrl, AuthSceneCtrl authSceneCtrl){
        this.userDao = userDao;
        this.audioCtrl = audioCtrl;
        this.authSceneCtrl = authSceneCtrl;
    }

    public void handleLoginQuit(){
        // show the warning
        // if yes, quit; if no, disappear
    }
    public void handleLoginConfirm(String username, String password){
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
            loadNumber = 1;
            authSceneCtrl.showAccountScene(this);
        } else{
            // warning : wrong password
        }
    }


}
