package com.example.nakao.sanontest;

/**
 * Created by nakao on 2017/09/23.
 */

public class SanonJudge {

    int level=3;
    double Lo=200,Hi=240;

    int[] caluculate(short[] buf){

        int t=0;
        int n,i;//ループ用
        short calc[] =new short[buf.length+1];
        int zcl;//ゼロクロス
        int tmp;
        int epow = 0;
        int[] zc_Level =new int[buf.length];
        double zc_Level_mem=0;
        double alpha=0.0000005;
        int tau=0;//減衰時間管理
        double epow1=0.5;

        for (n = 0; n < buf.length; n++){

            t=(t+1)%buf.length;

            calc[t]= (short) (buf[t]-buf[(t-1+buf.length)%buf.length]);

            zcl=0;

            for(i=0;i<512;i++){
                tmp=calc[(t-i+buf.length)%buf.length]*calc[(t-i-1+buf.length)%buf.length]; // 1つ前の信号と乗算
                if(tmp<0)zcl++; // 異符号ならゼロクロスなのでzclを+1
                epow=epow+calc[(t-i+buf.length)%buf.length]*calc[(t-i+buf.length)%buf.length]; // 入力パワー計算
            }

            zc_Level[n]=0;

            zc_Level_mem=zc_Level_mem*Math.exp(-alpha*tau);// レベルメータ値の減衰(指数関数)
            tau=tau+1;

            if(tau>=10000)tau=10000;

            zc_Level[n]=(int)zc_Level_mem;// レベルメータ値の整数化

            if(epow>epow1){                                     // ノイズ対策．信号パワーが大なら反応する。epow1の値は右下のボタンで調整
                if(zcl>Lo){                                     // zclが下限値より大きければサ音判定
                    zc_Level[n]=(int)((zcl-Lo)/(Hi-Lo)*10.0+1.0);         // サ音レベルを決定
                    if(zc_Level[n]>10)zc_Level[n]=10;                 // 最大値を10とする
                    zc_Level_mem=zc_Level[n];                      // レベルメータ値の更新
                    tau=0;                                      // レベルメータ値減衰時間のリセット
                }
            }

        }



        return zc_Level;

    }

    void setLevel(int number){

        level=number;

        switch (level){
            case 1:
                Lo=200;
                Hi=240;
                break;
            case 2:
                Lo=200;
                Hi=240;
                break;
            case 3:
                Lo=200;
                Hi=240;
                break;
            case 4:
                Lo=200;
                Hi=240;
                break;
            case 5:
                Lo=200;
                Hi=240;
                break;
            case 6:
                Lo=200;
                Hi=240;
                break;
        }
    }
}
