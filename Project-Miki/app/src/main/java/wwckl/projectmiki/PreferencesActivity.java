/*
 * Copyright (C) 2008 ZXing authors
 * Copyright (C) 2011 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package wwckl.projectmiki;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/*
 * Modified by lydialim on 2/4/15.
 *
 * The code for this class was adapted from the ZXing project: https://github.com/zxing/zxing/
 * and https://github.com/rmtheis/android-ocr/
 */
public final class PreferencesActivity extends PreferenceActivity {

    // Preference keys carried over from ZXing project
    public static final String KEY_PLAY_BEEP = "preferences_play_beep";
    public static final String KEY_VIBRATE = "preferences_vibrate";
    public static final String KEY_FRONT_LIGHT_MODE = "preferences_front_light_mode";
    public static final String KEY_BULK_MODE = "preferences_bulk_mode";
    public static final String KEY_REMEMBER_DUPLICATES = "preferences_remember_duplicates";
    public static final String KEY_AUTO_FOCUS = "preferences_auto_focus";
    public static final String KEY_INVERT_SCAN = "preferences_invert_scan";
    public static final String KEY_DISABLE_AUTO_ORIENTATION = "preferences_orientation";

    public static final String KEY_DISABLE_CONTINUOUS_FOCUS = "preferences_disable_continuous_focus";
    public static final String KEY_DISABLE_EXPOSURE = "preferences_disable_exposure";
    public static final String KEY_DISABLE_METERING = "preferences_disable_metering";


    @Override
    protected void onCreate (Bundle icicle) {
        super.onCreate(icicle);
//        getFragmentManager().beginTransaction().replace(android.R.id.content, new PreferencesFragment()).commit();
    }

    // Apparently this will be necessary when targeting API 19+:
  /*
  @Override
  protected boolean isValidFragment(String fragmentName) {
    return true;
  }
   */

}