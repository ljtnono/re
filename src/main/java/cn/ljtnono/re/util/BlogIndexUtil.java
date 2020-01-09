package cn.ljtnono.re.util;

import cn.ljtnono.re.entity.ReBlog;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.FSDirectory;


import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * lucene 工具类
 *
 * @author devin
 * @version 1.0.1
 * @date 2019/12/30
 */
@Slf4j
public class BlogIndexUtil {

    private FSDirectory dir;

    private static final String SAVE_PATH = "D:\\lucene";

    private BlogIndexUtil() {}

    private volatile static BlogIndexUtil instance = null;

    public static BlogIndexUtil getInstance() {
        if (instance == null) {
            synchronized (BlogIndexUtil.class) {
                if (instance == null) {
                    instance = new BlogIndexUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 获取索引writer
     *
     * @return IndexWrite
     * @throws IOException 当目录不存在时抛出IO异常
     */
    private IndexWriter getWriter() throws IOException {
        dir = FSDirectory.open(Paths.get(SAVE_PATH));
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        return new IndexWriter(dir, iwc);
    }


    /**
     * 添加分词索引
     *
     * @param reBlog 需要进行分词的实体对象
     * @throws IOException 当目录不存在时抛出IO异常
     */
    public void addIndex(ReBlog reBlog) throws IOException {
        IndexWriter writer = getWriter();
        Document doc = generateDocument(reBlog, reBlog.getContentHtml());
        writer.addDocument(doc);
        writer.close();
        log.info("对标题为：" + reBlog.getTitle() + "进行分词");
    }

    /**
     * 根据blog实体和博客contentHtml字段生成document对象
     *
     * @param reBlog      blog实体
     * @param contentHtml 字段
     * @return Document对象
     */
    private Document generateDocument(ReBlog reBlog, final String contentHtml) {
        Document doc = new Document();
        doc.add(new StringField("id", String.valueOf(reBlog.getId()), Field.Store.YES));
        doc.add(new TextField("title", reBlog.getTitle(), Field.Store.YES));
        doc.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), DateUtil.DateStyleEnum.yyyy_MM_dd), Field.Store.YES));
        doc.add(new TextField("content", contentHtml, Field.Store.YES));
        return doc;
    }


    /**
     * 更新博客索引
     *
     * @param blog 根据博客实体更新索引
     */
    public void updateIndex(ReBlog blog) throws IOException {
        IndexWriter writer = getWriter();
        Document doc = generateDocument(blog, blog.getContentMarkdown());
        writer.updateDocument(new Term("id", String.valueOf(blog.getId())), doc);
        writer.close();
    }


    /**
     * @param blogId 根据博客id删除索引
     * @throws IOException 当目录不存在时抛出IO异常
     */
    public void deleteIndex(String blogId) throws IOException {
        IndexWriter writer = getWriter();
        writer.deleteDocuments(new Term("id", blogId));
        // 强制删除
        writer.forceMergeDeletes();
        writer.commit();
        writer.close();
    }


    /**
     * @param keyword 索引关键字
     * @return 博客列表
     * @throws Exception
     */
    public List<ReBlog> searchBlog(String keyword) throws Exception {
        dir = FSDirectory.open(Paths.get(SAVE_PATH));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher is = new IndexSearcher(reader);
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();

        QueryParser titleParser = new QueryParser("title", analyzer);
        Query titleQuery = titleParser.parse(keyword);

        QueryParser contentParser = new QueryParser("content", analyzer);
        Query contentQuery = contentParser.parse(keyword);


        booleanQuery.add(titleQuery, BooleanClause.Occur.SHOULD);
        booleanQuery.add(contentQuery, BooleanClause.Occur.SHOULD);

        TopDocs hits = is.search(booleanQuery.build(), 100);
        //标红
        QueryScorer scorer = new QueryScorer(booleanQuery.build());
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter simpleHtmlFormatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
        Highlighter highlighter = new Highlighter(simpleHtmlFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);

        List<ReBlog> blogList = new LinkedList<>();
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            ReBlog blog = new ReBlog();
            blog.setCreateTime(DateUtil.formatString(doc.get("releaseDate"), "yy-mm-dd"));
            String title = doc.get("title");
            if (title != null) {
                TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(title));
                String hTitle = highlighter.getBestFragment(tokenStream, title);
                if (StringUtil.isEmpty(hTitle)) {
                    blog.setTitle(title);
                } else {
                    blog.setTitle(hTitle);
                }
            }
            //过虑掉html中的<标签>
            String content = doc.get("content");
            //这个content取得是  notag的content  索引中的
            if (content != null) {
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                String hContent = highlighter.getBestFragment(tokenStream, content);
                if (StringUtil.isEmpty(hContent)) {
                    if (content.length() <= 200) {
                        blog.setContentHtml(content);
//                        blog.setContent(content);
                    } else {
                        blog.setContentHtml(content.substring(0, 200));
                        //blog.setContent(content.substring(0, 200));
                    }
                } else {
                    blog.setContentMarkdown(hContent);
                }
            }
            blogList.add(blog);
        }
        return blogList;
    }
}
