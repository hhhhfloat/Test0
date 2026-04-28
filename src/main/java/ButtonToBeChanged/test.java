package ButtonToBeChanged;

class Processor {
    public void processData(ParameterModifier modifier) {
        int data = 100;
        data = modifier.modify(data);  // 允许外部修改处理逻辑
        System.out.println(data);
    }
}

// 回调接口
@FunctionalInterface
interface ParameterModifier {
    int modify(int value);
}

// 其他类中
public class test {
    static void modifyLogic() {
        Processor processor = new Processor();
        processor.processData(value -> value * 2);  // 修改为200

        processor.processData(value -> value + 50);  // 修改为150
    }

    public static void main(String[] args) {
        modifyLogic();
    }
}
