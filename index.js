import { NativeModules } from 'react-native';

const { DarkMode } = NativeModules;

export const initialMode = DarkMode.initialMode;
export const supportsDarkMode = DarkMode.supportsDarkMode;

export default DarkMode;
