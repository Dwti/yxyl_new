package com.yanxiu.gphone.student.questions.spoken;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Canghaixiao.
 * Time : 2017/10/19 15:47.
 * Function :
 */
public class SpokenLinkMovementMethod extends LinkMovementMethod {

    private static SpokenLinkMovementMethod sInstance;

    public static SpokenLinkMovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new SpokenLinkMovementMethod();

        return sInstance;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableImageSpan[] link = buffer.getSpans(off, off, ClickableImageSpan.class);

            if (link.length != 0) {
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        link[0].up();
                        link[0].onClick(widget);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        link[0].down();
                        Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                        break;
                }
                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }

}
