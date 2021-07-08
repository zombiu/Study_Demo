package com.hugo.study_dialog_demo.algo.stack

import java.util.*

/**
 * 150. 逆波兰表达式求值
 * https://leetcode-cn.com/problems/evaluate-reverse-polish-notation/
 * 解题思路：操作符总是在表达式的右边，例如 34+ ，并且一个操作符，对应两个数字， 或者对应 一个数字 加一个操作符 4++ 或者 3-+
 * 使用a、b两个栈，可以先把最右边的操作符先添加到b栈中，然后进行循环，每次从a栈取出两个元素，从b栈取出一个元素，如果遇到操作符就添加到b栈中，遇到两个数字就进行运算操作
 */
object _150_逆波兰表达式求值 {
    fun test() {
        var solution = Solution()
        var arr = arrayOf("18")
        solution.evalRPN(arr)
    }
}

/**
 * 测试通过
 */
class Solution {

    var stackA = Stack<String>()
    var stackB = Stack<String>()

    var plus = "+"
    var minus = "-"
    var multiply = "*"
    var divide = "/"

    // 遇到一个操作符，需要取出操作符之前的两个结果 进行运算
    fun evalRPN(tokens: Array<String>): Int {
        tokens.forEach {
            stackA.push(it)
        }

        var pop = stackA.peek()
        if (isOp(pop)) {
            stackB.push(stackA.pop())
        }
        while (!stackB.isEmpty()) {
            var pop1 = stackB.peek()
            if (isOp(pop1)) {
                var pop2 = stackA.pop()
                var pop3 = stackA.pop()
                if (isOp(pop3)) {
                    stackB.push(pop2)
                    stackB.push(pop3)
                } else if (isOp(pop2)) {
                    stackB.push(pop2)
                    stackA.push(pop3)
                } else {
                    stackB.pop()
                    if (plus == pop1) {
                        var i = pop2.toInt() + pop3.toInt()
                        stackA.push(i.toString())
                    } else if (minus == pop1) {
                        var i = pop3.toInt() - pop2.toInt()
                        stackA.push(i.toString())
                    } else if (multiply == pop1) {
                        var i = pop2.toInt() * pop3.toInt()
                        stackA.push(i.toString())
                    } else if (divide == pop1) {
                        var i = pop3.toInt() / pop2.toInt()
                        stackA.push(i.toString())
                    }
                    if (!stackB.isEmpty()) {
                        var pop4 = stackB.peek()
                        if (!isOp(pop4)) {
                            stackA.push(stackB.pop())
                        }
                    }
                }
            } else {
                stackA.push(pop1)
            }
        }
        return stackA.pop().toInt()
    }

    private fun isOp(string: String): Boolean {
        return plus == string || minus == string || multiply == string || divide == string
    }
}