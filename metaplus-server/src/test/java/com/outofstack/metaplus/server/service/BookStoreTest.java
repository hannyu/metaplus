package com.outofstack.metaplus.server.service;

import com.outofstack.metaplus.common.DateUtil;
import com.outofstack.metaplus.common.model.*;
import com.outofstack.metaplus.common.model.schema.Field;
import com.outofstack.metaplus.common.model.schema.Properties;
import com.outofstack.metaplus.common.model.search.Hits;
import com.outofstack.metaplus.server.MetaplusServerAppTest;
import com.outofstack.metaplus.server.storage.EsClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = MetaplusServerAppTest.class)
public class BookStoreTest {

    private static final Logger log = LoggerFactory.getLogger(BookStoreTest.class);

    @Autowired
    private EsClient esClient;

    @Autowired
    private PatchService patchService;

    @Autowired
    private DomainService domainService;

    @Autowired
    private QueryService queryService;

//    private static DomainService mDomainService;
//
//    @PostConstruct
//    public void initService() {
//        mDomainService = domainService;
//    }

    @BeforeAll
    public static void initBookStore(@Autowired DomainService domainService) {
        log.info("initBookStore: {}", domainService.listDomains());

        if (domainService.existDomain("book")) {
            DomainDoc domainDoc = new DomainDoc("book");
            domainService.deleteDomain(domainDoc);
        }

        Properties meta = new Properties();
        meta.putProperty("isbn", new Field("keyword", true));
        meta.putProperty("title", new Field("text"));

        Properties authors = new Properties();
        authors.putProperty("nationality", new Field("keyword"));
        meta.putProperty("authors", authors);

        meta.putProperty("language", new Field("keyword"));
        meta.putProperty("pageCount", new Field("integer"));
        meta.putProperty("tags", new Field("keyword"));
        meta.putProperty("isPublished", new Field("boolean"));
        meta.putProperty("publisher", new Field("keyword"));
        meta.putProperty("publishDate", new Field("date"));
        meta.putProperty("desc", new Field("text"));

        Properties mappings = new Properties();
        mappings.putProperty("meta", meta);

        DomainDoc domainDoc = new DomainDoc("book");
        domainDoc.getSchema().setMappings(mappings);

        log.info("domainDoc: " + domainDoc);
        domainService.createDomain(domainDoc);
        log.info("create domain book success!");
    }

    @AfterAll
    public static void cleanupBookStore(@Autowired DomainService domainService) {
        log.info("cleanupBookStore");
//        MetaPatch domainPatch = new MetaPatch(PatchMethod.META_DELETE, "", "domain", "book");
//        domainService.deleteDomain(domainPatch);
    }

    @Test
    public void testOne() {
        /// CRUD doc

        // exist
        assertTrue(domainService.existDomain("book"));

        // create book
        MetaplusDoc doc1 = new MetaplusDoc("::book::isbn-123-1234567");
        doc1.getMeta()
                .put("isbn", "123-1234567")
                .put("title", "钢铁是怎样练成的")
                .put("pageCount", 5555)
                .put("isPublished", true)
                .put("publishDate", DateUtil.formatNow())
                .put("sth_not_exist", "Yes, i am not exist~");
        doc1.getPlus().put("desc", "说点啥呢???");
        patchService.createMeta(doc1);

        MetaplusDoc doc = queryService.readDoc("::book::isbn-123-1234567");
        log.info("doc: {}", doc);
        assertEquals(5555, doc.getIntegerByPath("$.meta.pageCount"));

        // update book
        MetaplusDoc doc2 = new MetaplusDoc("::book::isbn-123-1234567");
        doc2.getMeta().put("pageCount", 6666);
        patchService.updateMeta(doc2);

        doc = queryService.readDoc("::book::isbn-123-1234567");
        log.info("doc: {}", doc);
        assertEquals(6666, doc.getIntegerByPath("$.meta.pageCount"));

        // delete book
        patchService.deleteMeta(doc2);
        boolean isExist = patchService.existMeta(new MetaplusDoc("::book::isbn-123-1234567"));
        assertFalse(isExist);
    }


    @Test
    public void testTwo() {
        /// generate sample

        MetaplusDoc sample = domainService.sample("book");
        assertEquals(0, sample.getMeta().getInteger("pageCount"));
        log.info("sample: {}", sample);
    }

    @Test
    public void testThree() {
        /// update domain add field

        DomainDoc domainDoc = new DomainDoc("book");
        log.info("original domain: {}", domainDoc);
        Properties mappings = domainDoc.getSchema().getMappings();
        Properties plusProps = new Properties();
        mappings.putProperty("plus", plusProps);
        plusProps.putProperty("like", new Field("integer", false, 100));
        domainService.updateDomain(domainDoc);

        // read updated domain
        domainDoc = domainService.readDomain("book");
        log.info("updated domain: {}", domainDoc);

        // then sample
        MetaplusDoc doc = domainService.sample("book");
        log.info("updated sample: {}", doc);

    }

    @Test
    public void testFour() throws InterruptedException {
        /// search

        // create many books
        String[] books = new String[]{"百年孤独", "战争与和平", "追风筝的人", "傲慢与偏见", "了不起的盖茨比", "1984", "飘",
                "小王子", "老人与海", "罪与罚", "呼啸山庄", "安娜·卡列尼娜", "悲惨世界", "尤利西斯", "堂吉诃德", "红楼梦",
                "Crime and Punishment", "The Count of Monte Cristo", "Don Quixote", "Emma", "Frankenstein",
                "The Iliad", "Jane Eyre", "Les Misérables", "Macbeth", "Moby Dick", "Oliver Twist",
                "三国演义", "水浒传", "西游记", "金瓶梅", "鲁滨逊漂流记", "简·爱", "麦田里的守望者", "局外人", "变形记",
                "伊利亚特", "奥德赛", "荷马史诗", "源氏物语", "挪威的森林", "海边的卡夫卡", "1Q84", "白鲸", "福尔摩斯探案集",
                "达·芬奇密码", "哈利·波特与魔法石", "魔戒", "霍比特人", "纳尼亚传奇", "银河系漫游指南", "美丽新世界", "动物农场",
                "One Hundred Years of Solitude", "The Picture of Dorian Gray", "The Adventures of Huckleberry Finn",
                "The Adventures of Tom Sawyer", "The Alchemist", "The Canterbury Tales", "The Catcher in the Rye",
                "The Count of Monte Cristo"};

        Random rand = new Random();
        for (int i=0; i<books.length; i++) {
            String isbn = "isbn-" + (1000000+i);
            MetaplusDoc book = new MetaplusDoc("book", isbn);
            book.getMeta()
                    .put("isbn", isbn)
                    .put("title", books[i])
                    .put("pageCount", rand.nextInt(1000));
            patchService.createMeta(book);
        }

        // wait ES build index
        Thread.sleep(1000);

        // simple search
        Hits hits = queryService.simpleSearch("book", "偏见");
        System.out.println("total [" + hits.getHitsSize() + "] hits: " + hits);
        assertTrue(hits.getHitsSize() > 0);

        hits = queryService.simpleSearch("*", "moby");
        System.out.println("total [" + hits.getHitsSize() + "] hits: " + hits);
        assertTrue(hits.getHitsSize() > 0);

        hits = queryService.simpleSearch("", "人 +-局外");
        System.out.println("total [" + hits.getHitsSize() + "] hits: " + hits);
        assertTrue(hits.getHitsSize() > 0);
    }



}
