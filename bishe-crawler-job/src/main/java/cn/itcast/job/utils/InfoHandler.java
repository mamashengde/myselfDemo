package cn.itcast.job.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfoHandler {


    /**
     * 处理薪资信息
     * @param salary
     * @return
     */
    public String[] parseSalary(String salary) {
        String[] result = new String[]{"未知", "未知", "未知"}; // 默认结果

        if (salary == null || salary.trim().isEmpty()) {
            return result; // 处理空输入
        }

        if(salary == "面议"){
            result[0] = "面议";
            result[1] = "面议";
            result[2] = "面议";
            return result;
        }

        salary = salary.replaceAll("\\s+", ""); // 去除所有空格

        // 匹配更灵活的薪资范围和单位
        String regex = "(\\d+(\\.\\d+)?)([kK千元万])(-|至)(\\d+(\\.\\d+)?)([kK千元万])(\\·(\\d+)薪)?(/天)?";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(salary);

        if (matcher.matches()) {
            double min = Double.parseDouble(matcher.group(1));
            String minUnit = matcher.group(3);
            double max = Double.parseDouble(matcher.group(5));
            String maxUnit = matcher.group(7);
            String monthsStr = matcher.group(9);
            int months = monthsStr == null ? 12 : Integer.parseInt(monthsStr);
            boolean isDaily = matcher.group(10) != null;

            // 单位转换
            min *= getUnitMultiplier(minUnit);
            max *= getUnitMultiplier(maxUnit);
            if (isDaily) {
                min *= 21.75; // 按每月工作21.75天计算
                max *= 21.75;
            }

            // 计算月薪范围
            result[0] = String.valueOf((int) min);
            result[1] = String.valueOf((int) max);
            result[2] = String.valueOf(months);
        }

        return result;
    }

    /**
     * 获取单位转换倍数
     * 单位包括：千、万、元
     * @param unit
     * @return
     */
    private double getUnitMultiplier(String unit) {
        switch (unit) {
            case "k":
            case "K":
            case "千":
                return 1000;
            case "万":
            case "w":
            case "W":
                return 10000;
            case "元":
            default:
                return 1;
        }
    }

    /**
     * 解析工作经验
     * @param experienceString
     * @return
     */
    public int parseExperience(String experienceString) {
        if (experienceString == null || experienceString.isEmpty()) {
            return 7; // 处理空输入
        }

        switch (experienceString) {
            case "经验不限":
                return 0;
            case "无经验":
                return 1;
            case "1年以下":
                return 2;
            case "1-3年":
                return 3;
            case "3-5年":
                return 4;
            case "5-10年":
                return 5;
            case "10年以上":
                return 6;
            default:
                return 7;
        }
    }

    /**
     * 解析学历要求
     * @param requiredDegree
     * @return
     */
    public Integer parseDegree(String requiredDegree) {
        if (requiredDegree == null || requiredDegree.isEmpty()) {
            return 0; // 默认不限
        }

        switch (requiredDegree) {
            case "初中及以下":
                return 1;
            case "高中":
                return 2;
            case "中专":
            case "中技":
            case "中专/中技":
                return 3;
            case "大专":
                return 4;
            case "本科":
                return 5;
            case "硕士":
                return 6;
            case "MBA":
            case "EMBA":
            case "MBA/EMBA":
                return 7;
            case "博士":
                return 8;
            default:
                return 0; // 默认不限
        }
    }

    /**
     * 解析职位类型
     * @param jobTypeString
     * @return
     */
    public Integer parseJobType(String jobTypeString) {
        if (jobTypeString == null || jobTypeString.isEmpty()) {
            return 0; // 默认不限
        }

        switch (jobTypeString) {
            case "全职":
                return 1;
            case "兼职":
            case "临时":
            case "兼职/临时":
                return 2;
            case "实习":
                return 3;
            case "校园":
                return 4;
            default:
                return 0; // 默认不限
        }
    }

    /**
     * 解析招聘人数
     * @param requiredNumberString
     * @return
     */
    public Integer parseRequiredNumber(String requiredNumberString) {
        if (requiredNumberString == null || requiredNumberString.isEmpty()) {
            return null; // 无法解析
        }

        // 使用正则表达式匹配数字
        String regex = "招(\\d+)人";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(requiredNumberString);

        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return null; // 无法解析
        }
    }


    /**
     * 解析公司规模
     * @param companySizeString
     * @return
     */
    public Integer parseCompanySize(String companySizeString) {
        if (companySizeString == null || companySizeString.isEmpty()) {
            return 7; // 无法解析
        }

        companySizeString = companySizeString.split(" ")[0];

        // 使用正则表达式匹配公司规模范围
        String regex = "(\\d+)-(\\d+)人";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(companySizeString);

        if (matcher.matches()) {
            int minSize = Integer.parseInt(matcher.group(1));
            int maxSize = Integer.parseInt(matcher.group(2));

            if (minSize <= 20) {
                return 0;
            } else if (minSize <= 99) {
                return 1;
            } else if (minSize <= 299) {
                return 2;
            } else if (minSize <= 499) {
                return 3;
            } else if (minSize <= 999) {
                return 4;
            } else if (minSize <= 9999) {
                return 5;
            } else {
                return 6;
            }
        } else {
            return 7; // 无法解析
        }
    }


    /**
     * 解析职位信息字符串
     * @param jobInfoString
     * @return
     */
    public String[] parseJobInfoString(String jobInfoString) {
        String[] result = new String[]{"未知", "未知", "未知", "未知", "未知"}; // 默认结果

        if (jobInfoString == null || jobInfoString.isEmpty()) {
            return result; // 处理空输入
        }

        String[] fields = jobInfoString.split("\\s+"); // 使用空格分割字段

        // 提取地址
        if (fields.length > 0) {
            result[0] = fields[0];
        }

        // 提取经验、学历、工作类型、招聘人数
        for (int i = 1; i < fields.length; i++) {
            String field = fields[i];
            switch (field) {
                case "经验不限":
                case "无经验":
                case "1年以下":
                case "1-3年":
                case "3-5年":
                case "5-10年":
                case "10年以上":
                    result[1] = field;
                    break;
                case "学历不限":
                case "初中及以下":
                case "高中":
                case "中专/中技":
                case "大专":
                case "本科":
                case "硕士":
                case "MBA/EMBA":
                case "博士":
                    result[2] = field;
                    break;
                case "全职":
                case "兼职/临时":
                case "实习":
                case "校园":
                    result[3] = field;
                    break;
                default:
                    if (field.startsWith("招") && field.endsWith("人")) {
                        result[4] = field;
                    }
                    break;
            }
        }

        return result;
    }

    /**
     * 提取 URL 前面的部分
     * @param url
     * @param target
     * @return
     */
    public String extractUrlBefore(String url, String target) {
        if (url == null || url.isEmpty() || target == null || target.isEmpty()) {
            return ""; // 处理空输入
        }

        int index = url.indexOf(target);
        if (index != -1) {
            return url.substring(0, index); // 截取 target 之前的内容
        } else {
            return url; // target 不存在，返回原 URL
        }
    }


    /**
     * 解析更新日期
     * @param updateDateString
     * @return
     */
    public String parseUpdateDate(String updateDateString) {
        if (updateDateString == null || updateDateString.isEmpty()) {
            return null; // 无法解析
        }

        if (updateDateString.contains("今天")) {
            // 获取今天的日期，格式为 "月日"
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M月d日");
            return today.format(formatter);
        } else {
            // 提取日期部分
            String regex = "更新于 (.*)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(updateDateString);
            if (matcher.matches()) {
                return matcher.group(1);
            } else {
                return null; // 无法解析
            }
        }
    }

    /**
     * 公司url解析
     * @param companyUrl
     * @return
     */
    public String parseCompanyUrl(String companyUrl){
        if(companyUrl == null || companyUrl.isEmpty() || companyUrl.length() > 100){
            return "未知";
        }
        String[] fields = companyUrl.split("type");
        if(fields.length > 0){
            return fields[0];
        }
        return "";
    }

}
