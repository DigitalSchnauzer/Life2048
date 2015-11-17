package schnauzer.digital.life2048;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

/**
 * Created by Rogelio on 11/5/2015.
 */
public class Adapter extends BaseAdapter {
    Context context;
    Integer[] m;

    public Adapter(Context context) {
        this.context = context;
    }

    public Adapter(Integer[] x) {
        m = x;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FrameLayout defaultFrame = new FrameLayout(context);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        params.setMargins(5, 5, 5, 5);
        defaultFrame.setLayoutParams(params);
        defaultFrame.setBackgroundColor(0xFFFFECC6);

        return defaultFrame;
    }
}
