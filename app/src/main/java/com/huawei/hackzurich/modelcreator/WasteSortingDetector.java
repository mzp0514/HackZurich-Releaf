package com.huawei.hackzurich.modelcreator;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.huawei.hiai.modelcreatorsdk.imageclassifier.ImageClassifier;
import com.huawei.hiai.modelcreatorsdk.imageclassifier.ImageConst;
import com.huawei.hiai.modelcreatorsdk.imageclassifier.tool.ImageTool;
//import com.huawei.agconnect.config.AGConnectServicesConfig;
//import com.huawei.hmf.tasks.Task;
//import com.huawei.hms.mlsdk.common.MLApplication;
//import com.huawei.hms.mlsdk.custom.MLCustomRemoteModel;
//import com.huawei.hms.mlsdk.model.download.MLLocalModelManager;
//import com.huawei.hms.mlsdk.model.download.MLModelDownloadListener;
//import com.huawei.hms.mlsdk.model.download.MLModelDownloadStrategy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class WasteSortingDetector {
    private static final String LOG_TAG = "hiai";
    //the model trained in hms toolkit
    private static final String MODEL_NAME = "image/wasteSorting.mc";
    private volatile ImageClassifier classifier;
    private Context context;

    public WasteSortingDetector(final Context context) throws Exception {
        this.context = context;

        loadModelFromAssets();
        //***** Recommend *****, please read annotation in loadModelFromHMSCloudHosting
        //loadModelFromHMSCloudHosting();
    }

    /**
     *  loadModelFromAssets
     */
    public void loadModelFromAssets() throws Exception {
        //context
        // MODEL_NAME the model  path in asset
        // 4 the thread num to run model
        classifier = new ImageClassifier(context, MODEL_NAME, 4);

        //// use InputStream to init ImageClassifier
        // InputStream inputStream = context.getAssets().open(MODEL_NAME);
        // classifier = new ImageClassifier(context, inputStream, 4);

        if(classifier.isSupportNpuPhone()) {
            String resFileName = "image_classifier_" + ImageTool.getDDkVersion() + "_" + classifier.getModelVersion() + "_base.mc";
            if(!this.containFileInAssets(resFileName)) {
                Log.e(LOG_TAG, "you need to get resFiles and put them to your app assets, please read annotation in codes");
                // you can get resFiles by "Generate Demo" function in Hms Toolkit >> Model Creator >> Image Classifier, you can find the resFiles in appDemo generated.
                // resFiles list:
                // image_classifier_ddk000_im_v1_base.mc
                // image_classifier_ddk150_im_v1_base.mc
                // image_classifier_ddk200_im_v1_base.mc
                // image_classifier_ddk210_im_v1_base.mc
                // image_classifier_ddk300_im_v1_base.mc
                Toast.makeText(context, "you need to get resFiles and put them to your app assets, please read annotation in codes", Toast.LENGTH_LONG).show();
                return;
            }
            InputStream inputStream = context.getAssets().open(resFileName);
            if(classifier.initFromResStream(ImageClassifier.NPU_MODE, inputStream)) {   //NPU_MODE only suport huawei android phone with NPU
                Log.d(LOG_TAG, "model npu from local assets init ok");
            } else {
                Log.d(LOG_TAG, "model npu from local assets init fail");
            }
        } else {
            String resFileName = "image_classifier_" + ImageConst.DDK000 + "_" + classifier.getModelVersion() + "_base.mc";
            if(!this.containFileInAssets(resFileName)) {
                Log.e(LOG_TAG, "you need to get resFiles and put them to your app assets, please read annotation in codes");
                // you can get resFiles by "Generate Demo" function in Hms Toolkit >> Model Creator >> Image Classifier, you can find the resFiles in appDemo generated.
                // resFiles list:
                // image_classifier_ddk000_im_v1_base.mc
                // image_classifier_ddk150_im_v1_base.mc
                // image_classifier_ddk200_im_v1_base.mc
                // image_classifier_ddk210_im_v1_base.mc
                // image_classifier_ddk300_im_v1_base.mc
                Toast.makeText(context, "you need to get resFiles and put them to your app assets, please read annotation in codes", Toast.LENGTH_LONG).show();
                return;
            }
            InputStream inputStream = context.getAssets().open(resFileName); //for CPU_MODE
            if(classifier.initFromResStream(ImageClassifier.CPU_MODE, inputStream)) {   //NPU_MODE only suport huawei android phone with NPU
                Log.d(LOG_TAG, "model cpu from local assets init ok");
            } else {
                Log.d(LOG_TAG, "model cpu from local assets init fail");
            }
        }
    }

