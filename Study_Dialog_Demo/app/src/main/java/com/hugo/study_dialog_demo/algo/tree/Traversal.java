package com.hugo.study_dialog_demo.algo.tree;

import com.blankj.utilcode.util.LogUtils;

import java.util.Stack;

class Traversal {

    public void preorder(TreeNode root) {
        if (root == null) {
            return;
        }

        Stack<TreeNode> stack = new Stack<>();
        TreeNode node = root;
        while (true) {
            if (node != null) {
                stack.push(node);
                node = node.getLeft();
            } else {
                if (!stack.isEmpty()) {
                    TreeNode pop = stack.pop();
                    LogUtils.e("-->>" + pop);
                    node = pop.getRight();
                } else {
                    break;
                }
            }
        }
    }

    public void inorder(TreeNode root) {
        if (root == null) {
            return;
        }

        Stack<TreeNode> stack = new Stack<>();
        TreeNode node = root;
        while (true) {
            if (node != null) {
                stack.push(node);
                node = node.getLeft();
            } else {
                if (!stack.isEmpty()) {
                    TreeNode pop = stack.pop();
                    LogUtils.e("-->>" + pop);
                    node = pop.getRight();
                } else {
                    break;
                }
            }
        }
    }

    public void postOrder(TreeNode root) {
        if (root == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode peek = stack.peek();

        }
    }
}
