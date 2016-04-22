@echo off
netsh wlan start hostednetwork
netsh wlan show hostednetwork
netsh wlan set hostednetwork key=roadrunner
adb kill-server
adb usb
ping 127.0.0.1 -n 3 > nul
adb tcpip 5555
set /P ip="Enter IP: "
adb connect %ip%:5555
