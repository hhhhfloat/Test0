package view.common;

import javafx.scene.control.Button;

// 用于创建按钮的类
public class ButtonUtils {
    // 样式用css配置，在场景初始化时就导入/css/button-styles.css
    // 创建文字按钮
    public static Button createTextButton(String text)
    {
        Button but = new Button(text);
        but.getStyleClass().add("menu-btn");
        return but;
    }

    // 创建控制按钮


    // 菜单按钮

}
