package view.common;

import javafx.scene.control.Button;

// 用于创建按钮的类
public class ButtonUtils {
    // 样式用css配置，在场景初始化时就导入/css/button-styles.css

    // 这里只创建按钮
    // 按钮的事件通过ButtonFactory定义
    // 创建按钮 - 配置样式 - 返回
    // 样式均在css文件中更改

    // 创建文字按钮
    public static Button createTextButton(String text)
    {
        Button btn = new Button(text);
        btn.getStyleClass().add("menu-btn");
        return btn;
    }

    // 创建控制按钮


    // 菜单按钮

}
