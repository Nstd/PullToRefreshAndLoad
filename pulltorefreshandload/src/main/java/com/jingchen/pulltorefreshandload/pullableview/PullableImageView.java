package com.jingchen.pulltorefreshandload.pullableview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;


public class PullableImageView extends ImageView implements Pullable
{
	public PullableConfig pullableConfig = new PullableConfig();

	public PullableImageView(Context context)
	{
		super(context);
	}

	public PullableImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public PullableImageView(Context context, AttributeSet attrs, int defStyle)
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
