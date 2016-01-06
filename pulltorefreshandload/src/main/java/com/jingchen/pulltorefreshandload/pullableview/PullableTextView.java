package com.jingchen.pulltorefreshandload.pullableview;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class PullableTextView extends TextView implements Pullable
{

	public PullableConfig pullableConfig = new PullableConfig();

	public PullableTextView(Context context)
	{
		super(context);
	}

	public PullableTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		return pullableConfig.canUserPullDown();
	}

	@Override
	public boolean canPullUp()
	{
		return  pullableConfig.canUserPullUp();
	}

}
