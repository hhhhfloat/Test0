# 函数使用使用文档

## 函数用法介绍

### 一、寻路算法

#### 1：自动寻路

**步骤：**

1. __初始化LinkyMap类对象__
2. __调用autoFindPath函数，返回一个哈希列表(待修改)，包含路径的所有点(待修改)__
3. 使用showPath函数，返回含有路径的地图二维数组，可直接打印
4. __调用delMap函数，更新地图与数表NumMap__

**参考代码：**

```java
/* other import things */
import org.exampl.Functions.*
import java.util.HashSet;

public void Func()
{
    int MAPX = 6, MAPY = 6;
    LinkyMap level = new LinkyMap(MAPX, MAPY);
    
    boolean msg = true;
    while(!isComplete(level.getMap()))
    {
        HashSet<Point>path = level.autoFindPath();
        if(path.isEmpty()) 
        {
            System.out.println("没有路径了");
            msg = false;
            break;
        }
        // 选用打印路径与地图
        int[][] map_p = ShowPath(path, level.getMap(), MAPX, MAPY);
        PrintMap(level.getMap(), MAPX, MAPY);
        
        // 必须更新的
        level.del(NumMap);
    }
    if(msg)
    { 
        System.out.println("消除完毕!");
    }
}