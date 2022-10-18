import java.util.LinkedList;

public class Node {

    public char getContent() {
        return content;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public int getCount() {
        return count;
    }

    public LinkedList<Node> getChildList() {
        return childList;
    }

    private char content;//存在当前节点的字
    private boolean isEnd;//是否是词的结尾
    private int count;//这个词在这个字下面的分支的个数，相当于看看这个节点会往下拓展出来几个分支
    private LinkedList<Node> childList;//子节点

    /***
     * @Description: 构造方法 初始化节点使用
     */
    public Node(char c){
        childList=new LinkedList<>();
        isEnd=false;
        content=c;
        count=0;
    }

    /****
     * @Description: 提供一个遍历node中的linkedList中是否有这个字。有就意味着可以继续查找下去，没有就没有
     */
    public Node subNode(char c){
        if(null!=childList&&!childList.isEmpty()){
            for (Node node : childList) {
                if(node.content==c){
                    return node;
                }
            }
        }
        return null;
    }

    public void setContent(char content) {
        this.content = content;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setChildList(LinkedList<Node> childList) {
        this.childList = childList;
    }
}

