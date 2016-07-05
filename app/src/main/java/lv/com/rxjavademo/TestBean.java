package lv.com.rxjavademo;

/**
 * User: 吕勇
 * Date: 2016-07-05
 * Time: 14:49
 * Description:
 */
public class TestBean {
    private int age=1;
    private String name="lv";

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
