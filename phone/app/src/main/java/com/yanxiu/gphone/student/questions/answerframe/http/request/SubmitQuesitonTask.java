package com.yanxiu.gphone.student.questions.answerframe.http.request;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.test.yanxiu.network.HttpCallback;
import com.test.yanxiu.network.RequestBase;
import com.yanxiu.gphone.student.constant.Constants;
import com.yanxiu.gphone.student.db.SaveAnswerDBHelper;
import com.yanxiu.gphone.student.db.UrlRepository;
import com.yanxiu.gphone.student.homework.response.PaperResponse;
import com.yanxiu.gphone.student.http.request.UpDataRequest;
import com.yanxiu.gphone.student.http.request.UpDataRequest.onUpDatalistener;
import com.yanxiu.gphone.student.questions.answerframe.bean.AnswerBean;
import com.yanxiu.gphone.student.questions.answerframe.bean.BaseQuestion;
import com.yanxiu.gphone.student.questions.answerframe.bean.Paper;
import com.yanxiu.gphone.student.questions.answerframe.bean.SubjectiveUpLoadImgBean;
import com.yanxiu.gphone.student.questions.answerframe.listener.SubmitAnswerCallback;
import com.yanxiu.gphone.student.questions.answerframe.ui.activity.AnswerQuestionActivity;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionShowType;
import com.yanxiu.gphone.student.questions.answerframe.util.QuestionTemplate;
import com.yanxiu.gphone.student.questions.subjective.SubjectiveQuestion;
import com.yanxiu.gphone.student.util.DataFetcher;
import com.yanxiu.gphone.student.util.LoginInfo;
import com.yanxiu.gphone.student.util.ToastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dyf on 2017/6/23.
 */
public class SubmitQuesitonTask extends AsyncTask {

    private interface OnPictureUploadFinish {
        void onFinish();
    }

    private final String TAG = "submit";
    //    "ppstatus" : 0         状态     0 未作答， 1 未完成， 2 已完成
    public static final int SUBMIT_CODE = 2;
    public static final int LIVE_CODE = 1;
    private SubmitAnswerCallback mCallBack;
    private Paper paper;
    private int status;
    private String ppid;
    private int mTotalCount;
    private SubmitAnswerRequest request;
    private boolean isError = false;
    private Handler mHandler = new Handler();

    public SubmitQuesitonTask(Context context, Paper paper, int status, SubmitAnswerCallback callBack) {
        mCallBack = callBack;
        this.paper = paper;
        this.status = status;
        ppid = paper.getId();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        //上传图片
        final ArrayList<ArrayList<SubjectiveUpLoadImgBean>> imgList = getSubjecttiveImgAnswer();
        if (null != imgList && imgList.size() > 0) {
//            final int totalCount = imgList.size() + 1;//上传总的数量= 图片数量 + 1（最后上传答案的请求算作一个计数）；
            startUpload(imgList, 0);
        } else { //没有图片，直接提交答案
            requestSumbmit();
        }
        return null;
    }

    private void startUpload(final ArrayList<ArrayList<SubjectiveUpLoadImgBean>> imgList, final int index) {
        if (index < mTotalCount) {
            uploadPicture(imgList.get(index), index + 1, new OnPictureUploadFinish() {
                @Override
                public void onFinish() {
                    startUpload(imgList, index + 1);
                }
            });
        }
    }

