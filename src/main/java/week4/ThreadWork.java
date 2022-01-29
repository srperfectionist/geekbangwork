package week4;

/**
 * @author shirui
 * @date 2022/1/30
 */
public class ThreadWork {

    private volatile String name;

    public static void main(String[] args) {

        long start = System.currentTimeMillis();

        final ThreadWork method = new ThreadWork();
        Thread thread = new Thread(() -> {
            method.hello();
        });
        thread.start();

        // 这是得到的返回值
        String result = method.getValue();

        System.out.println("异步计算结果为："+result);

        System.out.println("使用时间："+ (System.currentTimeMillis()-start) + " ms");
    }

    public void hello() {
        name = "This is Thread";
    }

    public String getValue(){
        while (null == name) {
        }
        return this.name;
    }
}
