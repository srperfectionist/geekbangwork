package week_1;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

/**
 * 自定义类加载器
 *
 * @author shirui
 * @date 2022/1/9
 */
public class CustomClassloader extends ClassLoader{

    /**
     * class路劲
     *
     */
    private static final String PATH = "/Users/shirui/Documents/极客时间/Java进阶训练营/JVM/Hello.xlass";

    /**
     * class名称
     *
     */
    private static final String CLASS_NAME= "Hello";

    /**
     * 执行的方法名称
     *
     */
    private static final String METHOD_NAME = "hello";

    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> clazz = new CustomClassloader().findClass(CLASS_NAME);
        Object obj = clazz.getDeclaredConstructor().newInstance();
        clazz.getMethod(METHOD_NAME).invoke(obj);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = getByteByFile();
        return defineClass(name, bytes, 0, bytes.length);
    }

    /**
     *  根据文件获取byte数组
     *
     * @return
     */
    private static byte[] getByteByFile(){
        FileInputStream inputstream = null;
        byte[] bytes = null;
        try {
            File file = new File(PATH);
            inputstream = new FileInputStream(file);
            bytes = new byte[inputstream.available()];
            inputstream.read(bytes);
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            classResource(inputstream);
        }

        return bytes;
    }

    /**
     * 关闭资源
     *
     * @param resource
     */
    private static void classResource(Closeable resource) {
        if (null != resource) {
            try {
                resource.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
