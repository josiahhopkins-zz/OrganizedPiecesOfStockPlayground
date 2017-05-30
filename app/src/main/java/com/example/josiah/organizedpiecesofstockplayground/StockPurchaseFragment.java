package com.example.josiah.organizedpiecesofstockplayground;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.josiah.organizedpiecesofstockplayground.UtilityClasses.Portfolio;
import com.example.josiah.organizedpiecesofstockplayground.UtilityClasses.Stock;
import com.example.josiah.organizedpiecesofstockplayground.UtilityClasses.UploadTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StockPurchaseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StockPurchaseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StockPurchaseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String PURCHASE_URL ="http://cssgate.insttech.washington.edu/~josiah3/PHP_Code/PHP%20Code/addRow.php?cmd=stockPurchase";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public StockPurchaseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StockPurchaseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StockPurchaseFragment newInstance(String param1, String param2) {
        StockPurchaseFragment fragment = new StockPurchaseFragment();
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
        Stock s = ((MainActivity) getActivity()).getCurrentStock();
        View view =  inflater.inflate(R.layout.fragment_stock_purchase, container, false);
        ((TextView)view.findViewById(R.id.stock_signature_purchase_fragment)).setText(s.getStockSignature());
        ((TextView)view.findViewById(R.id.stock_value_purchase_fragment)).setText(Double.toString(s.getTodaysPrice()));
        ((TextView)view.findViewById(R.id.stock_change_purchase_fragment)).setText(Double.toString(s.getTodaysChange()));


        Button b = (Button) view.findViewById(R.id.submit_stock_purchase);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(((EditText) getActivity().findViewById(R.id.stock_purchase_quantity)).getText().toString());
                Stock s = ((MainActivity) getActivity()).getCurrentStock();
                String signature = s.getStockSignature();
                Portfolio p = ((MainActivity) getActivity()).getCurrentPortfolio();
                String portfolio_name = p.getName();
                String owner_name = ((MainActivity) getActivity()).getCurrentUser().getMyUsername();
                try {
                    if(p.purchaseStock(signature,quantity,s)){
                        UploadTask u = new UploadTask();
                        u.execute(new String[]{buildPurchaseURL(s, p, owner_name, quantity)});
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.stockPurchaseInteraction();
        }
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
        void stockPurchaseInteraction();
    }

    public String buildPurchaseURL(Stock s, Portfolio p, String owner_name, int quantity){
        String toReturn = PURCHASE_URL;
        toReturn += "&portfolio_name=" + p.getName();
        toReturn += "&stock_signature=" + s.getStockSignature();
        toReturn += "&quantity=" + quantity;
        toReturn += "&portfolio_value=" + p.getTotalValue();
        toReturn += "&money_left=" + p.getMoneyLeft();
        toReturn += "&owner_name=" + owner_name;
        return toReturn;
    }
}
