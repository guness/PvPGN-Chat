package bnet.pvpgn.chat.gui.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import bnet.pvpgn.chat.C;
import bnet.pvpgn.chat.R;
import protocol.VersionInfo;

public class SettingsActivity extends Activity {

    private CheckBox mCheckBox2, mCheckBox3, mCheckBox4;
    private RadioButton mRadioButton1, mRadioButton2, mRadioButton3;
    private RadioGroup mRadioGroup;
    private EditText mEditText1, mEditText2, mEditText3;
    private Editor mEditor;
    private SharedPreferences mSharedPreferences;
    private SeekBar mSeekBar1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mCheckBox2 = findViewById(R.id.checkBox2);
        mCheckBox3 = findViewById(R.id.checkBox3);
        mCheckBox4 = findViewById(R.id.checkBox4);
        mEditText1 = findViewById(R.id.editText1);
        mEditText2 = findViewById(R.id.editText2);
        mEditText3 = findViewById(R.id.editText3);
        mSeekBar1 = findViewById(R.id.seekBar1);
        mRadioButton1 = findViewById(R.id.radio1);
        mRadioButton2 = findViewById(R.id.radio2);
        mRadioButton3 = findViewById(R.id.radio3);
        mRadioGroup = findViewById(R.id.radioGroup1);

        mSharedPreferences = getSharedPreferences(C.SETTINGS_FILE, MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        VersionInfo info = VersionInfo.valueOf(mSharedPreferences.getString(C.SETTINGS_TYPE, VersionInfo.W3XP_1285.name()));
        switch (info) {
            case W3XP_1285:
                mRadioButton1.setChecked(true);
                break;
            case W3XP_126A:
                mRadioButton2.setChecked(true);
                break;
            case W2BN_202B:
                mRadioButton3.setChecked(true);
                break;
        }

        mEditText2.setText(mSharedPreferences.getString(C.SETTINGS_GATEWAY, C.DEFAULT_GATEWAY));

        mCheckBox2.setChecked(mSharedPreferences.getBoolean(C.SETTINGS_PLAY_WHISP_SOUNDS, true));

        mCheckBox4.setChecked(mSharedPreferences.getBoolean(C.SETTINGS_RECONNECT, true));

        int buffer = mSharedPreferences.getInt(C.SETTINGS_CHAT_BUFFER, 40);

        mSeekBar1.setProgress(buffer);
        mEditText3.setText("" + buffer);

        mCheckBox2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditor.putBoolean(C.SETTINGS_PLAY_WHISP_SOUNDS, isChecked);
            }
        });
        mCheckBox3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(SettingsActivity.this, R.string.not_ready, Toast.LENGTH_SHORT).show();
            }
        });
        mCheckBox4.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditor.putBoolean(C.SETTINGS_RECONNECT, isChecked);
            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                VersionInfo info = VersionInfo.W3XP_1285;
                if (mRadioButton1.isChecked()) {
                    info = VersionInfo.W3XP_1285;
                }
                if (mRadioButton2.isChecked()) {
                    info = VersionInfo.W3XP_126A;
                }
                if (mRadioButton3.isChecked()) {
                    info = VersionInfo.W2BN_202B;
                }

                mEditor.putString(C.SETTINGS_TYPE, info.name());
            }
        });
        mEditText1.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Toast.makeText(SettingsActivity.this, R.string.not_ready, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSeekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0) {
                    Toast.makeText(SettingsActivity.this, R.string.zero_infinity, Toast.LENGTH_SHORT).show();
                }
                mEditText3.setText("" + progress);
            }
        });
    }

    @Override
    public void onPause() {
        int buffer;
        try {
            buffer = Integer.valueOf(mEditText3.getText().toString());
        } catch (Exception e) {
            buffer = 40;
        }
        mEditor.putInt(C.SETTINGS_CHAT_BUFFER, buffer);
        mEditor.putString(C.SETTINGS_GATEWAY, mEditText2.getText().toString());
        mEditor.commit();
        super.onPause();
    }
}
