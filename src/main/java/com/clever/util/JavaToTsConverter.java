package com.clever.util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaToTsConverter {

    /**
     * @param inputPath  输入目录或文件路径
     * @param outputPath 输出目录
     */
    public static void convert(String inputPath, String outputPath) {
        File inputFile = new File(getBasePathOrCreate(inputPath));
        if (inputFile.isDirectory()) {
            convertDirectory(inputFile, getBasePathOrCreate(outputPath));
        } else if (inputFile.isFile()) {
            convertFile(inputFile, getBasePathOrCreate(outputPath));
        } else {
            System.out.println("Invalid input path.");
        }
    }

    protected static String getBasePathOrCreate(String basePath) {
        // 获取项目根路径
        String projectRoot = System.getProperty("user.dir");
        String pathStr = "";
        try {
            // 获取文件路径
            Path path = Paths.get(projectRoot, basePath);
            // 如果路径不存在，则创建路径
            if (!Files.exists(path)) {

                Files.createDirectories(path);

            }
            pathStr = path.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pathStr;
    }

    private static void convertDirectory(File inputDirectory, String outputPath) {
        File[] files = inputDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".java")) {
                    convertFile(file, outputPath);
                } else if (file.isDirectory()) {
                    convertDirectory(file, Paths.get(outputPath, file.getName()).toString());
                }
            }
        }
    }

    private static void convertFile(File inputFile, String outputPath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String className = null;
            StringBuilder tsInterface = new StringBuilder();

            String line;
            Boolean isComment = false;
            StringBuilder comment = new StringBuilder();
            Boolean isSkip = false;
            while ((line = reader.readLine()) != null) {
                if (isSkip) {
                    isComment = false;
                    comment = new StringBuilder();
                    isSkip = false;
                } else if (line.matches("\\s*public\\s+class\\s+([\\w_$]+).*")) {
                    // Match class declaration and extract class name
                    Matcher matcher = Pattern.compile("\\s*public\\s+class\\s+([\\w_$]+).*").matcher(line);
                    if (matcher.matches()) {
                        className = matcher.group(1);
                        tsInterface.append(comment);
                        comment = new StringBuilder();
                        tsInterface.append("export default interface ").append(className).append(" {\n");
                    }
                } else if (!line.contains("private static") && !line.contains("final static") && line.matches("\\s*private\\s+([\\w_$<>]+)\\s+([\\w_$]+).*")) {
                    // Match private field declaration and extract field type and name
                    Matcher matcher = Pattern.compile("\\s*private\\s+([\\w_$<>]+)\\s+([\\w_$]+).*").matcher(line);
                    if (matcher.matches()) {
                        tsInterface.append(comment);
                        comment = new StringBuilder();
                        String fieldType = matcher.group(1);
                        String fieldName = matcher.group(2);
                        tsInterface.append("\t").append(fieldName).append(": ").append(getTsType(fieldType)).append(";\n");
                    }
                } else if (line.matches("\\s*\\/\\/\\s*(.*)")) {
                    // Match single-line comment and add it as a comment in TypeScript
                    Matcher matcher = Pattern.compile("\\s*\\/\\/\\s*(.*)").matcher(line);
                    if (matcher.matches()) {
                        tsInterface.append("\t// ").append(matcher.group(1)).append("\n");
                    }
                } else if (line.trim().startsWith("/*")) {
                    comment = new StringBuilder();
                    // Match multi-line comment and add it as a comment in TypeScript
                    comment.append(line).append("\n");
                    isComment = true;
                } else if (line.trim().endsWith("*/")) {
                    comment.append(line).append("\n");
                    isComment = false;
                } else if (isComment) {
                    comment.append(line).append("\n");
                } else if (line.trim().equals("@Resource") || line.trim().equals("@Autowired")) {
                    isSkip = true;
                }
            }

            tsInterface.append("}");

            // Create output directory if it doesn't exist
            File outputDirectory = new File(outputPath);
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs();
            }

            // Write TypeScript interface to file
            String outputFileName = className + ".d.ts";
            File outputFile = new File(outputPath, outputFileName);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(tsInterface.toString());
                System.out.println("Conversion successful. Output file: " + outputFile.getAbsolutePath());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getTsType(String type) {
        // 判断字符串
        if (type.endsWith("String") || type.endsWith("string") || type.endsWith("Date") || type.endsWith("date") || type.endsWith("Timestamp") || type.endsWith("timestamp")) {
            return "string";
        }
        // 判断数值型
        if (type.equalsIgnoreCase("Integer") || type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Long") || type.equalsIgnoreCase("long") || type.equalsIgnoreCase("Double") || type.equalsIgnoreCase("double") || type.equalsIgnoreCase("Float") || type.equalsIgnoreCase("float") || type.equalsIgnoreCase("BigDecimal") || type.equalsIgnoreCase("BigInteger")) {
            return "number";
        }
        // 判断布尔型
        if (type.equalsIgnoreCase("Boolean") || type.equalsIgnoreCase("boolean")) {
            return "boolean";
        }
        // 判断数组
        if (type.startsWith("List")) {
            // 使用正则解析List<xx>里的xx
            Pattern pattern = Pattern.compile("List<(.*)>");
            Matcher matcher = pattern.matcher(type);
            if (matcher.find()) {
                return getTsType(matcher.group(1)) + "[]";
            }
        }
        // 判断map对象
        if (type.startsWith("Map")) {
            // 使用正则解析Map<xx,bb>转成 Record<xx, bb>
            Pattern pattern = Pattern.compile("Map<(.*?),(.*?)>");
            Matcher matcher = pattern.matcher(type);
            if (matcher.find()) {
                return "Record<" + getTsType(matcher.group(1)) + "," + getTsType(matcher.group(2)) + ">";
            }
        }


        return type;
    }
}
