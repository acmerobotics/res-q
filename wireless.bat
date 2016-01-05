@echo off
netsh wlan start hostednetwork
netsh wlan show hostednetwork
netsh wlan set hostednetwork key=roadrunner
adb usb
adb tcpip 5555
set /P ip="Enter IP: "
adb connect %ip%:5555
