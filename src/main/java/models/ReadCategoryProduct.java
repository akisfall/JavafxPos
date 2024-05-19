package models;

import java.util.TreeMap;
import models.Product;

public class ReadCategoryProduct {
    
     //準備好產品清單  
    public static TreeMap<String, Product> readProduct() {
        TreeMap<String, Product> product_dict = new TreeMap<>();
        String[][] product_array = {
            {"p101", "水果", "奇異果", "70", "kiwi.png", "奇異果"},
            {"p102", "水果", "椰子", "80", "coconut.png", "椰子"},
            {"p103", "水果", "水蜜桃", "90", "peach.png", "水蜜桃"},
            {"p104", "水果", "葡萄", "100", "grapes.png", "葡萄"},
            {"p105", "水果", "草莓", "100", "strawberry.png", "草莓"},
            {"p106", "水果", "芒果", "100", "mango.png", "芒果"},
            {"p107", "水果", "櫻桃", "120", "cherry.png", "櫻桃"},
            {"p108", "水果", "香蕉", "75", "banana.png", "香蕉"},
            {"p109", "水果", "橘子", "65", "orange.png", "橘子"},
            {"p110", "水果", "西瓜", "60", "watermelon.png", "西瓜"},
            {"p111", "單茶", "紅茶", "45", "blacktea.jpg", "紅茶"},
            {"p112", "單茶", "綠茶", "45", "greentea.jpg", "綠茶"},
            {"p113", "奶茶", "珍珠奶茶", "70", "perlmilktea.jpg", "珍珠奶茶"},
            {"p114", "單茶", "青茶", "50", "chingtea.jpg", "青茶"},
            {"p115", "單茶", "東方美人茶","90","Touhoutea.jpg", "東方美人茶"},
            {"p116", "單茶", "玄米茶", "60", "riceTea.jpg", "玄米茶"},
            {"p117", "單茶", "抹茶", "75", "macha.jpg", "抹茶"},
            {"p118", "奶茶", "黑糖奶茶", "75","blacksugar.jpg","黑糖奶茶"},
            {"p119", "奶茶", "紅茶拿鐵", "80", "milktea.jpg", "紅茶拿鐵"},
            {"d201", "日用品", "洗衣精", "240", "washing.jpg","洗衣精"},
            {"d202", "日用品", "黏塵滾筒", "40", "adhere.jpg","黏塵滾筒"}
        };

        //一筆放入字典變數product_dict中
        for (String[] item : product_array) {
            Product product = new Product(
                    item[0], 
                    item[1], 
                    item[2], 
                    Integer.parseInt(item[3]), //價格轉為int
                    item[4], 
                    item[5]);
            //將這一筆放入字典變數product_dict中 
            product_dict.put(product.getProduct_id(), product);
        }
        return product_dict; 
    }
}
