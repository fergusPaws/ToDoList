package com.shaary.notes;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SortDialog extends DialogFragment {

    public static final String TAG = SortDialog.class.getSimpleName();

    public interface onInputListener {
        void sendInput(String type, String order);
    }

    private onInputListener listener;

    @BindView(R.id.sort_ok_button) Button okButton;
    @BindView(R.id.sort_dismiss_button) Button dismissButton;

    @BindView(R.id.type_sort_group) RadioGroup typeRadioGroup;
    @BindView(R.id.category_rbutton) RadioButton categoryRButton;
    @BindView(R.id.due_date_rbutton) RadioButton dueDateRButton;
    @BindView(R.id.priority_rbutton) RadioButton priorityRButton;
    @BindView(R.id.title_rbutton) RadioButton titleRButton;

    @BindView(R.id.order_sort_group) RadioGroup orderRadioGroup;
    @BindView(R.id.asc_rbutton) RadioButton ascOrder;
    @BindView(R.id.desc_rbutton) RadioButton descOrder;

    private String sortType;
    private String sortOrder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.dialog_sort, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();

        sortType = bundle.getString("type");
        sortOrder = bundle.getString("order");

        //Checks the radio button
        checkTypeButtons(sortType);
        checkOrderButtons(sortOrder);

        typeRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId) {
                case R.id.category_rbutton:
                    sortType = "category";
                    break;

                case R.id.due_date_rbutton:
                    sortType = "dueDate";
                    break;

                case R.id.priority_rbutton:
                    sortType = "priority";
                    break;

                case R.id.title_rbutton:
                    sortType = "title";
                    break;

                default:
                    sortType = "priority";
                    break;
            }
        });

        orderRadioGroup.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            switch (checkedId) {
                case R.id.asc_rbutton:
                    sortOrder = "asc";
                    break;

                case R.id.desc_rbutton:
                    sortOrder = "desc";
                    break;

                default:
                    sortOrder = "desc";
                    break;
            }
        });

        dismissButton.setOnClickListener(v -> getDialog().dismiss());

        okButton.setOnClickListener(view1 -> {
            listener.sendInput(sortType, sortOrder);
            dismiss();
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (onInputListener) getActivity();
        } catch (final ClassCastException e) {
            Log.d(TAG, "onAttach ClassCastException " + e.getMessage());
        }
    }

    private void checkTypeButtons(String buttonName) {
        switch (buttonName) {
            case "category":
                typeRadioGroup.check(categoryRButton.getId());
                break;

            case "dueDate":
                typeRadioGroup.check(dueDateRButton.getId());
                break;

            case "priority":
                typeRadioGroup.check(priorityRButton.getId());
                break;

            case "title":
                typeRadioGroup.check(titleRButton.getId());
                break;
        }
    }

    private void checkOrderButtons(String order) {
        switch (order) {
            case "asc":
                orderRadioGroup.check(ascOrder.getId());
                break;

            case "desc":
                orderRadioGroup.check(descOrder.getId());
                break;
        }
    }
}
