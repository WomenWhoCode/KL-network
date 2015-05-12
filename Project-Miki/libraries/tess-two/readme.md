Since the steps listed in any of the documentation available are bit old (I just don't want to use ant), here's what I did to get it working.  
1. Once ndk-build is completed, import it as module in Android Studio  
2. Update your local.properties with ndk.dir e.g. : 
ndk.dir=/Developer/android-ndk-r10d  
3. Update the build.gradle file same as the one in this module
