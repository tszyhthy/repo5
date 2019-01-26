package com.hui.lucene;


import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import java.io.File;

public class luceneFirst {

    @Test
    public void createIndex() throws Exception {
        Directory directory = FSDirectory.open(new File("D://index").toPath());
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());
        File file = new File("G:\\三大框架\\Java36\\008 A0.lucene2018\\02.参考资料\\searchsource");
        File[] files = file.listFiles();
        for (File file1 : files) {
            //文件名
            String fileName = file1.getName();
            //文件的路径
            String filePath = file1.getPath();
            //文件的内容
            String fileContent = FileUtils.readFileToString(file1, "utf-8");
            //文件的大小
            long fileSize = FileUtils.sizeOf(file1);
            //创建Field
            Field fieldName = new TextField("name", fileName, Field.Store.YES);
            Field fieldPath = new TextField("path", filePath, Field.Store.YES);
            Field fieldContent = new TextField("content", fileContent, Field.Store.YES);
            Field fieldSize = new TextField("size", fileSize + "", Field.Store.YES);
            //创建文档对象
            Document document = new Document();
            //向文档对象中添加域
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContent);
            document.add(fieldSize);
            //把文档写入索引库
            indexWriter.addDocument(document);
        }
        //关闭indexWrite对象
        indexWriter.close();
    }


    @Test
    public void searchIndex() throws Exception{
        //创建一个directory对象,指定索引库的位置
        Directory directory =FSDirectory.open(new File("d://index").toPath());
        //创建一个IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
       //创建一个indexSearcher对象,构造参数中指定IndexReader
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //创建一个query对象,termquery
        Query query = new TermQuery(new Term("content","spring"));
        //执行查询,得到一个TopDocs对象
        //参数一查询对象,参数二,查询结果返回的最大记录数
        TopDocs topDocs = indexSearcher.search(query, 10);
        //查询结果的总记录数
        System.out.println("查询总记录数"+topDocs.totalHits);
        //获取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //打印文档中的内容
        for (ScoreDoc scoreDoc : scoreDocs) {
            //获取文档id
            int docID = scoreDoc.doc;
            //根据id获取文档对象
            Document document = indexSearcher.doc(docID);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
           // System.out.println(document.get("content"));
            System.out.println(document.get("size"));
            System.out.println("========================");
        }
        //关闭indexreader
        indexReader.close();
    }

    @Test
    public void testTakenStream()throws Exception{
        Analyzer analyzer = new StandardAnalyzer();
        TokenStream tokenStream = analyzer.tokenStream("", "The Spring Framework provides a comprehensive programming and configuration model.");
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        tokenStream.reset();;
        while (tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
        tokenStream.close();
    }
}
