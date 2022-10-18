public class TrieTree {
    private Node root;//根

    /***
     * 一个字典树只有一个根
     */
    public TrieTree(){
        root = new Node(' ');//构造一个空的根节点
    }

    /***
     * @Description:查询
     * @Param:word 要判断的单词
     * @return :是否存在
     */
    public boolean search(String word){//华为
        Node current = root;//从根节点开始查找

        if (null!=word){
            //转化成字符数组
            char[] chars = word.toCharArray();
            if (null!=chars&&chars.length>0){
                for(char c : chars){
                    Node node = current.subNode(c);
                    if(null==node){
                        return false;
                    }else {
                        current = current.subNode(c);
                    }

                }
                //判断当前节点是否是结束节点
                if(current.isEnd()){
                    return true;
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public void insert(String word){
        //判断有没有这个词，如果有就说明这个词在整个字典树已经存在
        if(this.search(word)){
            return;
        }
        //如果不存在，就从根节点重新创建一个
        Node current = root;
        if(null!=word){
            char[] chars = word.toCharArray();
            if(null!=chars&&chars.length>0){
                for (char c : chars){
                    Node child = current.subNode(c);
                    if(null!=child){
                        current=child;
                    }else {
                        //构造新的
                        current.getChildList().add(new Node(c));
                        current = current.subNode(c);
                    }
                    current.setCount(current.getCount()+1);//出现次数+1
                }
                //循环结束之后把最后一个字变成isEnd是true
                current.setEnd(true);
            }
        }
    }

    /***
     * @Description: 删除分词
     * @Param: [word] 要删除的分词
     */
    public void deleteWord(String word) {
        //查询一个词在不在字典树，不在字典树就可以不用删除
        if (this.search(word) == false) {
            return;
        }
        Node current = root;
        if (null != word) {
            char[] chars = word.toCharArray();
            if (null != chars && chars.length > 0) {
                for (char c : chars) {
                    Node node = current.subNode(c);
                    if (node.getCount() == 1) {
                        //如果只剩一个了就直接从列表里面移除
                        current.getChildList().remove(node);
                        return;
                    } else {
                        current.setCount(current.getCount() - 1);
                        current = node;
                    }
                }
                current.setEnd(false);//isend设置为false代表当前路上的字连起来不是一相词了
            }
        }
    }
}
