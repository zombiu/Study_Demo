package com.hugo.study_dialog_demo.algo.tree;

/**
 * 红黑树
 */
public class RBTree {

    // 红黑树插入后平衡操作：
    // 特别注意：这里一定是在叶子结点中进行插入操作，12种情况
    /**
     * 1.有四种情况，父节点是黑色的，如果插入的父节点是黑色，因为插入的节点是红色，那么不用做任何处理，这棵树本来就是满足红黑树的五种性质，是平衡的。
     * 2.其他8种情况，父节点是红色：
     * 1>有四种情况，如果叔叔节点是黑色，将父节点染黑，祖父节点染红，然后进行旋转
     * a.LL情况，如果插入节点是父节点的左子树，父节点是祖父节点的左子树，那么对祖父节点进行右旋。
     *
     * b。LR情况，如果插入节点是父节点的右子树，父节点是祖父节点的左子树，那么先对父节点进行左旋，然后按照1>进行染色，再对祖父节点进行右旋
     *
     * c.RR情况，如果插入节点是父节点的右子树，父节点是祖父节点的右子树，那么对祖父节点进行左旋。
     *
     * d。RL情况，如果插入节点是父节点的左子树，父节点是祖父节点的右子树，那么先对父节点进行右旋，然后按照1>进行染色，再对祖父节点进行右旋
     *
     * 2>剩余四种情况，如果叔叔节点是红色(需要进行上溢)：
     * 将父节点、叔叔节点染黑，祖父节点染红，然后将祖父节点当做新插入的节点，进行恢复平衡的递归调用。
     */

    // 红黑树 删除后平衡操作
    // 这里需要注意:所有二叉搜索树的删除，都可以当做最终删除的是四阶b树的叶子节点，删除的叶子节点有4种情况
    /**
     * 1.第一种情况，删除的是红色节点，这种情况不会破坏红黑树的性质，不用处理。
     * 2.其他三种情况，删除的是黑色节点：
     *  // 来到这里，说明删除节点只有一个子节点，判断用来替换被删除节点的节点是否是红色节点
     * 1>前两种情况，用来替换被删除节点的节点是否是红色节点，那么使用红色子节点覆盖被删除节点，将该节点涂黑。
     * 2>最后一种情况，来到这里说明删除的节点是黑色节点，并且没有红色子节点：
     * 从4阶b树角度来看，删除该黑色节点后会发生下溢
     * a.删除结点的兄弟结点是黑色节点，并且有红色子节点：
     *  从b树角度看，表示此时可以从兄弟节点中借一个节点。
     *  代码层面，将兄弟节点染为父节点的颜色，然后将父、侄子节点染黑，再进行单旋或者双璇操作。
     * b.删除结点的兄弟结点是黑色节点，没有红色子节点：将父节点染黑，兄弟节点染红，将父节点当做要删除的黑色节点，然后递归进行平衡操作。
     *
     * c.兄弟节点是红色节点：将兄弟节点染黑，父节点染红，再对父节点进行右旋，然后就转换到了上面兄弟节点为黑色的删除情况。
     *
     * 注意：删除的节点没有父节点，说明是根节点，直接return即可
     * 注意：这里面有个点必须注意，当兄弟节点为黑色时，父节点肯定为红色，兄弟节点为红色时，父节点必然为黑色。
     */
}
