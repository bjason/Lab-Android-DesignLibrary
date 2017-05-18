package com.inthecheesefactory.lab.designlibrary.reference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WU on 2017/3/28.
 */

public class FloatQueue {
    private float[] a, y, z;
    private int dimension;
    private long[] time;
    private long initTime;
    private float[] lastValue;
    private SimpleDateFormat dateFormat;
    private boolean isEmpty;
    private String tag;
    private boolean isFull;
 /*对象数组，队列最多存储a.length-1个对象,浪费一个存储单元。这是因为如果将数组装满，则队列满和队列空的条件都是： rear=front
*为了便于判断，将队列满的条件改为：(rear+1)%a.length=front，这样便要浪费一个存储单元。
 *队列空的条件仍是： rear=front
*/

    private int front; //队首下标
    private int rear; //队尾下标

    public FloatQueue(int size) {
        this(size, 1);
    }

    public FloatQueue(int size, int dimension) {
        this(size, dimension, "null");
    }

    public FloatQueue(int size, int dimension, String tag) {
        this.dimension = dimension;
        this.tag = tag;
        a = new float[size];
        y = new float[size];
        z = new float[size];
        time = new long[size];
        initTime = System.currentTimeMillis();

        front = size - 1;
        rear = 0;
        dateFormat = new SimpleDateFormat("HH:mm:ss");
        lastValue = new float[3];
        isEmpty = true;
        isFull = false;
    }

    public String getTag() {
        return tag;
    }

    /**
     * 将一个对象追加到队列尾部
     *
     * @param obj 对象
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
        if (isFull || (rear + 1) % a.length == front) {
            front = (front + 1) % a.length;
            isFull = true;
        }
        lastValue[0] = a[rear] = obj;
        //Log.d("en", "enQueue: " + lastValue[0] + " " + a[rear]);
        lastValue[1] = y[rear] = objy;
        lastValue[2] = z[rear] = objz;
        time[rear] = System.currentTimeMillis() - initTime;
        rear = (rear + 1) % a.length;
        isEmpty = false;
    }

    public void enQueue(float[] obj) {
        switch (obj.length) {
            case 1:
                enQueue(obj[0]);
                break;
            case 2:
                enQueue(obj[0], obj[1]);
                break;
            case 3: {
                dimension = 3;
                enQueue(obj[0], obj[1], obj[2]);
                break;
            }
        }
    }

    public boolean isEmpty() {
        return isEmpty;
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
    //出队 最先len个元素
    public float[][] deQueue(int dimension, int len) {
        float[][] rawData = new float[3][len];
        List<float[]> res = getQueue(3);
        /*Log.d("fullLenQueue", "deQueue: " + Arrays.toString(res.get(0))
            + "\n" + deQueue());*/

        for (int i = 0; i < dimension; i++) {
            float[] oneDimen = res.get(i);
            //Log.d("dequene", "deQueue: " + Arrays.toString(oneDimen));

            for (int j = 0, index = getCurrentLen() - 1; j < 10; j++, index--) {
                rawData[i][j] = oneDimen[index];
            }
        }
        //Log.d("fullLenQueue", Arrays.toString(rawData[0]));

        return rawData;
    }

    public float[] deQueue(int dimension) {
        float[] tmp = new float[dimension];
        System.arraycopy(lastValue, 0, tmp, 0, dimension);
        return tmp;
    }

    public float deQueue() {
        return lastValue[0];
    }

    /*获得临时数组*/
    public float[] getQueue() {

        if (isFull()) {
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
            System.arraycopy(a, 0, tmp, 0, rear);
            return tmp;
        }
    }

    public int getCurrentLen() {
        if (isFull) {
            return getLen();
        } else
            return rear;
    }

    public boolean isFull() {
        if ((front - rear) == 1)
            return true;
        else return false;
    }

    public List<float[]> getQueue(int dimension) {
        List<float[]> res = new ArrayList<>(dimension);

        //满队
        if (isFull()) {
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
            //
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

    public long[] getTime() {
        if ((front - rear) == 1) {
            long[] tmp = new long[time.length];

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
            long[] tmp = new long[rear];
            System.arraycopy(time, 0, tmp, 0, rear);
            return tmp;
        }
    }
}