# react-native-darkmode
`react-native-darkmode` is very light weight library which provides user's mode of preference (DARK/LIGHT) in mobile device.  
Based on device mode user can change application mode.

## Requirements
-   React Native 0.59.10 or higher
    ### iOS  
    -   Xcode 11 and above
    -   iOS 13 and above
    ### Android
    -   Android 10 and above
    -   Previous android versions which supports dark mode

## Getting started

`npm install react-native-darkmode --save`

### Mostly automatic installation

`react-native link react-native-darkmode`

### Manual integration
refer [this link](https://facebook.github.io/react-native/docs/linking-libraries-ios#manual-linking) for manual installation.

### Additional step for Android 
Android app restarts when dark mode changes.  
You can prevent Android app from restarting when dark mode changes by performing simple one native change.  

You just need to append `uiMode` to the `android:configChanges` prop of <activity> in `AndroidManifest.xml`.
```java
      <activity
        android:name=".MainActivity"
        android:label="@string/app_name"
        android:configChanges="keyboard|keyboardHidden|orientation|screenSize|uiMode" //added here
        android:windowSoftInputMode="adjustResize">
```

## Usage
```javascript
import DarkMode from 'react-native-darkmode';
```
### Method
| Prop | Type | Description | Values |  
| ----- | ---- | ----------- | ------ |
| initialMode | String | indicates initial appearance | DARK/LIGHT |
| supportsDarkMode | Boolean | indicates whether app supports Dark mode or not | true/false |

### Event

| Prop | Type | Description | Values |  
| ----- | ---- | ----------- | ------ |
| onModeChange | String | emits event while user changes appearance mode | DARK/LIGHT |

## Example
```javascript
import DarkMode, { initialMode, supportsDarkMode } from 'react-native-darkmode';
import { NativeEventEmitter } from 'react-native';

console.log('supportsDarkMode', supportsDarkMode);
console.log('initialMode', initialMode);

const DarkModeChange = new NativeEventEmitter(DarkMode);

componentDidMount(){
    DarkModeChange.addListener('onModeChange', (event) => {
        console.log('onModeChange', event);
        // perform action on mode change
    })
}

componentWillUnmount() {
    // remove the listener
    DarkModeChange.removeListener();
}
```