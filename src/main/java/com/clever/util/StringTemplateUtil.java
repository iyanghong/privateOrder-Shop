package com.clever.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串变量模板渲染
 *
 * @Author xixi
 * @Date 2023-12-22 17:33
 **/
public class StringTemplateUtil {
    public static String render(String template, Map<String, String> variables) {
        String renderedTemplate = replaceVariables(template, variables);
        return evaluateConditions(renderedTemplate, variables);
    }

    private static String replaceVariables(String template, Map<String, String> variables) {
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)}");
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String variableName = matcher.group(1);
            String variableValue = variables.get(variableName);

            if (variableValue != null) {
                matcher.appendReplacement(sb, variableValue);
            } else {
                throw new IllegalArgumentException("Variable " + variableName + " not found.");
            }
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    private static String evaluateConditions(String template, Map<String, String> variables) {
        Pattern pattern = Pattern.compile("\\$\\{if\\(([^}]+)}([^\\{]*)\\{/if}");
        Matcher matcher = pattern.matcher(template);
        StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String condition = matcher.group(1);
            String content = matcher.group(2);

            boolean isConditionTrue = evaluateCondition(condition, variables);

            if (isConditionTrue) {
                matcher.appendReplacement(sb, content);
            } else {
                matcher.appendReplacement(sb, "");
            }
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    private static boolean evaluateCondition(String condition, Map<String, String> variables) {
        // 在这里实现您的条件评估逻辑
        // 您可以使用variables中的值来评估条件并返回布尔结果
        // 这里只是一个示例，处理了"age>20"、"age<=30"和"name=John"的条件
        String[] parts = condition.split("(?<=[=<>])|(?=[=<>])");
        if (parts.length == 3) {
            String leftOperand = parts[0].trim();
            String operator = parts[1].trim();
            String rightOperand = parts[2].trim();

            if (variables.containsKey(leftOperand)) {
                String leftValue = variables.get(leftOperand);

                if (leftValue != null && rightOperand != null) {
                    int comparisonResult = leftValue.compareTo(rightOperand);

                    switch (operator) {
                        case "=":
                            return comparisonResult == 0;
                        case ">":
                            return comparisonResult > 0;
                        case "<":
                            return comparisonResult < 0;
                        case ">=":
                            return comparisonResult >= 0;
                        case "<=":
                            return comparisonResult <= 0;
                        case "contains":
                            return leftValue.contains(rightOperand);
                        default:
                            throw new IllegalArgumentException("Invalid operator: " + operator);
                    }
                }
            }
        }

        return false;
    }
}
