# Bi-Directional SeekBar

[![](https://jitpack.io/v/faisalcodes/bidirectional-seekbar.svg)](https://jitpack.io/#faisalcodes/bidirectional-seekbar)
[![Platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://choosealicense.com/licenses/mit/)

--------------------------------------------------------------------
Seek progress in both direction.
<br>
<br>
_*Style Curve.*_
<br><br>
<img src = "/demos/demo_style_curve.gif" height="500px">
<br>
<br>
_*Style linear.*_
<br><br>
<img src = "/demos/demo_style_linear.gif" height="500px">
<br>
### Implementation

add the following to project level build.gradle
```groovy

allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

```

add the following to module build.gradle
```groovy

implementation 'com.github.faisalcodes:bidirectional-seekbar:1.0.1'

```
## How to use

### XML
```xml
<com.android.faisalkhan.seekbar.bidirectionalseekbar.BiDirectionalSeekBar
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:indicatorColor="#952400"
    app:labelColor="#ffffff"
    app:maxValue="50"
    app:minValue="-50"
    app:percentageSign="true"
    app:progress="10"
    app:seekBarTitle="My SeekBar"
    app:seekBarTitleColor="#952400"
    app:seekBarTitleSize="tiny"
    app:stickColor="#555555"
    app:stickGap="30dp"
    app:zeroStickColor="#000000"
    app:seekBar_Style="curve"/>
```

### Java
```java
BiDirectionalSeekBar seekBar = new BiDirectionalSeekBar(this);
seekBar.setMinValue(-50);
seekBar.setMaxValue(50);
seekBar.setProgress(0);
seekBar.setPercentageSign(true);
seekBar.setIndicatorColor(0xFF952400);
seekBar.setLabelColor(0xFFFFFFFF);
seekBar.setSeekBarTitle("My SeekBar");
seekBar.setSeekBarTitleSize(BiDirectionalSeekBar.TITLE_NORMAL);
seekBar.setSeekBarTitleColor(0xFF952400);
seekBar.setStickColor(0xFF555555);
seekBar.setZeroStickColor(0xFF000000);
seekBar.setStickGap(50); // in px
seekBar.setSeekBarStyle(BiDirectionalSeekBar.STYLE_CURVE);
```
#### Listeners
##### Progress Change Listener only
```java
seekBar.setOnProgressChangeListener(new BiDirectionalSeekBar.OnProgressChangeListener() {
    @Override
    public void onProgress(int progress) {
        Log.d("myLogs", String.valueOf(progress));
    }
});
```
##### All seekBar change listeners
```java
seekBar.setOnSeekBarChangeListener(new BiDirectionalSeekBar.OnSeekBarChangeListener() {
    @Override
    public void onStartTrackingTouch(BiDirectionalSeekBar seekBar) {
        // seekBar touch start
    }

    @Override
    public void onProgressChanged(BiDirectionalSeekBar seekBar, int progress, boolean fromUser) {
        // use progress value
    }

    @Override
    public void onStopTrackingTouch(BiDirectionalSeekBar seekBar) {
        // seekBar touch end
    }
});
```
### Static values
```
// for seekBar title size
public static final int TITLE_TINY;
public static final int TITLE_NORMAL;

// for seekBar style
public static final int STYLE_CURVE;
public static final int STYLE_LINEAR;
```
## Attributes
| XML Attribute | Java methods                      | Description                                                              |
|---------------|-----------------------------------|--------------------------------------------------------------------------|
| minValue      | setMinValue(int)<br>getMinValue() | Set the minimum value for seekBar.<br>Get the minimum value for seekBar. |
| maxValue      | setMaxValue(int)<br>getMaxValue() | Set the maximum value for seekBar.<br>Get the maximum value for seekBar. |
| progress      | setProgress(int)<br><br>getProgress() | Set progress to the seekBar. If (progress < minValue) or (progress > maxValue), then the progress becomes minValue or maxValue respectively. DEFAULT - 0<br><br>Get current progress value of seekBar. |
| seekBarTitle  | setSeekBarTitle(@StringRes or String)<br>getSeekBarTitle() | Set title to the seekBar.<br>Get title of the seekBar. |
| seekBarTitleSize  | setSeekBarSize(int) | Set the size of which the title should be. One of TITLE_TINY and TITLE_NORMAL . DEFAULT - TITLE_TINY |
| seekBarTitleColor | setSeekBarColor(int) | Set color to the title text. |
| labelColor | setLabelColor(int) | Set color to the label text. This is useful when background color and text color of the label are mismatching. By default, it uses theme primary text color. |
| indicatorColor | setIndicatorColor(int) | Set color to the indicator and label background. By default, it uses theme primary color. |
| stickColor | setStickColor(int) | Set color to the progress sticks. By default, it uses theme secondary text color. |
| stickGap | setStickGap(int) | Set gap between the sticks. This is useful to make seekBar look good when total progress sticks are lesser in count. |
| zeroStickColor | setZeroStickColor(int) | Set color to the stick at zero (0) progress. This is ignored if (minValue > 0) or (maxValue < 0). By default, it uses theme primary text color. |
| percentageSign| setPercentageSign(boolean) | Set whether the percentage sign (%) should be shown with progress label. True if (%) should be shown false otherwise. DEFAULT - False |
| seekBar_Style | setSeekBarStyle(int) | Set the style of the seekBar. One of STYLE_CURVE and STYLE_LINEAR DEFAULT - STYLE_CURVE |
