public class SearchTree {
    public static void main(String[] args) {
        String content="华为-华为手机-华为平板-华为牛逼-鸿蒙-华为鸿蒙操作系统";
        //模拟分词
        String[] split = content.split("-");

        //构造字典树
        TrieTree trie = new TrieTree();
        //把分词插入
        for (String s : split) {
            trie.insert(s);
        }

        System.out.println(trie.search("华为"));
        System.out.println(trie.search("华为手"));

        trie.deleteWord("华为");
        System.out.println(trie.search("华为"));

        System.out.println(trie.search("华为手机"));


    }
}
