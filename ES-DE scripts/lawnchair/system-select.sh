escape_for_su() {
  printf '%s' "$1" | sed 's/"/\\"/g'
}

SYSTEM="$(escape_for_su "$1")"

su -c "am broadcast \
  -n app.lawnchair.play.debug/app.lawnchair.esde.EsdeCommandReceiver \
  -a app.lawnchair.action.SET_SYSTEM_SELECTED \
  --es system \"$SYSTEM\""
