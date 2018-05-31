import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<Bean>list1=new ArrayList<>();
        List<Bean>list2=new ArrayList<>();
        Bean bean=new Bean(1);
        Bean bean1=new Bean(2);
        Bean bean2=new Bean(3);
        Bean bean3=new Bean(4);
        Bean bean4=new Bean(5);
        Bean bean5=new Bean(6);
        Bean bean6=new Bean(7);
        list1.add(bean);//1
        list1.add(bean2);//3
        list1.add(bean3);//4
        list1.add(bean4);//5
        list1.add(bean5);//6

        list2.add(bean);//1
        list2.add(bean1);//2
        list2.add(bean6);//7
        list2.removeAll(list1);//2 7
        System.out.println(list2.toString());

    }

    static class Bean {
        int id;

        public Bean(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Bean{" +
                    "id=" + id +
                    '}';
        }
    }
}
