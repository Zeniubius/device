import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by admin on 2018/1/25.
 */
public class DeviceUtil {

    /**
     * 批量生成IMEI
     *
     * @param begin
     * @param end
     * @return
     */
    public static List<String> beachIMEI(String begin, String end) {
        List<String> imeis = new ArrayList<String>();
        try {
            long count = Long.parseLong(end) - Long.parseLong(begin);
            Long currentCode = Long.parseLong(begin);
            String code;
            for (int i = 0; i <= count; i++) {
                code = currentCode.toString();
                code = code + genIMEICode(code);
                imeis.add(code);
                currentCode += 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imeis;
    }

    /**
     * IMEI 校验码
     *
     * @param code
     * @return
     */
    private static String genIMEICode(String code) {
        int total = 0, sum1 = 0, sum2 = 0;
        int temp = 0;
        char[] chs = code.toCharArray();
        for (int i = 0; i < chs.length; i++) {
            int num = chs[i] - '0';     // ascii to num
            /*(1)将奇数位数字相加(从1开始计数)*/
            if (i % 2 == 0) {
                sum1 = sum1 + num;
            } else {
                /*(2)将偶数位数字分别乘以2,分别计算个位数和十位数之和(从1开始计数)*/
                temp = num * 2;
                if (temp < 10) {
                    sum2 = sum2 + temp;
                } else {
                    sum2 = sum2 + temp + 1 - 10;
                }
            }
        }
        total = sum1 + sum2;
        /*如果得出的数个位是0则校验位为0,否则为10减去个位数 */
        if (total % 10 == 0) {
            return "0";
        } else {
            return (10 - (total % 10)) + "";
        }
    }

    /**
     * 批量生成MEID,步长为1
     *
     * @param begin  开始字段
     * @param length 长度
     * @return
     */
    public static List<String> beachMEID(String begin, int length) {
        List<String> meids = new ArrayList<String>();

        String code;
        long beg = Long.parseLong(begin);
        for (int i = 0; i < length; i++) {
            long courentCode = beg + i;
            code = Long.toString(courentCode) + genMEIDCode(Long.toString(courentCode));
            meids.add(code);
        }
        return meids;
    }

    /**
     * 根据MEID的前14位，得到第15位的校验位
     * MEID校验码算法：
     * (1).将偶数位数字分别乘以2，分别计算个位数和十位数之和，注意是16进制数
     * (2).将奇数位数字相加，再加上上一步算得的值
     * (3).如果得出的数个位是0则校验位为0，否则为10(这里的10是16进制)减去个位数
     * 如：AF 01 23 45 0A BC DE 偶数位乘以2得到F*2=1E 1*2=02 3*2=06 5*2=0A A*2=14 C*2=18 E*2=1C,
     * 计算奇数位数字之和和偶数位个位十位之和，得到 A+(1+E)+0+2+2+6+4+A+0+(1+4)+B+(1+8)+D+(1+C)=64
     * 校验位 10-4 = C
     *
     * @param meid
     * @return
     */
    private static String genMEIDCode(String meid) {
        if (meid.length() == 14) {
            String myStr[] = {"a", "b", "c", "d", "e", "f"};
            int sum = 0;
            for (int i = 0; i < meid.length(); i++) {
                String param = meid.substring(i, i + 1);
                for (int j = 0; j < myStr.length; j++) {
                    if (param.equalsIgnoreCase(myStr[j])) {
                        param = "1" + String.valueOf(j);
                    }
                }
                if (i % 2 == 0) {
                    sum = sum + Integer.parseInt(param);
                } else {
                    sum = sum + 2 * Integer.parseInt(param) % 16;
                    sum = sum + 2 * Integer.parseInt(param) / 16;
                }
            }
            if (sum % 16 == 0) {
                return "0";
            } else {
                int result = 16 - sum % 16;
                if (result > 9) {
                    result += 65 - 10;
                    return (char) result + "";
                }
                return result + "";
            }
        } else {
            return "";
        }
    }

    /**
     * 生成ICCID
     *
     * @param length 需要生成的个数
     * @return
     * @throws IOException
     */
    public static List<String> beachICCID(int length) {
        List<String> ICCIDS = new ArrayList<String>();
        String[] IMSI = {"898600", "898602", "898601", "898609", "898603", "898606"};
        for (int i = 0; i < length; i++) {
            int SupplierId = new Random().nextInt(6);
            if (SupplierId == 0 || SupplierId == 1) {
                String ICCID = CMCC(IMSI[SupplierId]);
                ICCIDS.add(ICCID);
            } else if (SupplierId == 2 || SupplierId == 3) {
                String ICCID = UNICOME(IMSI[SupplierId]);
                ICCIDS.add(ICCID);
            } else {
                String ICCID = TELECOM(IMSI[SupplierId]);
                ICCIDS.add(ICCID);
            }
        }

        return ICCIDS;
    }

    // 电信
    private static String TELECOM(String Supplier) {
        // 编制ICCID时年号的后两位
        String[] year = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
                "17"};

        // HHH：本地网地区代码，位数不够前补零。如上海区号为021，则HHH为'021’；长沙区号为0731，则HHH为‘731’，测试卡代码为001
        String HHH = String.valueOf(1 + new Random().nextDouble()).substring(2, 5);

        // XXXXXXP: 7位流水号，建议前2位作为批次号
        String XXXXXXP = String.valueOf(1 + new Random().nextDouble()).substring(2, 9);

        return Supplier + year[new Random().nextInt(year.length)] + new Random().nextInt(10) + "0" + HHH + XXXXXXP;
    }

    // 联通
    private static String UNICOME(String Supplier) {
        // 编制ICCID时年号的后两位
        String[] year = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
                "17"};

         /* 省编号
        10内蒙古 11北京 13天津 17山东 18河北 19山西 30安徽 31上海 34江苏
        36浙江 38福建 50海南 51广东 59广西 70青海 71湖北 74湖南 75江西
        76河南 79西藏 81四川 83重庆 84陕西 85贵州 86云南 87甘肃 88宁夏
        89新疆 90吉林 91辽宁 97黑龙江*/
        String[] SS = {"10", "11", "13", "17", "18", "19", "30", "31", "34", "36", "38", "50", "51", "59", "70", "71",
                "74", "75", "76", "79", "81", "83", "84", "85", "86", "87", "88", "89", "90", "91", "97"};

        // 卡商生产的顺序编码
        double pross = (1 + new Random().nextDouble()) * Math.pow(10, 9);
        String XX = String.valueOf(pross).substring(2, 10);

        // 校验位
        String P = String.valueOf(new Random().nextInt(10));

        return Supplier + year[new Random().nextInt(year.length)] + "8" + SS[new Random().nextInt(SS.length)] +
                XX + P;
    }

