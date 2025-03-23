package com.macle.jdk17;

public class Test {
    String jsonBefore = "{\n" +
            "  \"name\": \"张三\",\n" +
            "  \"age\": 25,\n" +
            "  \"address\": \"北京市朝阳区\"\n" +
            "}";

    String jsonAfter = """  
    {  
      "name": "张三",  
      "age": 25,  
      "address": "北京市朝阳区"  
    }  
    """;

    public static void main(String[] args) {
        instanceOf("abc");
        instanceOf(1);
    }

    private static void instanceOf(Object obj){
        //Before JDK17
        if (obj instanceof String) {
            String str = (String) obj;
            System.out.println(str.toUpperCase());
        } else if (obj instanceof Integer) {
            Integer num = (Integer) obj;
            System.out.println(num * 2);
        }

        //With JDK17
        if (obj instanceof String str) {
            System.out.println(str.toUpperCase());
        } else if (obj instanceof Integer num) {
            System.out.println(num * 2);
        }
    }

    private static void switchCase(String day){
        //Before JDK17
        String dayType;
        switch (day) {
            case"Monday":
            case"Tuesday":
            case"Wednesday":
            case"Thursday":
            case"Friday":
                dayType = "工作日";
                break;
            case"Saturday":
            case"Sunday":
                dayType = "周末";
                break;
            default:
                dayType = "未知";
        }

        //With JDK17
        String dayType2 = switch (day) {
            case "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" -> "工作日";
            case "Saturday", "Sunday" -> "周末";
            default -> "未知";
        };
    }


    //Within JDK17, sealed定义Animal只能被Dog和Cat继承
    private static sealed class Animal permits Dog, Cat {}

    private final class Dog extends Animal{}

    private final class Cat extends Animal{}

    //With in JDK17, POJO can be very simple, like
    private record User(String name, int age) {}

    private class OrderService{

        //Before JDK17
        private String processOrder(Object order){
            if (order instanceof OnlineOrder) {
                OnlineOrder onlineOrder= (OnlineOrder) order;
                return"处理在线订单：" + onlineOrder.id();
            } else if (order instanceof OfflineOrder) {
                OfflineOrder offlineOrder= (OfflineOrder) order;
                return"处理线下订单：" + offlineOrder.orderId();
            } else {
                throw new IllegalArgumentException("未知订单类型");
            }
        }

        //With JDK17
        private String processOrderJDK17(Object order) {
            /*
            return switch (order) {
                case OnlineOrder onlineOrder -> "处理在线订单：" + onlineOrder.id();
                case OfflineOrder offlineOrder -> "处理线下订单：" + offlineOrder.orderId();
                default -> throw new IllegalArgumentException("未知订单类型");
            };*/
            return null;
        }
    }

    public record OnlineOrder(String id){
    }

    public record OfflineOrder(String orderId){}
}
