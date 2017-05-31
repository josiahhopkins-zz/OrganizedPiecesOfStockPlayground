package com.example.josiah.organizedpiecesofstockplayground;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.josiah.organizedpiecesofstockplayground.UtilityClasses.Group;
import com.example.josiah.organizedpiecesofstockplayground.UtilityClasses.Portfolio;
import com.example.josiah.organizedpiecesofstockplayground.UtilityClasses.UploadTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JoinGroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JoinGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JoinGroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String JOIN_GROUP_URL = "http://cssgate.insttech.washington.edu/~josiah3/PHP_Code/PHP%20Code/addRow.php?cmd=userJoinGroup";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public JoinGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JoinGroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JoinGroupFragment newInstance(String param1, String param2) {
        JoinGroupFragment fragment = new JoinGroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_join_group, container, false);
        MainActivity ourActivity = ((MainActivity) this.getActivity());
        ((TextView) view.findViewById(R.id.join_group_name)).setText(ourActivity.getCurrentGroup().getMyOwnerUsername());
        ((TextView) view.findViewById(R.id.join_group_owner)).setText(ourActivity.getCurrentGroup().getMyName());
        ((TextView) view.findViewById(R.id.join_group_portfolio_name)).setText(ourActivity.getCurrentGroup().getMyName() + "Portfolio");
        Button b = (Button) view.findViewById(R.id.join_group_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group g = ((MainActivity) getActivity()).getCurrentGroup();

                String portfolioName = ((EditText) getActivity().findViewById(R.id.join_group_portfolio_name)).getText().toString();
                String username = ((MainActivity) getActivity()).getCurrentUser().getMyUsername();
                double value = ((MainActivity) getActivity()).getCurrentGroup().getPortfolioValue();
                Log.e("Adding username:", username);
                g.addMember(username, portfolioName, value);
                ((MainActivity)getActivity()).setCurrentGroup(g);
                UploadTask uploadTask = new UploadTask();
                uploadTask.execute(new String[]{buildURL()});

            }
        });
        return view;
    }

    public String buildURL(){
        MainActivity mainActivity = ((MainActivity) getActivity());
        String toReturn = JOIN_GROUP_URL;
        toReturn = toReturn + "&group_name=" + ((MainActivity) getActivity()).getCurrentGroup().getMyName();
        toReturn = toReturn + "&portfolio_value=" + Double.toString(mainActivity.getCurrentGroup().getPortfolioValue());
        toReturn = toReturn + "&username=" + ((MainActivity) getActivity()).getCurrentUser().getMyUsername();
        toReturn = toReturn + "&portfolio_name=" + ((EditText) getActivity().findViewById(R.id.join_group_portfolio_name)).getText().toString();
        toReturn = toReturn + "&default_value=" + ((MainActivity) getActivity()).getCurrentGroup().getPortfolioValue();
        toReturn = toReturn + "&owner=" +((MainActivity) getActivity()).getCurrentGroup().getMyOwnerUsername();
        toReturn = toReturn.replace(" ", "%20");
        toReturn = toReturn.replace(".", "%2E");

        Log.e("URL: ", toReturn);
        return toReturn;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void JoinGroupInteraction();
    }

    public String buildJoinGroupURL(){
        return "";
    }
}
