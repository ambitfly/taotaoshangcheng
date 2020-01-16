package com.taotao.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.taotao.dao.BrandMapper;
import com.taotao.dao.SpecMapper;
import com.taotao.service.goods.SkuSearchService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkuSearchServiceImpl implements SkuSearchService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private SpecMapper specMapper;

    public Map search(Map<String, String> searchMap) {
        System.out.println(searchMap);
        //1.封装查询请求
        SearchRequest searchRequest = new SearchRequest("sku");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        //1.1 关键字收索
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name",searchMap.get("keywords"));
        boolQueryBuilder.must(matchQueryBuilder);

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);

        //1.2 商品分类过滤
        if(searchMap.get("category")!=null){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("categoryName",searchMap.get("category"));
            boolQueryBuilder.filter(termQueryBuilder);
        }
        //1.3 品牌分类过滤
        if(searchMap.get("brand")!=null){
            TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("brandName",searchMap.get("brand"));
            boolQueryBuilder.filter(termQueryBuilder);
        }

        //1.4 规格过滤
        for(String key:searchMap.keySet()){
            if(key.startsWith("spec.")){
                TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(key+".keyword",searchMap.get(key));
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        //1.5 价格过滤
        if(searchMap.get("price")!=null){
            String[] price = searchMap.get("price").split("-");
            if(!price[0].equals("0")){
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price").gte(price[0]+"00");
                boolQueryBuilder.filter(rangeQueryBuilder);
            }

            if(!price[1].equals("*")){
                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("price").lte(price[1]+"00");
                boolQueryBuilder.filter(rangeQueryBuilder);
            }
        }

        //聚合查询
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("sku_category").field("categoryName.keyword");
        searchSourceBuilder.aggregation(termsAggregationBuilder);



        //2.封装查询结果
        Map resultMap = new HashMap();

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
            SearchHits searchHits = searchResponse.getHits();
            long totalHits = searchHits.getTotalHits();
            SearchHit[] hits = searchHits.getHits();

            //2.1商品列表
            List<Map<String,Object>> resultList = new ArrayList<Map<String, Object>>();
            for(SearchHit searchHit:hits){
                Map<String,Object> skuMap = searchHit.getSourceAsMap();
                //System.out.println(skuMap);
                resultList.add(skuMap);
            }
            resultMap.put("rows",resultList);

           //2.2商品分类列表

            Aggregations aggregations = searchResponse.getAggregations();
            Map<String,Aggregation> aggregationMap = aggregations.getAsMap();
            Terms terms = (Terms)aggregationMap.get("sku_category");
            List<? extends Terms.Bucket> buckets = terms.getBuckets();
            List<String> categoryList = new ArrayList<String>();
            for(Terms.Bucket bucket:buckets){
                categoryList.add(bucket.getKeyAsString());
            }
            resultMap.put("categoryList",categoryList);
            //2.3 品牌列表

            String categoryName = "";
            if(searchMap.get("category")==null){
                if(categoryList.size()>0){
                    categoryName = categoryList.get(0);
                }
            }else{
                categoryName = searchMap.get("category");
             //   System.out.println("categoryName="+categoryName);
            }
            List<Map> brandList = brandMapper.findBrandListByCategoryName(categoryName);
           // System.out.println("brandList="+brandList);
            resultMap.put("brandList",brandList);

            //2.4 规格列表
            List<Map> specList = specMapper.findSpecListByCategoryName(categoryName);
            for(Map spec:specList){
                String[] optipns  =((String) spec.get("options")).split(",");
                spec.put("options",optipns);
            }
            resultMap.put("specList",specList);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        System.out.println(resultMap);
        return resultMap;
    }
   /*public Map search(Map<String, String> searchMap) {
       //1.连解rest接口
       HttpHost http = new HttpHost("127.0.0.1",9200,"http");
       RestClientBuilder builder = RestClient.builder(http);
       RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);

       //2.封装请求对象
       SearchRequest searchRequest = new SearchRequest("sku");
       searchRequest.types("doc");
       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
       MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name",searchMap.get("keywords"));
       searchSourceBuilder.query(matchQueryBuilder);
       searchRequest.source(searchSourceBuilder);

       //3.获取响应结果
       try {
           SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
           SearchHits searchHits = searchResponse.getHits();
           long totalHits = searchHits.getTotalHits();
           System.out.println("totalHits="+totalHits);
           SearchHit[] hits = searchHits.getHits();
           for(SearchHit searchHit:hits){
               System.out.println(searchHit.getSourceAsString());
           }
       } catch (IOException e) {
           e.printStackTrace();
       } finally {
           try {
               restHighLevelClient.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
       return new HashMap();
   }*/

}