    private void uploadPicture(final ArrayList<SubjectiveUpLoadImgBean> imgList, final int nowIndex, final OnPictureUploadFinish uploadFinish) {
        UpDataRequest.getInstense().setConstantParams(new UpDataRequest.findConstantParams() {
            @NonNull
            @Override
            public String findUpdataUrl() {
                return UrlRepository.getInstance().getServer() + "/common/uploadImgs.do?";
            }

            @Override
            public int findFileNumber() {
                return imgList.size();
            }

            @Nullable
            @Override
            public Map<String, String> findParams() {
                return null;
            }
        }).setImgPath(new UpDataRequest.findImgPath() {
            @NonNull
            @Override
            public String getImgPath(int position) {
                return imgList.get(position).getPath();
            }
        }).setTag(new UpDataRequest.findImgTag() {
            @Nullable
            @Override
            public Object getImgTag(int position) {
                return imgList.get(position);
            }
        }).setProgressListener(new UpDataRequest.onProgressListener() {
            @Override
            public void onRequestStart() {
                isError = false;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "开始上传");
                        mCallBack.onUpdate(mTotalCount, nowIndex - 1);
                    }
                });
            }

            @Override
            public void onProgress(final int index, final int position) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.onUpdate(mTotalCount, nowIndex);
                    }
                });
            }

            @Override
            public void onRequestEnd() {
                //回调给UI线程
                Log.e(TAG, "结束上传");
                if (isError) {
                    Log.e(TAG, "上传失败");
                    mCallBack.onFail();
                } else {
                    Log.e(TAG, "上传成功");
                    uploadFinish.onFinish();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (nowIndex == mTotalCount) {
                                requestSumbmit(); //图片传完了，提交答案
                            }
                        }
                    });
                }
            }
        }).setListener(new onUpDatalistener() {
            @Override
            public void onUpDataStart(int position, Object tag) {
//                    Log.e(TAG, "onUpDataStart: " + position);
            }

            @Override
            public void onUpDataSuccess(final int position, final Object tag, final String jsonString) {
                //这需要保存服务服端返回的url
                String imgUrl = parseJson(jsonString);
                Log.e(TAG, "imgUrl   " + imgUrl);
                saveAnswer((SubjectiveUpLoadImgBean) tag, imgUrl);
            }

            @Override
            public void onUpDataFailed(int position, Object tag, String failMsg) {
                Log.e(TAG, "onUpDataFailed   " + failMsg);
                isError = true;
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mCallBack.onFail();
//                    }
//                });
            }

            @Override
            public void onError(String errorMsg) {
                Log.e(TAG, "onError" + errorMsg);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.onFail();
                    }
                });
            }
        });
    }

    private void requestSumbmit() {
        if (request != null) {
            request.cancelRequest();
            request = null;
        }
        request = new SubmitAnswerRequest(initParams_Answer(), ppid);
        request.startRequest(PaperResponse.class, new HttpCallback<PaperResponse>() {
            @Override
            public void onSuccess(RequestBase request, final PaperResponse ret) {
                if (ret.getStatus().getCode() == 0) {
                    Log.e(TAG, "提交成功");
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallBack.onSuccess();
                        }
                    });
                } else {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallBack.onDataError(ret.getStatus().getCode(), ret.getStatus().getDesc());
                        }
                    });
                }
            }

            @Override
            public void onFail(RequestBase request, Error error) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallBack.onFail();
                    }
                });
            }
        });
    }

    /**
     * 获取主观题图片路径
     */
    private ArrayList<ArrayList<SubjectiveUpLoadImgBean>> getSubjecttiveImgAnswer() {
        ArrayList<ArrayList<SubjectiveUpLoadImgBean>> arrayLists = new ArrayList<>();
        if (paper == null)
            return null;
        ArrayList<BaseQuestion> questions = paper.getQuestions();
        int size = questions.size();
        if (questions == null || size < 1) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            BaseQuestion outQuestion = questions.get(i);
            if (outQuestion.getTemplate().equals(QuestionTemplate.READING) || outQuestion.getTemplate().equals(QuestionTemplate.CLOZE) || outQuestion.getTemplate().equals(QuestionTemplate.LISTEN)) { //复合题
                ArrayList<BaseQuestion> childQuestions = outQuestion.getChildren();
                int childSize = childQuestions.size();
                if (null != childQuestions && childSize >= 1) {
                    for (int j = 0; j < childSize; j++) {
                        BaseQuestion childQuestion = childQuestions.get(j);
                        if (QuestionTemplate.ANSWER.equals(childQuestion.getTemplate())) {
                            ArrayList<SubjectiveUpLoadImgBean> imgList = new ArrayList<>();
                            boolean isShouldSetTotalCount = true;
                            Object answer = childQuestion.getAnswer();
                            ArrayList<String> answerList = null;
                            if (null != answer) {
                                answerList = (ArrayList) answer;
                                if (answerList != null && answerList.size() > 0) {
                                    for (int k = 0; k < answerList.size(); k++) {
                                        if (isShouldSetTotalCount) {
                                            mTotalCount++;
                                            isShouldSetTotalCount = false;
                                        }
                                        String path = answerList.get(k);
                                        if (!TextUtils.isEmpty(path) && !path.startsWith("http")) {
                                            SubjectiveUpLoadImgBean bean = new SubjectiveUpLoadImgBean();
                                            bean.setLevelPositions(childQuestion.getLevelPositions());
                                            bean.setPath(path);
                                            imgList.add(bean);
                                        }
                                    }
                                }
                                if (!isShouldSetTotalCount) {
                                    arrayLists.add(imgList);
                                }
                            }
                        }
                    }
                }

            } else if (QuestionTemplate.ANSWER.equals(outQuestion.getTemplate())) {
                Object answer = outQuestion.getAnswer();
                ArrayList<String> answerList = null;
                boolean isShouldSetTotalCount = true;
                if (null != answer) {
                    answerList = (ArrayList) answer;
                    if (answerList != null && answerList.size() > 0) {
                        ArrayList<SubjectiveUpLoadImgBean> imgList = new ArrayList<>();
                        for (int k = 0; k < answerList.size(); k++) {
                            String path = answerList.get(k);
                            if (!TextUtils.isEmpty(path) && !path.startsWith("http")) {
                                if (isShouldSetTotalCount) {
                                    mTotalCount++;
                                    isShouldSetTotalCount = false;
                                }
                                SubjectiveUpLoadImgBean bean = new SubjectiveUpLoadImgBean();
                                bean.setLevelPositions(outQuestion.getLevelPositions());
                                bean.setPath(path);
                                imgList.add(bean);
                            }
                        }
                        if (!isShouldSetTotalCount) {
                            arrayLists.add(imgList);
                        }
                    }
                }
            }
        }
        return arrayLists;
    }

    /**
     * 生成answer参数
     */
    private String initParams_Answer() {
        JSONObject node = new JSONObject();
        String id = "-1";
        String childId = "-1";
        String paperTestId = "-1";
        try {
            node.put("chapterId", paper.getChapterid());
            node.put("ptype", paper.getPtype());
            int count = paper.getQuestions().size();

            JSONArray paperDetailsArray = new JSONArray(); //paperDetails
            for (int i = 0; i < count; i++) { //大题循环

                JSONObject outQuestionObject = new JSONObject(); //封装每个大题数据
                BaseQuestion outerQuestionBean = paper.getQuestions().get(i);//大题数据

                if (outerQuestionBean.getTemplate().equals(QuestionTemplate.READING) || outerQuestionBean.getTemplate().equals(QuestionTemplate.CLOZE)
                        || outerQuestionBean.getTemplate().equals(QuestionTemplate.LISTEN)) { //是复合题

                    List<BaseQuestion> childQuestionList = outerQuestionBean.getChildren();//获得子题
                    if (childQuestionList == null || childQuestionList.isEmpty())
                        continue;
                    int childrenCount = childQuestionList.size();


                    JSONArray childrenArray = new JSONArray();//子题Array -- children

                    for (int j = 0; j < childrenCount; j++) { //小题循环
                        JSONObject childObject = new JSONObject();

                        BaseQuestion childQuestionBean = childQuestionList.get(j); //子题
                        Object childAnsewr = childQuestionBean.getAnswer(); //子题答案

                        if (childQuestionBean.getTemplate().equals(QuestionTemplate.CLASSIFY)) {
                            List<String> answerList = new ArrayList<>();
                            List<List<String>> listList = (List<List<String>>) childAnsewr;
                            for (List<String> stringList : listList) {
                                String answerString = "";
                                for (String s : stringList) {
                                    answerString += s + ",";
                                }
                                if (!TextUtils.isEmpty(answerString)) {
                                    answerString = answerString.substring(0, answerString.length() - 1);
                                }
                                answerList.add(answerString);
                            }
                            childAnsewr = answerList;
                        }

                        Gson gson = new Gson();
                        String answerJson = "";
                        if (null != childAnsewr) {
                            answerJson = gson.toJson(childAnsewr);//转化成json
                        }
                        JSONArray answers = new JSONArray(answerJson);
                        childObject.put("answer", answers);

                        if (childQuestionBean.getPad() != null && !TextUtils.isEmpty(childQuestionBean.getPad().getId())) {
                            childId = String.valueOf(childQuestionBean.getPad().getId());
                        } else {
                            childId = "-1";
                        }
                        childObject.put("id", childId);
                        childObject.put("qid", childQuestionBean.getQid());
                        //childJson.put("qtype", paper.getPaperTest().get(i).getQuestions().getChildren().get(j));
                        childObject.put("costtime", childQuestionBean.getCosttime());
                        childObject.put("ptid", childQuestionBean.getId());
                        childObject.put("status", childQuestionBean.getStatus());
                        childObject.put("uid", LoginInfo.getUID());


                        childrenArray.put(childObject);

                    }
                    outQuestionObject.put("children", childrenArray);

                } else if (!TextUtils.isEmpty(outerQuestionBean.getTypeId_complexToSimple())) { //只有一个子题的复合题
                    JSONArray childrenArray = new JSONArray();//子题Array -- children

//                    for (int j = 0; j < childrenCount; j++) { //小题循环
                    JSONObject childObject = new JSONObject();

                    BaseQuestion childQuestionBean = outerQuestionBean; //子题就是本身
                    Object childAnsewr = childQuestionBean.getAnswer(); //子题答案

                    if (childQuestionBean.getTemplate().equals(QuestionTemplate.CLASSIFY)) {
                        List<String> answerList = new ArrayList<>();
                        List<List<String>> listList = (List<List<String>>) childAnsewr;
                        for (List<String> stringList : listList) {
                            String answerString = "";
                            for (String s : stringList) {
                                answerString += s + ",";
                            }
                            if (!TextUtils.isEmpty(answerString)) {
                                answerString = answerString.substring(0, answerString.length() - 1);
                            }
                            answerList.add(answerString);
                        }
                        childAnsewr = answerList;
                    }

                    Gson gson = new Gson();
                    String answerJson = "";
                    if (null != childAnsewr) {
                        answerJson = gson.toJson(childAnsewr);//转化成json
                    }
                    JSONArray answers = new JSONArray(answerJson);
                    childObject.put("answer", answers);

                    if (childQuestionBean.getPad() != null && !TextUtils.isEmpty(childQuestionBean.getPad().getId())) {
                        childId = String.valueOf(childQuestionBean.getPad().getId());
                    } else {
                        childId = "-1";
                    }
                    childObject.put("id", childId);
                    childObject.put("qid", childQuestionBean.getQid());
                    //childJson.put("qtype", paper.getPaperTest().get(i).getQuestions().getChildren().get(j));
                    childObject.put("costtime", childQuestionBean.getCosttime());
                    childObject.put("ptid", childQuestionBean.getId());
                    childObject.put("status", childQuestionBean.getStatus());
                    childObject.put("uid", LoginInfo.getUID());


                    childrenArray.put(childObject);

//                    }
                    outQuestionObject.put("children", childrenArray);

                } else { //单题
                    Object ansewr = outerQuestionBean.getAnswer(); //答案

                    if (outerQuestionBean.getTemplate().equals(QuestionTemplate.CLASSIFY)) {
                        List<String> answerList = new ArrayList<>();
                        List<List<String>> listList = (List<List<String>>) ansewr;
                        for (List<String> stringList : listList) {
                            String answerString = "";
                            for (String s : stringList) {
                                answerString += s + ",";
                            }
                            if (!TextUtils.isEmpty(answerString)) {
                                answerString = answerString.substring(0, answerString.length() - 1);
                            }
                            answerList.add(answerString);
                        }
                        ansewr = answerList;
                    }

                    String answerJson = "";
                    Gson gson = new Gson();
                    if (null != ansewr) {
                        answerJson = gson.toJson(ansewr);//转化成json
                    }

                    JSONArray answers = new JSONArray(answerJson);
                    outQuestionObject.put("answer", answers);
                    outQuestionObject.put("children", "");
                }

                String ptid, qid;
                if (!TextUtils.isEmpty(outerQuestionBean.getTypeId_complexToSimple())) { //只有一个子题的复合题
                    ptid = outerQuestionBean.getPtid_ComplexToSimple();
                    qid = outerQuestionBean.getQid_ComplexToSimple();
                    if (!TextUtils.isEmpty(outerQuestionBean.getPadId_ComplexToSimple())) {
                        id = outerQuestionBean.getPadId_ComplexToSimple();
                    } else {
                        id = "-1";
                    }
                } else {
                    ptid = outerQuestionBean.getId();
                    qid = outerQuestionBean.getQid();
                    if (outerQuestionBean.getPad() != null && !TextUtils.isEmpty(outerQuestionBean.getPad().getId())) {
                        id = outerQuestionBean.getPad().getId();
                    } else {
                        id = "-1";
                    }
                }
                outQuestionObject.put("costtime", outerQuestionBean.getCosttime());
                outQuestionObject.put("ptid", ptid);
                outQuestionObject.put("qid", qid);
                outQuestionObject.put("id", id);
//                outQuestionObject.put("answer", "");
                outQuestionObject.put("status", outerQuestionBean.getStatus());
                outQuestionObject.put("uid", LoginInfo.getUID());

                paperDetailsArray.put(outQuestionObject);


            }
            node.put("paperDetails", paperDetailsArray);

            JSONObject paperStatusNode = new JSONObject();
            paperStatusNode.put("begintime", paper.getPaperStatus().getBegintime());
            paperStatusNode.put("endtime", paper.getPaperStatus().getEndtime());
            paperStatusNode.put("costtime", paper.getPaperStatus().getCosttime());
            paperStatusNode.put("ppid", paper.getId());
            if (paper.getPaperStatus() != null && !TextUtils.isEmpty(paper.getPaperStatus().getId())) {
                paperTestId = paper.getPaperStatus().getId();
                paperStatusNode.put("id", paperTestId);
            } else {
                paperStatusNode.put("id", paperTestId);
            }
            ////////这两个属性写死
            paperStatusNode.put("status", status);
            paperStatusNode.put("tid", 0);
            //////
            paperStatusNode.put("uid", LoginInfo.getUID());

            node.put("paperStatus", paperStatusNode);
            Log.d("dyf", "initParams_Answer: " + node.toString());
            return node.toString();
//            params.putString("answers", URLEncoder.encode(node.toString(), "UTF-8"));
        } catch (JSONException e) {
            e.printStackTrace();
            if (e != null)
                Log.e("dyf", "requestsubmitQ JSONException =   " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            if (e != null)
                Log.e("dyf", "requestsubmitQ Exception =   " + e.getMessage());
        }
        return "";
    }

    private String parseJson(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            if (null != jsonObject) {
                JSONArray pathArray = jsonObject.optJSONArray("data");
                if (pathArray != null && pathArray.length() > 0) {
                    String path = pathArray.getString(0);
                    if (!TextUtils.isEmpty(path)) {
                        return path;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存服务端返回的url
     */
    public void saveAnswer(SubjectiveUpLoadImgBean bean, String imgUrl) {
        ArrayList<Integer> LevelPositions = bean.getLevelPositions();//获取当前节点（题号）数据，通过节点可以判断出处在ArrayList的位置
        String localPath = bean.getPath();
        SubjectiveQuestion subjectQuestion;
        if (LevelPositions == null || LevelPositions.size() < 1)
            return;
        int outIndex = -1;//大题index
        int innerIndex = -1;//小题index
        ArrayList<BaseQuestion> quesitonList = paper.getQuestions();//获取试题数据
        if (LevelPositions.size() == 1) { //单题型
            outIndex = LevelPositions.get(0);
        } else if (LevelPositions.size() == 2) { //复合题
            outIndex = LevelPositions.get(0);
            innerIndex = LevelPositions.get(1);
        } else {
            return;
        }
        BaseQuestion outQuestion = quesitonList.get(outIndex);//大题数据
        BaseQuestion innerQuestion;//小题数据
        ArrayList<BaseQuestion> childrenQusetion;//小题集合
        if (innerIndex == -1) { //单题型
            subjectQuestion = (SubjectiveQuestion) outQuestion;
        } else { //复合题,需要取到当前小题
            childrenQusetion = outQuestion.getChildren();
            if (childrenQusetion == null || childrenQusetion.size() < 1) {
                //出错，既然是复合题，必须有小题
                return;
            }
            innerQuestion = childrenQusetion.get(innerIndex);
            subjectQuestion = (SubjectiveQuestion) innerQuestion;
        }

        //保存逻辑
        if (subjectQuestion.answerList.contains(localPath)) {
            int index = subjectQuestion.answerList.indexOf(localPath);
            subjectQuestion.answerList.set(index, imgUrl);
            Log.e(TAG, "保存url" + imgUrl);
        }

    }


    @Override
    protected void onCancelled() {
        if (request != null) {
            request.cancelRequest();
        }
//        if(mHandler != null){
//            mHandler = null;
//        }
        super.onCancelled();
    }
}
