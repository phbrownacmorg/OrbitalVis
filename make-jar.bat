set java_root="C:\Program Files\Java\jdk1.7.0_03"
if not exist %java_root% set java_root="C:\Program Files\Java\jdk1.6.0_04"

set dest="..\jar-stuff"
if not exist %dest% set dest="..\..\jar-stuff"

%java_root%\bin\javac.exe -classpath jogl.jar;junit-4.11.jar *.java
%java_root%\bin\jar.exe cmf manifest.txt OrbitalVis.jar *.java *.class *.gif
copy *.txt %dest%
copy *.dll %dest%
copy *.jar %dest%
copy *.html %dest%

pause
