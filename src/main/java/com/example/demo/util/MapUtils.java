/*
这是当使用室内地图等时的mock数据方案。
不过目前考虑到前端的展示，先直接使用腾讯开放平台的接口，详情见TencentMapUtils.java
 */

package com.example.demo.util;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class MapUtils {
    static class Site{
        int id;
        String name;
        Double lon;
        Double lat;
    }
    public static List<Site> siteList = new ArrayList<>();

    static class Edge{
        int from;
        int to;
        Double distance;
    }
    public static List<Edge> edgeList = new ArrayList<>();

    //初始化时读入数据
    public static void init() throws IOException {
        System.out.println("init");
        //读入siteList
        /*
        格式：
        - id: 1
          name: 1
          x: 1
          y: 1
        - id: 2
            name: 2
            x: 2
            y: 2
         */
        //从yaml文件中读入, 文件名为site.yaml
        //读入siteList
        File file = new File("tools/site.yaml");
        Yaml yaml = new Yaml();
        Iterable<Object> ret = yaml.loadAll(new FileInputStream(file));
        ArrayList<Map<String, Object> > list = (ArrayList<Map<String, Object>>) ret.iterator().next();

        for ( Map<String, Object> map : list) {
            Site site = new Site();
            site.id = (int) map.get("id");
            site.name = String.valueOf( map.get("name") );
            site.lon = (Double) map.get("lon");
            site.lat = (Double) map.get("lat");
            siteList.add(site);
        }
        //输出siteList
        for (Site site : siteList) {
            System.out.println(site.id + " " + site.name + " " + site.lon + " " + site.lat);
        }
        //从txt中读入edgeList
        /*
        格式：
        1 2 1
        2 3 2
         */
        File file2 = new File("tools/edge.txt");
        FileInputStream fileInputStream = new FileInputStream(file2);
        byte[] bytes = fileInputStream.readAllBytes();
        int len = 0;
        String str = "";
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] == '\n') {
                String[] split = str.split(" ");
                Edge edge = new Edge();
                edge.from = Integer.parseInt(split[0]);
                edge.to = Integer.parseInt(split[1]);
                edge.distance = Double.parseDouble(split[2]);
                edgeList.add(edge);
                str = "";
            } else {
                str += (char) bytes[i];
            }
        }

        //输出edgeList
        for (Edge edge : edgeList) {
            System.out.println(edge.from + " " + edge.to + " " + edge.distance);
        }
    }
}
