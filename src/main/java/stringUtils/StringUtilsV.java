package stringUtils;

import org.apache.commons.lang.StringUtils;

/**
 * *********************************************************************
 * <br/>
 *
 * @author rananhang <br/>
 * @Title StringUtilsV

 * @copyright Copyright: 2015-2020
 * @date 2018/10/18 17:05
 * @vision V1.0.1
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user   :{修改人}
 * *********************************************************************
 */
public class StringUtilsV {

    /*判断叠拼*/
    public static boolean diePin(String str) {
//        String dieStr = "";
        /*奇数位*/
        if ((str.length() & 1) == 1) {
            return false;
        }
        boolean isDiePin = false;
        for (int i = 1; i < str.length() / 2 + 1; ++i) {
            String dieStr = str.substring(0, i);
//            System.out.println("当前需要判断的字符串：" + str + "需求截取的字符串：" + dieStr);

            String str0 = str.replaceAll(dieStr, "");
//            System.out.println("是否满足叠拼：" + StringUtils.isBlank(str0));
            if (StringUtils.isBlank(str0)) {
                isDiePin = true;
                break;
            }
//            System.out.println(str0);
        }

        return isDiePin;
    }


}
