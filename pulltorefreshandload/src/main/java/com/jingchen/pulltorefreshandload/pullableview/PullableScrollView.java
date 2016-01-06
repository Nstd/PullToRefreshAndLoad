package com.jingchen.pulltorefreshandload.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable
{

	public PullableConfig pullableConfig = new PullableConfig();

	public PullableScrollView(Context context)
	{
		super(context);
	}

	public PullableScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		if(!pullableConfig.canUserPullDown())
		{
			return false;
		}

		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if(!pullableConfig.canUserPullUp()) {
			return false;
		}

		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
			return true;
		else
			return false;
	}

}
