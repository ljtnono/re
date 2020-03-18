package cn.ljtnono.re.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * html工具类
 * @author ljt
 * @date 2019/12/19
 * @version 1.0.0
 */
public class HtmlUtil {

    /**
     * 工具类不允许实例化
     */
    private HtmlUtil(){}

    /**
     * 删除字符串中的html标签，只保留内容部分
     * @param htmlStr 带有html标签的字符串
     * @return 不含html标签的字符串
     */
    public static String delHtmlTagFromStr(String htmlStr){
        //定义script的正则表达式
        String regExScript ="<script[^>]*?>[\\s\\S]*?<\\/script>";
        //定义style的正则表达式
        String regExStyle ="<style[^>]*?>[\\s\\S]*?<\\/style>";
        //定义HTML标签的正则表达式
        String regExHtml ="<[^>]+>";
        Pattern pScript =Pattern.compile(regExScript ,Pattern.CASE_INSENSITIVE);
        Matcher mScript =pScript .matcher(htmlStr);
        //过滤script标签
        htmlStr=mScript .replaceAll("");
        Pattern pStyle =Pattern.compile(regExStyle,Pattern.CASE_INSENSITIVE);
        Matcher mStyle =pStyle .matcher(htmlStr);
        //过滤style标签
        htmlStr=mStyle .replaceAll("");
        Pattern pHtml =Pattern.compile(regExHtml ,Pattern.CASE_INSENSITIVE);
        Matcher mHtml =pHtml .matcher(htmlStr);
        //过滤html标签
        htmlStr=mHtml .replaceAll("");
        //返回文本字符串
        return htmlStr.trim();
    }


    public static void main(String[] args) {
        String str = "<div style='text-align:center;'> 整治“四风”  清弊除垢<br/><span style='font-size:14px;'> </span><span style='font-size:18px;'>公司召开党的群众路线教育实践活动动员大会</span><br/></div>";
        System.out.println(delHtmlTagFromStr(str));
    }
}
