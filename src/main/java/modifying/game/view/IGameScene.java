package modifying.game.view;

import javafx.scene.Parent;

public interface IGameScene {
    // 返回该场景的根节点（你的 StackPane 或 Pane）
    Parent getView();

    // 进入场景时调用（加载资源、初始化数据）
    void onEnter();

    // 退出场景时调用（释放内存、移除监听）
    void onExit();

    // 场景被覆盖时调用（比如打开背包/暂停菜单）
    void onPause();

    // 场景重新回到顶层时调用
    void onResume();

    // 核心游戏循环更新方法，由 AnimationTimer 每帧驱动
    void update(long now);
}
