package com.inthecheesefactory.lab.designlibrary;

/**
 * Created by WU on 2017/3/28.
 */

public class FloatQueue {
    float[] a;
    private  float lastValue;
 /*对象数组，队列最多存储a.length-1个对象,浪费一个存储单元。这是因为如果将数组装满，则队列满和队列空的条件都是： rear=front
*为了便于判断，将队列满的条件改为：(rear+1)%a.length=front，这样便要浪费一个存储单元。
 *队列空的条件仍是： rear=front
*/

    int front; //队首下标
    int rear; //队尾下标

    public FloatQueue(int size) {
        a = new float[size];
        front = size - 1;
        rear = 0;
    }

    /**
     * 将一个对象追加到队列尾部
     *
     * @param obj 对象
     * @return 队列满时返回false, 否则返回true
     */
    public void enqueue(float obj) {
        if ((rear + 1) % a.length == front) {
            front = (front + 1) % a.length;
        }
        a[rear] = obj;
        lastValue = obj;
        rear = (rear + 1) % a.length;
        //return true;
    }

    /**
     * 队列头部的第一个对象出队
     *
     * @return 出队的对象，队列空时返回null
     */
    public float dequeue() {
        return lastValue;
    }

    /*获得临时数组*/
    public float[] getQueue() {
        float[] tmp = new float[a.length];

        if ((front - rear) == 1) {
            //float tmp[]= a[front: 99]+ a[0: rear];      
            for (int i = 0; i < 100 - front; i++) {
                tmp[i] = a[front + i];
            }
            for (int i = 0; i <= rear; i++) {
                tmp[i + 100 - front] = a[i];
            }

            return tmp;
        } else if ((front - rear) == -99) {
            return a;
        } else {
            for (int i = 0; i <= rear; i++) {
                tmp[i] = a[i];
            }
            return tmp;
        }
    }
}