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

implementation 'com.github.faisalcodes:bidirectional-seekbar:{latest-version}'

```
## How to use

### XML
```xml
<com.android.faisalkhan.seekbar.bidirectionalseekbar.BiDirectionalSeekBar
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:BDS_indicatorColor="#952400"
    app:BDS_labelColor="#ffffff"
    app:BDS_maxValue="50"
    app:BDS_minValue="-50"
    app:BDS_percentageSign="true"
    app:BDS_progress="10"
    app:BDS_seekBarTitle="My SeekBar"
    app:BDS_seekBarTitleColor="#952400"
    app:BDS_seekBarTitleSize="tiny"
    app:BDS_stickColor="#555555"
    app:BDS_stickGap="30dp"
    app:BDS_zeroStickColor="#000000"
    app:BDS_seekBar_Style="curve"/>
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
| BDS_minValue      | setMinValue(int)<br>getMinValue() | Set the minimum value for seekBar.<br>Get the minimum value for seekBar. |
| BDS_maxValue      | setMaxValue(int)<br>getMaxValue() | Set the maximum value for seekBar.<br>Get the maximum value for seekBar. |
| BDS_progress      | setProgress(int)<br><br>getProgress() | Set progress to the seekBar. If (progress < minValue) or (progress > maxValue), then the progress becomes minValue or maxValue respectively. DEFAULT - 0<br><br>Get current progress value of seekBar. |
| BDS_seekBarTitle  | setSeekBarTitle(@StringRes or String)<br>getSeekBarTitle() | Set title to the seekBar.<br>Get title of the seekBar. |
| BDS_seekBarTitleSize  | setSeekBarSize(int) | Set the size of which the title should be. One of TITLE_TINY and TITLE_NORMAL . DEFAULT - TITLE_TINY |
| BDS_seekBarTitleColor | setSeekBarColor(int) | Set color to the title text. |
| BDS_labelColor | setLabelColor(int) | Set color to the label text. This is useful when background color and text color of the label are mismatching. By default, it uses theme primary text color. |
| BDS_indicatorColor | setIndicatorColor(int) | Set color to the indicator and label background. By default, it uses theme primary color. |
| BDS_stickColor | setStickColor(int) | Set color to the progress sticks. By default, it uses theme secondary text color. |
| BDS_stickGap | setStickGap(int) | Set gap between the sticks. This is useful to make seekBar look good when total progress sticks are lesser in count. |
| BDS_zeroStickColor | setZeroStickColor(int) | Set color to the stick at zero (0) progress. This is ignored if (minValue > 0) or (maxValue < 0). By default, it uses theme primary text color. |
| BDS_percentageSign| setPercentageSign(boolean) | Set whether the percentage sign (%) should be shown with progress label. True if (%) should be shown false otherwise. DEFAULT - False |
| BDS_seekBar_Style | setSeekBarStyle(int) | Set the style of the seekBar. One of STYLE_CURVE and STYLE_LINEAR DEFAULT - STYLE_CURVE |

## License
```
 The MIT License (MIT)

 Copyright (c) 2021 Faisal Khan

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
```
