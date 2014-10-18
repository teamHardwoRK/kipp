package com.teamhardwork.kipp.dialogfragments;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.teamhardwork.kipp.KippApplication;
import com.teamhardwork.kipp.R;
import com.teamhardwork.kipp.adapters.AddActionAdapter;
import com.teamhardwork.kipp.enums.ActionType;
import com.teamhardwork.kipp.enums.Role;
import com.teamhardwork.kipp.models.Action;
import com.teamhardwork.kipp.models.BehaviorEvent;
import com.teamhardwork.kipp.models.users.Student;

import org.apache.http.protocol.HTTP;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddActionDialogFragment extends DialogFragment {

    static final int EMAIL_REQUEST_CODE = 123;
    Student student;
    BehaviorEvent event;
    ActionType type;

    @InjectView(R.id.lvActions)
    ListView lvActions;

    public static AddActionDialogFragment getInstance(Student student) {
        AddActionDialogFragment dialogFragment = new AddActionDialogFragment();
        dialogFragment.student = student;

        return dialogFragment;
    }

    public static AddActionDialogFragment getInstance(BehaviorEvent event) {
        AddActionDialogFragment dialogFragment = getInstance(event.getStudent());
        dialogFragment.event = event;

        return dialogFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_action, container, false);
        ButterKnife.inject(this, view);

        lvActions.setAdapter(new AddActionAdapter(getActivity(), Role.STUDENT));
        setupListeners();

        getDialog().setTitle(R.string.title_add_action);

        return view;
    }

    void setupListeners() {
        lvActions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type = (ActionType) lvActions.getItemAtPosition(position);
                Activity activity = getActivity();
                Intent intent = new Intent();
                String telephone = student.getTelephonNumber();
                String message = "";

                boolean hasInfo = false;
                switch (type) {
                    case CALL:
                        if(telephone != null) {
                            hasInfo = true;

                            intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + telephone));
                        }
                        break;
                    case EMAIL:
                        String email = student.getEmail();

                        if(email != null) {
                            hasInfo = true;

                            intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{ email });
                            intent.setType("message/rfc822");

                            if (event != null) {
                                intent.putExtra(Intent.EXTRA_SUBJECT, event.getBehavior().getTitle() + " - " + event.getOccurredAt().toString());
                            }
                            intent.putExtra(Intent.EXTRA_TEXT, activity.getResources().getString(R.string.salutation) + student.getFirstName() + ",\n");

                            message = activity.getResources().getString(R.string.choose_email_client);
                        }
                        break;
                    case NOTE:
                        break;
                    case TEXT:
                        if(telephone != null) {
                            hasInfo = true;

                            intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setType(HTTP.PLAIN_TEXT_TYPE);
                            intent.setData(Uri.parse("smsto:" + telephone));
                            intent.putExtra("sms_text", student.getFirstName() + ", ");
                        }
                        break;
                }

                if(hasInfo)
                    startActivityForResult(Intent.createChooser(intent, message), EMAIL_REQUEST_CODE);
                else
                    Toast.makeText(getActivity(), "No contact data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(resultCode) {
            // implicit EMAIL intents always return RESULT_CANCELED
            case Activity.RESULT_CANCELED:

                Action action = new Action();
                action.setType(type);

                if(event != null) {
                    action.setBehaviorEvent(event);
                }

                action.setOccurredAt(new Date());
                action.setSchoolClass(((KippApplication) getActivity().getApplication()).getSchoolClass());
                action.setStudent(student);
                action.saveInBackground();

                dismiss();
        }
    }
}