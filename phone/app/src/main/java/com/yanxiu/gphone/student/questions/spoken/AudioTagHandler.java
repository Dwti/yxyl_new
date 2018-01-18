package com.yanxiu.gphone.student.questions.spoken;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import com.yanxiu.gphone.student.customviews.SpokenAnimDrawable;
import com.yanxiu.gphone.student.util.ScreenUtils;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;

import java.lang.reflect.Field;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/23 15:04.
 * Function :
 */
public class AudioTagHandler implements Html.TagHandler {

    private Context mContext;
    private View mView;
    private ClickableImageSpan.onSpanClickListener mSpanClickListener;
    private ClickableImageSpan mImageSpan;

    public AudioTagHandler(Context context,View view,ClickableImageSpan.onSpanClickListener spanClickListener){
        this.mContext=context;
        this.mView=view;
        this.mSpanClickListener=spanClickListener;
    }

    public void start(){
        if (mImageSpan!=null){
            mImageSpan.start();
        }
    }

    public void stop(){
        if (mImageSpan!=null){
            try {
                mImageSpan.stop();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equalsIgnoreCase("audio")){
            if(opening){
                String url="";
                try {
                    Field elementField = xmlReader.getClass().getDeclaredField("theNewElement");
                    elementField.setAccessible(true);
                    Object element = elementField.get(xmlReader);
                    Field attsField = element.getClass().getDeclaredField("theAtts");
                    attsField.setAccessible(true);
                    Object atts = attsField.get(element);
                    Attributes attributes= (Attributes) atts;
                    url= attributes.getValue("", "src");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int len = output.length();
                output.append("\uFFFC");
                SpokenAnimDrawable drawable= new SpokenAnimDrawable(mContext);
                int px= (int) ScreenUtils.dpToPx(mContext,34);
                drawable.setBounds(0,0,px,px);
                drawable.setView(mView);
                mImageSpan=new ClickableImageSpan(drawable,url,mSpanClickListener);
                output.setSpan(mImageSpan,len,output.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

}
