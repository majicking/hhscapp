import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Test {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();

        list.add("11");
        list.add("12");
        list.add("13");
        list.add(0, "替换1");
        list.add(1, "替换2");
        list.add(2, "替换3");
        System.out.println(list);
    }
//    public static void main(String[] args) {
////        List<Test1> list1 = new ArrayList<>();
////        List<Test1> list2 = new ArrayList<>();
////        list1.add(new Test1("1", "a"));
////        list1.add(new Test1("2", "b"));
////        list1.add(new Test1("3", "c"));
////
////        Test1 test1 = list1.get(1);
////        test1.name = "修改111111111";
////        System.out.println(list1);
//
//
//        List<Integer>list=new ArrayList<>();
//        List<Integer>list1=new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        list.add(4);
//
//        list1.add(4);
//        list1.add(5);
//        list1.add(7);
//        list.retainAll(list1);
//
//        System.out.println(list);
//
//    }
//
//    static class Test1 {
//        String id;
//        String name;
//
//        public Test1(String id, String name) {
//            this.id = id;
//            this.name = name;
//        }
//
//        @Override
//        public String toString() {
//            return "Test1{" +
//                    "id='" + id + '\'' +
//                    ", name='" + name + '\'' +
//                    '}';
//        }
//    }
}
