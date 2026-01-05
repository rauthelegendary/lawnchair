#!/system/bin/sh

SCRIPT="/sdcard/ES-DE/lawnchair/game-select.sh"

ARG1="$1"
ARG2="$2"
ARG3="$3"

nohup /system/bin/sh "$SCRIPT" "$ARG1" "$ARG2" "$ARG3" \
    >/dev/null 2>&1 </dev/null &

exit 0