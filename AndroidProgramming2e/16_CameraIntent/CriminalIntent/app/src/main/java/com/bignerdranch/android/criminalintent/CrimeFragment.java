package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {

    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_PHOTO= 2;

    private Crime mCrime;
    private File mPhotoFile;
    private File mPhotoFile1;
    private File mPhotoFile2;
    private File mPhotoFile3;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckbox;
    private Button mReportButton;
    private Button mSuspectButton;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private ImageView mPhotoView1;
    private ImageView mPhotoView2;
    private ImageView mPhotoView3;
    private int count;
    private Button mDeleteAllImages;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
        mPhotoFile1 = CrimeLab.get(getActivity()).getPhotoFile1(mCrime);
        mPhotoFile2 = CrimeLab.get(getActivity()).getPhotoFile2(mCrime);
        mPhotoFile3 = CrimeLab.get(getActivity()).getPhotoFile3(mCrime);
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = (EditText) v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment
                        .newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        mSolvedCheckbox.setChecked(mCrime.isSolved());
        mSolvedCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));

                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button)v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mSuspectButton.setEnabled(false);
        }

        mPhotoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//        boolean canTakePhoto = mPhotoFile != null &&
//                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(true);

//        if (canTakePhoto) {
//            Uri uri = Uri.fromFile(mPhotoFile);
//            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
////            Uri uri1 = Uri.fromFile(mPhotoFile1);
//            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
//            Uri uri2 = Uri.fromFile(mPhotoFile2);
//            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
//            Uri uri3 = Uri.fromFile(mPhotoFile3);
//            captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri3);
//        }


        mPhotoView = (ImageView) v.findViewById(R.id.crime_photo);
        mPhotoView1 = (ImageView) v.findViewById(R.id.crime_photo1);
        mPhotoView2 = (ImageView) v.findViewById(R.id.crime_photo2);
        mPhotoView3 = (ImageView) v.findViewById(R.id.crime_photo3);


        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count >= 4){
                    count = 1;
                }else  count ++;
                switch (count){
                    case 1:
                        Uri uri = Uri.fromFile(mPhotoFile);
                        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        break;
                    case 2:
                        Uri uri1 = Uri.fromFile(mPhotoFile1);
                        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
                        break;
                    case 3:
                        Uri uri2 = Uri.fromFile(mPhotoFile2);
                        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri2);
                        break;
                    case 4:
                        Uri uri3 = Uri.fromFile(mPhotoFile3);
                        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri3);
                        break;
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mDeleteAllImages =(Button)v.findViewById(R.id.delete_all);
        mDeleteAllImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoFile.delete();
                mPhotoFile1.delete();
                mPhotoFile2.delete();
                mPhotoFile3.delete();

                mPhotoView.setImageBitmap(null);
                mPhotoView1.setImageBitmap(null);
                mPhotoView2.setImageBitmap(null);
                mPhotoView3.setImageBitmap(null);
                count =0;
            }
            });



        initiatePhotoView();
        count = 0;

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return
            // values for.
            String[] queryFields = new String[] {
                    ContactsContract.Contacts.DISPLAY_NAME,
            };
            // Perform your query - the contactUri is like a "where"
            // clause here
            ContentResolver resolver = getActivity().getContentResolver();
            Cursor c = resolver
                    .query(contactUri, queryFields, null, null, null);

            try {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst();

                String suspect = c.getString(0);
                mCrime.setSuspect(suspect);
                mSuspectButton.setText(suspect);
            } finally {
                c.close();
            }
        } else if (requestCode == REQUEST_PHOTO) {
            updatePhotoView();
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }
        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }
        String report = getString(R.string.crime_report,
                mCrime.getTitle(), dateString, solvedString, suspect);
        return report;
    }

    private void initiatePhotoView(){


        if (mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
            mPhotoView1.setImageDrawable(null);
            mPhotoView2.setImageDrawable(null);
            mPhotoView3.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            Bitmap bitmap1 = PictureUtils.getScaledBitmap(
                    mPhotoFile1.getPath(), getActivity());
            Bitmap bitmap2 = PictureUtils.getScaledBitmap(
                    mPhotoFile2.getPath(), getActivity());
            Bitmap bitmap3 = PictureUtils.getScaledBitmap(
                    mPhotoFile3.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
            mPhotoView1.setImageBitmap(bitmap1);
            mPhotoView2.setImageBitmap(bitmap2);
            mPhotoView3.setImageBitmap(bitmap3);
        }
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
            mPhotoView1.setImageDrawable(null);
            mPhotoView2.setImageDrawable(null);
            mPhotoView3.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            Bitmap bitmap1 = PictureUtils.getScaledBitmap(
                    mPhotoFile1.getPath(), getActivity());
            Bitmap bitmap2 = PictureUtils.getScaledBitmap(
                    mPhotoFile2.getPath(), getActivity());
            Bitmap bitmap3 = PictureUtils.getScaledBitmap(
                    mPhotoFile3.getPath(), getActivity());

            switch (count){
                case 1:mPhotoView.setImageBitmap(bitmap);
                    break;
                case 2:mPhotoView1.setImageBitmap(bitmap1);
                    break;
                case 3:mPhotoView2.setImageBitmap(bitmap2);
                    break;
                case 4:mPhotoView3.setImageBitmap(bitmap3);
                    break;
            }


        }
    }
}
