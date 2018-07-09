import com.guohanhealth.shop.utils.Logutils;

public class Test {
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            switch (i) {
                case 0:
                    if (i==0) {
                        System.out.println(i);
                    }
                    break;
                case 1:
                    if (i==1) {
                        System.out.println(i);
                    }
                case 2:
                    if (i==2) {
                        System.out.println(i);
                    }
                case 3:
                    if (i==3) {
                        System.out.println(i);
                    }
                    break;
                default:
                    System.out.println("default=" + i);

            }
        }
    }


}
