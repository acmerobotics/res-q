import subprocess, time

subprocess.call(["netsh", "wlan", "start", "hostednetwork"])
subprocess.call(["netsh", "wlan", "show", "hostednetwork"])
subprocess.call(["netsh", "wlan", "set", "hostednetwork", "key=roadrunner"])

subprocess.call(["adb", "usb"])

time.sleep(3)

subprocess.call(["adb", "tcpip", "5555"])

time.sleep(3)

proc = subprocess.Popen(["adb", "shell", "ifconfig", "wlan0"], stdout=subprocess.PIPE)
s = str(proc.stdout.read())
i = s.find("ip") + 3
ip = s[i:s.find(" ", i)]

subprocess.call(["adb", "connect", ip + ":5555"])
