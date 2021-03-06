package com.skypremiuminternational.app.app.features.skytextinputlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skypremiuminternational.app.R;

/**
 * Created by johnsonmaung on 10/28/17.
 */

public class SkyTextInputLayout extends LinearLayout {

  TextView tv;
  EditText edt;
  View v;

  int hint_color;
  String hint = "";
  String text = "";
  int error_color;
  String error = "";
  int inputType = -1;
  int imeOptions = -1;
  InputFilter[] inputFilters = null;
  int maxLines = -1;
  boolean enabled = true;
  Drawable drawableRight = null;

  public String getCharSeq() {
    return charSeq;
  }

  public void setCharSeq(String charSeq) {
    this.charSeq = charSeq;
  }

  String charSeq = "";

  public SkyTextInputLayout(Context context) {
    super(context);
  }

  public SkyTextInputLayout(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public SkyTextInputLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    if (!isInEditMode()) {
      TypedArray typedArray =
          getContext().obtainStyledAttributes(attrs, R.styleable.SkyTextInputLayout);
      try {

        hint_color = typedArray.getColor(R.styleable.SkyTextInputLayout_hintColor,
            ContextCompat.getColor(getContext(), R.color.orchid));

        hint = typedArray.getString(R.styleable.SkyTextInputLayout_android_hint);
        text = typedArray.getString(R.styleable.SkyTextInputLayout_android_text);

        error_color = typedArray.getColor(R.styleable.SkyTextInputLayout_errorColor,
            ContextCompat.getColor(getContext(), R.color.wineRed));
        error = typedArray.getString(R.styleable.SkyTextInputLayout_error);

        if (typedArray.hasValue(R.styleable.SkyTextInputLayout_android_inputType)) {
          inputType = (typedArray.getInt(R.styleable.SkyTextInputLayout_android_inputType, -1));
        }

        if (typedArray.hasValue(R.styleable.SkyTextInputLayout_android_enabled)) {
          enabled = typedArray.getBoolean(R.styleable.SkyTextInputLayout_android_enabled, true);
        }

        if (typedArray.hasValue(R.styleable.SkyTextInputLayout_android_drawableRight)) {
          drawableRight =
              typedArray.getDrawable(R.styleable.SkyTextInputLayout_android_drawableRight);
        }

        if (typedArray.hasValue(R.styleable.SkyTextInputLayout_android_imeOptions)) {
          imeOptions = typedArray.getInt(R.styleable.SkyTextInputLayout_android_imeOptions, -1);
        }

        if (typedArray.hasValue(R.styleable.SkyTextInputLayout_android_maxLength)) {
          inputFilters = new InputFilter[]{
              new InputFilter.LengthFilter(
                  typedArray.getInt(R.styleable.SkyTextInputLayout_android_maxLength, 0))
          };
        }

        if (typedArray.hasValue(R.styleable.SkyTextInputLayout_android_maxLines)) {
          maxLines = typedArray.getInt(R.styleable.SkyTextInputLayout_android_maxLines, 1);
        }
      } finally {
        typedArray.recycle();
      }
    }
  }

  public void setOnEditorActionListener(TextView.OnEditorActionListener onEditorActionListener) {
    edt.setOnEditorActionListener(onEditorActionListener);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    inflate(getContext(), R.layout.skytextinputlayout, this);
    tv = findViewById(R.id.tv);
    edt = findViewById(R.id.edt);
    v = findViewById(R.id.v);

    edt.setOnFocusChangeListener(new OnFocusChangeListener() {
      @Override
      public void onFocusChange(View view, boolean b) {
        if (view.getId() == R.id.edt) {
          if (hasFocus()) {
            if (TextUtils.isEmpty(edt.getText().toString())) {
              tv.setVisibility(View.INVISIBLE);
            } else {
              tv.setVisibility(View.VISIBLE);
            }
          } else {

          }
        }
      }
    });

    edt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String text = charSequence.toString();
        tv.setTextColor(hint_color);
        tv.setText(edt.getHint());

        if (TextUtils.isEmpty(text)) {
          tv.setVisibility(View.INVISIBLE);
        } else {
          tv.setVisibility(View.VISIBLE);
          charSequence = text;
        }
      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    tv.setText(hint);

    edt.setHint(hint);
    if (inputType != -1) edt.setInputType(inputType);
    if (imeOptions != -1) edt.setImeOptions(imeOptions);
    if (maxLines != -1) edt.setMaxLines(maxLines);
    if (inputFilters != null) edt.setFilters(inputFilters);
    if (drawableRight != null) {
      edt.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
    }
    edt.setEnabled(enabled);
    edt.setText(text);
    edt.setSelection(edt.getText().length());
  }

  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
    edt.setHint(hint);
  }

  public void showError(String error_text) {
    if (!TextUtils.isEmpty(edt.getText().toString())) {
      tv.setVisibility(VISIBLE);
      tv.setTextColor(error_color);
      if ((error_text != null) && (!TextUtils.isEmpty(error_text))) {
        tv.setText(error_text);
      } else {
        tv.setText(this.error);
      }
    }
  }

  public void showError() {
    //if (!TextUtils.isEmpty(edt.getText().toString())) {
    tv.setVisibility(VISIBLE);
    tv.setTextColor(error_color);
    if (!TextUtils.isEmpty(error)) {
      tv.setText(error);
    }
    //}
  }

  public void hideError() {
    if (TextUtils.isEmpty(edt.getText().toString())) {
      tv.setVisibility(INVISIBLE);
    } else {
      tv.setVisibility(VISIBLE);
    }
    tv.setTextColor(hint_color);
    tv.setText(hint);
  }

  public void setText(String text) {
    this.text = text;
    edt.setText(text);
    edt.setSelection(edt.getText().length());
  }

  public String getText() {
    return edt.getText().toString();
  }

  public void setError(String error) {
    this.error = error;
    tv.setText(error);
  }

  public String getError() {
    return error;
  }

  public void setInputType(int inputType) {
    this.inputType = inputType;
    edt.setInputType(inputType);
  }

  public int getInputType() {
    return edt.getInputType();
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    edt.setEnabled(enabled);
  }

  public void setTransformationMethod(TransformationMethod transformationMethod) {
    edt.setTransformationMethod(transformationMethod);
  }
}
