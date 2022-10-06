import matplotlib.pyplot as plt
from wordcloud import WordCloud
import jieba
from matplotlib import colors

color_list = ['#ff7f0e', '#17becf', '#9467bd', '#1f77b4', "#e377c2", "#bcbd22"]  # 建立颜色数组
colormap=colors.ListedColormap(color_list)  # 调用
def word_cloud():
    # 词云文本的路径
    path = "../file/final.txt"

    # 打开文件读取
    with open(path, 'r', encoding="utf-8") as f:
        cut_text = f.read()

    # 打印读取结果
    print(cut_text)

    # 配置词云图，并生成词云图
    word_cloud = WordCloud(
        # 如果不加font_path就会出现乱码的问题
        font_path="C:/Windows/Fonts/STFANGSO.TTF",
        background_color="white",
        width=1920,
        height=1080,
        colormap=colormap
    ).generate(cut_text)

    # 显示词云图
    plt.imshow(word_cloud, interpolation="bilinear")
    plt.axis('off')
    plt.show()

    # 保存词云图
    word_cloud.to_file("../pic/word_cloud_jieba_new.png")
    pass


def main():
    word_cloud()

    pass


if __name__ == '__main__':
    main()