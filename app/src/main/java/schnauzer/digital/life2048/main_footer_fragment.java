package schnauzer.digital.life2048;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link main_footer_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link main_footer_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class main_footer_fragment extends Fragment {

    public main_footer_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_footer_fragment, container, false);
    }

}
