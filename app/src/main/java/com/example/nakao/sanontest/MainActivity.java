package com.example.nakao.sanontest;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {

    //オーディオレコード関連の変数

    final static int SAMPLING_RATE = 16000;
    AudioRecord audioRec = null;
    boolean bIsRecording=true;
    int bufSize;
    int MaxZC_Level=0;//さ音レベル最大値の保存
    int LEVEL;
    int noizeCanceling;
    int sanonJudge;

    boolean judge=true;

    Handler handler;

    TextView result;
    Button startButton;

    EditText levelText,noizeText,judgeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton=(Button)findViewById(R.id.startButton);
        levelText=(EditText)findViewById(R.id.level);
        noizeText=(EditText)findViewById(R.id.noize);
        judgeText=(EditText)findViewById(R.id.judge);


        result=(TextView)findViewById(R.id.result);


        handler=new Handler();
        bufSize=AudioRecord.getMinBufferSize(SAMPLING_RATE,
                AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT);




        bIsRecording = true;

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                judge=true;


                //設定が整数値で入力されているかの判定
                try{
                    noizeCanceling=Integer.parseInt(noizeText.getText().toString());
                    LEVEL=Integer.parseInt(levelText.getText().toString());
                    sanonJudge=Integer.parseInt(judgeText.getText().toString());

                }catch (Exception e){

                    result.setText("指定された設定が正しくありません\n設定を正しくしてボタンを押してください");
                    judge=false;
                }

                //レベルの範囲が守られているかの判定
                if(LEVEL<1||LEVEL>6){

                    result.setText("指定された設定が正しくありません\n設定を正しくしてボタンを押してください");
                    judge=false;

                }

                //判定レベル指定が守られているかの判定
                if(sanonJudge<1||sanonJudge>10){

                    result.setText("指定された設定が正しくありません\n設定を正しくしてボタンを押してください");
                    judge=false;

                }

                if(judge){

                    startButton.setText("録音中");
                    startButton.setEnabled(false);

                    bIsRecording=true;


                    // 録音スレッド
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            // AudioRecordの作成
                            audioRec = new AudioRecord(
                                    MediaRecorder.AudioSource.MIC,
                                    SAMPLING_RATE,
                                    AudioFormat.CHANNEL_CONFIGURATION_MONO,
                                    AudioFormat.ENCODING_PCM_16BIT,
                                    bufSize);


                            audioRec.startRecording();





                            while(bIsRecording){

                                short buf[] = new short[bufSize];

                                Log.d("bufsize",String.valueOf(bufSize));



                                double Biggest=0;




                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {

                                        result.setText("話しかけてください");

                                    }
                                });

                                // TODO Auto-generated method stub
                                // 録音データ読み込み
                                audioRec.read(buf, 0, buf.length);

                                Biggest=new UseArray().SilentJudge(buf);

                                Log.d("Biggest",String.valueOf(Biggest));





                                if(Biggest>noizeCanceling){ //最大値が設定したノイズキャンセリングレベルを超えたときのみ判定を行う

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            result.setText("解析中");

                                        }
                                    });


                                    MaxZC_Level=0;//さ音最大レベル初期化

                                    SanonJudge sanonjudge=new SanonJudge();
                                    int[] ZC_Level=new int[bufSize];

                                    sanonjudge.setLevel(LEVEL);
                                    ZC_Level=sanonjudge.caluculate(buf);

                                    MaxZC_Level=new UseArray().FindBiggest(ZC_Level);




                                    Log.d("最大レベル",String.valueOf(MaxZC_Level));

                                    if(MaxZC_Level>=sanonJudge){


                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                result.setText("「さ音」です");


                                            }
                                        });

                                    }else{

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                result.setText("「さ音」ではありません");

                                            }
                                        });

                                    }

                                    try {
                                        Thread.sleep(2000); //3000ミリ秒Sleepする
                                    } catch (InterruptedException e) {
                                    }

                                    bIsRecording=false;

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            result.setText("ボタンを押して録音を開始して下さい");
                                            startButton.setText("録音開始");
                                            startButton.setEnabled(true);

                                        }
                                    });

                                    audioRec.stop();
                                    audioRec.release();


                                }



                            }




                        }
                    }).start();


                }







            }
        });





    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        audioRec.release();
    }

}
