#!/system/bin/sh

SCRIPT="/sdcard/ES-DE/lawnchair/system-select.sh"

ARG1="$1"

nohup /system/bin/sh "$SCRIPT" "$ARG1" \
    >/dev/null 2>&1 </dev/null &

exit 0