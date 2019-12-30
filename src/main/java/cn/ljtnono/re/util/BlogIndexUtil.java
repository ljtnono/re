package cn.ljtnono.re.util;

import cn.ljtnono.re.entity.ReBlog;
import cn.ljtnono.re.util.DateUtil;
import cn.ljtnono.re.util.StringUtil;
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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Jeremy
 * Date: 2019/12/30
 * Time: 12:46
 * Description: No Description
 */
@Slf4j
public class BlogIndexUtil {
    private FSDirectory dir;

    /**
    *@Description:  获取IndexWriter实例
    *@date: 2019/12/30
    */
    //private BlogIndexUtil(){};

    private IndexWriter getWriter() throws Exception {
        dir= FSDirectory.open(Paths.get("D:\\lucene"));
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
        IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
        IndexWriter writer=new IndexWriter(dir, iwc);
        return writer;
    }

    /**
     * 添加博客索引
     * @param blog
     * @throws Exception
     */
    public void addIndex(ReBlog reBlog)throws Exception{
        IndexWriter writer=getWriter();
        Document doc=new Document();
        doc.add(new StringField("id",String.valueOf(reBlog.getId()), Field.Store.YES));
        doc.add(new TextField("title",reBlog.getTitle(),Field.Store.YES));
        doc.add(new StringField("releaseDate", DateUtil.formatDate(new Date(), DateUtil.DateStyleEnum.yyyy_MM_dd),Field.Store.YES));
        doc.add(new TextField("content",reBlog.getContentHtml(),Field.Store.YES));
        writer.addDocument(doc);
        writer.close();
        log.info("对标题为："+reBlog.getTitle()+"进行分词");
    }


    /**
     * 更新博客索引
     */
    public void updateIndex(ReBlog blog)throws Exception{
        IndexWriter writer=getWriter();
        Document doc=new Document();
        doc.add(new StringField("id",String.valueOf(blog.getId()),Field.Store.YES));
        doc.add(new TextField("title",blog.getTitle(),Field.Store.YES));
        doc.add(new StringField("releaseDate",DateUtil.formatDate(new Date(), DateUtil.DateStyleEnum.yyyy_MM_dd),Field.Store.YES));
        doc.add(new TextField("content",blog.getContentMarkdown(),Field.Store.YES));
        writer.updateDocument(new Term("id",String.valueOf(blog.getId())), doc);
        writer.close();
    }


    /**
     * 删除指定博客的索引
     * @param blogId,String RootPath
     * @throws Exception
     */
    public void deleteIndex(String blogId)throws Exception{
        IndexWriter writer=getWriter();
        writer.deleteDocuments(new Term("id",blogId));
        writer.forceMergeDeletes(); // 强制删除
        writer.commit();
        writer.close();
    }



    /**
     * 查询博客信息
     * @param q
     * @return
     * @throws Exception
     */
    public List<ReBlog> searchBlog(String q)throws Exception{
        //dir=FSDirectory.open(Paths.get("/home/lucene/blog"));
        dir=FSDirectory.open(Paths.get("D:\\lucene"));
        IndexReader reader= DirectoryReader.open(dir);
        IndexSearcher is=new IndexSearcher(reader);
        BooleanQuery.Builder booleanQuery=new BooleanQuery.Builder();
        SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();

        QueryParser parser=new QueryParser("title", analyzer);
        Query query=parser.parse(q);

        QueryParser parser2=new QueryParser("content", analyzer);
        Query query2=parser2.parse(q);


        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);

        TopDocs hits=is.search(booleanQuery.build(), 100);
        //标红
        QueryScorer scorer=new QueryScorer(booleanQuery.build());
        Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
        Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);

        List<ReBlog> blogList=new LinkedList<ReBlog>();
        //System.out.println(hits.scoreDocs.length);
        //if(hits.scoreDocs.length!=0)
        for(ScoreDoc scoreDoc:hits.scoreDocs){
            Document doc=is.doc(scoreDoc.doc);
            ReBlog blog=new ReBlog();
            //System.out.println(doc.get("id"));
//            if(doc.get("id") == null)
//            blog.setId(Integer.parseInt(doc.get("id")));
            blog.setCreateTime(DateUtil.formatString(doc.get("releaseDate"),"yy-mm-dd"));
            String title=doc.get("title");
            if(title!=null){
                TokenStream tokenStream=analyzer.tokenStream("title", new StringReader(title));
                String hTitle=highlighter.getBestFragment(tokenStream, title);
                if(StringUtil.isEmpty(hTitle)){
                    blog.setTitle(title);
                }else{
                    blog.setTitle(hTitle);
                }
            }
            //过虑掉html中的<标签>

            String content=doc.get("content");//这个content取得是  notag的content  索引中的
            //把<>转义成   &lt; <    &gt; >
//            content = content.replace("<", "&lt;");
////            content =  content.replace(">", "&gt;");
            if(content!=null){
                TokenStream tokenStream=analyzer.tokenStream("content", new StringReader(content));
                String hContent=highlighter.getBestFragment(tokenStream, content);

                if(StringUtil.isEmpty(hContent)){
                    if(content.length()<=200){
                        blog.setContentHtml(content);
//                        blog.setContent(content);
                    }else{
                        blog.setContentHtml(content.substring(0,200));
                        //blog.setContent(content.substring(0, 200));
                    }
                }else{
                    blog.setContentMarkdown(hContent);
                }
            }
            blogList.add(blog);
        }
        return blogList;
    }


}
