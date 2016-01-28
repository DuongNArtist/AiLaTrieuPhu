package com.skynet.ailatrieuphu.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.skynet.ailatrieuphu.R;

public class CircleImageView extends ImageView {

	private final static int COLOR_BORDER = 0x004545ff;
	private final static int DEFAULT_WIDTH_BORDER = 1;
	private final static int DEFAULT_WIDTH_SHADOW = 0;

	private int borderWidth = DEFAULT_WIDTH_BORDER;
	private int shadowWidth = DEFAULT_WIDTH_SHADOW;
	private int viewWidth;
	private int viewHeight;
	private Bitmap image;
	private Paint paint;
	private Paint paintBorder;
	private Paint paintShadow;
	private BitmapShader shader;
	private int borderColor;

	public CircleImageView(Context context) {
		super(context);
		setup();
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setup(context, attrs);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setup(context, attrs);
	}

	private void setup() {
		// init paint
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);

		paintBorder = new Paint();
		setBorderColor(COLOR_BORDER);
		paintBorder.setAntiAlias(true);
		paintBorder.setStyle(Paint.Style.STROKE);

		paintShadow = new Paint();
		paintShadow.setColor(Color.parseColor("#AA000000"));
		paintShadow.setAntiAlias(true);
	}

	private void setup(Context context, AttributeSet attrs) {
		borderColor = COLOR_BORDER;
		if (!isInEditMode()) {
			TypedArray a = context.obtainStyledAttributes(attrs,
					R.styleable.CircleImageView);
			borderWidth = a.getInt(R.styleable.CircleImageView_borderWidth,
					DEFAULT_WIDTH_BORDER);
			shadowWidth = a.getInt(R.styleable.CircleImageView_shadowWidth,
					DEFAULT_WIDTH_SHADOW);
			borderColor = a.getColor(R.styleable.CircleImageView_borderColor,
					COLOR_BORDER);
			a.recycle();
		}

		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);

		paintBorder = new Paint();
		setBorderColor(borderColor);
		paintBorder.setAntiAlias(true);
		paintBorder.setStyle(Paint.Style.STROKE);

		paintShadow = new Paint();
		paintShadow.setColor(Color.parseColor("#AA000000"));
		paintShadow.setAntiAlias(true);
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
		this.invalidate();
	}

	public void setBorderColor(int borderColor) {
		if (paintBorder != null)
			paintBorder.setColor(borderColor);
		this.invalidate();
	}

	private void loadBitmap() {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) this.getDrawable();

		if (bitmapDrawable != null) {
			image = bitmapDrawable.getBitmap();
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
		// load the bitmap
		loadBitmap();

		int saveCount = canvas.getSaveCount();
		canvas.save();

		// init shader
		if (image != null) {
			if (getScaleType() == ScaleType.CENTER_INSIDE) {
				image = scaleCenterInside(image, viewHeight, viewWidth);
			} else {
				image = scaleCenterCrop(image, viewHeight, viewWidth);
			}

			int circleCenter = viewHeight / 2;
			// Create a shader with a scaled bitmap to match the view dimensions
			shader = new BitmapShader(image, Shader.TileMode.CLAMP,
					Shader.TileMode.CLAMP);
			paint.setShader(shader);
			// RadialGradient radialGradientShader = new RadialGradient(
			// circleCenter + borderWidth + shadowWidth, circleCenter
			// + borderWidth + shadowWidth, circleCenter
			// + borderWidth + shadowWidth, Color.BLACK,
			// Color.TRANSPARENT, Shader.TileMode.MIRROR);
			// paintShadow.setShader(radialGradientShader);

			// canvas.drawCircle(circleCenter + borderWidth + shadowWidth,
			// circleCenter + borderWidth + shadowWidth, circleCenter
			// + borderWidth + shadowWidth, paintShadow);

			// Draw the outer border
			canvas.drawCircle(circleCenter + borderWidth + shadowWidth,
					circleCenter + borderWidth + shadowWidth, circleCenter
							+ borderWidth, paintBorder);
			// circleCenter is the x or y of the view's center
			// radius is the radius in pixels of the cirle to be drawn
			// paint contains the shader that will texture the shape
			canvas.drawCircle(circleCenter + borderWidth + shadowWidth,
					circleCenter + borderWidth + shadowWidth, circleCenter,
					paint);
		}
		canvas.restoreToCount(saveCount);
	}

	public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		// Compute the scaling factors to fit the new height and width,
		// respectively.
		// To cover the final image, the final scaling will be the bigger
		// of these two.
		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.max(xScale, yScale);

		// Now get the size of the source bitmap when scaled
		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		// Let's find out the upper left coordinates if the scaled bitmap
		// should be centered in the new size give by the parameters
		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		// The target rectangle for the new, scaled version of the source bitmap
		// will now
		// be
		RectF targetRect = new RectF(left, top, left + scaledWidth, top
				+ scaledHeight);

		// Finally, we create a new bitmap of the specified size and draw our
		// new,
		// scaled bitmap onto it.
		if (source.getConfig() == null) {
			return Bitmap.createBitmap(image);
		}

		if (newWidth > 300) {
			newWidth = 300;
		}

		if (newHeight > 300) {
			newHeight = 300;
		}
		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight,
				source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}

	public Bitmap scaleCenterInside(Bitmap source, int newHeight, int newWidth) {
		int sourceWidth = source.getWidth();
		int sourceHeight = source.getHeight();

		// Compute the scaling factors to fit the new height and width,
		// respectively.
		// To cover the final image, the final scaling will be the bigger
		// of these two.
		float xScale = (float) newWidth / sourceWidth;
		float yScale = (float) newHeight / sourceHeight;
		float scale = Math.min(xScale, yScale);

		// Now get the size of the source bitmap when scaled
		float scaledWidth = scale * sourceWidth;
		float scaledHeight = scale * sourceHeight;

		// Let's find out the upper left coordinates if the scaled bitmap
		// should be centered in the new size give by the parameters
		float left = (newWidth - scaledWidth) / 2;
		float top = (newHeight - scaledHeight) / 2;

		// The target rectangle for the new, scaled version of the source bitmap
		// will now
		// be
		RectF targetRect = new RectF(left, top, left + scaledWidth, top
				+ scaledHeight);

		// Finally, we create a new bitmap of the specified size and draw our
		// new,
		// scaled bitmap onto it.
		if (source.getConfig() == null) {
			return Bitmap.createBitmap(image);
		}
		Bitmap dest = Bitmap.createBitmap(newWidth, newHeight,
				source.getConfig());
		Canvas canvas = new Canvas(dest);
		canvas.drawBitmap(source, null, targetRect, null);

		return dest;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (viewHeight <= 0 || viewWidth <= 0 || viewWidth > 300
				|| viewHeight > 300 || viewHeight != viewWidth) {
			int width = measureWidth(widthMeasureSpec);
			int height = measureHeight(heightMeasureSpec, widthMeasureSpec);

			Log.d("Circle Image", width + " " + height);

			viewWidth = width - (borderWidth + shadowWidth) * 2;
			viewHeight = height - (borderWidth + shadowWidth) * 2;

			if (viewHeight > 300) {
				viewHeight = 300;
				height = 300;
			}

			if (viewWidth > 300) {
				viewWidth = 300;
				width = 300;
			}

			setMeasuredDimension(width, height);
		}
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			if (viewWidth <= 0) {
				result = getMeasuredWidth();
			} else {
				result = viewWidth;
			}

		}

		return result;
	}

	private int measureHeight(int measureSpecHeight, int measureSpecWidth) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpecHeight);
		int specSize = MeasureSpec.getSize(measureSpecHeight);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text (beware: ascent is a negative number)
			if (viewHeight <= 0) {
				result = getMeasuredHeight();
			} else {
				result = viewHeight;
			}
		}
		return result;
	}
}
