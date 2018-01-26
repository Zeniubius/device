import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2017/10/18.
 */
public class deviceInfo {
    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {

//        String s = String.valueOf((1 + new Random().nextDouble()) * Math.pow(10, 6));
//        System.out.println(s);
//        System.out.println(s.substring(1, 7));

//        readAndSet();

//        for (int i = 0; i < 1048572; i++) {
//            getmeid15("1000065591202C");
//        }

//        genMEID();
//        genICCID();
        genBlueTooth();
    }

    private static void readAndSet() throws IOException {
        FileReader fileReader = new FileReader("C://Users//Administrator//Desktop//device1.txt");
        BufferedReader reader = new BufferedReader(fileReader);
        String temp = null;

        FileWriter fileWriter = new FileWriter("C://Users//Administrator//Desktop/device-new.txt", true);

//        int i = 0;
        while ((temp = reader.readLine()) != null) {
//            if (temp.split("\t")[0].length() != 17) {
//                continue;
//            }
//            if (i == 10) {
//                break;
//            }

            String imei = GenricOneIMEI();
            String imsi = GenricOneIMSI();

            StringBuffer androidId = new StringBuffer();

            for (int j = 0; j < 15; j++) {
                androidId.append(Integer.toHexString(new Random().nextInt(16)));
            }
            String line = temp + "\t" + imei + "\t" + imsi + "\t" + androidId.toString() + "\r\n";

            fileWriter.write(line);
//            i++;
        }

        fileWriter.close();
        fileReader.close();
        reader.close();
    }

    private static void genMEID() throws IOException {

        FileWriter fileWriter = new FileWriter("C://Users//Administrator//Desktop/meid.txt", true);

        List<String> meids = DeviceUtil.beachMEID("10000655912035", 1048572);
        for (String meid : meids) {
            fileWriter.write(meid + "\n");
        }

        fileWriter.close();
    }

    private static void genICCID() throws IOException {
        List<String> ICCIDS = DeviceUtil.beachICCID(1048572);

        FileWriter fileWriter = new FileWriter("C://Users//Administrator//Desktop/iccid.txt", true);
        for (String iccid : ICCIDS) {
            fileWriter.write(iccid + "\n");
        }
        fileWriter.close();
    }

    private static void genBlueTooth() throws IOException {

        List<String> blues = DeviceUtil.beachBlueTooth(1048572);

        FileWriter fileWriter = new FileWriter("C://Users//Administrator//Desktop/blue.txt", true);
        for (String blue : blues) {
//            System.out.println(blue);
            fileWriter.write(blue + "\n");
        }
        fileWriter.close();
    }

    /**
     * 生成IMSI
     *
     * @throws IOException
     */
    private static void genricIMSI() throws IOException {
        String[] mncArray = {"00", "01", "02", "03", "04", "05", "06", "07", "09", "11", "20"};
        String mcc = "460";

        List<String> imsis = new ArrayList<String>();
        for (int i = 1; i <= 13597401; i++) {
            int mncIndex = new Random().nextInt(11);
            int nextInt1 = new Random().nextInt(10);
            int nextInt2 = new Random().nextInt(10);
            int nextInt3 = new Random().nextInt(10);
            int nextInt4 = new Random().nextInt(10);
            int nextInt5 = new Random().nextInt(10);
            int nextInt6 = new Random().nextInt(10);
            int nextInt7 = new Random().nextInt(10);
            int nextInt8 = new Random().nextInt(10);
            int nextInt9 = new Random().nextInt(10);
            int nextInt10 = new Random().nextInt(10);

            String imsi = mcc + mncArray[mncIndex] + nextInt1 + nextInt2 + nextInt3 + nextInt4 + nextInt5 + nextInt6 +
                    nextInt7 + nextInt8 + nextInt9 + nextInt10;
            imsis.add(imsi);
        }
        FileWriter fileWriter = new FileWriter("C://Users//Administrator//Desktop/imsis.txt", true);
        for (String imsi : imsis) {
            fileWriter.write(imsi + "\n");
        }
        fileWriter.close();
    }

    /**
     * 生成IMSI
     *
     * @throws IOException
     */
    private static String GenricOneIMSI() throws IOException {
        String[] mncArray = {"00", "01", "02", "03", "04", "05", "06", "07", "09", "11", "20"};
        String mcc = "460";

        int mncIndex = new Random().nextInt(11);
        int nextInt1 = new Random().nextInt(10);
        int nextInt2 = new Random().nextInt(10);
        int nextInt3 = new Random().nextInt(10);
        int nextInt4 = new Random().nextInt(10);
        int nextInt5 = new Random().nextInt(10);
        int nextInt6 = new Random().nextInt(10);
        int nextInt7 = new Random().nextInt(10);
        int nextInt8 = new Random().nextInt(10);
        int nextInt9 = new Random().nextInt(10);
        int nextInt10 = new Random().nextInt(10);

        String imsi = mcc + mncArray[mncIndex] + nextInt1 + nextInt2 + nextInt3 + nextInt4 + nextInt5 + nextInt6 +
                nextInt7 + nextInt8 + nextInt9 + nextInt10;
        return imsi;

    }

    private static void genricIMEI() throws IOException {
        List<String> imeis = new ArrayList<String>();

        for (int i = 1; i <= 6798700; i++) {

            int nextInt1 = new Random().nextInt(10);
            int nextInt2 = new Random().nextInt(10);
            int nextInt3 = new Random().nextInt(10);
            int nextInt4 = new Random().nextInt(10);
            int nextInt5 = new Random().nextInt(10);
            int nextInt6 = new Random().nextInt(10);

            int seri = 111111 + i % 888888;
            String beginCode = "86" + nextInt1 + nextInt2 + nextInt3 + nextInt4 + nextInt5 + nextInt6 + seri;
            int seri2 = seri + 1;
            String endCodee = "86" + nextInt1 + nextInt2 + nextInt3 + nextInt4 + nextInt5 + nextInt6 + seri2;

//            beachIMEI(code, endCode);
            List<String> genimeis = DeviceUtil.beachIMEI(beginCode, endCodee);
            for (String imei : genimeis) {
                imeis.add(imei);
            }
        }
        FileWriter fileWriter = new FileWriter("C://Users//Administrator//Desktop/imeis.txt", true);
        for (String imei : imeis) {
            fileWriter.write(imei + "\n");
        }
        fileWriter.close();
    }

    private static String GenricOneIMEI() throws IOException {
        int nextInt1 = new Random().nextInt(10);
        int nextInt2 = new Random().nextInt(10);
        int nextInt3 = new Random().nextInt(10);
        int nextInt4 = new Random().nextInt(10);
        int nextInt5 = new Random().nextInt(10);
        int nextInt6 = new Random().nextInt(10);

        int seri = 111111 + new Random().nextInt(888888);
        String beginCode = "86" + nextInt1 + nextInt2 + nextInt3 + nextInt4 + nextInt5 + nextInt6 + seri;
        int seri2 = seri + 1;
        String endCodee = "86" + nextInt1 + nextInt2 + nextInt3 + nextInt4 + nextInt5 + nextInt6 + seri2;

        List<String> genimeis = DeviceUtil.beachIMEI(beginCode, endCodee);

        return genimeis.get(0);
    }


}
