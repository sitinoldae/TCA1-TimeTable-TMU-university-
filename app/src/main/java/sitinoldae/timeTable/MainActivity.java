package sitinoldae.timeTable;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appizona.yehiahd.fastsave.FastSave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class

MainActivity extends AppCompatActivity {
    String[] sec_a, sec_b, sec_ibm, sec_inorture;
    String toBeInspected = "";
    GridView gv;
    ImageButton settingBtn;
    RelativeLayout mainRL;
    Button reqest_new_timeTable;
    TextView mytimeview;
    int flag_default = 0;
    FrameLayout myView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            final Bundle extras = data.getExtras();
            if (extras != null) {
                //Get image
                Bitmap newProfilePic = extras.getParcelable("data");
                Toast.makeText(this, "Working", Toast.LENGTH_SHORT).show();
                if (isStoragePermissionGranted() == true) {
                    storeImage(newProfilePic);
                }
                //restarting app
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage(getBaseContext().getPackageName());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                Toast.makeText(getApplicationContext(), "App Restarted", Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting fullscreen and hiding Title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //initialising fastsave library
        FastSave.init(this);
        isStoragePermissionGranted();
        gv = (GridView) findViewById(R.id.grid_view);
        settingBtn = (ImageButton) findViewById(R.id.setting_btn);
        reqest_new_timeTable = (Button) findViewById(R.id.new_ttable_btn);
        mytimeview = (TextView) findViewById(R.id.my_time);
        mainRL = (RelativeLayout) findViewById(R.id.main_relative_layout);

        //frame layout used to show/hide choices for lectures
        myView = (FrameLayout) findViewById(R.id.myCustomView);

        //trying to set previously set image
        trySettingImage();
        //initializing custom methods
        initArrays();
        checkDefaultParams();
        everythingElse();
        // TODO: 13-Sep-18

    }

    //CustomMethodsForStringPropulsion
    String StringArrayToString(String[] test) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < test.length; i++) {
            sb.append(test[i]).append(",");
        }
        String gift = sb.toString();
        return gift;
    }

    String[] StringToStringArray(String test) {
        String[] gift = test.split(",");
        return gift;
    }

    @Override
    public void onBackPressed() {
        try {
            toBeInspected = FastSave.getInstance().getString("default_key").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (myView.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
        } else if (myView.getVisibility() == View.GONE && toBeInspected.equals("")) {
            myView.setVisibility(View.VISIBLE);
        } else if (myView.getVisibility() == View.GONE && !toBeInspected.equals("")) {
            super.onBackPressed();
        }
    }

    void initArrays() {
        sec_a = new String[]{
                "DAY",
                "9:00-9:55",
                "9:55-10:50",
                "10:50-11:45",
                "11:45-12:40",
                "12:40-1:30",
                "1:30-2:20",
                "2:20-3:10",
                "3:10-4:00",
                "MONDAY",
                "Economics",
                "Eng-LAB",
                "Algorithm",
                "English",
                "LUNCH",
                "Java Lab",
                "Java Lab",
                "ERP/M.Comm",
                "TUESDAY",
                "Comp-Arch",
                "Economics",
                "Java",
                "Compiler",
                "LUNCH",
                "Algorithm",
                "Java",
                "ERP/M.comm",
                "WEDNESDAY",
                "English",
                "Comp-Arch",
                "ERP/M.comm",
                "Algorithm",
                "LUNCH",
                "Library",
                "Java",
                "-",
                "THURSDAY",
                "Economics",
                "Comp-Arch",
                "Java",
                "Compiler",
                "LUNCH",
                "Compiler",
                "Algorithm",
                "-",
                "FRIDAY",
                "Algo Lab",
                "Java Lab",
                "ERP/M.comm",
                "Comp-Arch",
                "LUNCH",
                "GATE",
                "GATE",
                "-",
                "SATURDAY",
                "Economics",
                "Compiler",
                "Algo-LAB",
                "Algo-LAB",
                "LUNCH",
                "GATE",
                "GATE",
                "-"
        };
        sec_b = new String[]{
                "DAY",
                "9:00-9:55",
                "9:55-10:50",
                "10:50-11:45",
                "11:45-12:40",
                "12:40-1:30",
                "1:30-2:20",
                "2:20-3:10",
                "3:10-4:00",
                "MONDAY",
                "Economics",
                "English",
                "Java",
                "Algorithm",
                "LUNCH",
                "compiler",
                "Comp-Arch",
                "ERP/M.Comm",
                "TUESDAY",
                "Java",
                "Algorithm",
                "Comp-Arch",
                "Algorithm",
                "LUNCH",
                "Economics",
                "Compiler",
                "ERP/M.comm",
                "WEDNESDAY",
                "Algo-LAB",
                "Eng-LAB",
                "ERP/M.comm",
                "compiler",
                "LUNCH",
                "English",
                "Libray",
                "-",
                "THURSDAY",
                "Algo-LAB",
                "Algo-LAB",
                "Comp-Arch",
                "Economics",
                "LUNCH",
                "Economics",
                "compiler",
                "-",
                "FRIDAY",
                "Java LAB",
                "Comp-Arch",
                "ERP/M.Comm",
                "Java",
                "LUNCH",
                "GATE",
                "GATE",
                "-",
                "SATURDAY",
                "Java LAB",
                "Java LAB",
                "Algorithm",
                "Java",
                "LUNCH",
                "GATE",
                "GATE",
                "-"
        };
        sec_ibm = new String[]{
                "DAY",
                "9:00-9:55",
                "9:55-10:50",
                "10:50-11:45",
                "11:45-12:40",
                "12:40-1:30",
                "1:30-2:20",
                "2:20-3:10",
                "3:10-4:00",
                "MONDAY",
                "IBM",
                "Algorithm",
                "IBM LAB",
                "IBM LAB",
                "LUNCH",
                "Algo-LAB",
                "Algo-LAB",
                "-",
                "TUESDAY",
                "English",
                "Comp-Arch",
                "IBM",
                "compiler",
                "LUNCH",
                "-",
                "-",
                "-",
                "WEDNESDAY",
                "Comp-Arch",
                "English",
                "Compiler",
                "Economics",
                "LUNCH",
                "-",
                "-",
                "-",
                "THURSDAY",
                "IBM",
                "Algorithm",
                "Economics",
                "Algorithm",
                "LUNCH",
                "Comp-Arch",
                "IBM",
                "-",
                "FRIDAY",
                "Library",
                "Algorithm",
                "Economics",
                "Eng-LAB",
                "LUNCH",
                "IBM LAB",
                "Compiler",
                "-",
                "SATURDAY",
                "Compiler",
                "Algo-LAB",
                "Economics",
                "Comp-Arch",
                "LUNCH",
                "-",
                "-",
                "-"
        };
        sec_inorture = new String[]{
                "DAY",
                "9:00-9:55",
                "9:55-10:50",
                "10:50-11:45",
                "11:45-12:40",
                "12:40-1:30",
                "1:30-2:20",
                "2:20-3:10",
                "3:10-4:00",
                "MONDAY",
                "Java",
                "DR-BCM",
                "Eth-Hack",
                "Data-Cntr",
                "LUNCH",
                "Stor-Rec",
                "Web.T LAB",
                "Web.T LAB",
                "TUESDAY",
                "Data-Cntr",
                "Java",
                "Web.T",
                "Library",
                "LUNCH",
                "Stor-Rec",
                "Java",
                "DR-BCM",
                "WEDNESDAY",
                "Eth-Hack",
                "Data-Cntr",
                "Java LAB",
                "Java LAB",
                "LUNCH",
                "Stor-Rec",
                "Data-Cntr",
                "DR-BCM",
                "THURSDAY",
                "Eth-Hack",
                "Stor-Rec",
                "Web.T",
                "Library",
                "LUNCH",
                "Eth-hk LAB",
                "Eth-hk LAB",
                "Web.T",
                "FRIDAY",
                "Eth-hk LAB",
                "Eth-hk LAB",
                "Java",
                "Eth-Hack",
                "LUNCH",
                "Emp-Skill",
                "Emp-Skill",
                "-",
                "SATURDAY",
                "Java LAB",
                "Java LAB",
                "Web.T",
                "DR-BCM",
                "LUNCH",
                "Web.T LAB",
                "Web.T LAB",
                "-"
        };
    }

    void displaySelectedSection() {
        myView.setVisibility(View.VISIBLE);
        String[] section_array = {"Btech CS |section-A", "Btech CS |section-B", "Btech CS |section-IBM", "Btech CS |section-I-Nurture"};
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.listview1, section_array);
        ListView listView = (ListView) findViewById(R.id.lecture_list);
        listView.setAdapter(adapter);
        final Switch checked = (Switch) findViewById(R.id.toggle);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = (String) adapterView.getItemAtPosition(i);
                if (data.contains("-A")) {
                    initGridview(sec_a);
                } else if (data.contains("-B")) {
                    initGridview(sec_b);
                } else if (data.contains("-IBM")) {
                    initGridview(sec_ibm);
                } else if (data.contains("I-Nurture")) {
                    initGridview(sec_inorture);
                }
                myView.setVisibility(View.GONE);
                if (checked.isChecked()) {
                    FastSave.getInstance().saveString("default_key", data);
                } else {
                    FastSave.getInstance().saveString("default_key", null);
                }
                initGridviewListener();
            }
        });
    }

    void initGridview(String[] test) {

        // Populate a List from Array elements
        final List<String> lecturelist = new ArrayList<String>(Arrays.asList(test));

        // Data bind GridView with ArrayAdapter (String Array elements)

        gv.setAdapter(new ArrayAdapter<String>(
                this, R.layout.simple_list_item_1, lecturelist) {
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    void checkDefaultParams() {
        try {
            toBeInspected = FastSave.getInstance().getString("default_key").toString();
            Toast.makeText(getApplicationContext(), toBeInspected, Toast.LENGTH_SHORT).show();
            if (toBeInspected.contains("-A")) {
                initGridview(sec_a);
            } else if (toBeInspected.contains("-B")) {
                initGridview(sec_b);
            } else if (toBeInspected.contains("-IBM")) {
                initGridview(sec_ibm);
            } else if (toBeInspected.contains("I-Nurture")) {
                initGridview(sec_inorture);
            }
            myView.setVisibility(View.GONE);
        } catch (Exception c) {
            displaySelectedSection();
        }
    }

    void initGridviewListener() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int time = i % 9;
                int day = (i - time) / 9;
                String[] days = {"sunday", "monday", "Tuesday", "Wenesday", "Thursday", "Friday", "Saturday"};
                String[] lecture_timing = {"null", "First", "Second", "Third", "Fourth", "Lunch", "Fifth", "Sixth", "Seventh", "Eighth"};
                String Lecture = gv.getItemAtPosition(i).toString();
                String fullString = "This [" + Lecture + "] Lecture/Lab falls on " + days[day] + " in " + lecture_timing[time] + " Period.";
                Toast viewDetailsByToast = Toast.makeText(getApplicationContext(), fullString, Toast.LENGTH_SHORT);
                if (Lecture.contains("DAY") || Lecture.contains(":") || Lecture.contains("LUNCH") || Lecture.matches("-")) {
                    //do nothing
                } else {
                    viewDetailsByToast.show();
                }
            }

        });
    }

    void everythingElse() {

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a | EEE | dd/MM/yyyy");
        String currentDateandTime = sdf.format(new Date());
        mytimeview.setText(currentDateandTime);
        reqest_new_timeTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "sitinoldae@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Request to add new Time Table");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Name:\nPhone:\nCourse/Class/Section:\n\n\nNote: Please attach photo of your current time-table.");
                startActivity(Intent.createChooser(emailIntent, "Send email with Attachement..."));
            }
        });
        settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySelectedSection();
                myView.setVisibility(View.VISIBLE);
            }
        });
        settingBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pickImage();
                return false;
            }
        });
    }

    //todo
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Complete action using"),
                    1);
        } catch (ActivityNotFoundException e) {
        }
    }

    //method 1
    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 95, fos);
            fos.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    //method 2
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName = "background.jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    //method 3
    private void trySettingImage() {
        File mediaStorageDir2 = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        File mediaFile2;
        String mImageName = "background.jpg";
        mediaFile2 = new File(mediaStorageDir2.getPath() + File.separator + mImageName);

        try {
            Bitmap bitmapfile = BitmapFactory.decodeStream(new FileInputStream(mediaFile2));
            // TODO: 03-Oct-18
            Drawable d = new BitmapDrawable(getResources(), bitmapfile);
            mainRL.setBackgroundDrawable(d);
            myView.setBackgroundDrawable(d);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    //check app permissions
    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "Allow access to Storage", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }
}
