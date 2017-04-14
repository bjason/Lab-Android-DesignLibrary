package com.inthecheesefactory.lab.designlibrary.reference;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by WU on 2017/3/28.
 */

public class FloatQueue {
    float[] a, y, z;
    int dimension;
    String[] time;
    private float[] lastValue;
    SimpleDateFormat dateFormat;
    boolean isEmpty;
 /*对象数组，队列最多存储a.length-1个对象,浪费一个存储单元。这是因为如果将数组装满，则队列满和队列空的条件都是： rear=front
*为了便于判断，将队列满的条件改为：(rear+1)%a.length=front，这样便要浪费一个存储单元。
 *队列空的条件仍是： rear=front
*/

    int front; //队首下标
    int rear; //队尾下标

    public FloatQueue(int size) {
        this(size, 1);
    }

    public FloatQueue(int size, int dimension) {
        this.dimension = dimension;
        a = new float[size];
        y = new float[size];
        z = new float[size];
        time = new String[size];

        front = size - 1;
        rear = 0;
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        lastValue = new float[3];
        isEmpty = true;
    }

    /**
     * 将一个对象追加到队列尾部
     *
     * @param obj 对象
     * @return 队列满时返回false, 否则返回true
     */
    public void enQueue(float obj) {
        dimension = 1;
        enQueue(obj, 0, 0);
    }

    public void enQueue(float obj, float objy) {
        dimension = 2;
        enQueue(obj, objy, 0);
    }

    public void enQueue(float obj, float objy, float objz) {
        if ((rear + 1) % a.length == front) {
            front = (front + 1) % a.length;
        }
        lastValue[0] = a[rear] = obj;
        lastValue[1] = y[rear] = objy;
        lastValue[2] = z[rear] = objz;
        time[rear] = dateFormat.format(Calendar.getInstance().getTime());
        rear = (rear + 1) % a.length;
        isEmpty = false;
    }

    public void enQueue(float[] obj) {
        if (obj.length == 1)
            enQueue(obj[0]);
        if (obj.length == 2)
            enQueue(obj[0], obj[1]);
        if (obj.length == 3) {
            dimension = 3;
            enQueue(obj[0], obj[1], obj[2]);
        }
    }

    public int getLen() {
        return a.length;
    }

    public int getDimen() {
        return dimension;
    }

    /**
     * 队列头部的第一个对象出队
     *
     * @return 出队的对象，队列空时返回null
     */
    public float[] deQueue(int dimension) {
        float[] tmp = new float[dimension];
        for (int i = 0; i < dimension; i++) {
            tmp[i] = lastValue[i];
        }
        return tmp;
    }

    public float deQueue() {
        return lastValue[0];
    }

    /*获得临时数组*/
    public float[] getQueue() {

        if ((front - rear) == 1) {
            float[] tmp = new float[a.length];
            //float tmp[]= a[front: len-1]+ a[0: rear];
            for (int i = 0; i < a.length - front; i++) {
                tmp[i] = a[front + i];
            }
            for (int i = 0; i <= rear; i++) {
                tmp[i + a.length - front] = a[i];
            }
            return tmp;
        } else if ((front - rear) == -(a.length - 1)) {
            return a;
        } else {
            float[] tmp = new float[rear];
            for (int i = 0; i < rear; i++) {
                tmp[i] = a[i];
            }
            return tmp;
        }
    }

    public List<float[]> getQueue(int dimension) {
        List<float[]> res = new ArrayList<>(dimension);

        if ((front - rear) == 1) {
            float[] tmp = new float[a.length];
            float[] tmp2 = new float[a.length];
            float[] tmp3 = new float[a.length];

            for (int i = 0; i < a.length - front; i++) {
                tmp[i] = a[front + i];
                tmp2[i] = y[front + i];
                tmp3[i] = z[front + i];
            }
            for (int i = 0; i <= rear; i++) {
                tmp[i + a.length - front] = a[i];
                tmp2[i + a.length - front] = y[i];
                tmp3[i + a.length - front] = z[i];
            }
            res.add(tmp);
            if (dimension != 1)
                res.add(tmp2);
            if (dimension == 3)
                res.add(tmp3);
        } else if ((front - rear) == -(a.length - 1)) {
            res.add(a);
            if (dimension != 1)
                res.add(y);
            if (dimension == 3)
                res.add(z);
        } else {
            float[] tmp = new float[rear];
            float[] tmp2 = new float[rear];
            float[] tmp3 = new float[rear];
            for (int i = 0; i < rear; i++) {
                tmp[i] = a[i];
                tmp2[i] = y[i];
                tmp3[i] = z[i];
            }
            res.add(tmp);
            if (dimension != 1)
                res.add(tmp2);
            if (dimension == 3)
                res.add(tmp3);
        }
        return res;
    }

    public String[] getTime() {
        if ((front - rear) == 1) {
            String[] tmp = new String[time.length];

            //float tmp[]= a[front: len-1]+ a[0: rear];
            for (int i = 0; i < time.length - front; i++) {
                tmp[i] = time[front + i];
            }
            for (int i = 0; i <= rear; i++) {
                tmp[i + time.length - front] = time[i];
            }
            return tmp;
        } else if ((front - rear) == -(time.length - 1)) {
            return time;
        } else {
            String[] tmp = new String[rear];
            for (int i = 0; i < rear; i++) {
                tmp[i] = time[i];
            }
            return tmp;
        }
    }
}