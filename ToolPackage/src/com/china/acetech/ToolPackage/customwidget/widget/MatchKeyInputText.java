package com.china.acetech.ToolPackage.customwidget.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import com.china.acetech.ToolPackage.R;


public class MatchKeyInputText extends EditText {

	int mFocusBack;
	int mUnFocusBack;
	
	View mNextEditor;
	
	public MatchKeyInputText(Context context) {
		super(context);
	}
	
	public MatchKeyInputText(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray attrType = context.obtainStyledAttributes(attrs, R.styleable.Background);
		
		try {
			mFocusBack = attrType.getResourceId(R.styleable.Background_itemFocusBackground, -1);
			mUnFocusBack = attrType.getResourceId(R.styleable.Background_itemUnFocusBackground, -1);
		   } finally {
			   attrType.recycle();
		   }
		if ( mFocusBack != -1 )
			setBackgroundResource(mUnFocusBack);
		setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if ( mFocusBack == -1 || mUnFocusBack == -1 )
					return;
				
				if ( hasFocus ){
					setBackgroundResource(mFocusBack);
				}
				else{
					setBackgroundResource(mUnFocusBack);
				}
			}
		});
		
		
		addTextChangedListener(new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}
		
		@Override
		public void afterTextChanged(Editable s) {
			if ( mNextEditor != null && !mNextEditor.isFocused() )
				mNextEditor.requestFocus();
		}
	});
		this.setFilters(new InputFilter[]{new MyInputFilter()});
	}
	
	private boolean mJumpFlag;	
	public boolean isGoToNext(){
		return mJumpFlag;
	}
	
	public void setNextEditor(View editor){
		mNextEditor = editor;
	}
	

	private class MyInputFilter implements InputFilter {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {

			mJumpFlag = false;

			if (source.length() > 1)// paste and copy problem avoid
				return dest;

			if (dest.length() != 0) {
				if (source.length() != 0) {
					char character = source.charAt(0);
					if (character >= 0x30 && character <= 0x39) {
						MatchKeyInputText.this.setText(source);
						mJumpFlag = true;
					}
				} else {
					MatchKeyInputText.this.setText("");
				}

			}

			return source;
		}

	}
}
