/**
 * 调用腾讯地图API
 */
package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TencentMapUtil {
    static final String KEY = "腾讯地图API的KEY";

    //设置Point可以序列化
    public static class Point implements java.io.Serializable {
        Double lon;
        Double lat;
        public Point(Double lon,Double lat) {
            this.lon = lon;
            this.lat = lat;
        }
        public Double getLon() {
            return lon;
        }
        public Double getLat() {
            return lat;
        }
        public boolean equals(Object obj) {
            if (obj instanceof Point) {
                Point p = (Point) obj;
                return Math.abs(p.getLon() - lon) < 0.0001 && Math.abs(p.getLat() - lat) < 0.0001;
            }
            return super.equals(obj);
        }
    }
    /**
     * 调用腾讯地图API
     * @param start_lon
     * @param start_lat
     * @param end_lon
     * @param end_lat
     * @return
     */
    public List getRoute(Double start_lon, Double start_lat, Double end_lon, Double end_lat) {


        RestTemplate restTemplate=new RestTemplate();
        String url = "https://apis.map.qq.com/ws/direction/v1/driving/?from="+start_lat+","+start_lon+"&to="+end_lat+","+end_lon+"&key="+KEY;
        //返回的是一个json格式的字符串
        String json = restTemplate.getForObject(url, String.class);
        System.out.println(json);
        //{"status":0,"message":"query ok","request_id":"b3cb7c03a34c4aa989f63dee8a8eb8","result":{"routes":[{"mode":"DRIVING","distance":6416,"duration":25,"traffic_light_count":11,"toll":0,"restriction":{"status":1},"polyline":[39.907684,116.397545,-56,-2151,-59,-1908,-51,-1594,0,0,171,10,228,-2,2246,-145,490,-25,1418,-137,1252,-105,1462,-139,236,-4,357,-35,236,0,1213,-65,258,-7,283,6,634,-52,113,1,595,-46,804,-28,1594,-76,897,-14,0,0,452,-9,156,112,179,169,145,171,69,127,40,88,54,24,19,282,13,420,65,1392,30,948,67,2021,9,296,13,399,22,627,72,1377,9,186,0,0,1885,-65,537,-39,107,3,1954,-91,288,-35,74,-32,76,-57,0,0,30,-56,70,-179,-6,-1879,-9,-699,0,0,145,2,1865,-91,1620,-68,299,-31,113,33,98,7,413,-23,207,4,202,-10,327,-8,781,-39,396,-27,40,-46,453,-3,606,-27,238,-23,1195,-64,54,-3,520,-15,395,-45,30,-2,54,27,1512,-77,139,-17,40,-30,0,0,330,-555,112,-263,100,-1107,0,0,160,12,624,8,740,-31,1007,-19,169,-19,556,-17,653,20,470,-20,308,-4,87,-9,535,-6,605,-29,332,-24,1104,-43,748,-42,0,0,35,1751,7,1941],"steps":[{"instruction":"沿西长安街向西行驶483米,右转","polyline_idx":[0,7],"road_name":"西长安街","dir_desc":"西","distance":483,"act_desc":"右转","accessorial_desc":""},{"instruction":"沿南长街向北行驶1.6公里,直行","polyline_idx":[8,47],"road_name":"南长街","dir_desc":"北","distance":1610,"act_desc":"直行","accessorial_desc":""},{"instruction":"沿景山前街行驶825米,左转","polyline_idx":[48,83],"road_name":"景山前街","dir_desc":"","distance":825,"act_desc":"左转","accessorial_desc":""},{"instruction":"沿景山东街行驶548米,","polyline_idx":[84,99],"road_name":"景山东街","dir_desc":"","distance":548,"act_desc":"","accessorial_desc":""},{"instruction":"沿景山东街行驶243米,右转","polyline_idx":[100,109],"road_name":"景山东街","dir_desc":"","distance":243,"act_desc":"右转","accessorial_desc":""},{"instruction":"沿地安门内大街行驶1.3公里,左转","polyline_idx":[110,161],"road_name":"地安门内大街","dir_desc":"","distance":1307,"act_desc":"左转","accessorial_desc":""},{"instruction":"沿鼓楼西大街行驶180米,右转","polyline_idx":[162,169],"road_name":"鼓楼西大街","dir_desc":"","distance":180,"act_desc":"右转","accessorial_desc":""},{"instruction":"沿旧鼓楼大街向北行驶899米,右转","polyline_idx":[170,201],"road_name":"旧鼓楼大街","dir_desc":"北","distance":899,"act_desc":"右转","accessorial_desc":""},{"instruction":"沿安定门西大街辅路向东行驶315米,","polyline_idx":[202,207],"road_name":"安定门西大街辅路","dir_desc":"东","distance":315,"act_desc":"","accessorial_desc":""}],"tags":[],"taxi_fare":{"fare":22},"route_explain_v2":[],"events":[]}]}}
        //将json字符串转换为json对象
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            if (jsonObject.getInt("status") == 374) {
                log.info("{},{},{},{} 错误",start_lon,start_lat,end_lon,end_lat);
                return null;
            }
            //获取json对象中的result属性
            JSONObject result = jsonObject.getJSONObject("result");

            //获取result属性中的routes属性
            JSONArray routes =  (JSONArray)result.get("routes");

            JSONArray route = (JSONArray)((JSONObject)routes.get(0)).get("polyline");

            /**
             * 解压坐标
             * [坐标1纬度 , 坐标1经度 , 坐标2纬度 , 坐标2经度 , 坐标3纬度 , 坐标3经度…]，
             * 第一个坐标为原始未被压缩过的，之后的使用前向差分进行压缩，解压方法如下：
             * var coors=[127.496637,50.243916,-345,-1828,19867,-26154];
             * for (var i = 2; i < coors.length ; i++)
             * {coors[i] = coors[i-2] + coors[i]/1000000}
             *
             *
             */
            List<Point> list = new ArrayList<>();
            for (int i = 2; i < route.length(); i++) {
                // 使用Double.parseDouble(item.toString())将Object转换为Double
                route.put(i, Double.parseDouble(route.get(i - 2).toString()) + Double.parseDouble(route.get(i).toString()) / 1000000.0);
            }

            List<Point> points = new ArrayList<>();
            for (int i = 0; i < route.length(); i += 2) {
                points.add(new Point(route.getDouble(i + 1),route.getDouble(i)));
            }
            //加速，把路径点均等压缩到10个点以内,必须保证起点和终点在里面
            int size = points.size();
            int step = size / 10;
            if(step < 1){
                step = 1;
            }
            for(int i = 0; i < size; i += step){
                list.add(points.get(i));
            }
            list.add(points.get(size - 1));

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
