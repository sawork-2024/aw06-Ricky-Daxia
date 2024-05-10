package com.example.posproduct.service;

import com.example.posproduct.model.Product;
import com.example.posproduct.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    
    private List<Product> products = null;

    private ProductRepository productRepository;

    private Resilience4JCircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Autowired
    public void setCircuitBreakerFactory(Resilience4JCircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Override
    public List<Product> getProducts() {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(this::tryGetProducts, throwable -> getDefaultProductList());
    }

    private List<Product> tryGetProducts() {
        try {
            if (products == null) {
                products = parseJD("Java");
            }
        } catch (IOException e) {
            products = new ArrayList<>();
        }
        return productRepository.findAll();
    }

    private List<Product> getDefaultProductList() {
        List<Product> defaultProductList = new ArrayList<>();
        Product defaultProduct = new Product("default", "default", "default", "default", 100, "default", 1, "default.png");
        defaultProductList.add(defaultProduct);
        return defaultProductList;
    }

    @Override
    @Transactional
    public Product findByID(String productId) {
        return productRepository.findById(productId);
    }

    @Override
    public boolean updateProductQuantityById(String productId, int quantity) {
        return productRepository.updateProductQuantityById(productId, quantity) > 0;
    }

    public List<Product> parseJD(String keyword) throws IOException {
        //获取请求https://search.jd.com/Search?keyword=java
        String url = "https://search.jd.com/Search?keyword=" + keyword;

        // Map<String, String> cookies = new HashMap<>();
        // cookies.put("thor", "4FEDBAFDC8ED98F4ECA8EA73171228F5FDF865E03CC8D4ABA0FC06D596140E7958F48A3D6F04CDAF349E1EF1EA8E858EC41652DBA90195F195957B85C939E499F5428F681F31A09B2274B94CF1A4B608819B56D1A15BA890F8D8650E674FAC59BB3AE1650E6E3F37E4135E4FA6667C714D8A5043FAC973CE55527FAEAC1042372AE44C0DA3AE29D0E6CCD8F595EC79E4C10F1B4CF8512BDAAE8FFB6FE943172B");

        //解析网页
        // Document document = Jsoup.parse(new URL(url), 10000);
        Document document = Jsoup.connect(url).cookie("thor", "4FEDBAFDC8ED98F4ECA8EA73171228F5FDF865E03CC8D4ABA0FC06D596140E794DC30325C1461C544723FB9B712B04DFAAA219DD05993E914B25ED224A43DE7AF66F8171936A76E94658ED738A1530AE81B9788B996F3BF43A2846253FC420E742F28D2208C59EE8F688E489B6E920F385BB2A1D46533CBDC4C7E1691894AEC40036F26A72585F1A557817105117F0BA21A462338BF755525CF5DBDCCE9FB662").get();
        // System.out.println(document.body());
        //所有js的方法都能用
        Element element = document.getElementById("J_goodsList");
        //获取所有li标签
        Elements elements = element.getElementsByTag("li");
        
        List<Product> list = new ArrayList<>();

        //获取元素的内容
        for (Element el : elements
        ) {
            //关于图片特别多的网站，所有图片都是延迟加载的
            String id = el.attr("data-spu");
            String img = "https:".concat(el.getElementsByTag("img").eq(0).attr("data-lazy-img"));
            String price = el.getElementsByAttribute("data-price").text();
            String title = el.getElementsByClass("p-name").eq(0).text();
            if (title.contains("，"))
                title = title.substring(0, title.indexOf("，"));
            // System.out.println(img);
            Product product = new Product(id, id, price, "Java", Math.max((int)(Math.random() * 30), 10), title, 1, img);
            // System.out.println(id);
            // System.out.println(img);
            // System.out.println(title);

            productRepository.save(product);                

            list.add(product);
        }
        return list;
    }
}
