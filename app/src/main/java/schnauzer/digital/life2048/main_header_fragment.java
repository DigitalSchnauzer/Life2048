package schnauzer.digital.life2048;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link main_header_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link main_header_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class main_header_fragment extends Fragment {

    public main_header_fragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.main_header_fragment, container, false);
    }
}
