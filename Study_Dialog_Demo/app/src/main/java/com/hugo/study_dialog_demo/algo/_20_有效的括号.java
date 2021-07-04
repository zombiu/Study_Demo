package com.hugo.study_dialog_demo.algo;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Stack;

/**
 * _20_有效的括号
 * https://leetcode-cn.com/problems/valid-parentheses/
 */
public class _20_有效的括号 {
    /**
     * 测试通过，不过性能很低
     * @param s
     * @return
     */
    public boolean isValid1(String s) {
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (stack.isEmpty()) {
                stack.push(c);
            }else {
                Character character = stack.peek();
                String newStr = character + String.valueOf(c);
                if (newStr.equals("()") || newStr.equals("[]") || newStr.equals("{}")){
                    stack.pop();
                }else {
                    stack.push(c);
                }
            }
        }
        return stack.isEmpty();
    }

    public boolean isValid2(String s) {
        String eq1 = "()";
        String eq2 = "[]";
        String eq3 = "{}";
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (stack.isEmpty()) {
                stack.push(c);
            }else {
                Character character = stack.peek();
                String newStr = character + String.valueOf(c);
                if (newStr.equals(eq1) || newStr.equals(eq2) || newStr.equals(eq3)){
                    stack.pop();
                }else {
                    stack.push(c);
                }
            }
        }
        return stack.isEmpty();
    }
}
