help) ps -ef | grep (Process Name) | grep -v grep | awk '{print $2}'
```bash
ps -ef | grep Stress 
ps -ef | grep Stress | grep -v grep | awk '{print $2}'
top -pid (ProcessId)

(final)
top -pid $(ps -ef | grep "Stress" | grep -v grep | awk '{print $2}')
```


