package dashanzi.android.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

import dashanzi.android.R;

public class Voice extends Activity implements OnClickListener,
		RecognizerDialogListener {
	private static final String TAG = "IatDemoActivity";

	private TextView mCategoryText;
	private EditText mResultText;
	private SharedPreferences mSharedPreferences;
	
	private String result = null;
	
	private RecognizerDialog iatDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "[onCreate]" + savedInstanceState);
		setContentView(R.layout.voice);

		((TextView) findViewById(android.R.id.title))
				.setGravity(Gravity.CENTER);

		mCategoryText = (TextView) findViewById(R.id.categoty);
		mCategoryText.setVisibility(View.VISIBLE);
		Button iatButton = (Button) findViewById(android.R.id.button1);
		iatButton.setOnClickListener(this);
		iatButton.setText("开始");
		mResultText = (EditText) findViewById(R.id.txt_result);
		iatDialog = new RecognizerDialog(this, "appid=" + getString(R.string.app_id));
		iatDialog.setListener(this);

		
		mSharedPreferences = getSharedPreferences(getPackageName(),
				MODE_PRIVATE);
	}

	@Override
	protected void onStart() {
		super.onStart();

		String engine = mSharedPreferences.getString(
				getString(R.string.preference_key_iat_engine),
				getString(R.string.preference_default_iat_engine));
//		String[] engineEntries = getResources().getStringArray(
//				R.array.preference_entries_iat_engine);
//		String[] engineValues = getResources().getStringArray(
//				R.array.preference_values_iat_engine);
//		for (int i = 0; i < engineValues.length; i++) {
//			if (engineValues[i].equals(engine)) {
//				mCategoryText.setText(engineEntries[i]);
//				break;
//			}
//		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case android.R.id.button1:
			showIatDialog();
			break;
		case android.R.id.button2:
//			startActivity(new Intent(this, IatPreferenceActivity.class));
			break;
		default:
			break;
		}
	}
	
	public void showIatDialog()
	{
		String engine = mSharedPreferences.getString(
				getString(R.string.preference_key_iat_engine),
				getString(R.string.preference_default_iat_engine));

		String area = null;
//		if (IatPreferenceActivity.ENGINE_POI.equals(engine)) {
//			final String defaultProvince = getString(R.string.preference_default_poi_province);
//			String province = mSharedPreferences.getString(
//					getString(R.string.preference_key_poi_province),
//					defaultProvince);
//			final String defaultCity = getString(R.string.preference_default_poi_city);
//			String city = mSharedPreferences.getString(
//					getString(R.string.preference_key_poi_city),
//					defaultCity);
//
//			if (!defaultProvince.equals(province)) {
//				area = "area=" + province;
//				if (!defaultCity.equals(city)) {
//					area += city;
//				}
//			}
//		}

		iatDialog.setEngine(engine, area, null);

		String rate = mSharedPreferences.getString(
				getString(R.string.preference_key_iat_rate),
				getString(R.string.preference_default_iat_rate));
		if(rate.equals("rate8k"))
			iatDialog.setSampleRate(RATE.rate8k);
		else if(rate.equals("rate11k"))
			iatDialog.setSampleRate(RATE.rate11k);
		else if(rate.equals("rate16k"))
			iatDialog.setSampleRate(RATE.rate16k);
		else if(rate.equals("rate22k"))
			iatDialog.setSampleRate(RATE.rate22k);
		mResultText.setText(null);
		
		iatDialog.show();
	}

	@Override
	public void onEnd(SpeechError error) {
	}

	@Override
	public void onResults(ArrayList<RecognizerResult> results,boolean isLast) {
		StringBuilder builder = new StringBuilder();
		for (RecognizerResult recognizerResult : results) {
			builder.append(recognizerResult.text);
		}
		mResultText.append(builder);
		mResultText.setSelection(mResultText.length());
		
		Log.e("test", mResultText.getText().toString());
	}

}
