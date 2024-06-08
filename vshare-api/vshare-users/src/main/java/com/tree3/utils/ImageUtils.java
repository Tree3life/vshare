package com.tree3.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * 随机选择头像
 * </p>
 * <a>@Author: Rupert</ a>
 * <p>创建时间: 2024/3/13 17:38 </p>
 */
public class ImageUtils {

    private static List<String> photos;

    static {
        photos = new ArrayList<>();
        photos.add("https://z3.ax1x.com/2021/07/19/W8HFW4.jpg");
        photos.add("https://z3.ax1x.com/2021/07/19/W8HiYF.jpg");
        photos.add("https://z3.ax1x.com/2021/07/19/W8HPFU.jpg");
        photos.add("https://z3.ax1x.com/2021/07/19/W8HEl9.jpg");
        photos.add("https://z3.ax1x.com/2021/07/19/W8HASJ.jpg");
        photos.add("https://z3.ax1x.com/2021/07/19/W8HZO1.jpg");
        photos.add("https://z3.ax1x.com/2021/07/19/W8HVyR.jpg");
        photos.add(" https://z3.ax1x.com/2021/07/19/W8HpwV.jpg");
        photos.add(" https://z3.ax1x.com/2021/07/19/W8H9oT.jpg");

    }

    /**
     * 随机选择list中一个头像路径
     *
     * @return
     */
    public static String getPhoto() {
        //-2147483648  2147483647
        int i = new Random().nextInt(photos.size());
        return photos.get(i);
    }

}
