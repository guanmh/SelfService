package com.gmh.utils;

import com.google.common.math.LongMath;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @project: crm
 * @author: TYX
 * @create: 2019-02-13 11:25
 * @description: 日期格式
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    /**
     * 每天多少小时
     */
    private static final double DAY_HOURS = 24.0;
    /**
     * 100 便于四舍五入最后2位小数
     */
    private static final double DECIMAL = 100.0;

    private static final DateTimeFormatter FILE_YMD = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter HM = DateTimeFormatter.ofPattern("HH:mm");
    /**
     * 计算阴历日期参照1900年到2049年
     */
    private static final int[] LUNAR_INFO = {
            0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0,
            0x055d2,
            0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0,
            0x14977,
            0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2,
            0x04970,
            0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7,
            0x0c950,
            0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950,
            0x0b557,
            0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
            0x06aa0,
            0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57,
            0x056a0,
            0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0,
            0x195a6,
            0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60,
            0x09570,
            0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5,
            0x092e0,
            0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0,
            0x0cab5,
            0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0,
            0x0a930,
            0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
            0x0d530,
            0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520,
            0x0dd45,
            0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0
    };
    /**
     * 允许输入的最大年份
     */
    private static final int MAX_YEAR = 2049;

    private static final DateTimeFormatter MD = DateTimeFormatter.ofPattern("MM-dd");
    /**
     * 允许输入的最小年份
     */
    private static final int MIN_YEAR = 1900;

    private static final DateTimeFormatter NO_SPLIT_YMD = DateTimeFormatter.ofPattern("yyyyMMdd");
    /**
     * 年月日的格式化
     */
    private static final DateTimeFormatter YMD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final DateTimeFormatter YMDHM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter YMDHMS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * 没有分割的年月日
     */
    private static final DateTimeFormatter YMD_NO_SPLIT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final DateTimeFormatter YMD_SPLIT = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private static final DateTimeFormatter YMD_SPLIT_MINI = DateTimeFormatter.ofPattern("yyyy/M/d");

    /**
     * 根据天数计算
     *
     * @param date
     * @param days 转为小时计算时间（1.5=36小时）
     * @return
     */
    public static Date addDays(Date date, double days) {
        if (Objects.isNull(date)) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, daysToHours(days));
        return c.getTime();
    }

    /**
     * 时间差
     *
     * @param start
     * @param end
     * @return
     */
    public static Duration between(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end);
    }

    /**
     * 日期差
     *
     * @param start
     * @param end
     * @return
     */
    public static double betweenDays(Date start, Date end) {
        return hoursToDays(betweenHours(start, end));
    }

    public static long betweenMillis(Date start, Date end) {
        return LongMath.checkedSubtract(end.getTime(), start.getTime());
    }

    /**
     * 时间差秒
     *
     * @param start
     * @param end
     * @return
     */
    public static long betweenSeconds(LocalDateTime start, LocalDateTime end) {
        return LongMath.divide(betweenMillis(start, end), 1000L, RoundingMode.FLOOR);
    }

    /**
     * @param source 保留年月日
     * @param target 保留时分秒
     * @return
     */
    public static Date copyHms(Date source, Date target) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(source);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);
        calendar.setTime(target);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DATE, date);
        return calendar.getTime();
    }

    /**
     * 比较2个时间是否是同一天（忽略时分秒）
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean equalsDay(Date date1, Date date2) {
        return truncatedCompareTo(date1, date2, Calendar.DATE) == 0;
    }

    /**
     * 开始时间和结束时间的每天日期
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<Date> getDays(Date startTime, Date endTime) {
        List<Date> dates =
                new ArrayList(Double.valueOf(betweenDays(startTime, endTime)).intValue() + 1);
        /** 循环开始日期小于结束日期（2月1日-2月4日） */
        dates.add(startTime);
        startTime = DateUtils.addDays(startTime, 1);
        while (truncatedCompareTo(startTime, endTime, Calendar.DATE) < 0) {
            dates.add(startTime);
            startTime = DateUtils.addDays(startTime, 1);
        }
        // 最后一天用结束时间，加上结束日期2月5日 12点
        if (getMin(endTime).compareTo(endTime) < 0) {
            dates.add(endTime);
        }
        return dates;
    }

    public static String getFileYmd() {
        return FILE_YMD.format(LocalDateTime.now());
    }

    public static String getHM(Date date) {
        return HM.format(toLocalDateTime(date));
    }

    public static Date getMD(String date) {
        return toDate(date, MD);
    }

    public static Date getMapYMD(Map<String, Object> params, String key) {
        String date = MapUtils.getString(params, key);
        if (StringUtils.isNotEmpty(date)) {
            return getYMD(date);
        }
        return null;
    }

    /**
     * 获取当天最大时间
     *
     * @return
     */
    public static LocalDate getMax() {
        return getMax(LocalDate.now());
    }

    public static <D> D getMax(D date) {
        if (date instanceof Date) {
            return (D) toDate(getMax(toLocalDateTime((Date) date)));
        } else if (date instanceof LocalDateTime) {
            return (D) LocalDateTime.of(((LocalDateTime) date).toLocalDate(), LocalTime.MAX);
        } else if (date instanceof LocalDate) {
            return (D) LocalDateTime.of((LocalDate) date, LocalTime.MAX);
        }
        return null;
    }

    public static Date getMaxDate(Date date, Date nowDate) {
        return compareTo(date, nowDate) > 0 ? date : nowDate;
    }

    /**
     * 获取当天最小时间
     *
     * @return
     */
    public static LocalDateTime getMin() {
        return getMin(LocalDate.now()).atStartOfDay();
    }

    public static <D> D getMin(D date) {
        if (date instanceof Date) {
            return (D) toDate(getMin(toLocalDateTime((Date) date)));
        } else if (date instanceof LocalDateTime) {
            return (D) LocalDateTime.of(((LocalDateTime) date).toLocalDate(), LocalTime.MIN);
        } else if (date instanceof LocalDate) {
            return (D) LocalDateTime.of((LocalDate) date, LocalTime.MIN);
        }
        return null;
    }

    public static Date getMinDate(Date date, Date nowDate) {
        return compareTo(date, nowDate) < 0 ? date : nowDate;
    }

    public static Date getNoSplitYMD(String date) {
        return toDate(date, NO_SPLIT_YMD);
    }

    public static LocalDateTime getNow() {
        return LocalDateTime.now();
    }

    public static Date getNowDate() {
        return new Date();
    }

    public static Date getYMD(String date) {
        return toDate(date, YMD);
    }

    /**
     * 年月日 时分
     *
     * @param time
     * @return
     */
    public static Date getYMDHMS(String time) {
        return toDate(time, YMDHMS);
    }

    public static LocalDate getYMDLocalDate(String date) {
        return LocalDate.parse(date, YMD);
    }

    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前系统的年月日
     *
     * @return
     */
    public static String getYmd() {
        return YMD.format(LocalDateTime.now());
    }

    public static String getYmd(Date date) {
        return date == null ? null : YMD.format(toLocalDateTime(date));
    }

    static String getYmdNoSplit() {
        return YMD_NO_SPLIT.format(LocalDateTime.now());
    }

    public static String getYmdSplit() {
        return YMD_SPLIT.format(LocalDateTime.now());
    }

    public static String getYmdSplit(Date date) {
        return YMD_SPLIT.format(toLocalDateTime(date));
    }

    public static String getYmdSplitMini(Date date) {
        return YMD_SPLIT_MINI.format(toLocalDateTime(date));
    }

    public static String getYmdhms(Date time) {
        if (time == null) {
            return null;
        }
        return YMDHMS.format(toLocalDateTime(time));
    }


    /**
     * 比较时间
     *
     * @param startDate
     * @param endDate
     * @return true：开始时间大于结束时间
     */
    public static boolean greater(Date startDate, Date endDate) {
        return truncatedCompareTo(startDate, endDate, Calendar.SECOND) > 0;
    }

    /**
     * 时间是否在开始和结束之间（包含开始，结束）
     *
     * @param now   比较的现在时间
     * @param start 开始范围
     * @param end   结束范围
     * @return
     */
    public static boolean in(Date now, Date start, Date end) {
        Assert.isTrue(start.compareTo(end) <= 0, "开始时间{start}不能大于结束时间{end}！");
        return now.compareTo(start) >= 0 && now.compareTo(end) <= 0;
    }

    public static int compareTo(Date date1, Date date2) {
        if (date1 == null) {
            return -1;
        } else if (date2 == null) {
            return 1;
        } else {
            return date1.compareTo(date2);
        }
    }


    /**
     * 去除不需要的时间段
     *
     * @param dates        原本的时间｛[0]开始时间，[1]结束时间｝
     * @param removalDates 需要去除的时间｛[0]开始时间，[1]结束时间｝
     * @return
     */
    public static List<Date[]> removalTime(List<Date[]> dates, List<Date[]> removalDates) {
        return removalTime(dates, removalDates, true);
    }

    /**
     * 阳历日期转换为阴历日期
     *
     * @param myDate 阳历日期,格式YYYYMMDD
     * @return 阴历日期
     * @throws Exception
     * @author liu 2015-1-5
     */
    public static Date solarToLunar(Date myDate) {
        int i;
        int temp = 0;
        int lunarYear;
        int lunarMonth; // 农历月份
        int lunarDay; // 农历当月第几天
        Calendar instance = null;
        try {
            Date startDate = getNoSplitYMD("19000130");

            int offset = daysBetween(startDate, myDate);

            for (i = MIN_YEAR; i <= MAX_YEAR; i++) {
                // 求当年农历年天数
                temp = getYearDays(i);
                if (offset - temp < 1) {
                    break;
                } else {
                    offset -= temp;
                }
            }
            lunarYear = i;
            // 计算该年闰哪个月
            int leapMonth = getLeapMonth(lunarYear);
            // 设定当年是否有闰月
            boolean isLeapYear = leapMonth > 0;

            for (i = 1; i <= 12; i++) {
                if (i == leapMonth + 1 && isLeapYear) {
                    temp = getLeapMonthDays(lunarYear);
                    isLeapYear = false;
                    i--;
                } else {
                    temp = getMonthDays(lunarYear, i);
                }
                offset -= temp;
                if (offset <= 0) {
                    break;
                }
            }

            offset += temp;
            lunarMonth = i;
            lunarDay = offset;
            instance = Calendar.getInstance();
            instance.set(lunarYear, lunarMonth - 1, lunarDay, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance.getTime();
    }

    /**
     * 分割时间
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param split 需要分割的时间
     * @return
     */
    public static List<Date[]> split(Date start, Date end, Date... split) {
        List<Date[]> cycleTimes = new ArrayList<>(1);
        cycleTimes.add(new Date[]{start, end});
        int size = CollectionUtils.size(split);
        if (size == 0) {
            return cycleTimes;
        }
        List<Date[]> removes = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            removes.add(new Date[]{split[0], split[0]});
        }
        return removalTime(cycleTimes, removes, false);
    }

    public static Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * 合并时间段
     *
     * @param dates 时间段日期数组集合｛[0]开始时间，[1]结束时间｝
     * @return
     */
    public static List<Date[]> unionTime(List<Date[]> dates) {
        int size = CollectionUtils.size(dates);
        if (size == 0) {
            return dates;
        }
        sortArray(dates);
        List<Date[]> listOut = new ArrayList<>(size);
        Date begin = null;
        Date end = null;
        for (int i = 0; i < size; i++) {
            Date[] tmp = dates.get(i);
            Date eachBegin = tmp[0];
            Date eachEnd = tmp[1];
            if (i == 0) {
                begin = eachBegin;
                end = eachEnd;
            } else {
                if (eachBegin.compareTo(end) <= 0) {
                    if (eachEnd.compareTo(end) > 0) {
                        end = eachEnd;
                    }
                } else {
                    Date[] str = {begin, end};
                    listOut.add(str);
                    begin = eachBegin;
                    end = eachEnd;
                }
            }
            if (i == (size - 1)) {
                Date[] str = {begin, end};
                listOut.add(str);
            }
        }
        return listOut;
    }

    private static List<Date[]> removalTime(
            List<Date[]> dates, List<Date[]> removalDates, boolean isUnionTime) {
        int dateSize = CollectionUtils.size(dates);
        int removalSize = CollectionUtils.size(removalDates);
        List<Date[]> result = new ArrayList<>(dateSize + removalSize);
        if (dateSize > 0) {
            DateUtils.sortArray(dates);
            if (removalSize > 0) {
                DateUtils.sortArray(removalDates);
                for (int i = 0; i < dateSize; i++) {
                    // 保留时间
                    Date[] reserved = dates.get(i);
                    Date reservedBegin = reserved[0];
                    Date reservedEnd = reserved[1];
                    for (int j = 0; j < removalSize; j++) {
                        // 删除时间
                        Date[] removal = removalDates.get(j);
                        Date removalBegin = removal[0];
                        Date removalEnd = removal[1];
                        if (removalEnd.compareTo(reservedBegin) > 0
                                && removalBegin.compareTo(reservedEnd) < 0) {
                            if (removalBegin.compareTo(reservedBegin) > 0) {
                                result.add(new Date[]{reservedBegin, removalBegin});
                            }
                            reservedBegin = removalEnd;
                            if (removalEnd.compareTo(reservedEnd) < 0
                                    && reservedBegin.compareTo(reservedEnd) < 0) {
                                reservedBegin = removalEnd;
                            }
                        }
                    }
                    if (reservedBegin.compareTo(reservedEnd) < 0) {
                        result.add(new Date[]{reservedBegin, reservedEnd});
                    }
                }
            }
        }
        return isUnionTime ? DateUtils.unionTime(result) : result;
    }

    private static int betweenHours(Date start, Date end) {
        Duration between = Duration.between(date2LocalDateTime(start), date2LocalDateTime(end));
        return Long.valueOf(between.toHours()).intValue();
    }

    /**
     * 排序
     *
     * @param dates
     * @return
     */
    private static void sortArray(List<Date[]> dates) {
        Collections.sort(dates, Comparator.comparing(o -> o[0]));
    }

    /**
     * 时间差毫秒
     *
     * @param start
     * @param end
     * @return
     */
    private static long betweenMillis(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMillis();
    }

    private static LocalDateTime date2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * 计算两个阳历日期相差的天数。
     *
     * @param startDate 开始时间
     * @param endDate   截至时间
     * @return (int)天数
     * @author liu 2017-3-2
     */
    private static int daysBetween(Date startDate, Date endDate) {
        int days = 0;
        // 将转换的两个时间对象转换成Calendar对象
        Calendar can1 = Calendar.getInstance();
        can1.setTime(startDate);
        Calendar can2 = Calendar.getInstance();
        can2.setTime(endDate);
        // 拿出两个年份
        int year1 = can1.get(Calendar.YEAR);
        int year2 = can2.get(Calendar.YEAR);
        // 天数

        Calendar can;
        // 如果can1 < can2
        // 减去小的时间在这一年已经过了的天数
        // 加上大的时间已过的天数
        if (can1.before(can2)) {
            days -= can1.get(Calendar.DAY_OF_YEAR);
            days += can2.get(Calendar.DAY_OF_YEAR);
            can = can1;
        } else {
            days -= can2.get(Calendar.DAY_OF_YEAR);
            days += can1.get(Calendar.DAY_OF_YEAR);
            can = can2;
        }
        for (int i = 0; i < Math.abs(year2 - year1); i++) {
            // 获取小的时间当前年的总天数
            days += can.getActualMaximum(Calendar.DAY_OF_YEAR);
            // 再计算下一年。
            can.add(Calendar.YEAR, 1);
        }
        return days;
    }

    /**
     * 天数转小时
     *
     * @param days
     * @return
     */
    private static int daysToHours(double days) {
        return Double.valueOf(Math.rint(days * DAY_HOURS)).intValue();
    }

    /**
     * 计算阴历 {@code year}年闰哪个月 1-12 , 没闰传回 0
     *
     * @param year 阴历年
     * @return (int)月份
     * @author liu 2015-1-5
     */
    private static int getLeapMonth(int year) {
        return (LUNAR_INFO[year - 1900] & 0xf);
    }

    /**
     * 计算阴历{@code year}年闰月多少天
     *
     * @param year 阴历年
     * @return (int)天数
     * @author liu 2015-1-5
     */
    private static int getLeapMonthDays(int year) {
        if (getLeapMonth(year) != 0) {
            if ((LUNAR_INFO[year - 1900] & 0xf0000) == 0) {
                return 29;
            } else {
                return 30;
            }
        } else {
            return 0;
        }
    }

    /**
     * 计算阴历{@code lunarYeay}年{@code month}月的天数
     *
     * @param lunarYeay 阴历年
     * @param month     阴历月
     * @return (int)该月天数
     * @throws Exception
     * @author liu 2015-1-5
     */
    private static int getMonthDays(int lunarYeay, int month) throws Exception {
        if ((month > 31) || (month < 0)) {
            throw (new Exception("月份有错！"));
        }
        // 0X0FFFF[0000 {1111 1111 1111} 1111]中间12位代表12个月，1为大月，0为小月
        int bit = 1 << (16 - month);
        if (((LUNAR_INFO[lunarYeay - 1900] & 0x0FFFF) & bit) == 0) {
            return 29;
        } else {
            return 30;
        }
    }

    /**
     * 计算阴历{@code year}年的总天数
     *
     * @param year 阴历年
     * @return (int)总天数
     * @author liu 2015-1-5
     */
    private static int getYearDays(int year) {
        int sum = 29 * 12;
        for (int i = 0x8000; i >= 0x8; i >>= 1) {
            if ((LUNAR_INFO[year - 1900] & 0xfff0 & i) != 0) {
                sum++;
            }
        }
        return sum + getLeapMonthDays(year);
    }

    private static ZonedDateTime getZonedDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        return instant.atZone(zoneId);
    }

    /**
     * 小时转天数
     *
     * @param hours
     * @return
     */
    private static double hoursToDays(int hours) {
        return hours == 0 ? 0.0 : Math.rint(hours / DAY_HOURS * DECIMAL) / DECIMAL;
    }

    /**
     * 去除分秒
     *
     * @return
     */
    private static Date removeMinutesAndSeconds(Date date) {
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    private static Date toDate(LocalDateTime dateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = dateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    private static Date toDate(String time, DateTimeFormatter formatter) {
        // 获取时间地区ID
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime;
        if (StringUtils.isNotEmpty(time)) {
            if (formatter == YMDHMS) {
                LocalDateTime localDate = LocalDateTime.parse(time, formatter);
                // 转换为当地时间
                zonedDateTime = localDate.atZone(zoneId);
            } else {
                LocalDate localDate = LocalDate.parse(time, formatter);
                // 转换为当地时间
                zonedDateTime = localDate.atStartOfDay(zoneId);
            }
            // 转为Date类型
            return Date.from(zonedDateTime.toInstant());
        } else {
            return null;
        }
    }

    private static LocalDate toLocalDate(Date date) {
        return getZonedDateTime(date).toLocalDate();
    }

    private static LocalDateTime toLocalDateTime(Date date) {
        return getZonedDateTime(date).toLocalDateTime();
    }
}
