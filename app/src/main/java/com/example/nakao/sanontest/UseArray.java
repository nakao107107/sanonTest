package com.example.nakao.sanontest;

/**
 * Created by nakao on 2017/09/21.
 */

public class UseArray {

    double SilentJudge(short[] array){

        double Biggest = 0;//一番大きい数字格納用
        int i;

        for(i=0;i<array.length;i++){
            if(Math.abs((double)array[i])>Biggest){

                Biggest=Math.abs((double)array[i]);

            }
        }

        return Biggest;
    }

    int FindBiggest(int[] array){

        int Biggest = 0;//一番大きい数字格納用
        int i;

        for(i=0;i<array.length;i++){
            if(array[i]>Biggest){

                Biggest=array[i];

            }
        }

        return Biggest;
    }

    double MakeAverage(int[] array){

        int sum=0;
        int i;
        double average;

        for(i=0;i<array.length;i++){

            sum+=array[i];
        }

        average=sum/array.length;

        return average;


    }
}
