package com.hugo.study_dialog_demo.algo;

import java.util.HashMap;
import java.util.Stack;

/**
 * 856. 括号的分数
 * https://leetcode-cn.com/problems/score-of-parentheses/
 */
public class _856_括号的分数 {

    static HashMap<Character, Character> hashMap = new HashMap<>();

    static {
        hashMap.put('(', ')');
    }

    public int scoreOfParentheses(String s) {
        Stack<Object> stack = new Stack<>();
        for (int i = 0; i < s.length() ; i++) {
            char c = s.charAt(i);
            // '('
            if (hashMap.containsKey(c)) {
                stack.push(c);
            } else {
                // ')'
                if (stack.isEmpty()) {
                    // 如果是空，说明 只有 ) 这半边括号
                } else {
                    Object pop = stack.pop();
                    // 如果栈顶 是 '('
                    if (pop instanceof Character && hashMap.get(pop) == c) {
                        if (stack.isEmpty()) {
                            stack.push(1);
                        } else {
                            // 如果栈顶 是个数字
                            Object prevPop = stack.peek();
                            if (prevPop instanceof Integer) {
                                prevPop = stack.pop();
                                stack.push((int) prevPop + 1);
                            }else {
                                stack.push(1);
                            }
                        }
                    } else {
                        if (!stack.isEmpty()) {
                            Object prevPop = stack.pop();
                        }
                        // 这里保存的就是 分数了
                        int score = (int) pop;
                        score = score * 2;
                        // 这里还需要pop一次，下面有可能是 '(' 、分数、 或者空
                        if (!stack.isEmpty()) {
                            Object pop1 = stack.peek();
                            if (pop1 instanceof Integer) {
                                pop1 = stack.pop();
                                stack.push((int)pop1 + score);
                            }else {
                                stack.push(score);
                            }
                        }else {
                            stack.push(score);
                        }
                    }
                }
            }
        }
        Object pop = stack.pop();
        return (int) pop;
    }
}
