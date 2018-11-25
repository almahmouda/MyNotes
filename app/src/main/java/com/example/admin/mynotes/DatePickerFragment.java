package com.example.admin.mynotes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatePickerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    // Initialize a listener callback to be used in the calling activity
    private OnFragmentInteractionListener mListener;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a custom DatePicker Dialog container.
     *
     * @param savedInstanceState The last saved instance state of the Fragment, or null if
     *                           this is a freshly created Fragment.
     * @return Return a new DatePicker dialog instance to be displayed by the Fragment.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        FragmentActivity frag_act = getActivity();
        // create a new instance of DatPickerDialog and returns it
        return new DatePickerDialog(frag_act, this, year, month, day);
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
     * An abstract method from the DatePickerDialog.OnDateSetListener interface. It passed the
     * date selected in the DatePicker view.
     *
     * @param view       the picker associated with the dialog
     * @param year       the selected year
     * @param month      the selected month (months range from 0-11 where 0 represents January)
     * @param dayOfMonth the selected day of the month
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mListener.setDate(year, month, dayOfMonth);
    }

    /**
     * An interface to be implemented by the activities that will use the is DatePicker
     * fragment.  It allow an interaction in this fragment to be communicated to the activity
     * and potentially other fragments contained in that activity.
     */
    public interface OnFragmentInteractionListener {
        // This method is used to set the date in the calling activity
        void setDate(int year, int month, int day);
    }
}