//    /**
//     *  Recommend using HMS cloud hosting service, you can upload the base resFiles and the model that you trained to HMS cloud hosting service, app can download your model and resFile to load them;
//     *  Please follow these steps:
//     *  1. https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/config-agc-0000001050990353
//     *  2. https://developer.huawei.com/consumer/cn/doc/development/HMSCore-Guides/hosting-on-the-cloud-0000001051824955
//     *
//     *  the resFiles that you need to upload to HMS cloud hosting:
//     *  image_classifier_ddk000_im_v1_base.mc
//     *  image_classifier_ddk150_im_v1_base.mc
//     *  image_classifier_ddk200_im_v1_base.mc
//     *  image_classifier_ddk210_im_v1_base.mc
//     *  image_classifier_ddk300_im_v1_base.mc
//     *  you can get resFiles by "Generate Demo" function in Hms Toolkit >> Model Creator >> Image Classifier, you can find the resFiles in appDemo generated.
//     */
//    public void loadModelFromHMSCloudHosting() throws Exception {
//        //init hms
//        AGConnectServicesConfig config = AGConnectServicesConfig.fromContext(this.context);
//        MLApplication.getInstance().setAccessToken(config.getString("client/api_key"));
//
//        //download mc model
//        final String remoteModelName = "model_name";  // to do, you need to set, your model name
//        final Task<Void> downloadMcTask = downloadFile(remoteModelName);
//        new Thread() {
//            @Override
//            public void run() {
//                while(!downloadMcTask.isComplete()) {
//                    Wastesorting3Detector.sleep(1000);
//                    Log.d(LOG_TAG, "wait download mc task isComplete");
//                }
//                try {
//                    File mcFile = MLLocalModelManager.getInstance().getSyncRecentModelFile(new MLCustomRemoteModel.Factory(remoteModelName).create());
//                    Log.d(LOG_TAG, "mc model path:" + mcFile.getAbsolutePath());
//                    Wastesorting3Detector.this.classifier = new ImageClassifier(context, new FileInputStream(mcFile), 4);
//                    Log.d(LOG_TAG, "load model hms success");
//
//                    //download resFile and Load resFile
//                    //NPU_MODE only suport huawei android phone with NPU, CPU_MODE can be run in almost All Android Phone
//                    final String resRemoteName = "image_classifier_" + ImageTool.getDDkVersion() + "_" + classifier.getModelVersion() + "_base";
//                    //final String resRemoteName = "image_classifier_" + ImageConst.DDK000 + "_" + classifier.getModelVersion() + "_base"; //CPU Mode res File
//                    final Task<Void> downloadResTask = Wastesorting3Detector.this.downloadFile(resRemoteName);
//                    final String runMode = classifier.isSupportNpuPhone() ? ImageClassifier.NPU_MODE : ImageClassifier.CPU_MODE;
//
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            while(!downloadResTask.isComplete()) {
//                                Wastesorting3Detector.sleep(1000);
//                                Log.d(LOG_TAG, "wait download res task isComplete");
//                            }
//                            try {
//                                File resFile = MLLocalModelManager.getInstance().getSyncRecentModelFile(new MLCustomRemoteModel.Factory(resRemoteName).create());
//                                if(classifier.initFromResStream(runMode, new FileInputStream(resFile))) {
//                                    Log.d(LOG_TAG, "init res ok");
//                                } else {
//                                    Log.d(LOG_TAG, "init res fail");
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
//    }
//
//    public Task<Void> downloadFile(String remoteName) {
//        final MLModelDownloadStrategy strategy = new MLModelDownloadStrategy
//                .Factory()
//                .needWifi()
//                .setRegion(MLModelDownloadStrategy.REGION_DR_CHINA) // set Region： supported：REGION_DR_CHINA，REGION_DR_AFILA，REGION_DR_EUROPE，REGION_DR_RUSSIA。
//                .create();
//        final MLCustomRemoteModel customRemoteModel = new MLCustomRemoteModel.Factory(remoteName).create();
//        final Task<Void> task = MLLocalModelManager.getInstance().downloadModel(customRemoteModel, strategy, new MLModelDownloadListener() {
//            @Override
//            public void onProcess(long alreadyDownLength, long totalLength) {
//                Log.d(LOG_TAG, "current:" + alreadyDownLength + " total:" + totalLength);
//                if (alreadyDownLength == totalLength) { //download complete
//                    Log.d(LOG_TAG, "model download success");
//                }
//            }
//        });
//        return task;
//    }


    public boolean isReady() {
        if(this.classifier == null) {
            return false;
        }
        return this.classifier.isInitOk();
    }

    public Prediction detect(Bitmap imageBitmap) {
        if(!isReady()) {
            Log.e(LOG_TAG, "the classifier is not init ok");
            Toast.makeText(this.context, "the classifier is not init ok", Toast.LENGTH_SHORT ).show();
            return null;
        }
        long start = System.currentTimeMillis();
        Map<String, Float> predictsMap = this.classifier.predict(imageBitmap);
        long costTime = System.currentTimeMillis() - start;

        if(predictsMap == null) {
            Log.e(LOG_TAG, "the classifier run error");
            return null;
        }
        String bestLabel = this.classifier.getBestLabel(predictsMap);
        Prediction ans = new Prediction();
        ans.label = bestLabel;
        ans.confidence = predictsMap.get(bestLabel);
        ans.costMs = costTime;
        Log.d(ans.label, "best label");
        Log.d(String.valueOf(ans.confidence), "confidence level");
        Log.d(String.valueOf(ans.costMs), "prediction time");
        return ans;
    }

    public void release() {
         this.classifier.release();
    }

    public static class Prediction {
        public String label;
        public float confidence;
        public long costMs;
    }

    private boolean containFileInAssets(String fileName) throws IOException {
        for(String tName : this.context.getAssets().list("")) {
            if(tName.equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    private static void sleep(int ts) {
        try {
            Thread.sleep(ts);
        } catch (InterruptedException e) {

        }
    }

}