    // 移动
    private static String CMCC(String Supplier) {
         /*号段，对应用户号码前3位
        0：159 1：158 2：150
        3：151 4-9：134-139 A：157
        B：188 C：152 D：147 E：187*/
        String[] M = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E"};

        // 用户号码第4位
        String[] F = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        /* 省编号
        北京01 天津02 河北03 山西04 内蒙古05 辽宁06 吉林07
        黑龙江08 上海09 江苏10 浙江11 安徽12 福建13 江西14
        山东15 河南16 湖北17 湖南18 广东19 广西20 海南21
        四川22 贵州23 云南24 西藏25 陕西26 甘肃27 青海28
        宁夏29 新疆30 重庆31*/
        String[] SS = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
                "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};

        // 编制ICCID时年号的后两位
        String[] year = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16",
                "17"};

        /* SIM卡供应商代码
        0：雅斯拓 1：GEMPLUS 2：武汉天喻 3：江西捷德 4：珠海东信和平
        5：大唐微电子通 6：航天九州通 7：北京握奇 8：东方英卡
        9：北京华虹 A ：上海柯斯
         */
        String[] G = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A"};

        // 用户识别码
        double pross = (1 + new Random().nextDouble()) * Math.pow(10, 6);
        String XX = String.valueOf(pross).substring(1, 7);

        // 校验位
        String P = String.valueOf(new Random().nextInt(10));
        return Supplier + M[new Random().nextInt(M.length)] + F[new Random().nextInt(F.length)] +
                SS[new Random().nextInt(SS.length)] + year[new Random().nextInt(year.length)] +
                G[new Random().nextInt(G.length)] + XX + P;
    }

    /**
     * 批量生成蓝牙地址
     *
     * @param length 个数
     * @return
     */
    public static List<String> beachBlueTooth(int length) {
        String[] mncArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

        List<String> blueTooths = new ArrayList<String>();
        Random rand = new Random();
        String blueTooth = "";
        for (int i = 0; i < length; i++) {
            blueTooth = mncArray[rand.nextInt(16)] + mncArray[rand.nextInt(16)] + ":" + mncArray[rand.nextInt(16)] + mncArray[rand.nextInt(16)] + ":" +
                    mncArray[rand.nextInt(16)] + mncArray[rand.nextInt(16)] + ":" + mncArray[rand.nextInt(16)] + mncArray[rand.nextInt(16)] + ":" +
                    mncArray[rand.nextInt(16)] + mncArray[rand.nextInt(16)] + ":" + mncArray[rand.nextInt(16)] + mncArray[rand.nextInt(16)];
            blueTooths.add(blueTooth);
        }

        return blueTooths;
    }

    /**
     * 批量生成AndroidID
     *
     * @param length 生成个数
     * @return
     */
    public static List<String> beachAndroidID(int length) {

        List<String> androidIDs = new ArrayList<String>();

        for (int i = 0; i < length; i++) {
            StringBuffer sb = new StringBuffer();
            for (int j = 0; j < 15; j++) {
                sb.append(Integer.toHexString(new Random().nextInt(16)));
            }
            androidIDs.add(sb.toString());
        }
        return androidIDs;
    }
}
