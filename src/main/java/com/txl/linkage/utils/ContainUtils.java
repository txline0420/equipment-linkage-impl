package com.txl.linkage.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by TangXiangLin on 2020-05-28
 */
public class ContainUtils {

    /**
     * 时间区间与另一个区间是否有重叠
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param includeStartDate 对比的开始日期
     * @param includeEndDate 对比的结束日期
     * @return true 两个区间有交叉
     */
    public static boolean contain2StartEndDate(Date startDate,Date endDate,
                                               Date includeStartDate,Date includeEndDate){
        return ((startDate.getTime() >= includeStartDate.getTime())
                && startDate.getTime() < includeEndDate.getTime())
                || ((startDate.getTime() > includeStartDate.getTime())
                && startDate.getTime() <= includeEndDate.getTime())
                || ((includeStartDate.getTime() >= startDate.getTime())
                && includeStartDate.getTime() < endDate.getTime())
                || ((includeStartDate.getTime() > startDate.getTime())
                && includeStartDate.getTime() <= endDate.getTime())
                || ((includeStartDate.getTime() == startDate.getTime())
                && includeStartDate.getTime() == endDate.getTime());
    }

    /**
     * 两个字符串之间是否存在相同字符串
     * @param str1 CZ
     * @param include CZ/MU/MF/G5/3U/NS/OQ
     * @return true包含
     */
    public static boolean contain2StringInclude2(String str1,String include){
        String strA = StringUtils.upperCase(StringUtils.trimToEmpty(str1), Locale.ENGLISH);
        String strB = StringUtils.upperCase(StringUtils.trimToEmpty(include), Locale.ENGLISH);
        String[] bArray = org.apache.commons.lang3.StringUtils.split(strB, "/");
        List<String> bList = Arrays.asList(bArray);
        List<String> stringList = bList.stream()
                .filter(x -> x.equals(strA))
                .collect(Collectors.toList());
        return ObjectUtils.isNotEmpty(stringList);
    }


    /**
     * 两个字符串之间是否存在相同字符串
     * @param str1 CZ258
     * @param include CZ3102,CZ257,CZ258
     * @return true包含
     */
    public static boolean contain2StringInclude(String str1,String include){
        String strA = StringUtils.upperCase(StringUtils.trimToEmpty(str1), Locale.ENGLISH);
        String strB = StringUtils.upperCase(StringUtils.trimToEmpty(include), Locale.ENGLISH);
        String[] bArray = org.apache.commons.lang3.StringUtils.split(strB, ",，;；\\");
        List<String> bList = Arrays.asList(bArray);
        List<String> stringList = bList.stream()
                .filter(x -> x.equals(strA))
                .collect(Collectors.toList());
        return ObjectUtils.isNotEmpty(stringList);
    }

    /**
     * 两个字符串之间是否存在相同字符串
     * @param str1 AAA,BBB,CCC
     * @param str2 AAA,BBB,CCC
     * @return true 包不含
     */
    public static boolean contain2StringMore(String str1,String str2){
        String strA = StringUtils.upperCase(StringUtils.trimToEmpty(str1), Locale.ENGLISH);
        String strB = StringUtils.upperCase(StringUtils.trimToEmpty(str2), Locale.ENGLISH);
        String[] aArray = StringUtils.split(strA, ",，;；\\");
        String[] bArray = StringUtils.split(strB, ",，;；\\");
        List<String> aList = Arrays.asList(aArray);
        List<String> bList = Arrays.asList(bArray);
        List<String> IntersectionList = aList.stream()
                .filter(bList::contains)
                .collect(Collectors.toList());
        return ObjectUtils.isEmpty(IntersectionList);
    }

    /**
     * 两个字符串是否相等
     * @param str1 字符串 null
     * @param str2 字符串 null
     * @return true 相等
     */
    public static boolean contain2StringBool(String str1,String str2){
        if(StringUtils.isEmpty(str1)){
            return StringUtils.isEmpty(str2);
        }else{
            if(StringUtils.isEmpty(str2)){
                return false;
            }else{
                return str1.toUpperCase().equals(str2.toUpperCase());
            }
        }
    }

    /**
     * 两个字符串之间是否存在相同字符串
     * @param str1 1234567
     * @param str2 1234567
     * @return true 不包含
     */
    public static boolean contain2String(String str1,String str2){
        List<String> x1Dow = Stream.iterate(0, n -> ++n)
                .limit(str1.length())
                .map(n -> "" + str1.charAt(n))
                .collect(Collectors.toList());
        List<String> x2Dow = Stream.iterate(0, n -> ++n)
                .limit(str2.length())
                .map(n -> "" + str2.charAt(n))
                .collect(Collectors.toList());
        List<String> intersectionList = x1Dow.stream()
                .filter(x2Dow::contains)
                .collect(Collectors.toList());
        return ObjectUtils.isEmpty(intersectionList);
    }

}

