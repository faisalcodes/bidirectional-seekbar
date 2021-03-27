# Bi-Directional SeekBar

[![](https://jitpack.io/v/faisalcodes/bidirectional-seekbar.svg)](https://jitpack.io/#faisalcodes/bidirectional-seekbar)
[![Platform](https://img.shields.io/badge/platform-Android-yellow.svg)](https://www.android.com)
[![API](https://img.shields.io/badge/API-16%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=16)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

--------------------------------------------------------------------
###Seek progress in both direction.
<br><br>
<img src = "/demos/demo.gif" height="500px">
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

implementation 'com.github.faisalcodes:bidirectional-seekbar:{latest-release}'

```
### How to use

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
        app:progress="0"
        app:seekBarTitle="My SeekBar"
        app:seekBarTitleColor="#952400"
        app:seekBarTitleSize="tiny"
        app:stickColor="#555555"
        app:zeroStickColor="#000000" />
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
seekBar.setSeekBarTitleSize(BiDirectionalSeekBar.TITLE_TINY);
seekBar.setSeekBarTitleColor(0xFF952400);
seekBar.setStickColor(0xFF555555);
seekBar.setZeroStickColor(0xFF000000);
```
### Progress Change Listener
```java
seekBar.setOnProgressChangeListener(new BiDirectionalSeekBar.OnProgressChangeListener() {
    @Override
    public void onProgress(int progress) {
        Log.d("myLogs", String.valueOf(progress));
    }
});
```
## Attributes
| XML Attribute | Java methods                      | Description                                                              |
|---------------|-----------------------------------|--------------------------------------------------------------------------|
| minValue      | setMinValue(int)<br>getMinValue() | Set the minimum value for seekBar.<br>Get the minimum value for seekBar. |
| maxValue      | setMaxValue(int)<br>getMaxValue() | Set the maximum value for seekBar.<br>Get the maximum value for seekBar. |
| progress      | setProgress(int)<br><br>getProgress() | Set progress to the seekBar. If (progress < minValue) or (progress > maxValue), then the progress becomes minValue or maxValue respectively. DEFAULT - 0<br><br>Get current progress value of seekBar. |
| seekBarTitle  | setSeekBarTitle(@StringRes int)<br>setSeekBarTitle(String)<br>getSeekBarTitle() | Set title to the seekBar.<br>Get title of the seekBar. |
| seekBarTitleSize  | setSeekBarSize(int) | Set the size of which the title should be. One of TITLE_TINY and TITLE_NORMAL |
| seekBarTitleColor | setSeekBarColor(int) | Set color to the title text. |
| stickColor | setStickColor(int) | Set color to the progress sticks. |
| zeroStickColor | setZeroStickColor(int) | Set color to the stick at zero (0) progress. This is ignored if (minValue > 0) or (maxValue < 0). |
| percentageSign| setPercentageSign(boolean) | Set whether the percentage sign (%) should be shown with progress label or not. DEFAULT - False |
