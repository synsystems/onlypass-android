# TODO: Proper rules

-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-printmapping mapping.txt

-dontwarn **
-dontwarn **
-dontwarn javax.annotation.**