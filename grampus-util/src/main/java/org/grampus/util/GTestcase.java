package org.grampus.util;

import org.grampus.log.GLogger;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;

public class GTestcase {
    public static final String TEST_CASE_FOLDER = "cases";
    private String caseId;

    private String inputStr;
    private String outputStr;

    private String inputType;
    private String outputType;

    private GTestcase(String caseId) {
        this.caseId = caseId;
    }

    public static GTestcase load(String caseId){
        try {
            Reader reader = Files.newBufferedReader(GFileUtil.getResourcesPath(TEST_CASE_FOLDER+File.separator+caseId));
            GTestcase result = GJsonUtil.fromJson(reader, GTestcase.class);
            reader.close();
            return result;
        } catch (IOException e) {
            GLogger.error("failure to load test case [{}], with {}",caseId,e);
            return null;
        }
    }

    public static void save(String caseId, Object input, Object output){
        ensureTestcaseFolder();
        GTestcase testcase = new GTestcase(caseId);
        if(input instanceof  String){
            testcase.setInputStr((String) input);
            testcase.setInputType(String.class.getName());
        }else {
            testcase.setInputStr(GJsonUtil.toJson(input));
            testcase.setInputType(input.getClass().getName());
        }
        if(output instanceof  String){
            testcase.setOutputStr((String) output);
            testcase.setOutputType(String.class.getName());
        }else {
            testcase.setOutputStr(GJsonUtil.toJson(output));
            testcase.setOutputType(output.getClass().getName());
        }
        try {
            Writer writer = Files.newBufferedWriter(GFileUtil.getResourcesPath(TEST_CASE_FOLDER+File.separator+caseId));
            GJsonUtil.toJson(testcase,writer);
        } catch (IOException e) {
            GLogger.error("failure to save test case [{}], with {}",caseId,e);
        }

    }

    private static void ensureTestcaseFolder() {
        if(GFileUtil.isExistedInClasspath(TEST_CASE_FOLDER)){
            GFileUtil.createFolderInClasspath(TEST_CASE_FOLDER);
        }
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public String getOutputStr() {
        return outputStr;
    }

    public void setOutputStr(String outputStr) {
        this.outputStr = outputStr;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public Object output(){
        if(GStringUtil.equals(String.class.getName(),outputType)){
            return outputStr;
        }else {
            try {
                return GJsonUtil.fromJson(this.outputStr,Class.forName(outputType));
            } catch (ClassNotFoundException e) {
                GLogger.error("Cannot found the input class");
                return null;
            }
        }
    }

    public <T> T output(Class<T> type){
        return (T) GJsonUtil.fromJson(this.outputStr,type);
    }
    public Object input(){
        if(GStringUtil.equals(String.class.getName(),inputType)){
            return inputStr;
        }else {
            try {
                return GJsonUtil.fromJson(this.inputStr,Class.forName(inputType));
            } catch (ClassNotFoundException e) {
                GLogger.error("Cannot found the input class");
                return null;
            }
        }
    }
    public <T> T input(Class<T> type){
        return (T) GJsonUtil.fromJson(this.inputStr,type);
    }

}
